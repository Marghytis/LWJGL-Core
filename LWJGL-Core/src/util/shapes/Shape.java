package util.shapes;

import render.VAO;
import render.VBO;
import util.math.Vec;

public abstract class Shape extends VAO{

	public Shape(VBO indices, VBO... vbos) {
		super(indices, vbos);
	}

	public abstract void render(Vec offset, float size);
	
}
