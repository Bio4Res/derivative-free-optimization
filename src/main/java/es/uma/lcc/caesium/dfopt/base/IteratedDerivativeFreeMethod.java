package es.uma.lcc.caesium.dfopt.base;

import es.uma.lcc.caesium.dfopt.statistics.DerivativeFreeStatistics;

/**
 * Iterated derivative-free method. Performs multi-start for a given number of function calls
 * @author ccottap
 * @version 1.0
 */
public class IteratedDerivativeFreeMethod {
	/**
	 * configuration of the algorithm
	 */
	private DerivativeFreeConfiguration conf;
	/**
	 * the objective function
	 */
	private DerivativeFreeObjectiveFunction obj;
	/**
	 * the Nelder-Mead optimizer
	 */
	private DerivativeFreeMethod dfm;
	/**
	 * current seed of the algorithm
	 */
	private long currentSeed;
	/**
	 * to control verbosity
	 */
	private int verbosityLevel = 0;
	/**
	 * to measure computational times
	 */
	private long tic=0;
	/**
	 * to measure computational times
	 */
	private long toc=0;
	/**
	 * Statistics of the algorithm
	 */
	private DerivativeFreeStatistics stats;
	
	

	/**
	 * Creates the algorithm given a configuration
	 * @param conf the configuration of the algorithm
	 * @param dfm the underlying derivative-free method
	 */
	public IteratedDerivativeFreeMethod(DerivativeFreeConfiguration conf, DerivativeFreeMethod dfm) {
		this.conf = conf;
		setSeed(conf.getSeed());
		this.dfm = dfm;
		stats = new DerivativeFreeStatistics();
	}
	
	
	/**
	 * Sets the seed for the RNG
	 * @param s seed for the RNG
	 */
	public void setSeed (long s) {
		currentSeed = s;
	}
	
	/**
	 * Sets the verbosity level
	 * @param verbosityLevel the verbosity level to set
	 */
	public void setVerbosityLevel(int verbosityLevel) {
		this.verbosityLevel = verbosityLevel;
		dfm.setVerbosityLevel(Math.max(0, verbosityLevel-1));
	}

	/**
	 * Sets the objective function
	 * @param nmof the objective function
	 */
	public void setObjectiveFunction(DerivativeFreeObjectiveFunction nmof) {
		this.obj = nmof;
		dfm.setObjectiveFunction(obj);
	}
	
	/**
	 * Returns the computational time (s) of the last run
	 * @return the computational time (s) of the last run
	 */
	public double getTime() {
		return ((double)toc - (double)tic)/1e9;
	}
	
	
	/**
	 * Returns the statistics
	 * @return the statistics
	 */
	public DerivativeFreeStatistics getStatistics() {
		return stats;
	}
	
	/**
	 * runs the algorithm with the current seed, and increases it.
	 * @return the best solution found
	 */
	public EvaluatedSolution run () {
		tic = System.nanoTime();
		
		stats.newRun();
		EvaluatedSolution bestSol = new EvaluatedSolution(null, Double.POSITIVE_INFINITY);
		int evals = 0;
		dfm.setSeed(currentSeed);
		currentSeed += conf.getMaxevals()/(obj.getNumVariables()+1);
		while (evals < conf.getMaxevals()) {
			EvaluatedSolution sol = dfm.run();
			evals += obj.getNumEvals();
			if (sol.value() < bestSol.value()) {
				bestSol = sol;
			}
			stats.takeStats(evals, bestSol);
			if (verbosityLevel > 0) {
				System.out.println(evals + "\t" + obj.getNumEvals() + "\t" + bestSol.value());
			}
		}
		
		stats.closeRun();
		
		toc = System.nanoTime();
		return bestSol;
	}
	
	/**
	 * Runs the algorithm with the indicated seed. Restores the seed at the end,
	 * so that the sequence of parameterless invocations is not affected.
	 * @param i the seed
	 * @return the best solution found
	 */
	public EvaluatedSolution run (int i) {
		long seed = currentSeed;
		setSeed(i);
		EvaluatedSolution sol = run();
		setSeed(seed);
		return sol;
	}



}
