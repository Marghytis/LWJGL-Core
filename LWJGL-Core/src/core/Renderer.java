package core;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import util.Color;

public interface Renderer {

	Color clearColor = new Color(Color.BLACK);
	
	public static List<Renderer> renderers = new ArrayList<>();
	
	public static void render(){
		//reset OpenGL
		if(Core.clear) GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		for(Renderer r : renderers){
			Core.checkGLErrors(true, true, "before rendering " + r.debugName());
			r.draw();
			Core.checkGLErrors(true, true, "after rendering " + r.debugName());
		}
	}
	
	public void draw();
	
	public String debugName();
}
