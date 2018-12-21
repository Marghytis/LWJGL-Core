package util;

import java.util.ArrayList;
import java.util.List;

public class Time {

	public static List<Event> timers = new ArrayList<>();
	public static long[] time = new long[100];
	public static double[] delta = new double[time.length];
	
	public static double update(int i){
		long newTime = System.nanoTime();
		delta[i] = (newTime - time[i])/1000000000.0;
		time[i] = newTime;
		
		if(i == 0)//thats the main clock for the game loop
		for(int i2 = 0; i2 < timers.size(); i2++){
			Event e = timers.get(i2);
			if(e.timeLeft <= 0){
				e.action.run();
				timers.remove(i2);
				i2--;
			} else {
				e.timeLeft -= delta[i];
			}
		}
		
		return delta[i];
	}
	
	public static double peakDelta(int i) {
		return (System.nanoTime() - time[i])/1000000000.0;
	}
	
	public static void schedule(double time, Runnable action){
		timers.add(new Event(time, action));
	}
	
	public static void start(int i){
		time[i] = System.nanoTime();
	}
	
	public static class Event {
		Runnable action;
		double timeLeft;
		public Event(double time, Runnable action){
			this.timeLeft = time;
			this.action = action;
		}
	}
}
