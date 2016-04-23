package core;

import java.util.ArrayList;
import java.util.List;

import util.Time;

public interface Updater {

	public static List<Updater> updaters = new ArrayList<>();
	
	public static void tick(){
		double delta = Time.update(0);
		for(Updater u : updaters){
			Core.checkGLErrors(true, true, "before updating " + u.debugName());
			if(u.update(delta)){
				Core.checkGLErrors(true, true, "after updating " + u.debugName());
				break;
			} else {
				Core.checkGLErrors(true, true, "after updating " + u.debugName());
			}
		}
	}
	
	public boolean update(double delta);
	
	public String debugName();
}
