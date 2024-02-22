package es.uma.lcc.caesium.problem.sphere.dfopt;

import java.util.List;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeObjectiveFunction;

/**
 * Sphere function
 * @author ccottap
 * @version 1.0
 *
 */
public class Sphere extends DerivativeFreeObjectiveFunction {
	/**
	 * number of variables
	 */
	private int n;
	/**
	 * range
	 */
	private double range;
	
	/**
	 * Basic constructor
	 * @param i the number of variables and their range
	 * @param v range of variables ([-v, v])
	 */
	public Sphere(int i, double v) {
		n = i;
		range = v;
	}
	
	
	
	@Override
	protected double _evaluate(List<Double> sol) {
		double c = 0;
		for (int j=0; j<n; j++) {
			double v = sol.get(j);
			c += v*v;
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
