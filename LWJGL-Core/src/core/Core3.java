package core;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.opengl.GL11;

import util.Color;
import util.Time;

public class Core3 extends Core {


	private double dt = 1000/60.0, lastTime, tickLength;
	private int sleepTime, noSleepCounter;
	
	private Runnable doAfterTheRest;
	private Window3 window;

	public Core3(Window3 window, Color clearColor) {
		this.window = window;
		Renderer.clearColor.set(clearColor);
		GL11.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
	}
	
	public void init(){
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
	}

	public void coreLoop(){
		checkGLErrors(true, true, "after initialisation");
		Time.update(0);//this is used to get the time delta for moving objects
		Time.start(1);
		Time.update(1);
		while(!(Display.isCloseRequested() || Window.closeRequested))
		{

			Time.update(1);
			
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

			checkGLErrors(true, true, "at end of core loop");
			
		}
		Window.destroy();
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
