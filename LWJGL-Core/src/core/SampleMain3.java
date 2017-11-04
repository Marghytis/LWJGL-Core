package core;

import util.Color;

public class SampleMain3 {

	public static void main(String[] args){
		Core core = new Core("Witch.png");
		core.init(new Window("Sample", true), Color.BLACK);

//		Updater.updaters.add(null);
//		Renderer.renderers.add(null);
//		Listener3.listeners.add(null);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		core.coreLoop();
	}
	
}
