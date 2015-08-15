package util.math;



public class UsefulF {
	
	public static double[] sin = new double[100], cos = new double[100];
	static {
		double step = Math.PI/50;
		for(double angle = 0, i = 0; i < 100; angle += step, i++){
			sin[(int)i] = Math.sin(angle);
			cos[(int)i] = Math.cos(angle);
		}
	}
	public static Function cubicUnit = (x) -> 3*x*x - (2*x*x*x);
	
	public static int cycle(int i, int size){
		int mod = i%size;
		if(mod == 0 || i > 0){
			return 0;
		} else {
			return i%size + size;
		}
	}
	
	public static boolean equalsApprox(double d1, double d2, double equality){
		return (d2 - d1)/d1 <= equality; 
	}
	
	/**
	 * d2 has to be less than d1
	 * @param d1
	 * @param d2
	 * @param equality
	 * @return
	 */
	public static boolean equalOrLessApprox(double d1, double d2, double equality){
		return abs((d2 - d1)/d1) <= equality || d2 < d1; 
	}
	
	public static int abs(int i){
		if(i >= 0){
			return i;
		} else {
			return -i;
		}
	}
	
	public static double abs(double i){
		if(i >= 0){
			return i;
		} else {
			return -i;
		}
	}
	
	/**
	 * This first tests, if the line from A1 to A2 intersects the one from B1 to B2.
	 * If the test succeeds, the method will put the computed intersection point into 'toIntersection'
	 * @param A1
	 * @param A2
	 * @param B1
	 * @param B2
	 * @param toIntersection
	 * @return
	 */
	public static boolean intersectionLines(Vec A1, Vec A2, Vec B1, Vec B2, Vec toIntersection){
		Vec Ad = A2.minus(A1);
		Vec Bd = B2.minus(B1);
		Vec CmA = B1.minus(A1);//C - A
		double BcD = Ad.cross(Bd);//B x D

		if(BcD != 0){
			double f1 = CmA.cross(Bd)/BcD;
			double f2 = CmA.cross(Ad)/BcD;

			if(0 <= f1 && f1 <= 1 && 0 <= f2 && f2 <= 1){
			
				toIntersection.set(A1.copy().shift(Ad, f1));
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param p1 The first point of the line
	 * @param p2 The second point of the line
	 * @param c	Circle middle point
	 * @param r Radius of the circle
	 * @return Vec[0] is left of the center and Vec[1] right. If they're not there, they're null;
	 */
	public static Vec[] circleIntersection(Vec p1, Vec p2, Vec c, double r){
		Vec[] ausgabe = new Vec[2];
		double a, b;
		a = (p2.y - p1.y)/(p2.x - p1.x);
		b = (p1.y - c.y) - ((p1.x - c.x)*a);
		double quadSum = (((a*a)+1)*r*r)-(b*b);
		if(quadSum > 0){
			double xP1 = -((Math.sqrt(quadSum)+(a*b))/((a*a)+1));
			double yP1 = ((a*xP1)+b) + c.y;
			xP1 += c.x;
			double xP2 = ((Math.sqrt(quadSum)-(a*b))/((a*a)+1));
			double yP2 = ((a*xP2)+b) + c.y;
			xP2 += c.x;
			if((p1.x <= xP1 && xP1 <= p2.x)||(p2.x <= xP1 && xP1 <= p1.x)){
				ausgabe[0] = new Vec(xP1, yP1);
			}
			if((p1.x <= xP2 && xP2 <= p2.x)||(p2.x <= xP2 && xP2 <= p1.x)){
				ausgabe[1] = new Vec(xP2, yP2);
			}
		} else if(quadSum == 0){
			double xP = ((-a*b)/((a*a)+1));
			double yP = ((a*xP)+b) + c.y;
			xP += c.x;
			if((p1.x <= xP && xP <= p2.x)||(p2.x <= xP && xP <= p1.x)){
				ausgabe[0] = new Vec(xP, yP);
			}
		}
		return ausgabe;
	}
	
	public static boolean contains(double x0, double y0, double x1, double y1, double x2, double y2){
		return x0 <= x2 && x0 >= x1 && y0 <= y2 && y0 >= y1;
	}
}
