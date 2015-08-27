package core;

public class SampleMain {

	public static void main(String[] args){
		Core core = new Core("Sample");

		Updater.updaters.add(null);
		Renderer.renderers.add(null);
		Listener.listeners.add(null);
		
		core.coreLoop();
	}
	
}
