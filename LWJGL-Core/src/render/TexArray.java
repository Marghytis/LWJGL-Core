package render;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.EXTTextureArray;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import util.PNGDecoder;

public class TexArray {
	
	int handle;
	int width, height;
	String[] paths;
	
	public TexArray(String... paths){
		this.paths = paths;
		ByteBuffer data = null;
		// Open the PNG file as an InputStream
		try {
			for(int i = 0; i < paths.length; i++){
				// Link the PNG decoder to this stream
				PNGDecoder decoder = new PNGDecoder(new FileInputStream(paths[i]));
				
				// Get the width and height of the texture
				this.width = decoder.getWidth();
				this.height = decoder.getHeight();
				
				// Decode the PNG file in a ByteBuffer
				if(data == null)
					data = ByteBuffer.allocateDirect(4 * width * height*paths.length);
				decoder.decode(data, decoder.getWidth() * 4, PNGDecoder.RGBA);
				
				decoder.input.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		data.flip();
		handle = GL11.glGenTextures();
		GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, handle);
		// All RGB bytes are aligned to each other and each component is 1 byte
//		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, dataType, data);
		EXTTextureArray.gl
		// Setup what to do when the texture has to be scaled
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST); 
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}
	
	private ByteBuffer readFiles(InputStream... in){
		// Open the PNG file as an InputStream
		try {
			for(int i = 0; i < in.length; i++){
				// Link the PNG decoder to this stream
				PNGDecoder decoder = new PNGDecoder(in);
				
				// Get the width and height of the texture
				this.width = decoder.getWidth();
				this.height = decoder.getHeight();
				
				// Decode the PNG file in a ByteBuffer
				ByteBuffer data = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
				decoder.decode(data, decoder.getWidth() * 4, PNGDecoder.RGBA);
				data.flip();
				
				in.close();
			}
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
