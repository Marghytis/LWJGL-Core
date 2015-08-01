package util;

import org.lwjgl.opengl.GL11;

import render.Texture;

public class Render {

	public static void quad(double x1, double y1, double x2, double y2){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(x1, y1);
			GL11.glVertex2d(x2, y1);
			GL11.glVertex2d(x2, y2);
			GL11.glVertex2d(x1, y2);
		GL11.glEnd();
	}

	public static void quad(int x1, int y1, int x2, int y2){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(x1, y1);
			GL11.glVertex2i(x2, y1);
			GL11.glVertex2i(x2, y2);
			GL11.glVertex2i(x1, y2);
		GL11.glEnd();
	}

	public static void quad(double x1, double y1, double x2, double y2, Texture tex){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2d(tex.file.sectorPos[tex.x][tex.y].x,							tex.file.sectorPos[tex.x][tex.y].y + tex.file.sectorSize.y);
			GL11.glVertex2d(x1, y1);
			GL11.glTexCoord2d(tex.file.sectorPos[tex.x][tex.y].x + tex.file.sectorSize.x,	tex.file.sectorPos[tex.x][tex.y].y + tex.file.sectorSize.y);
			GL11.glVertex2d(x2, y1);
			GL11.glTexCoord2d(tex.file.sectorPos[tex.x][tex.y].x + tex.file.sectorSize.x,	tex.file.sectorPos[tex.x][tex.y].y);
			GL11.glVertex2d(x2, y2);
			GL11.glTexCoord2d(tex.file.sectorPos[tex.x][tex.y].x,							tex.file.sectorPos[tex.x][tex.y].y);
			GL11.glVertex2d(x1, y2);
		GL11.glEnd();
	}

	public static void quad(int x1, int y1, int x2, int y2, Texture tex){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2d(tex.file.sectorPos[tex.x][tex.y].x,							tex.file.sectorPos[tex.x][tex.y].y + tex.file.sectorSize.y);
			GL11.glVertex2i(x1, y1);
			GL11.glTexCoord2d(tex.file.sectorPos[tex.x][tex.y].x + tex.file.sectorSize.x,	tex.file.sectorPos[tex.x][tex.y].y + tex.file.sectorSize.y);
			GL11.glVertex2i(x2, y1);
			GL11.glTexCoord2d(tex.file.sectorPos[tex.x][tex.y].x + tex.file.sectorSize.x,	tex.file.sectorPos[tex.x][tex.y].y);
			GL11.glVertex2i(x2, y2);
			GL11.glTexCoord2d(tex.file.sectorPos[tex.x][tex.y].x,							tex.file.sectorPos[tex.x][tex.y].y);
			GL11.glVertex2i(x1, y2);
		GL11.glEnd();
	}
	
	public static void bounds(int x1, int y1, int x2, int y2){
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2i(x1, y1);
			GL11.glVertex2i(x2, y1);
			GL11.glVertex2i(x2, y2);
			GL11.glVertex2i(x1, y2);
		GL11.glEnd();
	}
	
	public static void bounds(double x1, double y1, double x2, double y2){
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2d(x1, y1);
			GL11.glVertex2d(x2, y1);
			GL11.glVertex2d(x2, y2);
			GL11.glVertex2d(x1, y2);
		GL11.glEnd();
	}
}
