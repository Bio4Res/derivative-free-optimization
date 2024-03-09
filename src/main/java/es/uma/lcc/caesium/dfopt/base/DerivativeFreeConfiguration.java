package es.uma.lcc.caesium.dfopt.base;

import java.math.BigDecimal;

import com.github.cliftonlabs.json_simple.JsonObject;

/**
 * Configuration of the Nelder-Mead method
 * @author ccottap
 * @version 1.1
 */
public class DerivativeFreeConfiguration {
	/**
	 * default value of the rng seed
	 */
	private final static long SEED = 1;
	/**
	 * default value of the number of runs
	 */
	private final static int NUMRUNS = 20;
	/**
	 * default value for the number of calls to the objective function
	 */
	private final static int MAXEVALS = 20000;
	/**
	 * default value for the maximum number of calls to the objective function in a cycle (for iterated method)
	 */
	private final static int MAXEVALSCYCLE = 1000;	
	/**
	 * RNG seed
	 */
	private long seed;
	/**
	 * number of runs
	 */
	private int numruns;
	/**
	 * number of calls to the objective function
	 */
	private int maxevals;
	/**
	 * maximum number of calls to the objective function in a cycle of Nelder-Mead algorithm
	 */
	private int maxevalsCycle;
	/**
	 * a string identifying the method
	 */
	protected String method;
	

	/**
	 * Constructor with default values
	 */
	public DerivativeFreeConfiguration() {
		seed = SEED;
		numruns = NUMRUNS;
		maxevals = MAXEVALS;
		maxevalsCycle = MAXEVALSCYCLE;
	}
	
	
	
	/**
	 * Convenience method to obtain an integer from the JSON object
	 * @param obj the JSON object
	 * @param key the key whose value is sought
	 * @return the value of the key as an integer
	 */
	protected static int getInt (JsonObject obj, String key) {
		return ((BigDecimal)obj.get(key)).intValue();
	}
	
	/**
	 * Convenience method to obtain a long from the JSON object
	 * @param obj the JSON object
	 * @param key the key whose value is sought
	 * @return the value of the key as a long
	 */
	protected static long getLong (JsonObject obj, String key) {
		return ((BigDecimal)obj.get(key)).longValue();
	}
	
	/**
	 * Convenience method to obtain a double from the JSON object
	 * @param obj the JSON object
	 * @param key the key whose value is sought
	 * @return the value of the key as a double
	 */
	protected static double getDouble (JsonObject obj, String key) {
		return ((BigDecimal)obj.get(key)).doubleValue();
	}

	/**
	 * Creates the configuration by reading from a file
	 * @param json a JSON object
	 */
	public DerivativeFreeConfiguration(JsonObject json) {
		this();
		if (json.containsKey("seed")) {
			setSeed(getLong(json, "seed"));
		}
		if (json.containsKey("numruns")) {
			setNumruns(getInt(json, "numruns"));
		}
		if (json.containsKey("maxevals")) {
			setMaxevals(getInt(json, "maxevals"));
		}
		if (json.containsKey("maxevalscycle")) {
			setMaxevalsCycle(getInt(json, "maxevalscycle"));
		}
	}



	/**
	 * Returns the seed
	 * @return the seed
	 */
	public long getSeed() {
		return seed;
	}


	/**
	 * Sets the seed
	 * @param seed the seed to set
	 */
	public void setSeed(long seed) {
		this.seed = seed;
	}


	/**
	 * Returns the number of runs
	 * @return the number of runs
	 */
	public int getNumruns() {
		return numruns;
	}


	/**
	 * Sets the number of runs
	 * @param numruns the number of runs to set
	 */
	public void setNumruns(int numruns) {
		assert numruns > 0;
		this.numruns = numruns;
	}



	/**
	 * Returns the maximum number of evaluations
	 * @return the maximum number of evaluations
	 */
	public int getMaxevals() {
		return maxevals;
	}


	/**
	 * Sets the maximum number of evaluations
	 * @param maxevals the maximum number of evaluations to set
	 */
	public void setMaxevals(int maxevals) {
		assert maxevals > 0;
		this.maxevals = maxevals;
	}
	
	/**
	 * Returns the maximum number of evaluations in a cycle
	 * @return the maximum number of evaluations in a cycle
	 */
	public int getMaxevalsCycle() {
		return maxevalsCycle;
	}



	/**
	 * Sets the maximum number of evaluations in a cycle
	 * @param maxevalsCycle the maximum number of evaluations in a cycle
	 */
	public void setMaxevalsCycle(int maxevalsCycle) {
		this.maxevalsCycle = maxevalsCycle;
	}



	/**
	 * Returns the method
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}



	/**
	 * Sets the method
	 * @param method the method
	 */
	public void setMethod(String method) {
		this.method = method;
	}



	@Override
	public String toString() {
		String str = "-------------------------------\n";
		str += 	"seed:\t\t " + seed + "\n" + 
				"numruns:\t " + numruns + "\n" +
				"maxevals:\t " + maxevals + "\n" +
				"maxevals-cycle:\t " + maxevalsCycle + "\n" +
				"-------------------------------\n" +
				"method:\t\t " + method + "\n" +
				"-------------------------------\n";
		return str;		
	}
	
	
	

}
