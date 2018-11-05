package core;

import java.nio.*;

import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.glfw.GLFW.*;

import core.Listener.PollData;
import util.Util;
import util.math.IntVec;

public class Window {

	// The window handle
	private long window;
	public IntVec SIZE = new IntVec(), SIZE_HALF = new IntVec();
	
	/**
	 * Creates a maximized window
	 * @param title
	 */
	public Window(String title, boolean iconsInternal){
		this(title, iconsInternal, 1, 1, true);
	}

	public Window(String title, boolean iconsInternal, int width, int height, boolean maximized, int... hints){

		setSize(width, height);
		
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		if(maximized)
			glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		for(int i = 0; i < hints.length; i += 2){
			glfwWindowHint(hints[i], hints[i+1]);
		}

		// Create the window
		window = glfwCreateWindow(width, height, title, NULL, NULL);
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
//		// Enable v-sync
//		glfwSwapInterval(1);
		
		Listener.pollData.put(window, new PollData(SIZE.h));
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
	}
	
}
