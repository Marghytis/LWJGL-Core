package util.math;

public interface Graph {

	public static Graph unitCircle = (v, a) -> v.set(Math.cos(a), Math.sin(a));
	public static Graph unitCircle2 = (v, a) -> v.set(Math.cos(a), Math.sin(a)).shift(0, 1);
	
	public Vec xy(Vec vec, double t);
}
