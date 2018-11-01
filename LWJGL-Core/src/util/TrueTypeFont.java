package util;

import java.awt.*;
import java.awt.image.*;
import java.nio.*;
import java.util.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import render.*;
import render.VBO.VAP;


/**
 * A TrueType font implementation originally for Slick, edited for Bobjob's Engine, edited for Sarah's World and updated to OpenGL 3.2
 * 
 * @original author James Chambers (Jimmy)
 * @original author Jeremy Adams (elias4444)
 * @original author Kevin Glass (kevglass)
 * @original author Peter Korzuszek (genail)
 * 
 * @new version edited by David Aaron Muhar (bobjob)
 */
public class TrueTypeFont {
	public final static int
		ALIGN_LEFT = 0,
		ALIGN_RIGHT = 1,
		ALIGN_CENTER = 2;
	public static int maxChars = 1000;
	public static Shader fontShader = Shader.internalWithGeometry("/res/shader/text.vert", "/res/shader/text.geom", "/res/shader/text.frag", "in_Position", "in_TexCoords");
	public static TrueTypeFont times = new TrueTypeFont(new Font("Times New Roman", 0, 40), true);
	/** Array that holds necessary information about the font characters */
	private CharData[] charArray = new CharData[256];
	/** Map of user defined font characters (Character <-> CharData) */
	private Map<Character, CharData> customChars = new HashMap<>();
	
	private int totalCharCount;
	/** Boolean flag on whether AntiAliasing is enabled or not */
	private boolean antiAlias;
	/** Font's size */
	private int fontSize = 0;
	/** Font's height */
	private int fontHeight = 0;
	/** Texture used to cache the font 0-255 characters */
	private int fontTextureID;
	/** Default font texture width */
	private int textureWidth = 512;
	/** Default font texture height */
	private int textureHeight = 512;
	/** A reference to Java's AWT Font that we create our font texture from */
	private Font font;
	/** The font metrics for our Java AWT font */
	FontMetrics fontMetrics;
	/** Correction offsets */
	private int correctL = 9, correctR = 8;
	
	public VAO vao;

