package es.uma.lcc.caesium.dfopt.hookejeeves;


import com.github.cliftonlabs.json_simple.JsonObject;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeConfiguration;

/**
 * Configuration of the Hooke-Jeeves method
 * @author ccottap
 * @version 1.0
 */
public class HookeJeevesConfiguration extends DerivativeFreeConfiguration {
	/**
	 * default value of the acceleration constant
	 */
	private final static double ACCELERATION = 1;
	/**
	 * default value of the contraction constant
	 */
	private final static double CONTRACTION = 0.5;
	/**
	 * default value of the stepsize (relative to the domain range)
	 */
	private final static double STEP = 0.01;
	/**
	 * default value of the minimum stepsize (relative to the domain range)
	 */
	private final static double MINSTEP = 1e-5;

	/**
	 * acceleration constant
	 */
	private double acceleration;
	/**
	 * contraction constant
	 */
	private double contraction;
	/**
	 * stepsize (relative to the domain range)
	 */
	private double step;
	/**
	 * minimum stepsize (relative to the domain range)
	 */
	private double minStep;

	/**
	 * Constructor with default values
	 */
	public HookeJeevesConfiguration() {
		super();
		acceleration = ACCELERATION;
		contraction = CONTRACTION;
		step = STEP;
		minStep = MINSTEP;
	}
	
	

	/**
	 * Creates the configuration by reading from a file
	 * @param json a JSON object
	 */
	public HookeJeevesConfiguration(JsonObject json) {
		super(json);
		if (json.containsKey("acceleration")) {
			setAcceleration(getDouble(json, "acceleration"));
		}
		else {
			setAcceleration(ACCELERATION);			
		}
		if (json.containsKey("contraction")) {
			setContraction(getDouble(json, "contraction"));
		}
		else {
			setContraction(CONTRACTION);			
		}
		if (json.containsKey("step")) {
			setStep(getDouble(json, "step"));
		}
		else {
			setStep(STEP);			
		}
		if (json.containsKey("minstep")) {
			setMinStep(getDouble(json, "minstep"));
		}
		else {
			setMinStep(MINSTEP);			
		}
	}



	/**
	 * Returns the acceleration constant
	 * @return the acceleration constant
	 */
	public double getAcceleration() {
		return acceleration;
	}


	/**
	 * Sets the acceleration constant
	 * @param acceleration the acceleration constant to set
	 */
	public void setAcceleration(double acceleration) {
		assert (acceleration>=1);
		this.acceleration = acceleration;
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
	 * Returns the stepsize
	 * @return the stepsize
	 */
	public double getStep() {
		return step;
	}



	/**
	 * Sets the stepsize
	 * @param step the stepsize
	 */
	public void setStep(double step) {
		this.step = step;
	}



	/**
	 * Returns the minimum stepsize
	 * @return the minimum stepsize
	 */
	public double getMinStep() {
		return minStep;
	}

	/**
	 * Sets the minimum stepsize
	 * @param minStep the minimum stepsize
	 */
	public void setMinStep(double minStep) {
		this.minStep = minStep;
	}



	@Override
	public String toString() {
		String str = super.toString();
		str += 	"acceleration:\t " + acceleration + "\n" + 
				"contraction:\t " + contraction + "\n" + 
				"step:\t\t " + step + "\n" + 
				"minstep:\t " + minStep + "\n" + 
				"-------------------------------\n";
		return str;		
	}
	
	
	

}
