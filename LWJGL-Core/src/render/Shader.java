package render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL20;

public class Shader {

	public static int VERT_COLOR;
	public static int FRAG_COLOR;
	public static int VERT_TEXTURE;
	public static int FRAG_TEXTURE;
	static {
		try {
			VERT_COLOR = createShaderPart(readFile("res/shader/color.vert"), true);
			FRAG_COLOR = createShaderPart(readFile("res/shader/color.frag"), false);
			VERT_TEXTURE = createShaderPart(readFile("res/shader/texture.vert"), true);
			FRAG_TEXTURE = createShaderPart(readFile("res/shader/texture.frag"), false);
		} catch(IOException ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}
	public static int COLOR = createShader(VERT_COLOR, FRAG_COLOR, "in_Position", "in_Color");
	public static int TEXTURE = createShader(VERT_TEXTURE, FRAG_TEXTURE, "in_Position", "in_Color", "in_TextureCoords");
	
	
	public static void bind(int shader){
		GL20.glUseProgram(shader);
	}
	
	public static void bindNone(){
		GL20.glUseProgram(0);
	}
	
	public static int createShaderFromFile(String pathFrag){
		try {
			return createShader(VERT_TEXTURE, createShaderPart(readFile(pathFrag), false), "in_Position", "in_Color", "in_TextureCoords");
		} catch(IOException ex){
			ex.printStackTrace();
			return TEXTURE;
		}
	}
	
	public static int createShaderFromFile(String pathFrag, String pathVert, String... attribs){
		try {
			return createShader(readFile(pathVert), readFile(pathFrag), attribs);
		} catch(IOException ex){
			ex.printStackTrace();
			return TEXTURE;
		}
	}
	
	public static int createShader(String frag, String vert, String... attribs){
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

	public static String readFile(String name) throws IOException{
		File f = new File(name);
		FileReader fReader = new FileReader(f);
		BufferedReader reader = new BufferedReader(fReader);
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
