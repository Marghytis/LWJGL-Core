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
		GL11.glLoadIdentity();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		renderers.forEach((r) -> r.draw());
	}
	
	public void draw();
}
