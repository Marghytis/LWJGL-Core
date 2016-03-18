package render;

import org.lwjgl.opengl.GL11;

import util.math.Rect;
import util.math.UsefulF;

public class Texture {
	
	public static TexAtlas empty = new TexAtlas(TexFile.emptyTex, 0, 0, 0, 0, 1, 1, 0, 0);

	public TexFile file;
	public float[] texCoords = new float[4];
	public int x1, y1, w, h;
	public int[] pixelCoords = new int[4];
	public TexInfo[] infos;
	
	public Texture(String path, double offsetX, double offsetY){
		this.file = new TexFile(path);
		this.texCoords[2] = 1;
		this.texCoords[3] = 1;//the rest is zero
		this.w = file.width;
		this.h = file.height;
		this.pixelCoords[0] = (int)(offsetX*file.width);
		this.pixelCoords[1] = (int)(offsetY*file.height);
		this.pixelCoords[2] = pixelCoords[0] + file.width;
		this.pixelCoords[3] = pixelCoords[1] + file.height;
	}

	public Texture(TexFile file, int x1, int y1, int w, int h, double offsetX, double offsetY){
		
		this.file = file;
		
		this.x1 = x1;
		this.y1 = y1;
		this.w = w;
		this.h = h;
		
		if(file.width > 0){//this if question is only for the "empty"-Texture
			this.texCoords[0] = (float)x1/file.width;
			this.texCoords[1] = (float)y1/file.height;
			this.texCoords[2] = (float)w/file.width + this.texCoords[0];
			this.texCoords[3] = (float)h/file.height + this.texCoords[1];
		}

		this.pixelCoords[0] = (int)(offsetX*w);
		this.pixelCoords[1] = (int)(offsetY*h);
		this.pixelCoords[2] = pixelCoords[0] + w;
		this.pixelCoords[3] = pixelCoords[1] + h;
	}

	/**
	 * This is only for Animators. they initialize themselves
	 */
	Texture(){}
	
	public void fill(double x1, double y1, double x2, double y2, boolean mirrored){
		GL11.glBegin(GL11.GL_QUADS);
			if(mirrored){
				GL11.glTexCoord2d(texCoords[0],	texCoords[3]);	GL11.glVertex2d(x1, y1);
				GL11.glTexCoord2d(texCoords[2],	texCoords[3]);	GL11.glVertex2d(x2,	y1);
				GL11.glTexCoord2d(texCoords[2], texCoords[1]); 	GL11.glVertex2d(x2,	y2);
				GL11.glTexCoord2d(texCoords[0], texCoords[1]); 	GL11.glVertex2d(x1, y2);
			} else {
				GL11.glTexCoord2d(texCoords[2], texCoords[3]);	GL11.glVertex2d(x1, y1);
				GL11.glTexCoord2d(texCoords[0], texCoords[3]);	GL11.glVertex2d(x2,	y1);
				GL11.glTexCoord2d(texCoords[0], texCoords[1]); 	GL11.glVertex2d(x2,	y2);
				GL11.glTexCoord2d(texCoords[2], texCoords[1]); 	GL11.glVertex2d(x1, y2);
			}
		GL11.glEnd();
	}
	public void fill(Rect rect, boolean mirrored){
		fill(rect.pos.x, rect.pos.y, rect.pos.x + rect.size.x, rect.pos.y + rect.size.y, mirrored);
	}
	
