package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import util.Color;
import util.Time;
import util.math.Vec;

public class Core {
	
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
	
	double dt = 1/60.0;
	
	public void coreLoop(){
		Time.update(0);
		while(!(Display.isCloseRequested() || Window.closeRequested))
		{
			Time.start(1);
			Listener.listen();//LISTEN
			Time.update(1);
			
			Time.start(2);
			Updater.tick();//UPDATE
			Time.update(2);
			
			Time.start(3);
			Renderer.render();//RENDER
			Time.update(3);
			
			Time.start(4);
//			Display.sync();//wait for next round
			try {
				Thread.sleep(Math.max((int)((dt - Time.delta[0])*1000), 0));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Time.update(4);

			Time.start(5);
			Display.update();//Update screen
			Time.update(5);
		}
		Window.destroy();
		System.exit(0);
	}
}
