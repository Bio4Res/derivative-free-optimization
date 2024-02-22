package es.uma.lcc.caesium.dfopt.statistics;

import es.uma.lcc.caesium.dfopt.base.EvaluatedSolution;

/**
 * A pair (evals, solution) to record the creation of a solution
 * at a specific moment
 * @author ccottap
 * @param evals number of evaluations (used as a timestamp)
 * @param solution a solution
 * @version 1.0
 *
 */
public record EvaluatedSolutionRecord(long evals, EvaluatedSolution solution) {
}
