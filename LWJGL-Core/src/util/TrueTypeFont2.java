package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import render.TexFile;
import render.Texture;

/**
 * A TrueType font implementation for Slick
 * 
 * @author James Chambers (Jimmy)
 * @author Jeremy Adams (elias4444)
 * @author Kevin Glass (kevglass)
 * @author Peter Korzuszek (genail)
 */
public class TrueTypeFont2 {
	/** The colour model including alpha for the GL image */
    private static final ColorModel glAlphaColorModel = 
    		new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
	            new int[] {8,8,8,8},
	            true,
	            false,
	            ComponentColorModel.TRANSLUCENT,
	            DataBuffer.TYPE_BYTE);
    
    /** The colour model for the GL image */
    private static final  ColorModel glColorModel =
    		new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,0},
                false,
                false,
                ComponentColorModel.OPAQUE,
                DataBuffer.TYPE_BYTE);
	/** Array that holds necessary information about the font characters */
	private IntObject[] charArray = new IntObject[256];
	
	/** Map of user defined font characters (Character <-> IntObject) */
	private Map customChars = new HashMap();

	/** Boolean flag on whether AntiAliasing is enabled or not */
	private boolean antiAlias;

	/** Font's size */
	private int fontSize = 0;

	/** Font's height */
	private int fontHeight = 0;

	/** Texture used to cache the font 0-255 characters */
	private Texture fontTexture;
	
	/** Default font texture width */
	private int textureWidth = 512;

	/** Default font texture height */
	private int textureHeight = 512;

	/** A reference to Java's AWT Font that we create our font texture from */
	private java.awt.Font font;

	/** The font metrics for our Java AWT font */
	private FontMetrics fontMetrics;

	/**
	 * This is a special internal class that holds our necessary information for
	 * the font characters. This includes width, height, and where the character
	 * is stored on the font texture.
	 */
	private class IntObject {
		/** Character's width */
		public int width;

		/** Character's height */
		public int height;

		/** Character's stored x position */
		public int storedX;

		/** Character's stored y position */
		public int storedY;
	}

	/**
	 * Constructor for the TrueTypeFont class Pass in the preloaded standard
	 * Java TrueType font, and whether you want it to be cached with
	 * AntiAliasing applied.
	 * 
	 * @param font
	 *            Standard Java AWT font
	 * @param antiAlias
	 *            Whether or not to apply AntiAliasing to the cached font
	 * @param additionalChars
	 *            Characters of font that will be used in addition of first 256 (by unicode).
	 */
	public TrueTypeFont2(java.awt.Font font, boolean antiAlias, char[] additionalChars) {
		this.font = font;
		this.fontSize = font.getSize();
		this.antiAlias = antiAlias;

		createSet( additionalChars );
	}
	
	/**
	 * Constructor for the TrueTypeFont class Pass in the preloaded standard
	 * Java TrueType font, and whether you want it to be cached with
	 * AntiAliasing applied.
	 * 
	 * @param font
	 *            Standard Java AWT font
	 * @param antiAlias
	 *            Whether or not to apply AntiAliasing to the cached font
	 */
	public TrueTypeFont2(java.awt.Font font, boolean antiAlias) {
		this( font, antiAlias, null );
	}

	/**
	 * Create a standard Java2D BufferedImage of the given character
	 * 
	 * @param ch
	 *            The character to create a BufferedImage for
	 * 
	 * @return A BufferedImage containing the character
	 */
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
		int charwidth = fontMetrics.charWidth(ch);

		if (charwidth <= 0) {
			charwidth = 1;
		}
		int charheight = fontMetrics.getHeight();
		if (charheight <= 0) {
			charheight = fontSize;
		}

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

		gt.setColor(Color.WHITE);
		int charx = 0;
		int chary = 0;
		gt.drawString(String.valueOf(ch), (charx), (chary)
				+ fontMetrics.getAscent());

		return fontImage;

	}

	/**
	 * Create and store the font
	 * 
	 * @param customCharsArray Characters that should be also added to the cache.
	 */
	private void createSet( char[] customCharsArray ) {
		// If there are custom chars then I expand the font texture twice		
		if	(customCharsArray != null && customCharsArray.length > 0) {
			textureWidth *= 2;
		}
		
		// In any case this should be done in other way. Texture with size 512x512
		// can maintain only 256 characters with resolution of 32x32. The texture
		// size should be calculated dynamicaly by looking at character sizes. 
		
		try {
			
			BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) imgTemp.getGraphics();

			g.setColor(new Color(255,255,255,1));
			g.fillRect(0,0,textureWidth,textureHeight);
			
			int rowHeight = 0;
			int positionX = 0;
			int positionY = 0;
			
			int customCharsLength = ( customCharsArray != null ) ? customCharsArray.length : 0; 

			for (int i = 0; i < 256 + customCharsLength; i++) {
				
				// get 0-255 characters and then custom characters
				char ch = ( i < 256 ) ? (char) i : customCharsArray[i-256];
				
				BufferedImage fontImage = getFontImage(ch);

				IntObject newIntObject = new IntObject();

				newIntObject.width = fontImage.getWidth();
				newIntObject.height = fontImage.getHeight();

				if (positionX + newIntObject.width >= textureWidth) {
					positionX = 0;
					positionY += rowHeight;
					rowHeight = 0;
				}

				newIntObject.storedX = positionX;
				newIntObject.storedY = positionY;

				if (newIntObject.height > fontHeight) {
					fontHeight = newIntObject.height;
				}

				if (newIntObject.height > rowHeight) {
					rowHeight = newIntObject.height;
				}

				// Draw it here
				g.drawImage(fontImage, positionX, positionY, null);

				positionX += newIntObject.width;

				if( i < 256 ) { // standard characters
					charArray[i] = newIntObject;
				} else { // custom characters
					customChars.put( new Character( ch ), newIntObject );
				}

				fontImage = null;
			}

			fontTexture = getTexture(imgTemp);

		} catch (IOException e) {
			System.err.println("Failed to create font.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Load a texture
	 * 
	 * @param resourceName
	 *            The location of the resource to load
	 * @param resourceImage
	 *            The BufferedImage we are converting
	 * @return The loaded texture
	 * @throws IOException
	 *             Indicates a failure to access the resource
	 */
	public static Texture getTexture(BufferedImage resourceImage) throws IOException {
		Texture tex = getTexture(resourceImage,
				GL11.GL_TEXTURE_2D, // target
				GL11.GL_RGBA8, // dest pixel format
				GL11.GL_LINEAR, // min filter (unused)
				GL11.GL_LINEAR);

		return tex;
	}
	
	/**
	 * Load a texture into OpenGL from a BufferedImage
	 * 
	 * @param resourceName
	 *            The location of the resource to load
	 * @param resourceimage
	 *            The BufferedImage we are converting
	 * @param target
	 *            The GL target to load the texture against
	 * @param dstPixelFormat
	 *            The pixel format of the screen
	 * @param minFilter
	 *            The minimising filter
	 * @param magFilter
	 *            The magnification filter
	 * @return The loaded texture
	 * @throws IOException
	 *             Indicates a failure to access the resource
	 */
	public static Texture getTexture(BufferedImage resourceimage, int target, int dstPixelFormat, int minFilter, int magFilter) throws IOException {
		int srcPixelFormat = 0;

		// create the texture ID for this texture
		int textureID = GL11.glGenTextures();
		TexFile texture = new TexFile("TrueTypeFont", textureID);

		// Enable texturing
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		// bind this texture
		GL11.glBindTexture(target, textureID);

		BufferedImage bufferedImage = resourceimage;
		texture.width = bufferedImage.getWidth();
		texture.height = bufferedImage.getHeight();

		if (bufferedImage.getColorModel().hasAlpha()) {
			srcPixelFormat = GL11.GL_RGBA;
		} else {
			srcPixelFormat = GL11.GL_RGB;
		}

		// convert that image into a byte buffer of texture data
		ByteBuffer textureBuffer = imageToByteBuffer(bufferedImage, false, false, null);
		texture.width = texWidth;
		texture.height = texHeight;
		
		GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
		GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, magFilter);

		GL11.glTexImage2D(target, 
                      0, 
                      dstPixelFormat, 
                      texWidth, 
                      texHeight, 
                      0, 
                      srcPixelFormat, 
                      GL11.GL_UNSIGNED_BYTE, 
                      textureBuffer); 

		return new Texture(texture, 0, 0, texture.width, texture.height, 0, 0);
	}
	static int width, height, depth, texHeight, texWidth;
	public static ByteBuffer imageToByteBuffer(BufferedImage image, boolean flipped, boolean forceAlpha, int[] transparent) {
	    ByteBuffer imageBuffer = null; 
        WritableRaster raster;
        BufferedImage texImage;
        
        int texWidth = 2;
        int texHeight = 2;
        
        // find the closest power of 2 for the width and height
        // of the produced texture

        while (texWidth < image.getWidth()) {
            texWidth *= 2;
        }
        while (texHeight < image.getHeight()) {
            texHeight *= 2;
        }
        
        width = image.getWidth();
        height = image.getHeight();
        TrueTypeFont2.texHeight = texHeight;
        TrueTypeFont2.texWidth = texWidth;
        
        // create a raster that can be used by OpenGL as a source
        // for a texture
        boolean useAlpha = image.getColorModel().hasAlpha() || forceAlpha; 
        
        if (useAlpha) {
        	depth = 32;
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
        } else {
        	depth = 24;
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable());
        }
            
        // copy the source image into the produced image
        Graphics2D g = (Graphics2D) texImage.getGraphics();
        
        // only need to blank the image for mac compatibility if we're using alpha
        if (useAlpha) {
	        g.setColor(new Color(0f,0f,0f,0f));
	        g.fillRect(0,0,texWidth,texHeight);
        }
        
        if (flipped) {
        	g.scale(1,-1);
        	g.drawImage(image,0,-height,null);
        } else {
        	g.drawImage(image,0,0,null);
        }
        
        if (height < texHeight - 1) {
        	copyArea(texImage, 0, 0, width, 1, 0, texHeight-1);
        	copyArea(texImage, 0, height-1, width, 1, 0, 1);
        }
        if (width < texWidth - 1) {
        	copyArea(texImage, 0,0,1,height,texWidth-1,0);
        	copyArea(texImage, width-1,0,1,height,1,0);
        }
        
        // build a byte buffer from the temporary image 
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData(); 
        
        if (transparent != null) {
	        for (int i=0;i<data.length;i+=4) {
	        	boolean match = true;
	        	for (int c=0;c<3;c++) {
	        		int value = data[i+c] < 0 ? 256 + data[i+c] : data[i+c];
	        		if (value != transparent[c]) {
	        			match = false;
	        		}
	        	}
	  
	        	if (match) {
	         		data[i+3] = 0;
	           	}
	        }
        }
        
        imageBuffer = ByteBuffer.allocateDirect(data.length); 
        imageBuffer.order(ByteOrder.nativeOrder()); 
        imageBuffer.put(data, 0, data.length); 
        imageBuffer.flip();
        g.dispose();
        
        return imageBuffer; 
	}

	/**
	 * Implement of transform copy area for 1.4
	 * 
	 * @param image The image to copy
 	 * @param x The x position to copy to
	 * @param y The y position to copy to
	 * @param width The width of the image
	 * @param height The height of the image
	 * @param dx The transform on the x axis
	 * @param dy The transform on the y axis
	 */
	private static void copyArea(BufferedImage image, int x, int y, int width, int height, int dx, int dy) {
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		g.drawImage(image.getSubimage(x, y, width, height),x+dx,y+dy,null);
	}
	
	
	/**
	 * Draw a textured quad
	 * 
	 * @param drawX
	 *            The left x position to draw to
	 * @param drawY
	 *            The top y position to draw to
	 * @param drawX2
	 *            The right x position to draw to
	 * @param drawY2
	 *            The bottom y position to draw to
	 * @param srcX
	 *            The left source x position to draw from
	 * @param srcY
	 *            The top source y position to draw from
	 * @param srcX2
	 *            The right source x position to draw from
	 * @param srcY2
	 *            The bottom source y position to draw from
	 */
	private void drawQuad(float drawX, float drawY, float drawX2, float drawY2,
			float srcX, float srcY, float srcX2, float srcY2) {
		float DrawWidth = drawX2 - drawX;
		float DrawHeight = drawY2 - drawY;
		float TextureSrcX = srcX / textureWidth;
		float TextureSrcY = srcY / textureHeight;
		float SrcWidth = srcX2 - srcX;
		float SrcHeight = srcY2 - srcY;
		float RenderWidth = (SrcWidth / textureWidth);
		float RenderHeight = (SrcHeight / textureHeight);

		GL11.glTexCoord2f(TextureSrcX, TextureSrcY + RenderHeight);
		GL11.glVertex2f(drawX, drawY);
		GL11.glTexCoord2f(TextureSrcX, TextureSrcY);
		GL11.glVertex2f(drawX, drawY + DrawHeight);
		GL11.glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY);
		GL11.glVertex2f(drawX + DrawWidth, drawY + DrawHeight);
		GL11.glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight);
		GL11.glVertex2f(drawX + DrawWidth, drawY);
	}

	/**
	 * Get the width of a given String
	 * 
	 * @param whatchars
	 *            The characters to get the width of
	 * 
	 * @return The width of the characters
	 */
	public int getWidth(String whatchars) {
		int totalwidth = 0;
		IntObject intObject = null;
		int currentChar = 0;
		for (int i = 0; i < whatchars.length(); i++) {
			currentChar = whatchars.charAt(i);
			if (currentChar < 256) {
				intObject = charArray[currentChar];
			} else {
				intObject = (IntObject)customChars.get( new Character( (char) currentChar ) );
			}
			
			if( intObject != null )
				totalwidth += intObject.width;
		}
		return totalwidth;
	}

	/**
	 * Get the font's height
	 * 
	 * @return The height of the font
	 */
	public int getHeight() {
		return fontHeight;
	}

	/**
	 * Get the height of a String
	 * 
	 * @return The height of a given string
	 */
	public int getHeight(String HeightString) {
		return fontHeight;
	}

	/**
	 * Get the font's line height
	 * 
	 * @return The line height of the font
	 */
	public int getLineHeight() {
		return fontHeight;
	}

	/**
	 * Draw a string
	 * 
	 * @param x
	 *            The x position to draw the string
	 * @param y
	 *            The y position to draw the string
	 * @param whatchars
	 *            The string to draw
	 * @param color
	 *            The color to draw the text
	 */
	public void drawString(float x, float y, String whatchars, util.Color color) {
		drawString(x,y,whatchars,color,0,whatchars.length()-1);
	}
	
	/**
	 * @see Font#drawString(float, float, String, org.newdawn.slick.Color, int, int)
	 */
	public void drawString(float x, float y, String whatchars, util.Color color, int startIndex, int endIndex) {
		
		color.bind();
		fontTexture.file.bind();

		IntObject intObject = null;
		int charCurrent;

		GL11.glBegin(GL11.GL_QUADS);

		int totalwidth = 0;
		for (int i = 0; i < whatchars.length(); i++) {
			charCurrent = whatchars.charAt(i);
			if (charCurrent < 256) {
				intObject = charArray[charCurrent];
			} else {
				intObject = (IntObject)customChars.get( new Character( (char) charCurrent ) );
			} 
			
			if( intObject != null ) {
				if ((i >= startIndex) || (i <= endIndex)) {
					drawQuad((x + totalwidth), y,
							(x + totalwidth + intObject.width),
							(y + intObject.height), intObject.storedX,
							intObject.storedY, intObject.storedX + intObject.width,
							intObject.storedY + intObject.height);
				}
				totalwidth += intObject.width;
			}
		}

		GL11.glEnd();
	}

	/**
	 * Draw a string
	 * 
	 * @param x
	 *            The x position to draw the string
	 * @param y
	 *            The y position to draw the string
	 * @param whatchars
	 *            The string to draw
	 */
	public void drawString(float x, float y, String whatchars) {
		drawString(x, y, whatchars, util.Color.WHITE);
	}

}