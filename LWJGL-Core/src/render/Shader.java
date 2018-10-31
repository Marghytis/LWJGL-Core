package render;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Hashtable;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import util.Color;

public class Shader {
	/**UNIFORMS: offset: vec2, scale: vec2, texCoords: vec4, color: vec4, textured: bool, z: float*/
	public static Shader singleQuad = Shader.internalFiles("/res/shader/singleQuad.vert", "/res/shader/singleQuad.frag", "in_Position", "in_TexCoords");
	/**UNIFORMS: scale: vec2, stuff=(height, zero, xOffset): vec3, colorHigh: vec4, colorLow: vec4*/
	public static Shader graph = Shader.internalFiles("/res/shader/graph.vert", "/res/shader/graph.frag", "in_X", "in_Value");
	/**UNIFORMS: scale: vec2, offset: vec3, color: vec4, texture: bool, size: float*/
	public static Shader simpleShape = Shader.internalFiles("/res/shader/SimpleShape.vert", "/res/shader/SimpleShape.frag", "in_Position");
	/**UNIFORMS: scale: vec2, offset: vec2, color: vec4*/
	public static Shader line = Shader.internalFiles("/res/shader/Line.vert", "/res/shader/Line.frag", "in_Position");
	/**UNIFORMS: scale: vec2, offset: vec2, box: vec4(right, up, left down), texWH: vec2*/
	public static Shader sprite = Shader.internalWithGeometry("/res/shader/sprite.vert", "/res/shader/sprite.geom", "/res/shader/sprite.frag", "in_position", "in_rotation", "in_texCoords", "in_mirror", "in_color", "in_z", "in_size");

	public static Shader current = null;

	public void set(String field, boolean boo){
		GL20.glUniform1i(uniformLoc(field), boo ? 1 : 0);
	}
	public void set(String field, float f1){
		GL20.glUniform1f(uniformLoc(field), f1);
	}
	public void seti(String field, int i1){
		GL20.glUniform1i(uniformLoc(field), i1);
	}
	public void set(String field, float f1, float f2){
		GL20.glUniform2f(uniformLoc(field), f1, f2);
	}
	public void set(String field, float f1, float f2, float f3){
		GL20.glUniform3f(uniformLoc(field), f1, f2, f3);
	}
	public void set(String field, float f1, float f2, float f3, float f4){
		GL20.glUniform4f(uniformLoc(field), f1, f2, f3, f4);
	}
	public void set(String field, Color color){
		GL20.glUniform4f(uniformLoc(field), color.r, color.g, color.b, color.a);
	}
	public void set(String field, int i1){
		GL20.glUniform1i(uniformLoc(field), i1);
	}
	public void set2x2(String field, FloatBuffer mat2){
		GL20.glUniformMatrix2fv(uniformLoc(field), true, mat2);
	}
	public void set3x3(String field, FloatBuffer mat3){
		GL20.glUniformMatrix3fv(uniformLoc(field), true, mat3);
	}
	public void set2(String field, FloatBuffer floats){
		GL20.glUniform2fv(uniformLoc(field), floats);
	}
	public void set2(String field, IntBuffer ints){
		GL20.glUniform2iv(uniformLoc(field), ints);
	}
	public void set4(String field, FloatBuffer floats){
		GL20.glUniform4fv(uniformLoc(field), floats);
	}
	
	public int uniformLoc(String field){
		Integer out = uniforms.get(field);
		if(out == null){
			out = GL20.glGetUniformLocation(handle, field);
			uniforms.put(field, out);
		}
		return out;
	}
	
	private int handle;
	private Hashtable<String, Integer> uniforms = new Hashtable<>();
	
	private Shader(int handle){
		this.handle = handle;
	}
	
