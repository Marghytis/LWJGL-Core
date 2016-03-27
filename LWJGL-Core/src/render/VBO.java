package render;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class VBO {
	
	public int handle;
	VAP[] vaps;
	int bytesPerVertex;

	public static Buffer createBuffer(float[] data){
		FloatBuffer out = BufferUtils.createFloatBuffer(data.length);
		out.put(data);
		out.flip();
		return out;
	}
	public static Buffer createBuffer(int[] data){
		IntBuffer out = BufferUtils.createIntBuffer(data.length);
		out.put(data);
		out.flip();
		return out;
	}
	public static Buffer createBuffer(byte[] data){
		ByteBuffer out = BufferUtils.createByteBuffer(data.length);
		out.put(data);
		out.flip();
		return out;
	}
	
	public VBO(Buffer buffer, int usage, int bytesPerVertex, VAP... vaps){
		this.vaps = vaps;
		this.bytesPerVertex = bytesPerVertex;
		handle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, handle);
			if(buffer instanceof FloatBuffer)
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (FloatBuffer) buffer, usage);
			else if(buffer instanceof IntBuffer)
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (IntBuffer) buffer, usage);
			else if(buffer instanceof ByteBuffer)
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (ByteBuffer) buffer, usage);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public int vertexAttribPointers(int index){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, handle);
		for(int i = 0; i < vaps.length; i++){
			GL20.glVertexAttribPointer(index++, vaps[i].size, vaps[i].dataType, vaps[i].normalized, bytesPerVertex, vaps[i].offset);
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return index;
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
	
