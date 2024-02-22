package es.uma.lcc.caesium.dfopt.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Points in n-dimensional space plus value of the objective function
 * @author ccottap
 * @param point an n-dimensional point
 * @param value value of the objective function at this point
 * @version 1.0
 */
public record EvaluatedSolution(List<Double> point, double value) implements Comparable<EvaluatedSolution> {
	/**
	 * Clones a solution
	 * @param sol the solution to be cloned
	 */
	public EvaluatedSolution(EvaluatedSolution sol) {
		this(new ArrayList<Double>(sol.point), sol.value);
	}

	@Override
	public int compareTo(EvaluatedSolution other) {
		return Double.compare(value, other.value);
	}
	
	@Override
	public String toString() {
		return "{" + point + ", " + value + "}";
	}

}
