package render;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import util.Color;
import util.math.Vec;

public class TexQuad {

	Texture tex;
	Vec pos = new Vec();
	double rot = 0;
	double size = 1;
	boolean mirror = false;
	
	VAO vao;
	
	/**
	 * This object is highly uneconomic, as it contains a VAO with only a single primitive! (Just so you know)
	 * @param tex
	 */
	public TexQuad(Texture tex){
		this.tex = tex;
		
		vao = Render.quadInScreen(tex.pixelCoords[0], tex.pixelCoords[1], tex.pixelCoords[2], tex.pixelCoords[3]);
		
	}
	
	public void updateTex(Texture tex){
		this.tex = tex;
		update(pos, rot, size, mirror);
	}
	public void update(Vec pos, double rot, double size, boolean mirror){
		this.pos = pos;
		this.rot = rot;
		this.size = size;
		this.mirror = mirror;

		double x1 = tex.pixelCoords[0];
		double y1 = tex.pixelCoords[1];
		double x2 = tex.pixelCoords[2];
		double y2 = tex.pixelCoords[3];
		//Base vectors
		Vec[] vertices = {new Vec(x1, y1), new Vec(x2, y1), new Vec(x2, y2), new Vec(x1, y2)};
		
		for(int i = 0; i < 4; i++){
			//apply rotation
			double angle = rot*Math.PI*2; 
			vertices[i].rotate(angle);
			//apply scaling
			vertices[i].scale(size);
			//apply offset
			vertices[i].shift(pos);
		}
		
		ByteBuffer newData = BufferUtils.createByteBuffer(Short.BYTES*16);
		newData.putShort((short)vertices[0].xInt());
		newData.putShort((short)vertices[0].yInt());
		newData.putShort((short)(mirror ? 1 : 0));
		newData.putShort((short)1);
		newData.putShort((short)vertices[1].xInt());
		newData.putShort((short)vertices[1].yInt());
		newData.putShort((short)(mirror ? 0 : 1));
		newData.putShort((short)1);
		newData.putShort((short)vertices[2].xInt());
		newData.putShort((short)vertices[2].yInt());
		newData.putShort((short)(mirror ? 0 : 1));
		newData.putShort((short)0);
		newData.putShort((short)vertices[3].xInt());
		newData.putShort((short)vertices[3].yInt());
		newData.putShort((short)(mirror ? 1 : 0));
		newData.putShort((short)0);
		newData.flip();
		
		vao.vbos[0].update(0, newData);
	}
	
	public void render(Vec offset, double scale){
		render(offset, 0, new Vec(scale, scale), Color.WHITE);
	}
	public void render(Vec offset, double z, Vec scale, Color color){

		Shader.singleQuad.bind();
		Shader.singleQuad.set("offset", (float)offset.x, (float)offset.y);
		Shader.singleQuad.set("scale", (float)scale.x, (float)scale.y);
		Shader.singleQuad.set("z", (float)z);
		tex.file.bind();
		Shader.singleQuad.set("texCoords", tex.texCoords[0], tex.texCoords[1], tex.texCoords[2] - tex.texCoords[0], tex.texCoords[3] - tex.texCoords[1]);
		if(color != null)
			Shader.singleQuad.set("color", color.r, color.g, color.b, color.a);
		else
			Shader.singleQuad.set("color", 1, 1, 1, 1);
		Shader.singleQuad.set("textured", true);
		vao.bindStuff();
			GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0);
		vao.unbindStuff();
		Color.WHITE.bind();
		TexFile.bindNone();
		Shader.bindNone();
	}
	
}
