package core;

import java.util.*;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

import input.PollData;
import util.math.Vec;

public class Input {

	public List<Listener> listeners = new ArrayList<>();

	Hashtable<Long, PollData> pollData = new Hashtable<>();
	
	//characters
	GLFWCharCallbackI ch =
			(long window, int ch) -> {
				for(Listener l : listeners){
					l.charTyped((char)ch);
				}
			};
			

	//Keyboard
	GLFWKeyCallbackI key =
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
	GLFWCursorPosCallbackI cursorPos = 
			(long window, double xpos, double ypos) -> {
				pollData.get(window).setMousePos(xpos, ypos);
			};
	
	//Mouse
	GLFWMouseButtonCallbackI mouseButton = 
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
	GLFWScrollCallbackI scroll = 
			(long window, double dx, double dy) -> {
				pollData.get(window).scrollOffset += dy;
			};
		
	public void listen(){
		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
	}
	
	public Vec getMousePos(long window){
		return pollData.get(window).getMousePos();
	}
	
	public double getDWheel(long window){
		return (int)pollData.get(window).getDWheel();
	}
	
	public void resetDeltas(long window) {
		pollData.get(window).resetDeltas();
	}
	
	public boolean isKeyDown(long window, int key){
		return pollData.get(window).isKeyDown(key);
	}
	
	public void connectToWindow(long window){
		glfwSetKeyCallback(window, key);
		glfwSetCursorPosCallback(window, cursorPos);
		glfwSetMouseButtonCallback(window, mouseButton);
		glfwSetScrollCallback(window, scroll);
		glfwSetCharCallback(window, ch);
	}
}
