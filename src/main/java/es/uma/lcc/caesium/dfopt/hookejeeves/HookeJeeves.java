package es.uma.lcc.caesium.dfopt.hookejeeves;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeConfiguration;
import es.uma.lcc.caesium.dfopt.base.DerivativeFreeMethod;
import es.uma.lcc.caesium.dfopt.base.EvaluatedSolution;

/**
 * Hooke-Jeeves algorithm
 * @author ccottap
 * @version 1.1
 */
public class HookeJeeves extends DerivativeFreeMethod {
	/**
	 * random number generator
	 */
	private Random rng;

	
	/**
	 * Default constructor
	 */
	public HookeJeeves () {
		this(new HookeJeevesConfiguration());
	}
	
	
	/**
	 * Creates the algorithm given a configuration
	 * @param conf the configuration of the algorithm
	 */
	public HookeJeeves(DerivativeFreeConfiguration conf) {
		super(conf);
		rng = new Random(1);
	}
	

	
	@Override
	protected EvaluatedSolution _run() {
		rng.setSeed(currentSeed);
		return _run(randomPoint());
	}
	
	/**
	 * Returns a random point in the domain
	 * @return a random point in the domain
	 */
	private List<Double> randomPoint() {
		int n = obj.getNumVariables();
		List<Double> p = new ArrayList<Double> (n);
		for (int j=0; j<n; j++) {
			p.add(rng.nextDouble(obj.getMinValue(j), obj.getMaxValue(j)));
		}
		return p;
	}


	@Override
	protected EvaluatedSolution _run(List<Double> p) {
		currentSeed++; 	// the method is not stochastic at this point, but the seed is increased because (1) there may have been
						// an invocation from _run(), and (2) consistency is kept in increasing the seed at each run.
		HookeJeevesConfiguration hjconf = (HookeJeevesConfiguration)conf;
		
		double curStep = hjconf.getStep();
		int n = obj.getNumVariables();
		double[] delta = new double[n];		// compute step sizes along each dimension
		initializeDelta (delta, curStep);
		double[] direct = new double[n];	// direction of the last improvement

		
		EvaluatedSolution current = new EvaluatedSolution(p, obj.evaluate(p));
		if (verbosityLevel > 0) {
			System.out.println(obj.getNumEvals() + "\t" + curStep + "\t" + current.value());
			if (verbosityLevel > 1) {
				System.out.println("HJ starts at " + current);
			}
		}
		while ((obj.getNumEvals() < hjconf.getMaxevalsCycle()) && (curStep > hjconf.getMinStep())) {
			EvaluatedSolution newSol = getBestNeighbor (current.point(), delta, false);
			while ((newSol.value() < current.value()) && (obj.getNumEvals() < hjconf.getMaxevalsCycle())) {
				getDirection (direct, current.point(), newSol.point());
				if (verbosityLevel > 1) {
					System.out.println("Better neighbor = " + newSol);
					System.out.println("Becomes current. Direction = " + toString(direct));
				}
				current = newSol;
				newSol = getBestNeighbor(displacePoint(newSol.point(), direct, hjconf.getAcceleration()), delta);
				if (verbosityLevel > 0) {
					System.out.println(obj.getNumEvals() + "\t" + curStep + "\t" + current.value());
				}
			}
			curStep *= hjconf.getContraction();
			initializeDelta (delta, curStep);
			if (verbosityLevel > 1) {
				System.out.println("Worst neighbor = " + newSol);
				System.out.println("Step reduced to = " + curStep);
			}
		}
		
		return current;
	}


	/**
	 * Returns a printable version of the double array
	 * @param v a double array
	 * @return a printable version of the double array
	 */
	private String toString(double[] v) {
		int n = v.length;
		String str = "[" + v[0];
		for (int i=1; i<n; i++)
			str += ", " + v[i];
		str += "]";
		return str;
	}


	/**
	 * Initializes delta values given the current step size
	 * @param delta an array where the deltas for each dimension will be stored (allocated outside)
	 * @param curStep the current step size (expressed as a fraction of the domain range)
	 */
	private void initializeDelta(double[] delta, double curStep) {
		int n = obj.getNumVariables();
		assert (delta.length == n);
		for (int i=0; i<n; i++) {
			delta[i] = curStep*(obj.getMaxValue(i)-obj.getMinValue(i));
		}
	}
	
	/**
	 * Gets the direction from a point {@code origin} to another point {@code destination}, and stores it in the array {@code delta} provided.
	 * @param delta a double array where the displacements along each direction will be stored
	 * @param origin the origin point
	 * @param destination the destination point
	 */
	private void getDirection(double[] delta, List<Double> origin, List<Double> destination) {
		assert (delta.length == origin.size()) && (origin.size() == destination.size());
		int n = origin.size();
		for (int i=0; i<n; i++) {
			delta[i] = destination.get(i) - origin.get(i);
		}
	}

	/**
	 * Displaces a {@code point} given a direction {@code delta} and a numerical factor {@code acc} weighting this direction
	 * @param point the origin point
	 * @param delta the displacements along each direction
	 * @param acc the factor that expresses how much the displacement is scaled
	 * @return a point p' = {@code point} + {@code acc} * {@code delta}
	 */
	private List<Double> displacePoint(List<Double> point, double[] delta, double acc) {
		assert (delta.length == point.size());
		int n = point.size();
		List<Double> p = new ArrayList<Double>(n);
		for (int i=0; i<n; i++) {
			p.add(Math.min(obj.getMaxValue(i), Math.max(obj.getMinValue(i), point.get(i) + acc*delta[i])));
		}
		return p;
	}

	/**
	 * Gets the best point in the solid neighborhood of a given point, i.e. including this point itself
	 * @param point the base point
	 * @param delta step sizes along each dimension
	 * @return the best point neighboring the base point
	 */
	private EvaluatedSolution getBestNeighbor(List<Double> point, double[] delta) {
		return getBestNeighbor(point, delta, true);
	}


	/**
	 * Gets the best point in the neighborhood of a given point
	 * @param point the base point
	 * @param delta step sizes along each dimension
	 * @param solid whether the neighborhood is solid or not, i.e. whether the base point is included as well or not
	 * @return the best point neighboring the base point
	 */
	private EvaluatedSolution getBestNeighbor(List<Double> point, double[] delta, boolean solid) {
		assert (point.size() == delta.length) && (point.size() == obj.getNumVariables());
		
		EvaluatedSolution best;		
		if (solid) {
			best = new EvaluatedSolution(point, obj.evaluate(point));
		}
		else {
			best = new EvaluatedSolution(null, Double.POSITIVE_INFINITY);
		}
		
		int n = point.size();
		for (int i=0; i<n; i++) {
			for (int j=-1; j<=1; j+=2) {
				List<Double> p = new ArrayList<Double>(point);
				p.set(i, Math.min(obj.getMaxValue(i), Math.max(obj.getMinValue(i), point.get(i) + j*delta[i])));
				EvaluatedSolution sol = new EvaluatedSolution(p, obj.evaluate(p));
				if (sol.value() < best.value()) {
					best = sol;
				}
			}
		}
		
		return best;
	}
	
	
	@Override
	public String toString() {
		return  "===============================\nHooke-Jeeves\n===============================\n" + conf;
	}

}
