package render;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL20;

public class Shader {

	public static int VERT_TEXTURE;
	public static int FRAG_TEXTURE;
	static {
		try {
			VERT_TEXTURE = createShaderPart(readInternalFile("/res/texture.vert"), true);
			FRAG_TEXTURE = createShaderPart(readInternalFile("/res/texture.frag"), false);
		} catch(IOException ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}
	public static int TEXTURE = createShader(VERT_TEXTURE, FRAG_TEXTURE, "in_Position", "in_Color", "in_TextureCoords");
	public static int STANDARD = TEXTURE;

	public static void set(String field, float f1){
		GL20.glUniform1f(GL20.glGetUniformLocation(STANDARD, field), f1);
	}
	public static void set(String field, float f1, float f2){
		GL20.glUniform2f(GL20.glGetUniformLocation(STANDARD, field), f1, f2);
	}
	public static void set(String field, float f1, float f2, float f3){
		GL20.glUniform3f(GL20.glGetUniformLocation(STANDARD, field), f1, f2, f3);
	}
	public static void set(String field, float f1, float f2, float f3, float f4){
		GL20.glUniform4f(GL20.glGetUniformLocation(STANDARD, field), f1, f2, f3, f4);
	}
	public static void set(String field, int i1){
		GL20.glUniform1i(GL20.glGetUniformLocation(STANDARD, field), i1);
	}
	public static void set2x2(String field, FloatBuffer mat2){
		GL20.glUniformMatrix2(GL20.glGetUniformLocation(STANDARD, field), true, mat2);
	}
	public static void set3x3(String field, FloatBuffer mat3){
		GL20.glUniformMatrix3(GL20.glGetUniformLocation(STANDARD, field), true, mat3);
	}
	public static void setRotation(float rotation){
		set("rotation", rotation);
	}
	public static void setTranslation(float dx, float dy){
		set("tranlate", dx, dy);
	}
	public static void setColor(float r, float g, float b, float a){
		set("color", r, g, b, a);
	}
	public static void setMirrored(boolean mirrored){
		set("mirror", mirrored?1:0);
	}
	public static void setTexCoords(Texture tex){
		set("texCoords", tex.texCoords[0], tex.texCoords[1], tex.texCoords[2], tex.texCoords[3]);
	}
	
	
	public static void bind(int shader){
		GL20.glUseProgram(shader);
	}
	
	public static void bindNone(){
		GL20.glUseProgram(0);
	}
	
	public static int createShaderFromFile(String pathFrag){
		try {
			return createShader(VERT_TEXTURE, createShaderPart(readExternalFile(pathFrag), false), "in_Position", "in_Color", "in_TextureCoords");
		} catch(IOException ex){
			ex.printStackTrace();
			return TEXTURE;
		}
	}
	
	public static int createShaderFromFile(String pathVert, String pathFrag, String... attribs){
		try {
			return createShader(readExternalFile(pathVert), readExternalFile(pathFrag), attribs);
		} catch(IOException ex){
			ex.printStackTrace();
			return TEXTURE;
		}
	}
	
	public static int createShader(String vert, String frag, String... attribs){
		return createShader(createShaderPart(vert, true), createShaderPart(frag, false), attribs);
	}
	
	/**
	 * Don't forget the attribs!!
	 * @param frag
	 * @param vert
	 * @param attribs
	 * @return
	 */
	public static int createShader(int vert, int frag, String... attribs){
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
		GL20.glValidateProgram(program);
		//delete shader parts
		GL20.glDeleteShader(vert);
		GL20.glDeleteShader(frag);
		return program;
	}
	
	public static int createShaderPart(String src, boolean type){
		int handle = GL20.glCreateShader(type ? GL20.GL_VERTEX_SHADER : GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(handle, src);
		GL20.glCompileShader(handle);
		String error = GL20.glGetShaderInfoLog(handle, 1000);
		if(error.length() > 3){
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
