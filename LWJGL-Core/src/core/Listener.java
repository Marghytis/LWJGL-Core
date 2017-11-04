package core;

import java.util.*;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

import util.math.Vec;

public interface Listener {

	public static List<Listener> listeners = new ArrayList<>();
	
	public static Vec mousePos = new Vec();
	static Vec[] buttonsPressed = {new Vec(), new Vec(), new Vec()};

	//Keyboard
	static GLFWKeyCallbackI key =
			(long window, int key, int scancode, int action, int mods) -> {
				//forward the events to the listeners
				if ( action == GLFW_PRESS ){
					//key pressed
					for(Listener l : listeners){
						l.keyPressed(key);
					}

				} else {
					//key released
					for(Listener l : listeners){
						l.keyReleased(key);
					}
				}
			};

	//Cursor position
	static GLFWCursorPosCallbackI cursorPos = 
			(long window, double xpos, double ypos) -> {
				mousePos.set(xpos, ypos);
			};
	
	//Mouse
	static GLFWMouseButtonCallbackI mouseButton = 
			(long window, int button, int action, int mods) -> {
				if(button >= 0){
					if(action == GLFW.GLFW_PRESS){
						//MOUSE PRESSED
						for(Listener l : listeners){
							if(l.mousePressed(button, mousePos)) break;
						}
						buttonsPressed[button].set(mousePos);
					} else {
						//MOUSE RELEASED
						for(Listener l : listeners){
							if(l.mouseReleased(button, mousePos, mousePos.minus(buttonsPressed[button]))) break;
						}
					}
				}
			};
			
	public static void listen(){
		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
	}
	
	public static void connectToWindow(Window window){
		glfwSetKeyCallback(window.getHandle(), key);
		glfwSetCursorPosCallback(window.getHandle(), cursorPos);
		glfwSetMouseButtonCallback(window.getHandle(), mouseButton);
	}

	public boolean mousePressed(int button, Vec mousePos);
	public boolean mouseReleased(int button, Vec mousePos, Vec mouseDelta);
	public boolean keyPressed(int key);
	public boolean keyReleased(int key);
}
