package core;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

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
		GLFW.glfwSetKeyCallback(window, key);
		GLFW.glfwSetCursorPosCallback(window, cursorPos);
		GLFW.glfwSetMouseButtonCallback(window, mouseButton);
		GLFW.glfwSetErrorCallback(errorCallback);
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
