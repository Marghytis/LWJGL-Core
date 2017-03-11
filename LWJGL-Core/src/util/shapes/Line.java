package util.shapes;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import core.Window;
import render.Shader;
import render.VBO;
import render.VBO.VAP;
import util.Color;
import util.math.Vec;

public class Line extends Shape {

	private int length;
	private Color color;
	
	public Line(int length, Color color){
		this(color, createStartData(length));
	}
	
	public Line(Color color, float[] startData) {
		super(null, createVertexBufferObjects(startData));//createIndexBufferObject(startData.length/2)
		this.length = startData.length/2;
		this.color = color;
	}

	public void render(Vec offset, float size) {
		Shader.line.bind();
		bindStuff();

		Shader.line.set("scale", size/Window.WIDTH_HALF, size/Window.HEIGHT_HALF);
		Shader.line.set("offset", (float)offset.x, (float)offset.y);
		Shader.line.set("color", color);
		GL11.glDrawArrays(GL11.GL_LINE_STRIP, 0, length);
		
		unbindStuff();
		Shader.bindNone();
	}
	
	public void updateData(float[] newData){
		ByteBuffer buffer = vbos[0].getBuffer();
		for(int i = 0; i < newData.length; i++){
			buffer.putFloat(newData[i]);
		}
		buffer.flip();
		vbos[0].update();
	}
	
	private static float[] createStartData(int length){
		float[] out = new float[length];
		for(int i = 0; i < length; i++){
			out[2*i] = 0;
			out[2*i+1] = i*10;
		}
		return out;
	}
	
	private static VBO[] createVertexBufferObjects(float[] startData){
		ByteBuffer vertices = BufferUtils.createByteBuffer(startData.length*Float.BYTES);
		for(int i = 0; i < startData.length; i++){
			vertices.putFloat(startData[i]);
		}
		vertices.flip();
		return new VBO[]{new VBO(vertices, GL15.GL_DYNAMIC_DRAW, 2*Float.BYTES, 
				new VAP(2, GL11.GL_FLOAT, false, 0))};
	}

}
