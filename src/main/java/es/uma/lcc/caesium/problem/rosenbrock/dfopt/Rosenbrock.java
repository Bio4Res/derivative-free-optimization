package es.uma.lcc.caesium.problem.rosenbrock.dfopt;

import java.util.List;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeObjectiveFunction;

/**
 * Rosenbrock function
 * @author ccottap
 * @version 1.0
 *
 */
public class Rosenbrock extends DerivativeFreeObjectiveFunction {
	/**
	 * number of variables
	 */
	private int n;
	/**
	 * range
	 */
	private double range;
	/**
	 * function constant
	 */
	private static final double A = 100.0;
	
	/**
	 * Basic constructor
	 * @param i the number of variables and their range
	 * @param v range of variables ([-v, v])
	 */
	public Rosenbrock(int i, double v) {
		n = i;
		range = v;
	}
	
	
	
	@Override
	protected double _evaluate(List<Double> sol) {
		double c = 0;
		for (int j=1; j<n; j++) {
			double v1 = sol.get(j-1);
			double v2 = sol.get(j);
			double t1 = v2-v1*v1;
			double t2 = 1-v1;
			c += A*t1*t1 + t2*t2;
		}
		return c;
	}

	@Override
	public int getNumVariables() {
		return n;
	}

	@Override
	public double getMinValue(int i) {
		return -range;
	}

	@Override
	public double getMaxValue(int i) {
		return range;
	}



}
