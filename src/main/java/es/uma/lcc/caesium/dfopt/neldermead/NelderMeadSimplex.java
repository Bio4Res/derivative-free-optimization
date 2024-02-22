package es.uma.lcc.caesium.dfopt.neldermead;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import es.uma.lcc.caesium.dfopt.base.DerivativeFreeObjectiveFunction;
import es.uma.lcc.caesium.dfopt.base.EvaluatedSolution;

/**
 * Simplex in the Nelder-Mead algorithm
 * @author ccottap
 * @version 1.0
 */
public class NelderMeadSimplex {
	/**
	 * default value (expressed as a factor of the total range) used
	 * for initializing the simplex around a value
	 */
	private static final double SIDE = 0.1;
	/**
	 * number of dimensions
	 */
	private int n;
	/**
	 * points in the simplex. 
	 */
	private List<EvaluatedSolution> points;
	/**
	 * centroid of the simplex (n-dimensional point)
	 */
	private List<Double> centroid;
	/**
	 * the objective function
	 */
	private DerivativeFreeObjectiveFunction obj;
	/**
	 * random number generator
	 */
	private Random rng;


	/**
	 * Creates an empty simplex for {@code n} dimensions
	 * @param n the number of dimensions
	 */
	public NelderMeadSimplex(int n) {
		this.n = n;
		points = new ArrayList<EvaluatedSolution> (n+1);
		centroid = new ArrayList<Double> (n);
		for (int i=0; i<n; i++) {
			centroid.add(0.0); // placeholder
		}
		rng = new Random(1);
	}
	
	
	/**
	 * Sets the seed for the RNG
	 * @param s seed for the RNG
	 */
	public void setSeed (long s) {
		rng.setSeed(s);
	}
	
	/**
	 * Clears the simplex
	 */
	public void clear() {
		points.clear();
		reset (centroid);
	}
	
	/**
	 * Sets to 0 an n-dimensional point
	 * @param sol an n-dimensional point
	 */
	private void reset (List<Double> sol) {
		for (int i=0; i<n; i++) {
			sol.set(i, 0.0); 
		}
	}

	/**
	 * Sets the objective function. If there are points in the simplex, these are reevaluated.
	 * Note that objective function must have the dimensionality for which the simplex was created.
	 * @param dfof the objective function
	 */
	public void setObjectiveFunction(DerivativeFreeObjectiveFunction dfof) {
		this.obj = dfof;
		int k = points.size();
		for (int i=0; i<k; i++) {
			List<Double> p = points.get(i).point();
			points.set(i, new EvaluatedSolution (p, obj.evaluate(p)));
		}
	}
	
	/**
	 * initializes the simplex with random points in the search space
	 */
	public void initialize() {
		clear();
		for (int i=0; i<=n; i++) {
			List<Double> p = new ArrayList<Double> (n);
			for (int j=0; j<n; j++) {
				p.add(rng.nextDouble(obj.getMinValue(j), obj.getMaxValue(j)));
			}
			addPoint(p);
		}
	}
	

	
	/**
	 * Initializes the simplex around a certain point, by considering it and {@code n}
	 * other points obtained by adding a default distance along each of the dimensions.
	 * @param point a point in n-dimensional space
	 */
	public void initialize (List<Double> point) {
		initialize (point, SIDE);
	}
	
	/**
	 * Initializes the simplex around a certain point, by considering it and {@code n}
	 * other points obtained by adding a default distance along each of the dimensions.
	 * @param point a point in n-dimensional space
	 * @param side the increment  (expressed as a factor of the total range) along dimensions.
	 */
	public void initialize (List<Double> point, double side) {
		List<Double> sides = new ArrayList<Double>(n);
		for (int i=0; i<n; i++) {
			sides.add(side*(obj.getMaxValue(i)-obj.getMinValue(i)));
		}
		initialize (point, sides);
	}
	
