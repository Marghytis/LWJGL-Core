package core;

import java.awt.Canvas;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.ContextAttribs;
//github.com/Marghytis/Sarah-s-Welt.git
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import util.PNGDecoder;

public class Window {

	public static int WIDTH, HEIGHT, WIDTH_HALF, HEIGHT_HALF;
	private static ByteBuffer icon16 = loadTexture("icons/icon16.png"), icon32 = loadTexture("icons/icon32.png"), icon64 = loadTexture("icons/icon64.png");
	public static boolean closeRequested;
	public static boolean openAl;
	public static PixelFormat pixelFormat;
	public static ContextAttribs contextAttribs;
	
	public static void create(String name, int width, int height, boolean openAl){
		Window.openAl = openAl;
		
		if(Display.isCreated()){
			Display.destroy();
			Mouse.destroy();
			Keyboard.destroy();
		}
		
		Display.setTitle(name);
		setSize(width, height);
		create();
	}
	
	public static JFrame frame;
	
	public static void createMaximized(String name, boolean openAl){
		Window.openAl = openAl;
		frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    	Window.closeRequested = true;
		    }
		});
		
	    Canvas canvas = new Canvas();
	    List<BufferedImage> icons = new ArrayList<>();
	    try {
	    	icons.add(ImageIO.read(new File("res/icons/Icon64.png")));
			icons.add(ImageIO.read(new File("res/icons/Icon32.png")));
			icons.add(ImageIO.read(new File("res/icons/Icon16.png")));
		} catch (IOException e1) {}
	    frame.setIconImages(icons);
		frame.setVisible(true);
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		frame.add(canvas);
		canvas.setSize(frame.getWidth(), frame.getHeight());

		Window.createCanvas(canvas);
	}
	
	private static void createCanvas(Canvas canvas){
		if(Display.isCreated()){
			Display.destroy();
			Mouse.destroy();
			Keyboard.destroy();
		}
		WIDTH = canvas.getWidth();
		HEIGHT = canvas.getHeight();
		updateHalfs();
		create(canvas);
	}
	
	static void updateHalfs(){
		WIDTH_HALF = WIDTH/2;
		HEIGHT_HALF = HEIGHT/2;
	}
	
	public static void createFullScreen(String name, boolean openAl){
		Window.openAl = openAl;
		
		if(Display.isCreated()){
			Display.destroy();
			Mouse.destroy();
			Keyboard.destroy();
		}

		Display.setTitle(name);
		try {
			Display.setFullscreen(true);
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		WIDTH = Display.getWidth();
		HEIGHT = Display.getHeight();
		updateHalfs();

		create();
	}
	
	private static void create(){
		if(icon16 != null)Display.setIcon(new ByteBuffer[] {icon16, icon32, icon64});
		try{
			if(pixelFormat != null && contextAttribs != null){
				Display.create(pixelFormat, contextAttribs);
			} else {
				Display.create();
			}
			Mouse.create();
			Keyboard.create();
			setupOpenGL();
			if(openAl){
				setupOpenAL();
			}
		} catch(LWJGLException e){
			e.printStackTrace();
		}
	}
	
	public static void create(Canvas canvas){
		if(icon16 != null)Display.setIcon(new ByteBuffer[] {icon16, icon32, icon64});
		try{
//	        Display.setVSyncEnabled(true);
			Display.setParent(canvas);
			if(pixelFormat != null && contextAttribs != null){
				Display.create(pixelFormat, contextAttribs);
			} else {
				Display.create();
			}
			Mouse.create();
			Keyboard.create();
			setupOpenGL();
			if(openAl){
				setupOpenAL();
			}
		} catch(LWJGLException e){
			e.printStackTrace();
		}
	}
	
	private static void setupOpenGL(){
		//set the viewport
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glCullFace(GL11.GL_FRONT_AND_BACK);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private static void setupOpenAL(){
	    try {
	    	AL.create(null, 15, 22050, true);
	    } catch (LWJGLException le) {
	    	le.printStackTrace();
	      return;
	    }
	    AL10.alGetError();
	}
	
	public static void setSize(int w, int h){
		WIDTH = w;
		HEIGHT = h;
		updateHalfs();
		try {
			Display.setDisplayMode(new DisplayMode(w, h));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void destroy(){
		Display.destroy();
		Mouse.destroy();
		Keyboard.destroy();
		AL.destroy();
	}
	
	public static void fill(int texture){
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);	GL11.glVertex2i(0, 0);
			GL11.glTexCoord2f(1, 1);	GL11.glVertex2i(WIDTH, 0);
			GL11.glTexCoord2f(1, 0);	GL11.glVertex2i(WIDTH, HEIGHT);
			GL11.glTexCoord2f(0, 0);	GL11.glVertex2i(0, HEIGHT);
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public static void fill(){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(0, 0);
			GL11.glVertex2i(WIDTH, 0);
			GL11.glVertex2i(WIDTH, HEIGHT);
			GL11.glVertex2i(0, HEIGHT);
		GL11.glEnd();
	}

    private static ByteBuffer loadTexture(String pathInRes){
        try {
        	InputStream in = new FileInputStream("res/" + pathInRes);
	            PNGDecoder decoder = new PNGDecoder(in);
	            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
	            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.RGBA);
	            bb.flip();
        	in.close();
            return bb;
        } catch (IOException e){
        	e.printStackTrace();
        }
		return null;
    }
    
    public static int createTexture(String pathInRes){

		// Create a new texture object in memory and bind it
    	int handle = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
		
		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		
		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, WIDTH, HEIGHT, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, loadTexture(pathInRes));
				
		// Setup what to do when the texture has to be scaled
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); 
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		return handle;
	}
}
