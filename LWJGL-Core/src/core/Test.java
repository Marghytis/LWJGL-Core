package core;

import java.awt.Font;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import render.Framebuffer;
import render.Render;
import render.Shader;
import render.TexAtlas;
import render.TexFile;
import render.TexQuad;
import render.Texture;
import render.VAO;
import render.VBO;
import render.VBO.VAP;
import util.Color;
import util.TrueTypeFont;
import util.math.Vec;
import util.shapes.Circle;

public class Test implements Renderer {

	public static void main(String[] args){
		
		Window.contextAttribs = new ContextAttribs(3, 3)
		    .withForwardCompatible(true)
		    .withProfileCore(true);
		Core core = new Core("OpenGL test");System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
		Test main = new Test();
		Renderer.renderers.add(main);
		main.init();
		core.coreLoop();
	}

	public Shader shader = Shader.internalWithGeometry("/res/shader/sprite.vert", "/res/shader/sprite.geom", "/res/shader/sprite.frag", "in_position", "in_rotation", "in_texCoords", "in_mirror", "in_color", "in_z", "in_size");
	public Texture tex = new TexAtlas(new TexFile("/res/Meteor.png", true), 4, 1, -0.5f, -0.5f).texs[0];
	public TrueTypeFont font = new TrueTypeFont(new Font("Times New Roman", 0, 40), true);
	//in_position	: vec2
	//in_rotation	: float
	//in_texCoords	: vec2
	//in_mirror		: float
	//in_color		: vec4
	//in_z			: float
	//in_size		: float
	
	VBO vbo;
	VAO vao;
	float[] vertices = {
	        0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1
//	        0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1
	};
	public int vertexCount, indexCount;
	
	VAO singleQuad = Render.quadInScreen((short)(-500), (short)(-500), (short)(500), (short)(500));
	
	public void init(){
		vertexCount = 1;
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertexCount*12);
		buffer.put(vertices);
		buffer.flip();
		vbo = new VBO(buffer, GL15.GL_STATIC_DRAW, 12*Float.BYTES,
				new VAP(2, GL11.GL_FLOAT, false, 0),//vec2 in_position
				new VAP(1, GL11.GL_FLOAT, false, 2*Float.BYTES),//float in_rotation
				new VAP(2, GL11.GL_FLOAT, false, 3*Float.BYTES),//vec2 in_texCoords
				new VAP(1, GL11.GL_FLOAT, false, 5*Float.BYTES),//float in_mirror
				new VAP(4, GL11.GL_FLOAT, false, 6*Float.BYTES),//vec4 in_color
				new VAP(1, GL11.GL_FLOAT, false, 10*Float.BYTES),//float in_z
				new VAP(1, GL11.GL_FLOAT, false, 11*Float.BYTES));//float in_size
		vao = new VAO(null, vbo);
		
		circle = new Circle(new Vec(), 100, 50, Color.BLUE);
		framebuffer = new Framebuffer("Frame", Window.WIDTH, Window.HEIGHT);
		quad = Render.quadInScreen((short)-Window.WIDTH_HALF, (short)-Window.HEIGHT_HALF, (short)Window.WIDTH_HALF, (short)Window.HEIGHT_HALF);
		
		quad2 = new TexQuad(tex);
	}
	
	Circle circle;
	Framebuffer framebuffer;
	VAO quad;
	TexQuad quad2;

	public void draw() {
		
//		shader.bind();
//		tex.file.bind();
//		shader.set("scale", 1f/Window.WIDTH_HALF, 1f/Window.HEIGHT_HALF);
//		shader.set("offset", 0, 0);
//		shader.set("box", -50, -50, 50, 50);
//		shader.set("texWH", tex.texCoords[2] - tex.texCoords[0], tex.texCoords[3] - tex.texCoords[1]);
//		vao.bindStuff();
//				GL11.glDrawArrays(GL11.GL_POINTS, 0, vertexCount);
//		vao.unbindStuff();
//		TexFile.bindNone();
//		Shader.bindNone();
		
//		//Render a single quad with a texture
//		Render.drawSingleQuad(singleQuad, Color.GREEN, tex, 1f/Window.WIDTH_HALF, 1f/Window.HEIGHT_HALF, true);
//		
//		//Render a String
//		String test = "012345Test im Testingh)(&()()()(){} stuff. \n .-#?!)({}\n\nHuhu!!!!876543210";
//		test = "0123456789";
//		font.drawString(0, 0, test, Color.GREEN, 0, test.length()-1, 1, 1, TrueTypeFont.ALIGN_CENTER);
//		
//		//Render a circle shape to a framebuffer and then render the framebuffer to the screen
//		framebuffer.bind();
//		circle.render(new Vec(), 1);
//		Framebuffer.bindNone();
//		Render.drawSingleQuad(quad, Color.WHITE, framebuffer.getTex(), 1f/Window.WIDTH_HALF, 1f/Window.HEIGHT_HALF, true);
//		
		//render a texQuad
		quad2.update(new Vec(100, 0), 0.5, 2, true);
		quad2.render(new Vec(), 0, new Vec(1f/Window.WIDTH_HALF, 1f/Window.HEIGHT_HALF), Color.YELLOW);
	}
	
	public void after(){
		GL20.glDisableVertexAttribArray(0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(ibo);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbo);
		
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vao);
	}

	public String debugName() {
		return "LWJGL-Core-Tester";
	}
}
