package renderOld;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import util.PNGDecoder;
import util.math.Rect;
import util.math.Vec;

public class TexFile {
	
	public static List<TexFile> allTexFiles = new ArrayList<>();
	public static Texture emptyTex = new Texture(new TexFile()); 
	static TexFile boundFile = emptyTex.file;

	public int handle;
	public Vec size = new Vec();
	public Rect pixelBox = new Rect();
	public String name;
	
	int xParts, yParts;
	
	public Vec sectorSize = new Vec();
	public Vec[][] sectorPos;
	
	public TexFileInfo[] infos;

	/**should only be used once: for the empty texture above*/
	private TexFile(){
		handle = 0;
		sectorPos = new Vec[][]{{new Vec()}};
		allTexFiles.add(this);
	}
	
	public TexFile(String name) {
		this(name, 0, 0);
	}
	
	public TexFile(String name, double relOffsetX, double relOffsetY) {
		this(name, 1, 1, relOffsetX, relOffsetY);
	}
	
	public TexFile(String name, int xParts, int yParts, double relOffsetX, double relOffsetY) {
		create(readFile(name), GL11.GL_RGBA, GL11.GL_RGBA8, GL11.GL_UNSIGNED_BYTE);
		manageParts(xParts, yParts, relOffsetX, relOffsetY);
		allTexFiles.add(this);
		this.name = name;
	}
	
	public TexFile(String name, Vec size, ByteBuffer data, int format, int internalFormat, int dataType){
		this(name, 1, 1, size, data, format, internalFormat, dataType);
	}
	
	public TexFile(String name, int xParts, int yParts, Vec size, ByteBuffer data, int format, int internalFormat, int dataType){
		this(name, xParts, yParts, size, 0, 0, data, format, internalFormat, dataType);
	}
	
	public TexFile(String name, int xParts, int yParts, Vec size, double relOffsetX, double relOffsetY, ByteBuffer data, int format, int internalFormat, int dataType){
		this.size.set(size);
		create(data, format, internalFormat, dataType);
		manageParts(xParts, yParts, relOffsetX, relOffsetY);
		allTexFiles.add(this);
		this.name = name;
	}
	
	public void manageParts(int xParts, int yParts, double relOffsetX, double relOffsetY){
		this.xParts = xParts;
		this.yParts = yParts;
		this.sectorSize.set(1.0/xParts, 1.0/yParts);
		this.sectorPos = new Vec[xParts][yParts];
		for(int y = 0; y < yParts; y++) for(int x = 0; x < xParts; x++) {
			sectorPos[x][y] = new Vec(x*sectorSize.x, y*sectorSize.y);
		}
		this.pixelBox.size.set(sectorSize.x*size.x, sectorSize.y*size.y);
		this.pixelBox.pos.set(relOffsetX*pixelBox.size.x, relOffsetY*pixelBox.size.y);
	}
	
	public void addInfo(TexFileInfo... infos){
		this.infos = infos;
	}
	
	/**
	 * Binds this texture file if its not yet bound
	 */
	public void bind(){
		if(!this.equals(boundFile)){
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
		}
	}
	
	public static void bindNone(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public static TexFile boundOne(){
		return boundFile;
	}
	
	public Texture tex(){
		return new Texture(this);
	}
	
	public Texture tex(int x, int y){
		return new Texture(this, x, y);
	}
	
	/**
	 * Width and height have to be set before
	 * @param data The data of the texture
	 * @param format Texture format (usually GL11.GL_RGBA)
	 * @param internalFormat Internal format of the texture (usually GL11.GL_RGBA8)
	 * @param dataType Data type of each information (GL11.GL_FLOAT usually) 
	 */
	private void create(ByteBuffer data, int format, int internalFormat, int dataType){
		// Create a new texture object in memory and bind it
		this.handle = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, size.xInt(), size.yInt(), 0, format, dataType, data);
		// Setup what to do when the texture has to be scaled
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST); 
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}
	
	private ByteBuffer readFile(String name){
		try {
			// Open the PNG file as an InputStream
			InputStream in = new FileInputStream(name);
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);
			
			// Get the width and height of the texture
			size.set(decoder.getWidth(), decoder.getHeight());
			
			// Decode the PNG file in a ByteBuffer
			ByteBuffer data = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
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
	 * 
	 * @param quad
	 * @param orientation 0 = normal, 1 = x mirrored, 2 = y mirrored
	 */
	public void fill(Rect quad, int orientation) {
		bind();
		int x = quad.pos.xInt();
		int y = quad.pos.yInt();
		int width = quad.size.xInt();
		int height = quad.size.yInt();
		switch(orientation){
		case 0://Normal
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2d(0, 	1);		GL11.glVertex2i(x, 			y);
				GL11.glTexCoord2d(1, 	1);		GL11.glVertex2i(x + width,	y);
				GL11.glTexCoord2d(1, 	0); 	GL11.glVertex2i(x + width,	y + height);
				GL11.glTexCoord2d(0, 	0); 	GL11.glVertex2i(x, 			y + height);
			GL11.glEnd();
			break;
		case 1: //Mirrored
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2d(1, 	1);		GL11.glVertex2i(x, 			y);
				GL11.glTexCoord2d(0, 	1);		GL11.glVertex2i(x + width,	y);
				GL11.glTexCoord2d(0, 	0); 	GL11.glVertex2i(x + width,	y + height);
				GL11.glTexCoord2d(1, 	0); 	GL11.glVertex2i(x, 			y + height);
			GL11.glEnd();
			break;
		case 2: //Flipped
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2d(0, 	0);		GL11.glVertex2i(x, 			y);
				GL11.glTexCoord2d(1, 	0);		GL11.glVertex2i(x + width,	y);
				GL11.glTexCoord2d(1, 	1); 	GL11.glVertex2i(x + width,	y + height);
				GL11.glTexCoord2d(0, 	1); 	GL11.glVertex2i(x, 			y + height);
			GL11.glEnd();
			break;
		}
	}
}
