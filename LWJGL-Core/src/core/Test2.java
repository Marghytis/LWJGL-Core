package core;

import org.lwjgl.opengl.*;

import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.glfw.GLFW.*;

public class Test2 {
	
	static String frag = "#version 150 core\n" + 
			"   //singleQuad" + 
			"\n " + 
			"\n uniform sampler2D texture_diffuse;" + 
			"\n uniform bool textured;" + 
			"\n uniform vec4 color;" + 
			"\n " + 
			"\n in vec2 pass_texCoords;" + 
			"\n " + 
			"\n out vec4 out_Color;" + 
			"\n " + 
			"\n void main(void){" + 
			"\n " + 
			"\n		if(textured){" + 
			"\n			out_Color = texture(texture_diffuse, pass_texCoords)*color;" + 
			"\n		} else {" + 
			"\n			out_Color = color;" + 
			"\n		}" + 
			"\n }" + 
			"\n ";

	public static void main(String[] args) {

	// Initialize GLFW.
		glfwInit();
		
	// Create Window and set context
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		long window = glfwCreateWindow(1000, 1000, "test", NULL, NULL);
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		//Enable openGL bindings
		GL.createCapabilities();
		
	// Try to compile a fragment shader.
		int handle = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(handle, frag);
		GL20.glCompileShader(handle);
		
	// Print out errors.
		String error = GL20.glGetShaderInfoLog(handle, 1000);
		if(error.length() > 1){
			System.out.println("SHADER INFO LOG: \n" + frag);
			System.out.println(error);
		}
	}

//	private static int createShaderPart(String src, int type){
//		int handle = GL20.glCreateShader(type);
//		GL20.glShaderSource(handle, src);
//		GL20.glCompileShader(handle);
//		String error = GL20.glGetShaderInfoLog(handle, 1000);
//		if(error.length() > 1){
//			System.out.println("SHADER INFO LOG: \n" + src);
//			System.out.println(error);
//		}
//		return handle;
//	}
//	private static int createShader(int vert, int frag, String... attribs){
//		//create program
//		int program = GL20.glCreateProgram();
//		//add shader parts to program
//		GL20.glAttachShader(program, vert);
//		GL20.glAttachShader(program, frag);
//		for(int i = 0; i < attribs.length; i++){
//			GL20.glBindAttribLocation(program, i, attribs[i]);
//		}
//		//link and validate program
//		GL20.glLinkProgram(program);
//		String error = GL20.glGetProgramInfoLog(program, 1000);
//		if(error.length() > 3){
//			System.out.println("SHADER INFO LOG: LINKAGE: " + program);
//			System.out.println(error);
//		}
//		GL20.glValidateProgram(program);
//		//delete shader parts
//		GL20.glDeleteShader(vert);
//		GL20.glDeleteShader(frag);
//		return program;
//	}
	
}
