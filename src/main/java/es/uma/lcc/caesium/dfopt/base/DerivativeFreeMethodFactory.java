package es.uma.lcc.caesium.dfopt.base;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import es.uma.lcc.caesium.dfopt.hookejeeves.HookeJeeves;
import es.uma.lcc.caesium.dfopt.hookejeeves.HookeJeevesConfiguration;
import es.uma.lcc.caesium.dfopt.neldermead.NelderMead;
import es.uma.lcc.caesium.dfopt.neldermead.NelderMeadConfiguration;

/**
 * Factory class for Derivative-Free Optimization Methods
 * @author ccottap
 * @version 1.0
 */
public class DerivativeFreeMethodFactory {

	/**
	 * Creates the factory
	 */
	public DerivativeFreeMethodFactory() {
	}
	
	
	/**
	 * Reads the configuration of a derivative-free method given the name of a configuration file
	 * @param configuration the filename of the method configuration
	 * @return a configured derivative-free method
	 */
	public DerivativeFreeConfiguration readConfiguration (String configuration) {
		DerivativeFreeConfiguration conf = null;
		try {
			JsonObject jsonconf = (JsonObject) Jsoner.deserialize(new FileReader(configuration));
			String method = (String)jsonconf.get("method");
			switch(method.toLowerCase()) {
			case "neldermead": 		
				conf = new NelderMeadConfiguration(jsonconf);
				break;
			case "hookejeeves":
				conf = new HookeJeevesConfiguration(jsonconf);
				break;
			default:
				System.out.println("Unknown method " + method);
				System.exit(1);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File " + configuration + " not found");
			System.exit(1);
		} catch (JsonException e) {
			System.out.println("Malformed JSON " + configuration);
			System.exit(1);
		}
		
		return conf;
	}
	
	
	/**
	 * Creates a derivative-free method given the name of a configuration file
	 * @param configuration the filename of the method configuration
	 * @return a configured derivative-free method
	 */
	public DerivativeFreeMethod create (String configuration) {
		return create(readConfiguration(configuration));
	}

	
	/**
	 * Creates a derivative-free method given the method name and the configuration (which must be 
	 * an instance of the subclass corresponding to the method which is intended to create)
	 * @param configuration the method configuration
	 * @return a configured derivative-free method
	 */
	public DerivativeFreeMethod create(DerivativeFreeConfiguration conf) {
		DerivativeFreeMethod solver = null;
		String method = conf.getMethod();
		switch(method.toLowerCase()) {
		case "neldermead": 		
			solver = new NelderMead((NelderMeadConfiguration)conf);
			break;
		case "hookejeeves":
			solver = new HookeJeeves((HookeJeevesConfiguration)conf);
			break;
		default:
			System.out.println("Unknown method " + method);
			System.exit(1);
		}
		
		return solver;
	}

}