	public TrueTypeFont(Font font, boolean antiAlias, char[] additionalChars) {
		this.font = font;
		this.fontSize = font.getSize()+3;
		this.antiAlias = antiAlias;

		createSet(additionalChars);
		
		fontHeight -= 1;
		if (fontHeight <= 0) fontHeight = 1;
	}
	public TrueTypeFont(Font font, boolean antiAlias) {
		this( font, antiAlias, null );
	}

//	@SuppressWarnings("unchecked")
	private void createSet( char[] customCharsArray ) {
		// If there are custom chars then I expand the font texture twice		
		if	(customCharsArray != null && customCharsArray.length > 0) {
			textureWidth *= 2;
		}
		
		// In any case this should be done in another way. Texture with size 512x512
		// can maintain only 256 characters with resolution of 32x32. The texture
		// size should be calculated dynamicaly by looking at character sizes. 
		try {
			
			BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) imgTemp.getGraphics();

			g.setColor(new java.awt.Color(0,0,0,1));
			g.fillRect(0,0,textureWidth,textureHeight);
			
			int rowHeight = 0;
			int positionX = 0;
			int positionY = 0;
			
			int customCharsLength = ( customCharsArray != null ) ? customCharsArray.length : 0; 
			totalCharCount = 256 + customCharsLength;

			for (int i = 0; i < 256 + customCharsLength; i++) {
				
				// get 0-255 characters and then custom characters
				char ch = ( i < 256 ) ? (char) i : customCharsArray[i-256];
				
				BufferedImage fontImage = getFontImage(ch);

				CharData newCharData = new CharData();

				newCharData.width = (byte)fontImage.getWidth();
				newCharData.height = (byte)fontImage.getHeight();

				if (positionX + newCharData.width >= textureWidth) {
					positionX = 0;
					positionY += rowHeight;
					rowHeight = 0;
				}

				newCharData.storedX = (short)positionX;
				newCharData.storedY = (short)positionY;

				if (newCharData.height > fontHeight) {
					fontHeight = newCharData.height;
				}

				if (newCharData.height > rowHeight) {
					rowHeight = newCharData.height;
				}

				// Draw it here
				g.drawImage(fontImage, positionX, positionY, null);

				positionX += newCharData.width;

				if( i < 256 ) { // standard characters
					charArray[i] = newCharData;
				} else { // custom characters
					customChars.put( new Character( ch ), newCharData );
				}

				fontImage = null;
			}

			fontTextureID = loadImage(imgTemp);

			CharData charData = null;
			//					   pos texCoords
			byte zero = 0;
			int bytesPerChar = 4*(2*Byte.BYTES + 2*Short.BYTES);
			ByteBuffer buffer = BufferUtils.createByteBuffer((256 + customCharsLength)*bytesPerChar);
			for (int i = 0; i < 256 + customCharsLength; i++) {
				charData = getChar(i);
				short texX1 = (short)(Short.MAX_VALUE*charData.storedX/ textureWidth),
					texY1 = (short)(Short.MAX_VALUE*charData.storedY/ textureHeight),
					texX2 = (short)(texX1 + (Short.MAX_VALUE*charData.width/ textureWidth)),
					texY2 = (short)(texY1 + (Short.MAX_VALUE*charData.height/ textureHeight));
				buffer.put(zero);//byte
				buffer.put(zero);//byte
				buffer.putShort(texX1);//normalized short
				buffer.putShort(texY2);//normalized short
				
				buffer.put(charData.width);
				buffer.put(zero);
				buffer.putShort(texX2);
				buffer.putShort(texY2);
				
				buffer.put(charData.width);
				buffer.put(charData.height);
				buffer.putShort(texX2);
				buffer.putShort(texY1);
				
				buffer.put(zero);
				buffer.put(charData.height);
				buffer.putShort(texX1);
				buffer.putShort(texY1);
			}
			buffer.flip();
			ShortBuffer indices = BufferUtils.createShortBuffer(maxChars*6);
			vao = new VAO(
						new VBO(indices, GL15.GL_DYNAMIC_READ),
						new VBO(buffer, GL15.GL_STATIC_DRAW, 2*Byte.BYTES + 2*Short.BYTES,
							new VAP(2, GL11.GL_BYTE, false, 0),//Location
							new VAP(2, GL11.GL_SHORT, true, 2*Byte.BYTES)));//Tex coords
		} catch (Exception e) {
			System.err.println("Failed to create font.");
			e.printStackTrace();
		}
	}

	public int getWidth(String whatchars) {
		int totalwidth = 0, maxLineWidth = 0;
		CharData charData = null;
		char currentChar = 0;
		for (int i = 0; i < whatchars.length(); i++) {
			currentChar = whatchars.charAt(i);
			if(currentChar == '\n'){
				maxLineWidth = Math.max(totalwidth, maxLineWidth);
				totalwidth = 0;
				continue;
			}
			charData = getChar(currentChar);
			
			if( charData != null )
				totalwidth += charData.width-correctL;
		}
		maxLineWidth = Math.max(totalwidth, maxLineWidth);
		return maxLineWidth;
	}
	
	public int getHeight(String whatchars){
		int height = 1;
		char currentChar = 0;
		for (int i = 0; i < whatchars.length(); i++) {
			currentChar = whatchars.charAt(i);
			if(currentChar == '\n'){
				height++;
			}
		}
		return height*fontHeight;
	}

	public int getHeight() {
		return fontHeight;
	}

	public void drawString(float x, float y, String whatchars, float scaleX, float scaleY){
		drawString(x, y, whatchars, Color.WHITE, 1, 1, scaleX, scaleY);}
	
	public void drawString(float x, float y, String whatchars, float sizeX, float sizeY, float scaleX, float scaleY){
		drawString(x, y, whatchars, Color.WHITE, sizeX, sizeY, scaleX, scaleY);}
	
	public void drawString(float x, float y, String whatchars, Color color, float scaleX, float scaleY) {
		drawString(x,y,whatchars, color, 0, whatchars.length()-1, 1, 1, scaleX, scaleY, ALIGN_LEFT);}
	
	public void drawString(float x, float y, String whatchars, Color color, float sizeX, float sizeY, float scaleX, float scaleY) {
		drawString(x,y,whatchars, color, 0, whatchars.length()-1, sizeX, sizeY, scaleX, scaleY, ALIGN_LEFT);}
	
	public void drawString(float x, float y, String whatchars, Color color, float sizeX, float sizeY, float scaleX, float scaleY, int format) {
		drawString(x,y,whatchars, color, 0, whatchars.length()-1, sizeX, sizeY, scaleX, scaleY, format);}
	
	public void drawString(float x, float y, String whatchars, Color color, int startIndex, int endIndex, float sizeX, float sizeY, float scaleX, float scaleY, int format) {
		
		char[] chars = whatchars.toCharArray();
		CharData charData = null;
		int currentChar;

		float startX = 0;
		int dir, correction;
		float startY = 0;
		switch (format) {
			case ALIGN_RIGHT: {
				dir = -1;
				correction = correctR;
			
				for(int i = startIndex; i <= endIndex; i++) {
					if(chars[i] == '\n') startY -= fontHeight;
				}
				break;
			}
			case ALIGN_CENTER: {
				dir = 1;
				for (int i = startIndex; i <= endIndex; i++) {
					currentChar = chars[i];
					if (currentChar == '\n') break;
					charData = getChar(currentChar);
					startX += charData.width-correctL;
				}
				startX /= -2;
			}
			case ALIGN_LEFT:
			default: {
				dir = 1;
				correction = correctL;
				break;
			}
		}

		int charCount = endIndex - startIndex + 1;
		for(int i = startIndex; i <= endIndex; i++){
			if(chars[i] == '\n'){
				charCount--;
			}}
		
		ShortBuffer indices = BufferUtils.createShortBuffer(maxChars*6);
		indices.clear();
		for(int i = (format == ALIGN_RIGHT ? endIndex : startIndex); i <= endIndex && i >= startIndex; i+=dir){
			if(chars[i] == '\n')continue;
			short index = (short)((chars[i])*4);
			indices.put(index);
			indices.put((short)(index + 1));
			indices.put((short)(index + 2));

			indices.put(index);
			indices.put((short)(index + 2));
			indices.put((short)(index + 3));
		}
		for(int i = charCount*6; i < maxChars*6; i++) {
			indices.put((short)0);
		}
		indices.flip();
		
		IntBuffer offsets = BufferUtils.createIntBuffer(charCount*2);

		int X = (int)startX;
		int Y = (int)startY;
		
		for(int i = (format == ALIGN_RIGHT ? endIndex : startIndex); i <= endIndex && i >= startIndex; i+=dir){
			
			currentChar = chars[i];
			charData = getChar(currentChar);
			
			if(charData != null ) {
				if (dir < 0) X += (charData.width-correction) * dir;//align right
				if (currentChar == '\n') {//new line
					Y -= fontHeight * dir;
					X = 0;
					if(format == ALIGN_CENTER) {
						for (int j = i+1; j <= endIndex; j++) {
							currentChar = chars[j];
							if (currentChar == '\n') break;
							if (currentChar < 256) {
								charData = charArray[currentChar];
							} else {
								charData = (CharData)customChars.get( new Character( (char) currentChar ) );
							}
							X += charData.width-correctL;
						}
						X /= -2;
					}//if center get next lines total width/2;
					
				} else {
					offsets.put((int)(X * sizeX + x));
					offsets.put((int)(Y * sizeY + y));
					if (dir > 0) X += (charData.width-correction) * dir ;
				}
			}
		}
		offsets.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vao.indices.handle);
		GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, 0, indices);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		fontShader.bind();
		fontShader.set("scale", scaleX, scaleY);
		fontShader.set("size", sizeX, sizeY);
		fontShader.set2("offsets", offsets);
		fontShader.set("color", color.r, color.g, color.b, color.a);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureID);
		
		vao.bindStuff();
			GL11.glDrawElements(GL11.GL_TRIANGLES, maxChars*2, GL11.GL_UNSIGNED_SHORT, 0);
		vao.unbindStuff();
		
		TexFile.bindNone();
		Shader.bindNone();
	}
	
	public CharData getChar(int i){
		if (i < 256) {
			return charArray[i];
		} else {
			return (CharData)customChars.get( new Character( (char) i ) );
		}
	}
	
	private class CharData {
		/** Character's width */
		public byte width;
		/** Character's height */
		public byte height;
		/** Character's stored x position */
		public short storedX;
		/** Character's stored y position */
		public short storedY;
	}
	
	public static int loadImage(BufferedImage bufferedImage) {
	    try {
		    short width       = (short)bufferedImage.getWidth();
		    short height      = (short)bufferedImage.getHeight();
		    //textureLoader.bpp = bufferedImage.getColorModel().hasAlpha() ? (byte)32 : (byte)24;
		    int bpp = (byte)bufferedImage.getColorModel().getPixelSize();
		    ByteBuffer byteBuffer;
		    DataBuffer db = bufferedImage.getData().getDataBuffer();
		    if (db instanceof DataBufferInt) {
		    	int intI[] = ((DataBufferInt)(bufferedImage.getData().getDataBuffer())).getData();
		    	byte newI[] = new byte[intI.length * 4];
		    	for (int i = 0; i < intI.length; i++) {
		    		byte b[] = intToByteArray(intI[i]);
		    		int newIndex = i*4;
		    		
		    		newI[newIndex]   = b[1];
		    		newI[newIndex+1] = b[2];
		    		newI[newIndex+2] = b[3];
		    		newI[newIndex+3] = b[0];
		    	}
		    	
		    	byteBuffer  = ByteBuffer.allocateDirect(
		    			width*height*(bpp/8))
			                           .order(ByteOrder.nativeOrder())
			                            .put(newI);
		    } else {
		    	byteBuffer  = ByteBuffer.allocateDirect(
		    			width*height*(bpp/8))
			                           .order(ByteOrder.nativeOrder())
			                            .put(((DataBufferByte)(bufferedImage.getData().getDataBuffer())).getData());
		    }
		    byteBuffer.flip();
		    
		    
		    int internalFormat = GL11.GL_RGBA8,
			format = GL11.GL_RGBA;
			IntBuffer   textureId =  BufferUtils.createIntBuffer(1);;
			GL11.glGenTextures(textureId);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId.get(0));
			// All RGB bytes are aligned to each other and each component is 1 byte
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

			// Upload the texture data and generate mip maps (for scaling)
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0/*??*/, internalFormat, width, height, 0, format, GL11.GL_UNSIGNED_BYTE, byteBuffer);

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

