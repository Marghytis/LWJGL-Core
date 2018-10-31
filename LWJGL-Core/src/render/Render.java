package render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import render.VBO.VAP;
import util.Color;

public class Render {
	
	public static ByteBuffer standardIndex = BufferUtils.createByteBuffer(6);
	static {standardIndex.put(new byte[]{0, 1, 2, 0, 2, 3}); standardIndex.flip();}

	public static ShortBuffer quadIndexBufferShort(int quadCount){
		ShortBuffer buffer = BufferUtils.createShortBuffer(quadCount*6);
		int indicesCount = quadCount*4;
		for(int i = 0; i < indicesCount; i+=4){
			buffer.put((short)(i));
			buffer.put((short)(i+1));
			buffer.put((short)(i+2));

			buffer.put((short)(i));
			buffer.put((short)(i+2));
			buffer.put((short)(i+3));
		}
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer quadIndexBufferInt(int quadCount){
		IntBuffer buffer = BufferUtils.createIntBuffer(quadCount*6);
		int indicesCount = quadCount*4;
		for(int i = 0; i < indicesCount; i+=4){
			buffer.put(i);
			buffer.put(i+1);
			buffer.put(i+2);

			buffer.put(i);
			buffer.put(i+2);
			buffer.put(i+3);
		}
		buffer.flip();
		return buffer;
	}
	
	/**
	 * 
	 * @param in_Position : vec2 perVertex
	 * @param in_TexCoords : vec2 perVertex
	 * @param scale : vec2 uniform
	 * @param texCoords : vec4 uniform - normalized (x, y, w, h)
	 * @param color : vec4 uniform
	 * @param texture : bool uniform - use Texture or not
	 * @return
	 */
	public static VAO quadInScreen(int x1, int y1, int x2, int y2){
		return new VAO(
				new VBO(standardIndex, GL15.GL_STATIC_READ),
				new VBO(createBuffer(new short[]{
						(short)x1, (short)y1, 0, 1,
						(short)x2, (short)y1, 1, 1,
						(short)x2, (short)y2, 1, 0,
						(short)x1, (short)y2, 0, 0}), GL15.GL_STATIC_DRAW, 4*Short.BYTES,
						new VAP(2, GL11.GL_SHORT, false, 0),
						new VAP(2, GL11.GL_SHORT, false, 2*Short.BYTES)));
	}

	public static void drawSingleQuad(VAO vao, Color color, Texture tex, float scaleX, float scaleY, boolean texture){
		drawSingleQuad(vao, color, tex, 0, 0, scaleX, scaleY, texture, 0);
	}
	public static void drawSingleQuad(VAO vao, Color color, Texture tex, double offX, double offY, float scaleX, float scaleY, boolean texture, double z){
		drawSingleQuad(vao, color, tex, offX, offY, scaleX, scaleY, texture, z, 1);
	}
	
	public static void drawSingleQuad(VAO vao, Color color, Texture tex, double offX, double offY, float scaleX, float scaleY, boolean texture, double z, double size){

		Shader.singleQuad.bind();
		Shader.singleQuad.set("offset", (float)offX, (float)offY);
		Shader.singleQuad.set("scale", scaleX, scaleY);
		Shader.singleQuad.set("size", (float)size);
		Shader.singleQuad.set("z", (float)z);
		if(texture){
			tex.file.bind();
			Shader.singleQuad.set("texCoords", tex.texCoords[0], tex.texCoords[1], tex.texCoords[2] - tex.texCoords[0], tex.texCoords[3] - tex.texCoords[1]);
		}
		if(color != null)
			Shader.singleQuad.set("color", color.r, color.g, color.b, color.a);
		else
			Shader.singleQuad.set("color", 1, 1, 1, 1);
		Shader.singleQuad.set("textured", texture);
		vao.bindStuff();
			GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0);
		vao.unbindStuff();
		Color.WHITE.bind();
		TexFile.bindNone();
		Shader.bindNone();
	}

	public static FloatBuffer createBuffer(float... data){
		FloatBuffer out = BufferUtils.createFloatBuffer(data.length);
		out.put(data);
		out.flip();
		return out;
	}
	public static IntBuffer createBuffer(int... data){
		IntBuffer out = BufferUtils.createIntBuffer(data.length);
		out.put(data);
		out.flip();
		return out;
	}
	public static ByteBuffer createBuffer(byte... data){
		ByteBuffer out = BufferUtils.createByteBuffer(data.length);
		out.put(data);
		out.flip();
		return out;
	}
	public static ShortBuffer createBuffer(short... data){
		ShortBuffer out = BufferUtils.createShortBuffer(data.length);
		out.put(data);
		out.flip();
		return out;
	}
//	public static void quad(double x1, double y1, double x2, double y2){
//		GL11.glBegin(GL11.GL_QUADS);
//			GL11.glVertex2d(x1, y1);
//			GL11.glVertex2d(x2, y1);
//			GL11.glVertex2d(x2, y2);
//			GL11.glVertex2d(x1, y2);
//		GL11.glEnd();
//	}
//
//	public static void quad(int x1, int y1, int x2, int y2){
//		GL11.glBegin(GL11.GL_QUADS);
//			GL11.glVertex2i(x1, y1);
//			GL11.glVertex2i(x2, y1);
//			GL11.glVertex2i(x2, y2);
//			GL11.glVertex2i(x1, y2);
//		GL11.glEnd();
//	}
//	
//	public static void bounds(int x1, int y1, int x2, int y2){
//		GL11.glBegin(GL11.GL_LINE_LOOP);
//			GL11.glVertex2i(x1, y1);
//			GL11.glVertex2i(x2, y1);
//			GL11.glVertex2i(x2, y2);
//			GL11.glVertex2i(x1, y2);
//		GL11.glEnd();
//	}
//	
//	public static void bounds(double x1, double y1, double x2, double y2){
//		GL11.glBegin(GL11.GL_LINE_LOOP);
//			GL11.glVertex2d(x1, y1);
//			GL11.glVertex2d(x2, y1);
//			GL11.glVertex2d(x2, y2);
//			GL11.glVertex2d(x1, y2);
//		GL11.glEnd();
//	}
//	static double x1, y1, x2, y2;
//	public static void bounds(int[] coords, double offsetX, double offsetY){
//		x1 = coords[0] + offsetX;
//		y1 = coords[1] + offsetY;
//		x2 = coords[2] + offsetX;
//		y2 = coords[3] + offsetY;
//		GL11.glBegin(GL11.GL_LINE_LOOP);
//			GL11.glVertex2d(x1, y1);
//			GL11.glVertex2d(x2, y1);
//			GL11.glVertex2d(x2, y2);
//			GL11.glVertex2d(x1, y2);
//		GL11.glEnd();
//	}
//	
//	public static void circle(double xS, double yS, double r){
//		GL11.glBegin(GL11.GL_POLYGON);
//			for(int angle = 0; angle < 100; angle++){
//				GL11.glVertex2d(r*UsefulF.cos100[angle] + xS, r*UsefulF.sin100[angle] + yS);
//			}
//		GL11.glEnd();
//	}
}