	public static Shader create(String pathVert, String pathFrag, String... attribs){
		try {
			return new Shader(createShader(readExternalFile(pathVert), readExternalFile(pathFrag), attribs));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Shader withGeometry(String pathVert, String pathGeom, String pathFrag, String... attribs){
		try {
			return new Shader(createShaderGeom(readExternalFile(pathVert), readExternalFile(pathGeom), readExternalFile(pathFrag), attribs));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Shader internalFiles(String pathVert, String pathFrag, String... attribs){
		try {
			return new Shader(createShader(readInternalFile(pathVert), readInternalFile(pathFrag), attribs));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Shader internalWithGeometry(String pathVert, String pathGeom, String pathFrag, String... attribs){
		try {
			return new Shader(createShaderGeom(readInternalFile(pathVert), readInternalFile(pathGeom), readInternalFile(pathFrag), attribs));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void bind(){
		GL20.glUseProgram(handle);
		current = this;
	}
	
	public static void bindNone(){
		GL20.glUseProgram(0);
		current = null;
	}
	
	private static int createShader(String vert, String frag, String... attribs){
		return createShader(createShaderPart(vert, GL20.GL_VERTEX_SHADER), createShaderPart(frag, GL20.GL_FRAGMENT_SHADER), attribs);
	}
	
	private static int createShaderGeom(String vert, String geom, String frag, String... attribs){
		return createShader(createShaderPart(vert, GL20.GL_VERTEX_SHADER), createShaderPart(geom, GL32.GL_GEOMETRY_SHADER), createShaderPart(frag, GL20.GL_FRAGMENT_SHADER), attribs);
	}
	
	/**
	 * Don't forget the attribs!!
	 * @param frag
	 * @param vert
	 * @param attribs
	 * @return
	 */
	private static int createShader(int vert, int frag, String... attribs){
		//create program
		int program = GL20.glCreateProgram();
		//add shader parts to program
		GL20.glAttachShader(program, vert);
		GL20.glAttachShader(program, frag);
		for(int i = 0; i < attribs.length; i++){
			GL20.glBindAttribLocation(program, i, attribs[i]);
		}
		//link and validate program
		GL20.glLinkProgram(program);
		String error = GL20.glGetProgramInfoLog(program, 1000);
		if(error.length() > 3){
			System.out.println("SHADER INFO LOG: LINKAGE: " + program);
			System.out.println(error);
		}
		GL20.glValidateProgram(program);
		//delete shader parts
		GL20.glDeleteShader(vert);
		GL20.glDeleteShader(frag);
		return program;
	}
	
	private static int createShader(int vert, int geom, int frag, String... attribs){
		//create program
		int program = GL20.glCreateProgram();
		//add shader parts to program
		GL20.glAttachShader(program, vert);
		GL20.glAttachShader(program, geom);
		GL20.glAttachShader(program, frag);
		for(int i = 0; i < attribs.length; i++){
			GL20.glBindAttribLocation(program, i, attribs[i]);
		}
		//link and validate program
		GL20.glLinkProgram(program);
		String error = GL20.glGetProgramInfoLog(program, 1000);
		if(error.length() > 3){
			System.out.println("SHADER INFO LOG: LINKAGE: " + program);
			System.out.println(error);
		}
		GL20.glValidateProgram(program);
		//delete shader parts
		GL20.glDeleteShader(vert);
		GL20.glDeleteShader(geom);
		GL20.glDeleteShader(frag);
		return program;
	}
	
	private static int createShaderPart(String src, int type){
		int handle = GL20.glCreateShader(type);
		GL20.glShaderSource(handle, src);
		GL20.glCompileShader(handle);
		String error = GL20.glGetShaderInfoLog(handle, 1000);
		if(error.length() > 1){
			System.out.println("SHADER INFO LOG: " + src);
			System.out.println(error);
		}
		return handle;
	}

	public static String readExternalFile(String path) throws IOException {
		return readFile(new BufferedReader(new FileReader(path)));
	}
	
	public static String readInternalFile(String path) throws IOException {
		return readFile(new BufferedReader(new InputStreamReader(Shader.class.getResourceAsStream(path), "UTF-8")));
	}
	public static String readFile(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		
		String output = "";
		while(line != null){
			output += line + "\n";
			line = reader.readLine();
		}
		
		reader.close();
		return output;
	}
}
