package core;

import java.nio.*;
import java.util.*;

import javax.swing.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.glfw.GLFW.*;

import input.PollData;
import util.*;
import util.math.IntVec;

public class Window {

	Input input;
	// The window handle
	private long window;
	public IntVec SIZE = new IntVec(), SIZE_HALF = new IntVec();
	
	/**
	 * Creates a maximized window
	 * @param title
	 */
	public Window(String title, boolean iconsInternal, Input input){
		this(title, iconsInternal, 1, 1, true, input);
	}

	String title;
	boolean iconsInternal, maximized, vSync;
	int[] hints;
	PollData inputData;
	
	public Window(String title, boolean iconsInternal, int width, int height, boolean maximized, Input input, int... hints){
		this(title, iconsInternal, width, height, maximized, false, input, hints);
	}
	public Window(String title, boolean iconsInternal, int width, int height, boolean maximized, boolean vSync, Input input, int... hints){
		this.title = title;
		this.iconsInternal = iconsInternal;
		this.maximized = maximized;
		this.vSync = vSync;
		this.hints = hints;
		setSize(width, height);
		this.input = input;
	}

	public void init() {
		
		initGLFW();
		
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		if(maximized)
			glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		for(int i = 0; i < hints.length; i += 2){
			glfwWindowHint(hints[i], hints[i+1]);
		}

		// Create the window
		window = glfwCreateWindow(SIZE.w, SIZE.h, title, NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		if(!maximized){
			centralize();
		} else {
			// Get the thread stack and push a new frame
			try ( MemoryStack stack = stackPush() ) {
				IntBuffer pWidth = stack.mallocInt(1); // int*
				IntBuffer pHeight = stack.mallocInt(1); // int*

				// Get the window size passed to glfwCreateWindow
				glfwGetWindowSize(window, pWidth, pHeight);
				setSize(pWidth.get(0), pHeight.get(0));
			}
		}
		
		ByteBuffer icon = null;
		IntVec sizeVector = new IntVec();
		GLFWImage image = null;
		GLFWImage.Buffer imagebf = GLFWImage.malloc(3);
		for(int i = 0, size = 64; i < 3; i++, size /= 2){
			icon = Util.readFile((iconsInternal ? "/" : "") + "res/icons/Icon" + size + ".png", iconsInternal, sizeVector);
			if(icon != null){
				image = GLFWImage.malloc();
				image.set(sizeVector.w, sizeVector.h, icon);
				imagebf.put(i, image);
				image.free();
			}
		}
		if(image != null) {
			glfwSetWindowIcon(window, imagebf);
		} else {
			new Exception("Icons not found!").printStackTrace();
		}
        imagebf.flip();
		imagebf.free();
		

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		if(vSync)
			glfwSwapInterval(1);
		
		
		initOpenGL(SIZE.w, SIZE.h);

		inputData = new PollData(window, SIZE.h);
		input.pollData.put(window, inputData);
		input.connectToWindow(window);
	}
	
	public void listen() {
		input.listen();
	}
	
	public void setListeners(Listener... listeners) {
		input.listeners.clear();
		for(Listener l : listeners) {
			input.listeners.add(l);
		}
	}

	private static JDialog splashScreen;
	public static void showSplashScreen(String filePath){
		splashScreen = new JDialog();
		splashScreen.setUndecorated(true);
		splashScreen.add(new JLabel( new ImageIcon(filePath) ));
		splashScreen.pack();
		splashScreen.setLocationRelativeTo(null);
		splashScreen.setVisible(true);
	}
	
	public static void removeSplashScreen() {
		splashScreen.setVisible(false);
		splashScreen.dispose();
	}
	
	private void initGLFW(){
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
	}
	
	public void requestClose(){
		glfwSetWindowShouldClose(window, true);
	}
	
	public void setSize(int width, int height){
		SIZE.w = width;
		SIZE.h = height;
		SIZE_HALF.w = width/2;
		SIZE_HALF.h = height/2;
	}
	
	public void centralize(){
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically
	}
	
	public void show() {
		if(splashScreen != null) {
			removeSplashScreen();
		}
		glfwShowWindow(window);
	}
	
	public void hide() {
		glfwHideWindow(window);
	}
	
	public void swapBuffers() {
		glfwSwapBuffers(window);
	}
	
	public long getHandle(){
		return window;
	}
	
	public void terminate(){

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}


	Color clearColor = new Color(Color.BLACK);
	
	List<Renderer> renderers = new ArrayList<>();
	
	public void render(double beta){
		//reset OpenGL
		if(Configuration.clear) GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//forward the render call to all renderers
		for(Renderer r : renderers){
			r.draw(beta);
		}
	}
	
	public void initOpenGL(int width, int height) {
		initOpenGL(Color.BLACK, width, height);
	}
	public void initOpenGL(Color clearColor, int width, int height){
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		//Set the clear color
		setClearColor(clearColor);
		//set the viewport
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glCullFace(GL11.GL_FRONT_AND_BACK);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void setClearColor(Color color){
		clearColor.set(color);
		GL11.glClearColor(color.r, color.g, color.b, color.a);
	}
	
	public void setRenderers(Renderer...renderers) {
		this.renderers.clear();
		
		for(Renderer r : renderers) {
			this.renderers.add(r);
		}
	}
	
	public PollData getPollData() {
		return inputData;
	}
}
