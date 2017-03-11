package render;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class Framebuffer {

	private int handle;
	private TexFile tex;
	Texture texture;
	
	public Framebuffer(String texName, int width, int height){
		handle = GL30.glGenFramebuffers();
		bind();
		
		tex = new TexFile(texName, width, height);
		texture = new Texture(tex, 0, 0);

		int depthrenderbuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthrenderbuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthrenderbuffer);
		
		// Set "tex" as our colour attachement #0
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, tex.handle, 0);

		// Set the list of draw buffers.
		IntBuffer drawBuffers = BufferUtils.createIntBuffer(1);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT0);
		drawBuffers.flip();
		GL20.glDrawBuffers(drawBuffers);
		
		check();
		
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	public void bind(){
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, handle);
	}
	public static void bindNone(){
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	public void bindTex(){
		tex.bind();
	}
	public Texture getTex(){
		return texture;
	}

	public void check(){
		int framebuffer = GL30.glCheckFramebufferStatus( GL30.GL_FRAMEBUFFER); 
		switch ( framebuffer ) {
			case GL30.GL_FRAMEBUFFER_COMPLETE:
				break;
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
				throw new RuntimeException( "FrameBuffer: " + handle
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception" );
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
				throw new RuntimeException( "FrameBuffer: " + handle
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception" );
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
				throw new RuntimeException( "FrameBuffer: " + handle
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception" );
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
				throw new RuntimeException( "FrameBuffer: " + handle
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception" );
			default:
				throw new RuntimeException( "Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer );
		}
	}
	
}
