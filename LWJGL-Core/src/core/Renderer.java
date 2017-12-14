package core;

import java.util.*;

import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;

import util.Color;

public interface Renderer {

	static Color clearColor = new Color(Color.BLACK);
	
	public static List<Renderer> renderers = new ArrayList<>();
	
	public static void render(){
		//reset OpenGL
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		//forward the render call to all renderers
		for(Renderer r : renderers){
			r.draw();
		}
	}
	
	public static void init(Color clearColor, int width, int height){
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
	
	public static void setClearColor(Color color){
		clearColor.set(color);
		GL11.glClearColor(color.r, color.g, color.b, color.a);
	}
	
	public void draw();
	
	public String debugName();
}
