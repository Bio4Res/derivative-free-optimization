package es.uma.lcc.caesium.dfopt.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeConfiguration;
import es.uma.lcc.caesium.dfopt.base.DerivativeFreeMethod;
import es.uma.lcc.caesium.dfopt.base.DerivativeFreeMethodFactory;
import es.uma.lcc.caesium.dfopt.base.DerivativeFreeObjectiveFunction;
import es.uma.lcc.caesium.dfopt.base.EvaluatedSolution;
import es.uma.lcc.caesium.dfopt.base.IteratedDerivativeFreeMethod;
import es.uma.lcc.caesium.problem.ackley.dfopt.Ackley;
import es.uma.lcc.caesium.problem.griewank.dfopt.Griewank;
import es.uma.lcc.caesium.problem.rastrigin.dfopt.Rastrigin;
import es.uma.lcc.caesium.problem.rosenbrock.dfopt.Rosenbrock;
import es.uma.lcc.caesium.problem.sphere.dfopt.Sphere;



/**
 * Class for testing the derivative-free optimization algorithms
 * @author ccottap
 * @version 1.1
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
			System.out.println("Run filename must be provided");
			System.exit(1);
		}
		JsonObject runconf = (JsonObject) Jsoner.deserialize(new FileReader(args[0]));

		String filename = (String)runconf.get("configuration");
		
		// creates factory
		DerivativeFreeMethodFactory dfmf = new DerivativeFreeMethodFactory();
		
		// reads configuration
		DerivativeFreeConfiguration conf = dfmf.readConfiguration(filename);
		
		// creates the solver
		DerivativeFreeMethod solver = dfmf.create(conf);
		
		// gets problem information
		String problem = ((String)runconf.get("problem")).toLowerCase();
		int dimension = Integer.parseInt((String)runconf.get("dimension"));
		double range = Double.parseDouble((String)runconf.get("range"));
		
		// creates the objective function
		DerivativeFreeObjectiveFunction obj = null;
		switch(problem) {
		case "sphere":
			obj = new Sphere(dimension, range);
			break;
		case "rastrigin":
			obj = new Rastrigin(dimension, range);
			break;
		case "rosenbrock":
			obj = new Rosenbrock(dimension, range);
			break;
		case "griewank":
			obj = new Griewank(dimension, range);
			break;
		case "ackley":
			obj = new Ackley(dimension, range);
			break;
		default:
			System.out.println("Unknown problem " + problem);
			System.exit(1);
		}
		
		System.out.println("Configuration:\t " + filename);
		System.out.println("Problem:\t " + problem + " (" + dimension + ", " + range + ")");
		System.out.println(conf);		
		
		
		// creates the iterated solver
		IteratedDerivativeFreeMethod idfm = new IteratedDerivativeFreeMethod(conf, solver);
		idfm.setObjectiveFunction(obj);
		idfm.setVerbosityLevel(0);

		// runs the iterated solver
		for (int i=0; i<conf.getNumruns(); i++) {
			EvaluatedSolution sol = idfm.run();
			System.out.println ("Run " + i + " (" + idfm.getTime() + "s)\t: " + sol.value());
		}
		
		// writes stats
		PrintWriter file = new PrintWriter(conf.getMethod() + "-stats.json");
		file.print(idfm.getStatistics().toJSON().toJson());
		file.close();

	}
	
	
}
