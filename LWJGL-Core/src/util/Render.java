package util;

import org.lwjgl.opengl.GL11;

import util.math.UsefulF;

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
	static double x1, y1, x2, y2;
	public static void bounds(int[] coords, double offsetX, double offsetY){
		x1 = coords[0] + offsetX;
		y1 = coords[1] + offsetY;
		x2 = coords[2] + offsetX;
		y2 = coords[3] + offsetY;
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2d(x1, y1);
			GL11.glVertex2d(x2, y1);
			GL11.glVertex2d(x2, y2);
			GL11.glVertex2d(x1, y2);
		GL11.glEnd();
	}
	
	public static void circle(double xS, double yS, double r){
		GL11.glBegin(GL11.GL_POLYGON);
			for(int angle = 0; angle < 100; angle++){
				GL11.glVertex2d(r*UsefulF.cos100[angle] + xS, r*UsefulF.sin100[angle] + yS);
			}
		GL11.glEnd();
	}
}
