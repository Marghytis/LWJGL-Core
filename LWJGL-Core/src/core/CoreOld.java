package core;

import org.lwjgl.opengl.GL11;

import util.*;
import util.math.Vec;

public class CoreOld {

	public static boolean clear = true;
	
	public Runnable doAfterTheRest;
	
	public CoreOld(){}
	
	public CoreOld(Color clearColor){
		Renderer.clearColor.set(clearColor);
		GL11.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
	}
	
	public CoreOld(String name){
		this(name, Color.BLACK);
	}
	public CoreOld(String name, Color clearColor){
		this(name, clearColor, true);
	}
	public CoreOld(String name, Color clearColor, boolean visible){
		WindowOld.createMaximized(name, true, visible);
		Renderer.clearColor.set(clearColor);
		GL11.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
	}
	public CoreOld(String name, Vec windowSize){
		WindowOld.create(name, windowSize.xInt(), windowSize.yInt(), true);
	}
	
	double dt = 1000/60.0, lastTime, tickLength;
	public int sleepTime, noSleepCounter;
	
	public void coreLoop(){
		checkGLErrors(true, true, "after initialisation");
		Time.update(0);//this is used to get the time delta for moving objects
		Time.start(1);
		Time.update(1);
		while(!(Display.isCloseRequested() || WindowOld.closeRequested))
		{

			Time.update(1);
			
			lastTime = System.currentTimeMillis();
			
			ListenerOld.listen();//LISTEN INPUT
			
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

			checkGLErrors(true, true, "at end of core loop");
			
		}
		WindowOld.destroy();
		System.exit(0);
	}
	
	public static boolean checkGLErrors(boolean exit, boolean print, String when){
		int errorValue;
		boolean foundSomething = false;
		String out = "";
		 while ((errorValue = GL11.glGetError()) != GL11.GL_NO_ERROR) {
			 if(print){
				 if(!foundSomething) out += "FOUND ERROR(S) " + when + ":";
		         out += "\n   - " + GLU.gluErrorString(errorValue);
			 }
			 foundSomething = true;
        }
		 if(foundSomething && print){
			 new Exception(out).printStackTrace();
		 }
		if(foundSomething && exit){
			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}
		return foundSomething;
	}
}
