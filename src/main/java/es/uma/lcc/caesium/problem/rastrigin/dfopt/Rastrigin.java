package es.uma.lcc.caesium.problem.rastrigin.dfopt;

import java.util.List;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeObjectiveFunction;

/**
 * Rastrigin function
 * @author ccottap
 * @version 1.0
 *
 */
public class Rastrigin extends DerivativeFreeObjectiveFunction {
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
	private static final double A = 10.0;
	
	/**
	 * Basic constructor
	 * @param i the number of variables and their range
	 * @param v range of variables ([-v, v])
	 */
	public Rastrigin(int i, double v) {
		n = i;
		range = v;
	}
	
	
	
	@Override
	protected double _evaluate(List<Double> sol) {
		double c = A * n;
		for (int j=0; j<n; j++) {
			double v = sol.get(j);
			c += v * v - A * Math.cos(2.0*Math.PI*v);
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
