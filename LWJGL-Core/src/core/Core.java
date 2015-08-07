package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import util.Color;
import util.Time;
import util.math.Vec;

public class Core {
	
	public Runnable doAfterTheRest;
	
	public Core(String name){
		this(name, Color.BLACK);
	}
	public Core(String name, Color clearColor){
		Window.createMaximized(name, true);
		Renderer.clearColor.set(clearColor);;
		GL11.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
	}
	public Core(String name, Vec windowSize){
		Window.create(name, windowSize.xInt(), windowSize.yInt(), true);
	}
	
	double dt = 1000/60.0, lastTime, tickLength;
	public int sleepTime, noSleepCounter;
	
	public void coreLoop(){
		Time.update(0);//this is used to get the time delta for moving objects
		while(!(Display.isCloseRequested() || Window.closeRequested))
		{

			Time.start(1);
			
			lastTime = System.currentTimeMillis();
			
			Listener.listen();//LISTEN INPUT
			
			Updater.tick();//UPDATE GAME LOGIC
			
			Renderer.render();//RENDER SCENE
			
			if(doAfterTheRest != null){
				doAfterTheRest.run();
				doAfterTheRest = null;
			}
			
			try {
				int sleepTime = (int)(dt - (System.currentTimeMillis() - lastTime));
				if(sleepTime > 0){
					Thread.sleep(sleepTime);//wait until the cycle took enough time for 60 FPS
					noSleepCounter = 0;
				} else {
					noSleepCounter++;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Display.update();//Update screen

			Time.update(1);
		}
		Window.destroy();
		System.exit(0);
	}
}
