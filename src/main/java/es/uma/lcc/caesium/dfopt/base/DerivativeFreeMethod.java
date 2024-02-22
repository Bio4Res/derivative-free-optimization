package es.uma.lcc.caesium.dfopt.base;

import java.util.List;

/**
 * Abstract class for a derivative-free method
 * @author ccottap
 * @version 1.0
 */
public abstract class DerivativeFreeMethod {
	/**
	 * configuration of the algorithm
	 */
	protected DerivativeFreeConfiguration conf;
	/**
	 * the objective function
	 */
	protected DerivativeFreeObjectiveFunction obj;
	/**
	 * current seed of the algorithm
	 */
	protected long currentSeed;
	/**
	 * to control verbosity
	 */
	protected int verbosityLevel = 0;
	/**
	 * to measure computational times
	 */
	protected long tic=0;
	/**
	 * to measure computational times
	 */
	protected long toc=0;
	


	/**
	 * Creates the algorithm given a configuration
	 * @param conf the configuration of the algorithm
	 */
	public DerivativeFreeMethod(DerivativeFreeConfiguration conf) {
		this.conf = conf;
		setSeed(conf.getSeed());
	}
	
	/**
	 * Returns the current seed
	 * @return the current seed
	 */
	public long getSeed () {
		return currentSeed;
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
	}

	/**
	 * Sets the objective function
	 * @param dfof the objective function
	 */
	public void setObjectiveFunction(DerivativeFreeObjectiveFunction dfof) {
		this.obj = dfof;

	}
	
	/**
	 * Returns the computational time (s) of the last run
	 * @return the computational time (s) of the last run
	 */
	public double getTime() {
		return ((double)toc - (double)tic)/1e9;
	}
	
	/**
	 * Runs the algorithm with the current seed, and increases it. 
	 * @return the best solution found
	 */
	public EvaluatedSolution run () {
		tic = System.nanoTime();
		obj.newRun();
		
		EvaluatedSolution sol = _run();
		toc = System.nanoTime();
		return sol;
	}
	
	
	
	/**
	 * Runs the algorithm from a certain starting point (using the current seed if the  
	 * method is stochastic, and increasing it). 
	 * @param p the starting point
	 * @return the best solution found
	 */
	public EvaluatedSolution run (List<Double> p) {
		tic = System.nanoTime();
		obj.newRun();
		
		EvaluatedSolution sol = _run(p);
		toc = System.nanoTime();
		return sol;
	}
	
	/**
	 * Internal method to run the algorithm.
	 * @return the best solution found
	 */
	protected abstract EvaluatedSolution _run();

	/**
	 * Internal method to run the algorithm from a starting point
	 * @param p the starting point
	 * @return the best solution found
	 */
	protected abstract EvaluatedSolution _run(List<Double> p);
	
	
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
