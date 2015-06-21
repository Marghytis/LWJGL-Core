package render;

import org.lwjgl.opengl.GL11;

import util.math.Rect;


public class Texture {
	
	public TexFile file;
	public double dt;
	public int x, y;
	public int[] sequenceX;
	
	public Texture(String filePath){
		this(new TexFile(filePath));
	}

	public Texture(TexFile file){
		this(file, 0, 0);
	}
	
	public Texture(TexFile file, int x, int y){
		this.file = file;
		this.x = x;
		this.y = y;
	}
	
	public Texture(TexFile file, int fps, int y, int... sequenceX) {
		this.file = file;
		this.dt = 1.0/fps;
		this.y = y;
		this.x = sequenceX[0];
		this.sequenceX = sequenceX;
	}
	
	@Deprecated
	public void fill(Rect quad){
		fill(quad, 0);
	}
	/**
	 * Use only, if the texture is never going to be an animation
	 * @param quad
	 * @param orientation
	 */
	@Deprecated 
	public void fill(Rect quad, int orientation){
		file.bind();
		int xQ = quad.pos.xInt();
		int yQ = quad.pos.yInt();
		int width = quad.size.xInt();
		int height = quad.size.yInt();
		switch(orientation){
		case 0://Normal
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2d(file.sectorPos[x][y].x, 							file.sectorPos[x][y].y + file.sectorSize.y);	GL11.glVertex2d(xQ, 				yQ);
				GL11.glTexCoord2d(file.sectorPos[x][y].x + file.sectorSize.x, 	file.sectorPos[x][y].y + file.sectorSize.y);	GL11.glVertex2d(xQ + width,	yQ);
				GL11.glTexCoord2d(file.sectorPos[x][y].x + file.sectorSize.x, 	file.sectorPos[x][y].y); 						GL11.glVertex2d(xQ + width,	yQ + height);
				GL11.glTexCoord2d(file.sectorPos[x][y].x, 							file.sectorPos[x][y].y); 						GL11.glVertex2d(xQ, 				yQ + height);
			GL11.glEnd();
			break;
		case 1://Mirrored
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2d(file.sectorPos[x][y].x + file.sectorSize.x, 	file.sectorPos[x][y].y + file.sectorSize.y);	GL11.glVertex2d(xQ, 				yQ);
				GL11.glTexCoord2d(file.sectorPos[x][y].x, 							file.sectorPos[x][y].y + file.sectorSize.y);	GL11.glVertex2d(xQ + width,	yQ);
				GL11.glTexCoord2d(file.sectorPos[x][y].x, 							file.sectorPos[x][y].y); 						GL11.glVertex2d(xQ + width,	yQ + height);
				GL11.glTexCoord2d(file.sectorPos[x][y].x + file.sectorSize.x, 	file.sectorPos[x][y].y); 						GL11.glVertex2d(xQ, 				yQ + height);
			GL11.glEnd();
			break;
		case 2://Flipped
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2d(file.sectorPos[x][y].x, 							file.sectorPos[x][y].y);						GL11.glVertex2d(xQ, 				yQ);
				GL11.glTexCoord2d(file.sectorPos[x][y].x + file.sectorSize.x, 	file.sectorPos[x][y].y);						GL11.glVertex2d(xQ + width,	yQ);
				GL11.glTexCoord2d(file.sectorPos[x][y].x + file.sectorSize.x, 	file.sectorPos[x][y].y + file.sectorSize.y); 	GL11.glVertex2d(xQ + width,	yQ + height);
				GL11.glTexCoord2d(file.sectorPos[x][y].x, 							file.sectorPos[x][y].y + file.sectorSize.y); 	GL11.glVertex2d(xQ, 				yQ + height);
			GL11.glEnd();
			break;
		}
		
	}
}
