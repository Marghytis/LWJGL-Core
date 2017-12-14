package util.shapes;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import render.*;
import render.VBO.VAP;
import util.Color;
import util.math.Vec;

public class Circle extends Shape {

	private Vec pos;
	private double radius;
	private int corners;
	private Color color;
	
	public Circle(Vec pos, double radius, int corners, Color color) {
		super(createIndexBufferObject(corners), createVertexBufferObjects(pos, radius, corners));
		this.pos = pos;
		this.radius = radius;
		this.corners = corners;
		this.color = color;
	}
	
	public Vec getPos() {
		return pos;
	}
//
//	public void setPos(Vec pos) {
//		this.pos = pos;
//	}

	public double getRadius() {
		return radius;
	}
//
//	public void setRadius(double radius) {
//		this.radius = radius;
//	}

	public int getCorners() {
		return corners;
	}
//
//	public void setCorners(int corners) {
//		this.corners = corners;
//	}

	private static VBO createIndexBufferObject(int corners){
		ByteBuffer indices = BufferUtils.createByteBuffer(corners*3);
			//starts at 1, because 0 is center
			for(int i = 1; i < corners+1; i++){
				indices.put((byte)0);
				indices.put((byte)(i));
				if(i+1==corners+1)
					indices.put((byte)1);
				else
					indices.put((byte)((i+1)));
			}
			indices.flip();
		return new VBO(indices, GL15.GL_STATIC_DRAW);
	}
	
	private static VBO[] createVertexBufferObjects(Vec pos, double r, int corners){
		ByteBuffer vertices = BufferUtils.createByteBuffer((corners+1)*2*Float.BYTES);
			//center first
			vertices.putFloat(0);
			vertices.putFloat(0);
			//now the boundary
			for(int i = 0; i < corners; i++){
				vertices.putFloat((float)(r*Math.cos(Math.PI*2*i/corners)));
				vertices.putFloat((float)(r*Math.sin(Math.PI*2*i/corners)));
			}
			vertices.flip();
		return new VBO[]{new VBO(vertices, GL15.GL_DYNAMIC_DRAW, 2*Float.BYTES, 
				new VAP(2, GL11.GL_FLOAT, false, 0))};
	}

	public void render(Vec offset, Vec scale, float size){
		this.render(offset, scale, 0, size);
	}
	public void render(Vec offset, Vec scale, double z, float size) {
		Shader.simpleShape.bind();

		Shader.simpleShape.set("size", size);
		Shader.simpleShape.set("scale", (float)scale.x, (float)scale.y);
		Shader.simpleShape.set("offset", (float)offset.x, (float)offset.y, (float)z);
		Shader.simpleShape.set("color", color);
		Shader.simpleShape.set("texture", false);
		
		bindStuff();
		GL11.glDrawElements(GL11.GL_TRIANGLES, corners*3, GL11.GL_UNSIGNED_BYTE, 0);
		unbindStuff();
		
		Shader.bindNone();
	}

	
	
}
