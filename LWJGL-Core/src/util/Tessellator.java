package util;

import static org.lwjgl.util.glu.GLU.gluNewTess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;
import org.lwjgl.util.glu.GLUtessellatorCallbackAdapter;

public class Tessellator extends GLUtessellatorCallbackAdapter{
	
	public GLUtessellator t;
	
	public Tessellator(){
		t = gluNewTess();
		t.gluTessCallback(GLU.GLU_TESS_BEGIN, this);
		t.gluTessCallback(GLU.GLU_TESS_END, this);
		t.gluTessCallback(GLU.GLU_TESS_VERTEX, this);
//		tessellator.gluTessCallback(GLU.GLU_TESS_EDGE_FLAG, this);
		t.gluTessCallback(GLU.GLU_TESS_ERROR, this);
		t.gluTessCallback(GLU.GLU_TESS_COMBINE, this);
//		tessellator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_POSITIVE);
//		tessellator.gluTessProperty(GLU.GLU_TESS_BOUNDARY_ONLY, GL11.GL_TRUE);
	}
	
	public void begin(int mode){
		GL11.glBegin(mode);
	}
	
	public void vertex(Object data) {
		float[] coords = (float[]) data;

		GL11.glColor4f(coords[2], coords[3], coords[4], coords[5]);
		GL11.glVertex2f(coords[0], coords[1]);
	}
	
	public void end(){
		GL11.glEnd();
	}
	
	public void combine(double[] coords, Object[] data, float[] weight, Object[] outData){
		outData = data;
	}
}
