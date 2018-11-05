package core;

import java.util.*;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

import util.math.Vec;

public interface Listener {

	public static List<Listener> listeners = new ArrayList<>();
	
	static Hashtable<Long, PollData> pollData = new Hashtable<>();
	static class PollData {
		public Vec mousePos = new Vec();
		public Vec[] buttonsPressed = {new Vec(), new Vec(), new Vec()};
		public double scrollOffset;
		private int windowHeight;
		
		public PollData(int windowHeight){
			this.windowHeight = windowHeight;
		}
		
		public void setMousePos(double x, double y){
			this.mousePos.set(x, windowHeight - y);
		}
	}
	
	//characters
	static GLFWCharCallbackI ch =
			(long window, int ch) -> {
				for(Listener l : listeners){
					l.charTyped((char)ch);
				}
			};
			

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
				pollData.get(window).setMousePos(xpos, ypos);
			};
	
	//Mouse
	static GLFWMouseButtonCallbackI mouseButton = 
			(long window, int button, int action, int mods) -> {
				if(button >= 0){
					if(action == GLFW.GLFW_PRESS){
						//MOUSE PRESSED
						for(Listener l : listeners){
							if(l.pressed(button, pollData.get(window).mousePos)) break;
						}
						pollData.get(window).buttonsPressed[button].set(pollData.get(window).mousePos);
					} else {
						//MOUSE RELEASED
						for(Listener l : listeners){
							if(l.released(button, pollData.get(window).mousePos, pollData.get(window).mousePos.minus(pollData.get(window).buttonsPressed[button]))) break;
						}
					}
				}
			};
		
	//Scrolling
	static GLFWScrollCallbackI scroll = 
			(long window, double dx, double dy) -> {
				pollData.get(window).scrollOffset += dy;
			};
			
	public static void listen(){
		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
	}
	
	public static Vec getMousePos(long window){
		return pollData.get(window).mousePos.copy();
	}
	
	public static double getDWheel(long window){
		double out = (int)pollData.get(window).scrollOffset;
		pollData.get(window).scrollOffset = 0;
		return out;
	}
	
	public static boolean isKeyDown(long window, int key){
		return glfwGetKey(window, key) == GLFW_PRESS;
	}
	
	public static void connectToWindow(Window window){
		glfwSetKeyCallback(window.getHandle(), key);
		glfwSetCursorPosCallback(window.getHandle(), cursorPos);
		glfwSetMouseButtonCallback(window.getHandle(), mouseButton);
		glfwSetScrollCallback(window.getHandle(), scroll);
	}

	public boolean pressed(int button, Vec mousePos);
	public boolean released(int button, Vec mousePos, Vec mouseDelta);
	public boolean keyPressed(int key);
	public boolean keyReleased(int key);
	public boolean charTyped(char ch);
}
