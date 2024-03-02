package es.uma.lcc.caesium.problem.ackley.dfopt;

import java.util.List;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeObjectiveFunction;

/**
 * Ackley function
 * @author ccottap
 * @version 1.0
 *
 */
public class Ackley extends DerivativeFreeObjectiveFunction {
	/**
	 * number of variables
	 */
	private int n;
	/**
	 * range
	 */
	private double range;
	/**
	 * function constant A
	 */
	private static final double A = 20.0;
	/**
	 * function constants B
	 */
	private static final double B = 0.2;
	/**
	 * function constant C
	 */
	private static final double C = 2.0*Math.PI;
	
	/**
	 * Basic constructor
	 * @param i the number of variables and their range
	 * @param v range of variables ([-v, v])
	 */
	public Ackley(int i, double v) {
		n = i;
		range = v;
	}
	
	
	
	@Override
	protected double _evaluate(List<Double> sol) {
		double s1 = 0.0;
		double s2 = 0.0;
		for (int j=0; j<n; j++) {
			double v = sol.get(j);
			s1 += v*v;
			s2 += Math.cos(C*v);
		}
		return -A*Math.exp(-B*Math.sqrt(s1/n)) - Math.exp(s2/n) + A + Math.E;
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
