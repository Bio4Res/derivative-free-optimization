package es.uma.lcc.caesium.dfopt.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeConfiguration;
import es.uma.lcc.caesium.dfopt.base.DerivativeFreeMethod;
import es.uma.lcc.caesium.dfopt.base.DerivativeFreeObjectiveFunction;
import es.uma.lcc.caesium.dfopt.base.EvaluatedSolution;
import es.uma.lcc.caesium.dfopt.base.IteratedDerivativeFreeMethod;
import es.uma.lcc.caesium.dfopt.hookejeeves.HookeJeeves;
import es.uma.lcc.caesium.dfopt.hookejeeves.HookeJeevesConfiguration;
import es.uma.lcc.caesium.dfopt.neldermead.NelderMead;
import es.uma.lcc.caesium.dfopt.neldermead.NelderMeadConfiguration;
import es.uma.lcc.caesium.problem.rastrigin.dfopt.Rastrigin;
import es.uma.lcc.caesium.problem.rosenbrock.dfopt.Rosenbrock;
import es.uma.lcc.caesium.problem.sphere.dfopt.Sphere;



/**
 * Class for testing the Nelder-Mead algorithm
 * @author ccottap
 * @version 1.0
 */
public class RunDerivativeFree {

	/**
	 * Main method
	 * @param args command-line arguments
	 * @throws FileNotFoundException if configuration file cannot be read 
	 * @throws JsonException if the configuration file is not correctly formatted
	 */
	public static void main(String[] args) throws FileNotFoundException, JsonException {		
		if (args.length < 1) {
			System.out.println("Run filename muest be provided");
			System.exit(1);
		}
		JsonObject runconf = (JsonObject) Jsoner.deserialize(new FileReader(args[0]));

		String method = ((String)runconf.get("method")).toLowerCase();
		String filename = (String)runconf.get("configuration");
		
		DerivativeFreeConfiguration conf = null;
		DerivativeFreeMethod solver = null;
		JsonObject jsonconf = (JsonObject) Jsoner.deserialize(new FileReader(filename));
		switch(method) {
		case "neldermead": 		
			conf = new NelderMeadConfiguration(jsonconf);
			solver = new NelderMead((NelderMeadConfiguration)conf);
			break;
		case "hookejeeves":
			conf = new HookeJeevesConfiguration(jsonconf);
			solver = new HookeJeeves((HookeJeevesConfiguration)conf);
			break;
		default:
			System.out.println("Unknown method " + method);
			System.exit(1);
		}
		
		String problem = ((String)runconf.get("problem")).toLowerCase();
		int dimension = Integer.parseInt((String)runconf.get("dimension"));
		double range = Double.parseDouble((String)runconf.get("range"));
		
		DerivativeFreeObjectiveFunction obj = null;
		switch(problem) {
		case "sphere":
			obj = new Sphere(dimension, range);
			break;
		case "rastrigin":
			obj = new Rastrigin(dimension, range);
			break;
		case "Rosenbrock":
			obj = new Rosenbrock(dimension, range);
			break;
		default:
			System.out.println("Unknown problem " + problem);
			System.exit(1);
		}
		
		System.out.println("Method:\t\t" + method);
		System.out.println("Configuration:\t" + filename);
		System.out.println("Problem:\t" + problem + " (" + dimension + ", " + range + ")");
		System.out.println(conf);		
		
		IteratedDerivativeFreeMethod idfm = new IteratedDerivativeFreeMethod(conf, solver);
		idfm.setObjectiveFunction(obj);
		idfm.setVerbosityLevel(0);

		for (int i=0; i<conf.getNumruns(); i++) {
			EvaluatedSolution sol = idfm.run();
			System.out.println ("Run " + i + " (" + idfm.getTime() + "s)\t: " + sol.value());
		}
		
		PrintWriter file = new PrintWriter(method + "-stats.json");
		file.print(idfm.getStatistics().toJSON().toJson());
		file.close();

	}
	
	
}
