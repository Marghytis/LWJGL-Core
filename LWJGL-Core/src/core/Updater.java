package core;

import java.util.ArrayList;
import java.util.List;

import util.Time;

public interface Updater {

	public static List<Updater> updaters = new ArrayList<>();
	
	public static void tick(){
		double delta = Time.update(0);
		for(Updater u : updaters){
			if(u.update(delta))
				break;
		}
	}
	
	public boolean update(double delta);
	
	public String debugName();
}
