package es.uma.lcc.caesium.dfopt.base;

import java.util.List;

/**
 * Abstract class for the objective function of a derivative-free optimization algorithm
 * @author ccottap
 * @version 1.0
 */
public abstract class DerivativeFreeObjectiveFunction {
	/**
	 * number of calls to the objective function in the current run
	 */
	private int evals = 0;
	
	/**
	 * Gets the number of variables in the problem
	 * @return the number of variables in the problem
	 */
	 public abstract int getNumVariables();
	
	/**
	 * Returns the minimum value of the {@code i}-th variable
	 * @param i the index of the variable
	 * @return the minimum value of the {@code i}-th variable
	 */
	 public abstract double getMinValue (int i);
	
	/**
	 * Returns the maximum value of the {@code i}-th variable
	 * @param i the index of the variable
	 * @return the maximum value of the {@code i}-th variable
	 */	
	 public abstract double getMaxValue (int i);
	
	/**
	 * Evaluates a point
	 * @param solution an n-dimentional point
	 * @return the value of the objective function at this point
	 */
	public double evaluate (List<Double> solution) {
		evals++;
		return _evaluate(solution);
	}
	
	/**
	 * Internal method for computing the value for the objective function
	 * @param solution an n-dimentional point
	 * @return the value of the objective function at this point
	 */
	protected abstract double _evaluate(List<Double> solution);

	/**
	 * Returns the number of calls to the objective function in the current run
	 * @return the number of calls to the objective function in the current run
	 */
	public int getNumEvals() {
		return evals;
	}
	
	/**
	 * performs any actions that might be required at the start of a run
	 */
	public void newRun() {
		evals = 0;
	}
}
