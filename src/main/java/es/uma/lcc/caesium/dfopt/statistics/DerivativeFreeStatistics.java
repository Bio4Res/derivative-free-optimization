package es.uma.lcc.caesium.dfopt.statistics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import es.uma.lcc.caesium.dfopt.base.EvaluatedSolution;



/**
 * Class for storing the statistics of the functioning of an iterated derivative-free algorithm
 * @author ccottap
 * @version 1.0
 *
 */
public class DerivativeFreeStatistics {
	/**
	 * whether a run is active or not 
	 */
	private boolean runActive;
	/**
	 * statistics of all runs
	 */
	private List<List<StatsEntry>> stats;
	/**
	 * statistics of the current run
	 */
	private List<StatsEntry> current;
	/**
	 * evolution of the best solutions in all runs 
	 */
	private List<List<EvaluatedSolutionRecord>> sols;
	/**
	 * evolution of the best solution in the current run
	 */
	private List<EvaluatedSolutionRecord> currentSols;
	/**
	 * last best solution in the current run
	 */
	private EvaluatedSolution last;
	/**
	 * to measure computational times
	 */
	private List<Double> runtime;
	/**
	 * time at the beginning of a run
	 */
	private long tic;
	/**
	 * time at the end of a run
	 */
	private long toc;
	
	/**
	 * Initializes statistics for a batch of runs
	 */
	public DerivativeFreeStatistics() {
		clear();
	}
	
	/**
	 * Clears all statistics
	 */
	public void clear() {
		stats = new ArrayList<List<StatsEntry>> ();
		current = null;
		sols = new ArrayList<List<EvaluatedSolutionRecord>> ();
		currentSols = null;
		runActive = false;	
		runtime = new LinkedList<Double>();
	}
	
	/**
	 * Logs the start of a new run
	 */
	public void newRun() {
		if (runActive)
			closeRun();
		current = new ArrayList<StatsEntry> ();
		currentSols = new ArrayList<EvaluatedSolutionRecord>();	
		runActive = true;
		tic = System.nanoTime();
	}
	
	
	/**
	 * Closes the current run and commits the statistics to the
	 * global record.
	 */
	public void closeRun() {
		if (runActive) {
			stats.add(current);
			sols.add(currentSols);
			toc = System.nanoTime();
			runtime.add((toc-tic)/1e9);
		}
		current = null;
		currentSols = null;
		runActive = false;
	}
	
	/**
	 * Takes statistics of the algorithm at a given time
	 * @param evals number of evaluations so far
	 * @param sol the current solution
	 */
	public void takeStats(long evals, EvaluatedSolution sol) {
		
		current.add(new StatsEntry(evals, sol.value()));

		if ((currentSols.size()==0) || (sol.value() < last.value())) {
			currentSols.add(new EvaluatedSolutionRecord(evals, sol));
			last = new EvaluatedSolution(sol);
		}
	}

	
	
	/**
	 * Returns the data of a certain run in JSON format
	 * @param i the run index
	 * @return a JSON object with the data of the i-th run
	 */
	public JsonObject toJSON(int i) {
		JsonObject json = new JsonObject();
		json.put("run", i);
		json.put("time", runtime.get(i));
		JsonArray jsondata = new JsonArray();
		jsondata.add(rundataTotoJSON(i));
		json.put("rundata", jsondata);
		return json;
	}
	
	/**
	 * Returns the rundata of a certain run in JSON format
	 * @param i the run index
	 * @return a JSON object with the rundata of the i-th run
	 */
	private JsonObject rundataTotoJSON(int i) {
		JsonObject json = new JsonObject();
		
		JsonObject jsonstats = new JsonObject();		
		JsonArray jsonevals = new JsonArray();
		JsonArray jsonbest = new JsonArray();

		List<StatsEntry> data = stats.get(i);
		for (StatsEntry s: data) {
			jsonevals.add(s.evals());
			jsonbest.add(s.best());
		}		
		jsonstats.put("evals", jsonevals);
		jsonstats.put("best", jsonbest);
		json.put("idata", jsonstats);
		
		JsonObject jsonsols = new JsonObject();		
		JsonArray jsonsolsevals = new JsonArray();
		JsonArray jsonsolsfitness = new JsonArray();
		JsonArray jsonsolsgenome = new JsonArray();
		List<EvaluatedSolutionRecord> soldata = sols.get(i);
		for (EvaluatedSolutionRecord p: soldata) {
			jsonsolsevals.add(p.evals());
			jsonsolsfitness.add(p.solution().value());
			List<Double> g = p.solution().point();
			JsonArray jsongenome = new JsonArray();
			int n = g.size();
			for (int j=0; j<n; j++) 
				jsongenome.add(g.get(j));
			jsonsolsgenome.add(jsongenome);
		}		
		jsonsols.put("evals", jsonsolsevals);
		jsonsols.put("fitness", jsonsolsfitness);
		jsonsols.put("genome", jsonsolsgenome);
		json.put("isols", jsonsols);		
		return json;
	}
	
	
	/**
	 * Returns the data of all runs in JSON format
	 * @return a JSON object with the data of all runs
	 */
	public JsonArray toJSON() {
		JsonArray jsondata = new JsonArray();
		int n = stats.size();
		for (int i=0; i<n; i++)
			jsondata.add(toJSON(i));
		return jsondata;
	}
	
	
	/**
	 * Returns the CPU time of a certain run
	 * @param i the index of the run
	 * @return the CPU time of the i-th run
	 */
	public double getTime(int i) {
		return runtime.get(i);
	}
	
	/**
	 * Returns the best solution found so far in the current run
	 * @return the best solution found so far in the current run
	 */
	public EvaluatedSolution getCurrentBest() {
		return currentSols.get(currentSols.size()-1).solution();
	}
	

	/**
	 * Returns the best solution of a given run
	 * @param i the index of the run
	 * @return the best individual in the i-th run
	 */
	public EvaluatedSolution getBest(int i) {
		List<EvaluatedSolutionRecord> data = sols.get(i);
		int n = data.size();
		return data.get(n-1).solution();
	}
	
	/**
	 * Returns the best solution of all runs
	 * @return the best solution of all runs
	 */
	public EvaluatedSolution getBest() {
		int numruns = stats.size();
		EvaluatedSolution best = getBest(0);
		for (int j=1; j<numruns; j++) {
			EvaluatedSolution cand = getBest(j);
			if (cand.value() < best.value()) 
				best = cand;
		}
		return best;
	}

	
	@Override
	public String toString() {
		String str = "";
		int runs = stats.size();
		for (int i=0; i<runs; i++) {
			List<StatsEntry> runstats = stats.get(i);
			str += "Run " + i + "\n=======\n";
			str += "#evals\tbest\tmean\n------\t----\t----\n";
			for (StatsEntry s: runstats) {
				str += s.evals() + "\t" + s.best() + "\n";
			}
		}
		return str;
	}
	
}
