package core;

import java.util.*;

import org.lwjgl.opengl.*;

import util.Color;

public interface Renderer {

	public void draw(double beta);
	
	public String debugName();
}