	/**
	 * Initializes the simplex around a certain point, by considering it and {@code n}
	 * other points obtained by adding {@code side}<sub>i</sub> along each of the dimensions.
	 * @param point a point in n-dimensional space
	 * @param sides the increments along dimensions.
	 */
	public void initialize (List<Double> point, List<Double> sides) {
		assert point.size() == n;
		clear();
		addPoint(point);
		for (int i=0; i<n; i++) {
			List<Double> p = new ArrayList<Double> (point);
			p.set(i, Math.min(obj.getMaxValue(i), Math.max(obj.getMinValue(i), point.get(i) + sides.get(i))));
			addPoint(p);
		}
	}
	
	
	/**
	 * initializes the simplex with a given collection of points
	 * @param ps the initial points
	 */
	public void initialize(Collection<List<Double>> ps) {
		assert ps.size() == (n + 1);
		clear();
		for (List<Double> p: ps) {
			addPoint(p);
		}
	}
	
		
	/**
	 * Returns (a copy of) the centroid 
	 * @return the centroid
	 */
	public List<Double> getCentroid() {
		return new ArrayList<Double>(centroid);
	}
	
	/**
	 * Adds a new evaluated point to the simplex. If the simplex already has n+1 points, the worst one is substituted.
	 * If the number of points after the addition is n+1, the centroid is computed.
	 * @param sol an n-dimensional point with its evaluation
	 */
	public void addPoint(EvaluatedSolution sol) {
		if (points.size() <= n) {
			points.add(sol);
		}
		else {
			points.set(n, sol);
		}
		updateCentroid();
		
	}
	
	/**
	 * Adds a new point to the simplex. If the simplex already has n+1 points, the worst one is substituted.
	 * If the number of points after the addition is n+1, the centroid is computed.
	 * @param p an n-dimensional point
	 */
	public void addPoint (List<Double> p) {
		EvaluatedSolution sol = new EvaluatedSolution(new ArrayList<Double>(p), obj.evaluate(p));
		addPoint(sol);
	}
	
	/**
	 * Updates the centroid, if there are enough points to do so.
	 */
	private void updateCentroid() {
		if (points.size() > n) {
			points.sort(null);
			reset(centroid);			
			for (int j=0; j<n; j++) { // for all points except the worst
				List<Double> sp = points.get(j).point();
				for (int i=0; i<n; i++) {
					centroid.set(i, centroid.get(i) + sp.get(i));
				}
			}
			for (int i=0; i<n; i++) {
				centroid.set(i, centroid.get(i) / (n+1));
			}
		}
	}
	
	/**
	 * Returns a point of the simplex. Since it is sorted, index=0 is the best one and
	 * index=n+1 is the worst one
	 * @param index the index of the point
	 * @return the index-th point of the simplex
	 */
	public EvaluatedSolution get(int index) {
		assert index <= points.size();
		
		return points.get(index);
	}

	
	/**
	 * Returns the vector that goes from a point to another, i.e., the difference
	 * between the points: {@code dest} - {@code origin}
	 * @param origin the origin of the vector
	 * @param dest the destination of the vector
	 * @return the vector that connects those two points
	 */
	public List<Double> getVector (List<Double> origin, List<Double> dest) {
		assert (dest.size() ==  n) && (origin.size() == n);
	 
		List<Double> x = new ArrayList<Double>(n);
		for (int i=0; i<n; i++) {
			x.add(dest.get(i) - origin.get(i));
		}
		
		return x;
	}

	/**
	 * Computes a new point by adding {@code k} times {@code vector} to {@code origin}.
	 * @param origin the origin point
	 * @param vector a displacement vector
	 * @param k the constant determining a multiple of the vector to be added 
	 * @return a point p' = origin + k·vector
	 */
	public EvaluatedSolution getPoint(List<Double> origin, List<Double> vector, double k) {
		assert (vector.size() ==  n) && (origin.size() == n);
		
		List<Double> x = new ArrayList<Double>(n);
		
		for (int i=0; i<n; i++) {
			double o = origin.get(i);
			double d = vector.get(i);
			x.add(Math.min(obj.getMaxValue(i), Math.max(obj.getMinValue(i), o + k*d)));
		}
		
		return new EvaluatedSolution (x, obj.evaluate(x));
	}
	
	/**
	 * Shrinks the simplex
	 * @param s shrink constant
	 */
	public void shrink(double s) {
		List<Double> best = points.get(0).point();
		for (int i=1; i<=n; i++) {
			points.set(i, getPoint(best, getVector(best, points.get(i).point()), s));
		}
		updateCentroid();	
	}
	

	
	@Override
	public String toString() {
		String str = "{\npoints:\n";
		for (EvaluatedSolution sol: points) {
			str += "\t" + sol + "\n";
		}
		str += "centroid: " + centroid + "\n}";
		return str;
	}



	
	

}
