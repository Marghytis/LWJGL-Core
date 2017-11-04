package core;

public class SampleMain {

	public static void main(String[] args){
		CoreOld core = new CoreOld("Sample");

		Updater.updaters.add(null);
		Renderer.renderers.add(null);
		ListenerOld.listeners.add(null);
		
		core.coreLoop();
	}
	
}
