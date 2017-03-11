package render;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL33;

public class VBO {
	
	public int handle;
	public ByteBuffer buffer;
	VAP[] vaps;
	int bytesPerVertex;
	int divisor;
	
	/**
	 * For indices
	 * @param buffer
	 * @param usage
	 */
	public VBO(Buffer buffer, int usage){
		handle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, handle);
		if(buffer instanceof IntBuffer)
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, (IntBuffer) buffer, usage);
		else if(buffer instanceof ShortBuffer)
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, (ShortBuffer) buffer, usage);
		else if(buffer instanceof ByteBuffer)
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, (ByteBuffer) buffer, usage);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public VBO(ByteBuffer buffer, int usage, int bytesPerVertex, VAP... vaps){
		this(buffer, usage, bytesPerVertex, 0, vaps);
	}
	
	public VBO(ByteBuffer buffer, int usage, int bytesPerVertex, int divisor, VAP... vaps){
		this.vaps = vaps;
		this.bytesPerVertex = bytesPerVertex;
		this.divisor = divisor;
		handle = GL15.glGenBuffers();
		this.buffer = buffer;
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, handle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, usage);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	/**Better use a ByteBuffer*/
	@Deprecated
	public VBO(Buffer buffer, int usage, int bytesPerVertex, VAP... vaps){
		this(buffer, usage, bytesPerVertex, 0, vaps);
	}
	
	/**Better use a ByteBuffer*/
	@Deprecated
	public VBO(Buffer buffer, int usage, int bytesPerVertex, int divisor, VAP... vaps){
		this.vaps = vaps;
		this.bytesPerVertex = bytesPerVertex;
		this.divisor = divisor;
		handle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, handle);
		if(buffer instanceof FloatBuffer)
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (FloatBuffer) buffer, usage);
		else if(buffer instanceof IntBuffer)
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (IntBuffer) buffer, usage);
		else if(buffer instanceof ShortBuffer)
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (ShortBuffer) buffer, usage);
		else if(buffer instanceof ByteBuffer)
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (ByteBuffer) buffer, usage);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public int vertexAttribPointers(int index){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, handle);
		for(int i = 0; i < vaps.length; i++){
			GL20.glVertexAttribPointer(index, vaps[i].size, vaps[i].dataType, vaps[i].normalized, bytesPerVertex, vaps[i].offset);
			if(divisor != 0) 
				GL33.glVertexAttribDivisor(index, divisor);
			index++;
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return index;
	}
	
	public void bind(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, handle);
	}
	
	public ByteBuffer getBuffer(){
		return buffer;
	}
	
	public void update(){
		update(0, buffer);
	}
	
	public void update(long offset, ByteBuffer newData){
		bind();
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, newData);
		unbind();
	}
	
	public static void unbind(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Vertex Attrib Pointer
	 */
	public static class VAP{
		int size, dataType, offset;
		boolean normalized;

		public VAP(int size, int dataType, boolean normalized, int offset){
			this.size = size;
			this.dataType = dataType;
			this.normalized = normalized;
			this.offset = offset;
		}
	}
}
	