	public void draw(boolean mirrored){
		GL11.glBegin(GL11.GL_QUADS);
			if(mirrored){
				GL11.glTexCoord2d(texCoords[0], texCoords[3]);	GL11.glVertex2i(pixelCoords[0],	pixelCoords[1]);
				GL11.glTexCoord2d(texCoords[2], texCoords[3]);	GL11.glVertex2i(pixelCoords[2],	pixelCoords[1]);
				GL11.glTexCoord2d(texCoords[2], texCoords[1]); 	GL11.glVertex2i(pixelCoords[2],	pixelCoords[3]);
				GL11.glTexCoord2d(texCoords[0], texCoords[1]); 	GL11.glVertex2i(pixelCoords[0],	pixelCoords[3]);
			} else {
				GL11.glTexCoord2d(texCoords[2], texCoords[3]);	GL11.glVertex2i(pixelCoords[0],	pixelCoords[1]);
				GL11.glTexCoord2d(texCoords[0], texCoords[3]);	GL11.glVertex2i(pixelCoords[2],	pixelCoords[1]);
				GL11.glTexCoord2d(texCoords[0], texCoords[1]); 	GL11.glVertex2i(pixelCoords[2],	pixelCoords[3]);
				GL11.glTexCoord2d(texCoords[2], texCoords[1]); 	GL11.glVertex2i(pixelCoords[0],	pixelCoords[3]);
			}
		GL11.glEnd();
	}
	
