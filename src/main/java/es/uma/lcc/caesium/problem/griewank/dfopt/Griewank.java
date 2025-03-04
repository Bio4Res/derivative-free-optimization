package es.uma.lcc.caesium.problem.griewank.dfopt;

import java.util.List;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeObjectiveFunction;

/**
 * Griewank function
 * @author ccottap
 * @version 1.0
 *
 */
public class Griewank extends DerivativeFreeObjectiveFunction {
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
	private static final double A = 1.0/4000.0;
	
	/**
	 * Basic constructor
	 * @param i the number of variables and their range
	 * @param v range of variables ([-v, v])
	 */
	public Griewank(int i, double v) {
		n = i;
		range = v;
	}
	
	
	
	@Override
	protected double _evaluate(List<Double> sol) {
		double s = 0.0;
		double p = 1.0;
		for (int j=0; j<n; j++) {
			double v = sol.get(j);
			s += v*v;
			p *= Math.cos(v/Math.sqrt(j+1));
		}
		return 1.0 + A*s - p;
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
