package es.uma.lcc.caesium.dfopt.neldermead;

import java.util.Collection;
import java.util.List;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeMethod;
import es.uma.lcc.caesium.dfopt.base.DerivativeFreeObjectiveFunction;
import es.uma.lcc.caesium.dfopt.base.EvaluatedSolution;

/**
 * Nelder-Mead optimizer
 * @author ccottap
 * @version 1.0
 */
public class NelderMead extends DerivativeFreeMethod {
	/**
	 * the simplex
	 */
	private NelderMeadSimplex simplex;
	


	/**
	 * Default constructor
	 */
	public NelderMead() {
		this(new NelderMeadConfiguration());
	}

	/**
	 * Creates the algorithm given a configuration
	 * @param conf the configuration of the algorithm
	 */
	public NelderMead(NelderMeadConfiguration conf) {
		super(conf);
	}
	
	

	@Override
	public void setObjectiveFunction(DerivativeFreeObjectiveFunction dfof) {
		super.setObjectiveFunction(dfof);
		simplex = new NelderMeadSimplex(obj.getNumVariables());
		simplex.setObjectiveFunction(obj);
		simplex.setSeed(currentSeed);
	}
	

	
	@Override
	public EvaluatedSolution _run () {		
		simplex.setSeed(currentSeed++);		
		simplex.initialize();
		nelderMeadCycle();
		return simplex.get(0);
	}
	
	
	@Override
	public EvaluatedSolution _run (List<Double> p) {		
		simplex.setSeed(currentSeed++);		
		simplex.initialize(p);
		nelderMeadCycle();
		return simplex.get(0);
	}
	
	
	/**
	 * Runs the algorithm with a collection of points supplied as initial simplex. This is 
	 * a fully deterministic procedure (as long as the objective function is deterministic as well). 
	 * The collection of points supplied must have the right size (n+1, where n is the dimensionality
	 * of solutions).
	 * @param points the initial simplex
	 * @return the best solution found
	 */
	public EvaluatedSolution run (Collection<List<Double>> points) {
		tic = System.nanoTime();
		obj.newRun();		
		simplex.initialize(points);
		nelderMeadCycle();
		toc = System.nanoTime();
		return simplex.get(0);
	}
	
	
	
	

	/**
	 * main cycle of the Nelder-Mead algorithm
	 */
	private void nelderMeadCycle () {
		NelderMeadConfiguration conf = (NelderMeadConfiguration)this.conf;
		int n = obj.getNumVariables();
		if (verbosityLevel > 0) {
			System.out.println(obj.getNumEvals() + "\t" + normStdDev(simplex) + "\t" + simplex.get(0).value());
		}

		while ((obj.getNumEvals() < conf.getMaxevalsCycle()) && (normStdDev(simplex) > conf.getTolerance())) {
			List<Double> centroid = simplex.getCentroid();			
			List<Double> worstPoint = simplex.get(n).point();
			EvaluatedSolution x_r = simplex.getPoint(centroid, simplex.getVector(worstPoint, centroid), conf.getReflection());
			double best = simplex.get(0).value();
			double secondWorst = simplex.get(n-1).value(); 
			
			if (verbosityLevel > 1) {
				System.out.println(simplex);
				System.out.println("worst: " + worstPoint);
				System.out.println("reflection: " + x_r);
			}
			
			if (x_r.value() < secondWorst) {
				if (best < x_r.value()) { 	// Accept reflected
					simplex.addPoint(x_r);
					if (verbosityLevel > 1) {
						System.out.println("Better than 2nd worst but worse than best");
						System.out.println(x_r + " added to simplex");
					}
				}
				else {	// Choose between reflected and expanded
					EvaluatedSolution x_e = simplex.getPoint(centroid, simplex.getVector(centroid, x_r.point()), conf.getExpansion());
					if (verbosityLevel > 1) {
						System.out.println("Better than best");
						System.out.println("Expanded: " + x_e);
					}
					if (x_e.value() < x_r.value()) {
						if (verbosityLevel > 1) {
							System.out.println("Better than reflection");
							System.out.println(x_e + " added to simplex");
						}
						simplex.addPoint(x_e);
					}
					else {
						if (verbosityLevel > 1) {
							System.out.println("Worst than reflection");
							System.out.println(x_r + " added to simplex");
						}
						simplex.addPoint(x_r);
					}
				}
			} 
			else { // Contract or shrink
				double worst = simplex.get(n).value(); 
				boolean better;
				EvaluatedSolution x_c;
				if (x_r.value() < worst) {
					x_c = simplex.getPoint(centroid, simplex.getVector(centroid, x_r.point()), conf.getContraction());
					better = x_c.value() < x_r.value();
					if (verbosityLevel > 1) {
						System.out.println("Worst than 2nd worst but better than worst");
						System.out.println("Contracted on the outside: " + x_c);
					}
				}
				else {
					x_c = simplex.getPoint(centroid, simplex.getVector(centroid, worstPoint), conf.getContraction());						
					better = x_c.value() < worst;
					if (verbosityLevel > 1) {
						System.out.println("Worst than worst");
						System.out.println("Contracted in the inside: " + x_c);
					}
				}
				if (better) {
					if (verbosityLevel > 1) {
						System.out.println(x_c + " added to simplex");
					}
					simplex.addPoint(x_c);
				}
				else {
					if (verbosityLevel > 1) {
						System.out.println("Simplex shrinks");
					}
					simplex.shrink(conf.getShrink());
				}
			}
			if (verbosityLevel > 0) {
				System.out.println(obj.getNumEvals() + "\t" + normStdDev(simplex) + "\t" + simplex.get(0).value());
			}
		}
	}
	
	
	/**
	 * Computes the normalized standard deviation of values of the objective function in the simplex
	 * @param s the simplex
	 * @return the normalized standard deviation of values of the objective function in the simplex
	 */
	private double normStdDev (NelderMeadSimplex s) {
		double mean = 0.0;
		double std = 0.0;
		int n = obj.getNumVariables();
		for (int i=0; i<=n; i++) {
			EvaluatedSolution sol = s.get(i);
			mean += sol.value();			
		}
		mean /= (n+1);
		for (int i=0; i<=n; i++) {
			EvaluatedSolution sol = s.get(i);
			double v = (sol.value()-mean);		
			std += v*v;
		}
		std = Math.sqrt(std/(n+1));
		if (mean != 0)
			std /= mean;
		else
			std = Double.POSITIVE_INFINITY;
		return std;
	}
	
	

}
