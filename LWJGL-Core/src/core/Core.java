package core;

import javax.swing.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.*;

import static org.lwjgl.glfw.GLFW.*;

import util.*;
import util.math.IntVec;

public class Core {

	public IntVec SIZE_HALF, SIZE;
	private double dt = 1000/60.0, lastTime, tickLength;
	private int sleepTime, noSleepCounter;
	private long device;
	private long context;
	
	private Runnable doAfterTheRest;
	private JDialog splashScreen;
	private Window window;

	public Core(String splashScreenTexPath) {
		showSplashScreen(splashScreenTexPath);
		initGLFW();
		initOpenAL();
	}
	
	private void showSplashScreen(String filePath){
		splashScreen = new JDialog();
		splashScreen.setUndecorated(true);
		splashScreen.add(new JLabel( new ImageIcon(filePath) ));
		splashScreen.pack();
		splashScreen.setLocationRelativeTo(null);
		splashScreen.setVisible(true);
	}
	
	public void init(Window window, Color clearColor){
		this.window = window;
		this.SIZE_HALF = window.SIZE_HALF;
		this.SIZE = window.SIZE;
		Renderer.init(clearColor, window.SIZE.w, window.SIZE.h);
		Listener.connectToWindow(window);
	}
	
	public void coreLoop(){
		
		if ( window == null ){
			new IllegalStateException("No window defined").printStackTrace();
			System.exit(1);
		}
		
		if ( splashScreen != null ){
			window.show();
			splashScreen.setVisible(false);
			splashScreen.dispose();
		}
		
		Time.update(0);//this is used to get the time delta for moving objects
		Time.start(1);
		Time.update(1);
		while(!(glfwWindowShouldClose(window.getHandle())))//TODO remove double functionality for exiting
		{

			Time.update(1);
			
			lastTime = System.currentTimeMillis();
			
			Listener.listen();//LISTEN INPUT
			
			Updater.tick();//UPDATE GAME LOGIC
			
			Renderer.render();//RENDER SCENE
			
			//some other action can be performed after this cycle
			if(doAfterTheRest != null){
				doAfterTheRest.run();
				doAfterTheRest = null;
			}
			
			//get exactly 60 fps
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

			window.swapBuffers();// swap the color buffers
		}
		
		//When the WHILE-loop has ended, exit the program
		exit();
	}
	
	public void doAfterTheRest(Runnable run){
		this.doAfterTheRest = run;
	}
	
	//TODO add resource deletion
	private void exit(){
		
		window.terminate();
		
		finalizeGLFW();
		finalizeOpenAL();
		System.exit(0);
	}
	
	/**
	 * Has to be called first, especially before the Window is created
	 */
	private void initGLFW(){
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
	}
	
	private void initOpenAL() {

		String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		device = ALC10.alcOpenDevice(defaultDeviceName);
		int[] attributes = {0};
		context = ALC10.alcCreateContext(device, attributes);
		ALC10.alcMakeContextCurrent(context);
		
		ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
		AL.createCapabilities(alcCapabilities);

	}
	
	private void finalizeGLFW() {
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	private void finalizeOpenAL() {
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}
	
	public Window getWindow(){
		return window;
	}
}
