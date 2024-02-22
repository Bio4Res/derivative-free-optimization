package es.uma.lcc.caesium.dfopt.statistics;


/**
 * Statistic snapshot of the algorithm
 * @author ccottap
 * @param evals number of evaluations
 * @param best best fitness
 * @version 1.0
 */
public record StatsEntry(long evals, double best) {
}
