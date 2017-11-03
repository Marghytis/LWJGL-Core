package core;

import java.util.*;

import org.lwjgl.glfw.*;

import util.math.Vec;

public interface Listener3 {

	public static List<Listener3> listeners = new ArrayList<>();
	
	public static Vec mousePos = new Vec();
	static Vec[] buttonsPressed = {new Vec(), new Vec(), new Vec()};

	//Keyboard
	static GLFWKeyCallback key = new GLFWKeyCallback(){
		public void invoke(long window, int key, int scancode, int action, int mods) {
        	if(action == GLFW.GLFW_PRESS){
    			for(Listener3 l : listeners){
    				l.keyPressed(key);
    }}}};
    
    //Cursor position
    static GLFWCursorPosCallback cursorPos = new GLFWCursorPosCallback(){
		public void invoke(long window, double xpos, double ypos) {
			mousePos.set(xpos, ypos);
	}};
	
	//Mouse
	static GLFWMouseButtonCallback mouseButton = new GLFWMouseButtonCallback(){
		public void invoke(long window, int button, int action, int mods) {
			if(button >= 0){
				if(action == GLFW.GLFW_PRESS){
					//MOUSE PRESSED
					for(Listener3 l : listeners){
						if(l.mousePressed(button, mousePos)) break;
					}
					buttonsPressed[button].set(mousePos);
				} else {
					//MOUSE RELEASED
					for(Listener3 l : listeners){
						if(l.mouseReleased(button, mousePos, mousePos.minus(buttonsPressed[button]))) break;
	}}}}};
	
	//Error
	static GLFWErrorCallback errorCallback = errorCallbackPrint(System.err);
	
	public static void init(long window){
		glfwSetKeyCallback(window, key);
		glfwSetCursorPosCallback(window, cursorPos);
		glfwSetMouseButtonCallback(window, mouseButton);
		glfwSetErrorCallback(errorCallback);
	}
	
	public static void exit(){
		key.release();
		cursorPos.release();
		mouseButton.release();
		errorCallback.release();
	}

	public boolean mousePressed(int button, Vec mousePos);
	public boolean mouseReleased(int button, Vec mousePos, Vec mouseDelta);
	public boolean keyPressed(int key);
	
}
