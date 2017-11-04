package util.shapes;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import core.WindowOld;
import render.Render;
import render.Shader;
import render.VBO;
import render.VBO.VAP;
import util.Color;
import util.math.Vec;

public class Quad extends Shape {

	Color color;
	
	public Quad(Vec pos, Vec size, Color color) {
		super(createIndexBufferObject(pos, size), createVertexBufferObjects(pos, size));
		this.color = color;
	}

	public void render(Vec offset, float size){
		this.render(offset, 0, size);
	}
	public void render(Vec offset, double z, float size) {
		Shader.simpleShape.bind();
		bindStuff();

		Shader.simpleShape.set("scale", size/WindowOld.WIDTH_HALF, size/WindowOld.HEIGHT_HALF);
		Shader.simpleShape.set("offset", (float)offset.x, (float)offset.y, (float)z);
		Shader.simpleShape.set("color", color);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0);
		
		unbindStuff();
		Shader.bindNone();
	}
	
	public void update(Vec pos, Vec size){
		ByteBuffer vertices = vbos[0].getBuffer();
		vertices.putFloat((float)(pos.x));
		vertices.putFloat((float)(pos.y));
		vertices.putFloat((float)(pos.x + size.x));
		vertices.putFloat((float)(pos.y));
		vertices.putFloat((float)(pos.x + size.x));
		vertices.putFloat((float)(pos.y + size.y));
		vertices.putFloat((float)(pos.x));
		vertices.putFloat((float)(pos.y + size.y));
		vertices.flip();
		vbos[0].update();
	}

	private static VBO createIndexBufferObject(Vec pos, Vec size){
		return new VBO(Render.standardIndex, GL15.GL_STATIC_DRAW);
	}
	
	private static VBO[] createVertexBufferObjects(Vec pos, Vec size){
		ByteBuffer vertices = BufferUtils.createByteBuffer((4)*2*Float.BYTES);
			vertices.putFloat((float)(pos.x));
			vertices.putFloat((float)(pos.y));
			vertices.putFloat((float)(pos.x + size.x));
			vertices.putFloat((float)(pos.y));
			vertices.putFloat((float)(pos.x + size.x));
			vertices.putFloat((float)(pos.y + size.y));
			vertices.putFloat((float)(pos.x));
			vertices.putFloat((float)(pos.y + size.y));
			vertices.flip();
		return new VBO[]{new VBO(vertices, GL15.GL_STATIC_DRAW, 2*Float.BYTES, 
				new VAP(2, GL11.GL_FLOAT, false, 0))};
	}

}
