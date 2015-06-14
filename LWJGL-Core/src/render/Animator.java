package render;

import org.lwjgl.opengl.GL11;

import util.math.Rect;

public class Animator{

	double deltaT;
	public int pos;
	private Texture ani;
	public int x, y;
	/**
	 * Has to be set every time the Texture is used (if it's needed)
	 */
	public Runnable endTask;
	
	public Animator(Texture ani){
		if(ani != null){
			this.ani = ani;
			this.x = ani.sequenceX == null ? ani.x : ani.sequenceX[0];
			this.y = ani.y;
		}
	}
	
	public void update(double delta){
		if(ani.sequenceX == null){
			executeTask();
		} else {
			deltaT += delta;
			if(deltaT >= ani.dt){
				
				pos += (int)(deltaT/ani.dt);
				deltaT %= ani.dt;
	
				if(pos >= ani.sequenceX.length){
					pos %= ani.sequenceX.length;
					executeTask();
				}
				if(ani.sequenceX != null){//max occur, when the endTask changes the animation!
					this.x = ani.sequenceX[pos];
				} else {
					this.x = ani.x;
				}
			}
		}
	}
	
	public void executeTask(){
		if(endTask != null){
			endTask.run();
			endTask = null;
		}
	}
	
	public void setAnimation(Texture ani, Runnable task){
		if(this.ani != ani){
			this.ani = ani;
			this.pos = 0;
			this.x = ani.sequenceX == null ? ani.x : ani.sequenceX[0];
			this.y = ani.y;
			this.endTask = task;
		}
	}
	
	public void setAnimation(Texture ani){
		if(this.ani != ani){
			this.ani = ani;
			this.pos = 0;
			this.x = ani.sequenceX == null ? ani.x : ani.sequenceX[0];
			this.y = ani.y;
			this.endTask = null;
		}
	}
	
	public Texture getAnimation(){
		return ani;
	}
	
	public void fill(Rect quad){
		fill(quad, 0);
	}

	public void fill(Rect quad, int orientation){
		fill(quad.pos.xInt(), quad.pos.yInt(), quad.size.xInt(), quad.size.yInt(), orientation);
	}
	
	public void fill(int xB, int yB, int width, int height, int orientation){
		ani.file.bind();
		switch(orientation){
		case 0://Normal
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y + ani.file.sectorSize.y);	GL11.glVertex2d(xB, 				yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y + ani.file.sectorSize.y);	GL11.glVertex2d(xB + width,			yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y); 							GL11.glVertex2d(xB + width,			yB + height);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y); 							GL11.glVertex2d(xB, 				yB + height);
			GL11.glEnd();
			break;
		case 1://Mirrored
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y + ani.file.sectorSize.y);	GL11.glVertex2d(xB, 				yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y + ani.file.sectorSize.y);	GL11.glVertex2d(xB + width,			yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y); 							GL11.glVertex2d(xB + width,			yB + height);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y); 							GL11.glVertex2d(xB, 				yB + height);
			GL11.glEnd();
			break;
		case 2://Flipped
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y);							GL11.glVertex2d(xB, 				yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y);							GL11.glVertex2d(xB + width,			yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y + ani.file.sectorSize.y); 	GL11.glVertex2d(xB + width,			yB + height);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y + ani.file.sectorSize.y); 	GL11.glVertex2d(xB, 				yB + height);
			GL11.glEnd();
			break;
		}
		
	}
	
	public void fillBash(Rect quad, double xT, double yT){
		fillBash(quad, xT, yT, 0);
	}

	public void fillBash(Rect quad, double xT, double yT, int orientation){
		fillBash(quad.pos.xInt(), quad.pos.yInt(), quad.size.xInt(), quad.size.yInt(), xT, yT, orientation);
	}
	
	public void fillBash(int xB, int yB, int width, int height, double xT, double yT, int orientation){
		xB += xT;
		yB += yT;
		switch(orientation){
		case 0://Normal
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y + ani.file.sectorSize.y);	GL11.glVertex2d(xB, 				yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y + ani.file.sectorSize.y);	GL11.glVertex2d(xB + width,			yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y); 							GL11.glVertex2d(xB + width,			yB + height);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y); 							GL11.glVertex2d(xB, 				yB + height);
			break;
		case 1://Mirrored
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y + ani.file.sectorSize.y);	GL11.glVertex2d(xB, 				yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y + ani.file.sectorSize.y);	GL11.glVertex2d(xB + width,			yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y); 							GL11.glVertex2d(xB + width,			yB + height);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y); 							GL11.glVertex2d(xB, 				yB + height);
			break;
		case 2://Flipped
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y);							GL11.glVertex2d(xB, 				yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y);							GL11.glVertex2d(xB + width,			yB);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x + ani.file.sectorSize.x, 	ani.file.sectorPos[x][y].y + ani.file.sectorSize.y); 	GL11.glVertex2d(xB + width,			yB + height);
				GL11.glTexCoord2d(ani.file.sectorPos[x][y].x, 							ani.file.sectorPos[x][y].y + ani.file.sectorSize.y); 	GL11.glVertex2d(xB, 				yB + height);
			break;
		}
		
	}
}
