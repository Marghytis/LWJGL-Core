package util;

public class Time {

	public static long[] time = new long[10], delta = new long[time.length];
	
	public static double update(int i){
		long newTime = System.nanoTime();
		delta[i] = newTime - time[i];
		time[i] = newTime;
		return delta[i]/1000000000.0;
	}
	
	public static void start(int i){
		time[i] = System.nanoTime();
	}
}
