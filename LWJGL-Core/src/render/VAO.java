package render;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VAO {

	public int handle;
	public VBO[] vbos;
	public VBO indices;
	public int numberOfAttribs;
	
	public VAO(VBO indices, VBO... vbos){
		this.indices = indices;
		this.vbos = vbos;
		this.handle = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(handle);
		numberOfAttribs = 0;
		for(int i = 0; i < vbos.length; i++){
			numberOfAttribs = vbos[i].vertexAttribPointers(numberOfAttribs);
		}
		GL30.glBindVertexArray(0);
	}
	
	public void bindStuff(){
		GL30.glBindVertexArray(handle);
		for(int i = 0; i < numberOfAttribs; i++){
			GL20.glEnableVertexAttribArray(i);
		}
		if(indices != null) GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indices.handle);
	}
	
	public void unbindStuff(){
		if(indices != null) GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		for(int i = numberOfAttribs-1; i >= 0; i--){
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
	}
}
