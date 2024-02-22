package es.uma.lcc.caesium.dfopt.neldermead;


import com.github.cliftonlabs.json_simple.JsonObject;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeConfiguration;

/**
 * Configuration of the Nelder-Mead method
 * @author ccottap
 * @version 1.0
 */
public class NelderMeadConfiguration extends DerivativeFreeConfiguration {
	/**
	 * default value of the reflection constant
	 */
	private final static double REFLECTION = 1;
	/**
	 * default value of the expansion constant
	 */
	private final static double EXPANSION = 2;
	/**
	 * default value of the contraction constant
	 */
	private final static double CONTRACTION = 0.5;
	/**
	 * default value of the shrink constant
	 */
	private final static double SHRINK = 0.5;
	/**
	 * default value of the tolerance
	 */
	private final static double TOLERANCE = 1e-2;

	/**
	 * reflection constant
	 */
	private double reflection;
	/**
	 * expansion constant
	 */
	private double expansion;
	/**
	 * contraction constant
	 */
	private double contraction;
	/**
	 * shrink constant
	 */
	private double shrink;
	/**
	 * tolerance constant
	 */
	private double tolerance;


	/**
	 * Constructor with default values
	 */
	public NelderMeadConfiguration() {
		super();
		reflection = REFLECTION;
		expansion = EXPANSION;
		contraction = CONTRACTION;
		shrink = SHRINK;
		tolerance = TOLERANCE;
	}
	
	

	/**
	 * Creates the configuration by reading from a file
	 * @param json a JSON object
	 */
	public NelderMeadConfiguration(JsonObject json) {
		super(json);
		if (json.containsKey("reflection")) {
			setReflection(getDouble(json, "reflection"));
		}
		else {
			setReflection(REFLECTION);			
		}
		if (json.containsKey("expansion")) {
			setExpansion(getDouble(json, "expansion"));
		}
		else {
			setExpansion(EXPANSION);			
		}
		if (json.containsKey("contraction")) {
			setContraction(getDouble(json, "contraction"));
		}
		else {
			setContraction(CONTRACTION);			
		}
		if (json.containsKey("shrink")) {
			setShrink(getDouble(json, "shrink"));
		}
		else {
			setShrink(SHRINK);			
		}
		if (json.containsKey("tolerance")) {
			setTolerance(getDouble(json, "tolerance"));
		}
		else {
			setTolerance(TOLERANCE);
		}
	}



	/**
	 * Returns the reflection constant
	 * @return the reflection constant
	 */
	public double getReflection() {
		return reflection;
	}


	/**
	 * Sets the reflection constant
	 * @param reflection the reflection constant to set
	 */
	public void setReflection(double reflection) {
		assert (reflection > 0);
		this.reflection = reflection;
	}


	/**
	 * Returns the expansion constant
	 * @return the expansion constant
	 */
	public double getExpansion() {
		return expansion;
	}


	/**
	 * Sets the expansion constant
	 * @param expansion the expansion constant to set
	 */
	public void setExpansion(double expansion) {
		assert (expansion>1);
		this.expansion = expansion;
	}


	/**
	 * Returns the contraction constant
	 * @return the contraction constant
	 */
	public double getContraction() {
		return contraction;
	}


	/**
	 * Sets the contraction constant
	 * @param contraction the contraction constant to set
	 */
	public void setContraction(double contraction) {
		assert (contraction>0) && (contraction<1);
		this.contraction = contraction;
	}


	/**
	 * Returns the shrink constant
	 * @return the shrink constant
	 */
	public double getShrink() {
		return shrink;
	}


	/**
	 * Sets the shrink constant
	 * @param shrink the shrink constant to set
	 */
	public void setShrink(double shrink) {
		assert (shrink>0) && (shrink<1);
		this.shrink = shrink;
	}


	/**
	 * Returns the tolerance constant
	 * @return the tolerance constant
	 */
	public double getTolerance() {
		return tolerance;
	}


	/**
	 * Sets the tolerance constant
	 * @param tolerance the tolerance constant to set
	 */
	public void setTolerance(double tolerance) {
		assert tolerance >= 0;
		this.tolerance = tolerance;
	}



	@Override
	public String toString() {
		String str = super.toString();
		str += 	"reflection:\t " + reflection + "\n" + 
				"expansion:\t " + expansion + "\n" + 
				"contraction:\t " + contraction + "\n" + 
				"shrink:\t\t " + shrink + "\n" +
				"-------------------------------\n";
		return str;		
	}
	
	
	

}