	public void drawBash(boolean mirrored, int offsetX, int offsetY){
		int x1__translated = pixelCoords[0] + offsetX;
		int y1__translated = pixelCoords[1] + offsetY;
		if(mirrored){
			GL11.glTexCoord2d(texCoords[0], texCoords[3]);	GL11.glVertex2i(x1__translated,	y1__translated);
			GL11.glTexCoord2d(texCoords[2], texCoords[3]);	GL11.glVertex2i(pixelCoords[2],	y1__translated);
			GL11.glTexCoord2d(texCoords[2], texCoords[1]); 	GL11.glVertex2i(pixelCoords[2],	pixelCoords[3]);
			GL11.glTexCoord2d(texCoords[0], texCoords[1]); 	GL11.glVertex2i(x1__translated,	pixelCoords[3]);
		} else {
			GL11.glTexCoord2d(texCoords[2], texCoords[3]);	GL11.glVertex2i(x1__translated,	y1__translated);
			GL11.glTexCoord2d(texCoords[0], texCoords[3]);	GL11.glVertex2i(pixelCoords[2],	y1__translated);
			GL11.glTexCoord2d(texCoords[0], texCoords[1]); 	GL11.glVertex2i(pixelCoords[2],	pixelCoords[3]);
			GL11.glTexCoord2d(texCoords[2], texCoords[1]); 	GL11.glVertex2i(x1__translated,	pixelCoords[3]);
		}
	}
	public void draw(boolean mirrored, int offsetX, int offsetY){
		GL11.glBegin(GL11.GL_QUADS);
		drawBash(mirrored, offsetX, offsetY);
		GL11.glEnd();
	}
	static int[] c = new int[8]; public boolean fresh;
	public void resetMod(){
		c[0] = pixelCoords[0];
		c[1] = pixelCoords[1];
		c[2] = pixelCoords[2];
		c[3] = pixelCoords[1];
		c[4] = pixelCoords[2];
		c[5] = pixelCoords[3];
		c[6] = pixelCoords[0];
		c[7] = pixelCoords[3];
		fresh = true;
	}
	public void translate(double x, double y){
		for(int i = 0; i < c.length; i+=2)
			c[i] += x;
		for(int i = 1; i < c.length; i+=2)
			c[i] += y;
	}
	public void scale(double x, double y){
		for(int i = 0; i < c.length; i+=2)
			c[i] *= x;
		for(int i = 1; i < c.length; i+=2)
			c[i] *= y;
	}
	public void rotate(double grad){
		grad %= 360;
		if(grad < 0){
			grad += 360;
		}
		
		int angle = (int)(grad*100/360);
		c[0] = (int)((pixelCoords[0]*UsefulF.cos100[angle]) - (pixelCoords[1]*UsefulF.sin100[angle]));
		c[1] = (int)((pixelCoords[0]*UsefulF.sin100[angle]) + (pixelCoords[1]*UsefulF.cos100[angle]));
		c[2] = (int)((pixelCoords[2]*UsefulF.cos100[angle]) - (pixelCoords[1]*UsefulF.sin100[angle]));
		c[3] = (int)((pixelCoords[2]*UsefulF.sin100[angle]) + (pixelCoords[1]*UsefulF.cos100[angle]));
		c[4] = (int)((pixelCoords[2]*UsefulF.cos100[angle]) - (pixelCoords[3]*UsefulF.sin100[angle]));
		c[5] = (int)((pixelCoords[2]*UsefulF.sin100[angle]) + (pixelCoords[3]*UsefulF.cos100[angle]));
		c[6] = (int)((pixelCoords[0]*UsefulF.cos100[angle]) - (pixelCoords[3]*UsefulF.sin100[angle]));
		c[7] = (int)((pixelCoords[0]*UsefulF.sin100[angle]) + (pixelCoords[3]*UsefulF.cos100[angle]));
	}
	public void bashMod(boolean mirrored){
		if(!fresh){
			(new Exception("You didn't reset the modification coords.")).printStackTrace();
		}
		if(mirrored){
			GL11.glTexCoord2d(texCoords[0], texCoords[3]);	GL11.glVertex2i(c[0], c[1]);
			GL11.glTexCoord2d(texCoords[2], texCoords[3]);	GL11.glVertex2i(c[2], c[3]);
			GL11.glTexCoord2d(texCoords[2], texCoords[1]); 	GL11.glVertex2i(c[4], c[5]);
			GL11.glTexCoord2d(texCoords[0], texCoords[1]); 	GL11.glVertex2i(c[6], c[7]);
		} else {
			GL11.glTexCoord2d(texCoords[2], texCoords[3]);	GL11.glVertex2i(c[0], c[1]);
			GL11.glTexCoord2d(texCoords[0], texCoords[3]);	GL11.glVertex2i(c[2], c[3]);
			GL11.glTexCoord2d(texCoords[0], texCoords[1]); 	GL11.glVertex2i(c[4], c[5]);
			GL11.glTexCoord2d(texCoords[2], texCoords[1]); 	GL11.glVertex2i(c[6], c[7]);
		}
		fresh = false;
	}
	public void drawMod(boolean mirrored){
		GL11.glBegin(GL11.GL_QUADS);
		bashMod(mirrored);
		GL11.glEnd();
	}
	public void fillBash(double x1, double y1, double x2, double y2, boolean mirrored){
			if(mirrored){
				GL11.glTexCoord2d(texCoords[0],	texCoords[3]);	GL11.glVertex2d(x1, y1);
				GL11.glTexCoord2d(texCoords[2], texCoords[3]);	GL11.glVertex2d(x2,	y1);
				GL11.glTexCoord2d(texCoords[2], texCoords[1]); 	GL11.glVertex2d(x2,	y2);
				GL11.glTexCoord2d(texCoords[0], texCoords[1]); 	GL11.glVertex2d(x1, y2);
			} else {
				GL11.glTexCoord2d(texCoords[2], texCoords[3]);	GL11.glVertex2d(x1, y1);
				GL11.glTexCoord2d(texCoords[0], texCoords[3]);	GL11.glVertex2d(x2,	y1);
				GL11.glTexCoord2d(texCoords[0], texCoords[1]); 	GL11.glVertex2d(x2,	y2);
				GL11.glTexCoord2d(texCoords[2], texCoords[1]); 	GL11.glVertex2d(x1, y2);
			}
	}
	public void fillBash(Rect rect, boolean mirrored, double offsetX, double offsetY){
		fillBash(rect.pos.x + offsetX, rect.pos.y + offsetY, rect.pos.x + offsetX + rect.size.x, rect.pos.y + offsetY + rect.size.y, mirrored);
	}
	
	public void addInfo(TexInfo... infos){
		this.infos = infos;
	}
	
	public Rect createBox(){
		return new Rect(pixelCoords);
	}
	public void debugPrintC(){
		for(int i = 0; i < c.length; i++){
			System.out.println(c[i]);
		}
	}
}
