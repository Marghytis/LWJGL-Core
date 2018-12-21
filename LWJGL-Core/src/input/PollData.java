package input;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

import util.math.Vec;

public class PollData {
	public Vec mousePos = new Vec();
	public Vec[] buttonsPressed = {new Vec(), new Vec(), new Vec()};
	public double scrollOffset;
	private int windowHeight;
	private long window;
	
	public PollData(long window, int windowHeight){
		this.window = window;
		this.windowHeight = windowHeight;
	}

	public Vec getMousePos(){
		return mousePos.copy();
	}
	
	public void setMousePos(double x, double y){
		this.mousePos.set(x, windowHeight - y);
	}
	
	public boolean isMousePressed(int button) {
		return glfwGetMouseButton(window, button) == GLFW_PRESS;
	}
	
	public double getDWheel(){
		return (int)scrollOffset;
	}
	
	public void resetDeltas() {
		scrollOffset = 0;
	}
	
	public boolean isKeyDown(int key){
		return glfwGetKey(window, key) == GLFW_PRESS;
	}
}