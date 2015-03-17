package util.math;

public interface Function {
	
	public static Function cubicStandard = (x) -> 3*x*x - (2*x*x*x);
	
	public static Function parabola(double sX, double sY, double y0){
		return (x) -> -((y0-sY)/(-sX*sX))*(x - sX)*(x - sX) + sY;
	}
	
	/**
	 * creates a parabola starting at (0|0), ending at (x0|0) and having the area of A
	 * @param x0 zero point
	 * @param A integral in the interval [x1;x2]
	 * @return
	 */
	public static Function parabola2(double x0, double A){
		double a = 6*A/(x0*x0);
		double b = -a/x0;
		return (x) -> x*a + (x*x*b);
	}
	
	/**
	 * creates a cubic function starting at (0|0) with slope 0 and ending at (x1|y1) with slope 0
	 * @param x1
	 * @param y1
	 * @return
	 */
	public static Function cubic(double x1, double y1){
		double part = y1/(x1*x1);
		double c = 3*part;
		double d = -2*part/x1;
		return (x) -> x*x*c + (x*x*x*d);
	}
	
	
	/**
	 * Creates the derivative of a function of grade 4 which starts at (0|0), ends at (width|0) and has a peak at (width/2|height)
	 * @param width
	 * @param height
	 * @return
	 */
	public static Function cubic2(double width, double height){
		double a = 32*height/(width*width);
		double b = -3*a/width;
		double c = 2*a/(width*width);
		return (x) -> c*x*x*x + b*x*x + a*x;
	}

	public double f(double x);
}
