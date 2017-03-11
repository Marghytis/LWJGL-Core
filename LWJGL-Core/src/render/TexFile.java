package render;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

import util.PNGDecoder;

public class TexFile {

	public static TexFile emptyTex = new TexFile("/res/EmptyTex.png", true);
	static TexFile boundFile = emptyTex;

	public String path;
	public int handle;
	public int width, height;
	
	public TexFile(String path, boolean internal){
		this.path = path;
		if(internal){
			createGL(readInternalFile(path), GL11.GL_RGBA, GL11.GL_RGBA8, GL11.GL_UNSIGNED_BYTE);
		} else {
			createGL(readExternalFile(path), GL11.GL_RGBA, GL11.GL_RGBA8, GL11.GL_UNSIGNED_BYTE);
		}
	}
	
	public TexFile(String path){
		this(path, false);
	}
	
	public TexFile(String name, int handle){
		this.path = name;
		this.handle = handle;
	}
	
	public TexFile(String name, int width, int height){
		this.width = width;
		this.height = height;
		createGL(null, GL11.GL_RGBA, GL11.GL_RGBA8, GL11.GL_UNSIGNED_BYTE);
	}
	
	/**
	 * Width and height have to be set before
	 * @param data The data of the texture
	 * @param format Texture format (usually GL11.GL_RGBA)
	 * @param internalFormat Internal format of the texture (usually GL11.GL_RGBA8)
	 * @param dataType Data type of each information (GL11.GL_UNSIGNED_BYTE usually) 
	 */
	private void createGL(ByteBuffer data, int format, int internalFormat, int dataType){
		// Create a new texture object in memory and bind it
		this.handle = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, dataType, data);
		// Setup what to do when the texture has to be scaled
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST); 
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}
	
	public ByteBuffer readExternalFile(String path){
		try {
			return readFile(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ByteBuffer readInternalFile(String path){
		return readFile(TexFile.class.getResourceAsStream(path));
	}
	
	private ByteBuffer readFile(InputStream in){
		// Open the PNG file as an InputStream
		try {
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);
			
			// Get the width and height of the texture
			this.width = decoder.getWidth();
			this.height = decoder.getHeight();
			
			// Decode the PNG file in a ByteBuffer
			ByteBuffer data = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(data, decoder.getWidth() * 4, PNGDecoder.RGBA);
			data.flip();
			
			in.close();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Binds this texture file if its not yet bound
	 */
	public void bind(){
		if(!this.equals(boundFile)){
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
			boundFile = this;
		}
	}
	
	public static void bindNone(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		boundFile = null;
	}
	
	public static TexFile boundOne(){
		return boundFile;
	}
}