//			GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D,
//			      internalFormat,
//			      width,
//			      height,
//			      format,
//			      GL11.GL_UNSIGNED_BYTE,
//			      byteBuffer);

			return textureId.get(0);
		    
		} catch (Exception e) {
	    	e.printStackTrace();
	    	System.exit(-1);
	    }
		
		return -1;
	}
	
	private BufferedImage getFontImage(char ch) {
		// Create a temporary image to extract the character's size
		BufferedImage tempfontImage = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
		if (antiAlias == true) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setFont(font);
		fontMetrics = g.getFontMetrics();
		
		int charwidth = fontMetrics.charWidth(ch)+8;
		if (charwidth <= 0) charwidth = 7;
		
		int charheight = fontMetrics.getHeight()+3;
		if (charheight <= 0) charheight = fontSize;

		// Create another image holding the character we are creating
		BufferedImage fontImage;
		fontImage = new BufferedImage(charwidth, charheight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D gt = (Graphics2D) fontImage.getGraphics();
		if (antiAlias == true) {
			gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		gt.setFont(font);

		gt.setColor(java.awt.Color.WHITE);
		int charx = 3;
		int chary = 1;
		gt.drawString(String.valueOf(ch), (charx), (chary)
				+ fontMetrics.getAscent());

		return fontImage;
	}
	public static boolean isSupported(String fontname) {
		Font font[] = getFonts();
		for (int i = font.length-1; i >= 0; i--) {
			if (font[i].getName().equalsIgnoreCase(fontname))
				return true;
		}
		return false;
	}
	public static Font[] getFonts() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
	}
	public static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
	}
	
	public void destroy() {
		IntBuffer scratch = BufferUtils.createIntBuffer(1);
		scratch.put(0, fontTextureID);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDeleteTextures(scratch);
	}
}