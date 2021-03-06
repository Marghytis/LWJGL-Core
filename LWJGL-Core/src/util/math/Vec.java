package util.math;

import org.lwjgl.opengl.GL11;



public class Vec {

	public double x;
	public double y;
	
	public Vec(){}
	/**
	 * Do not create a constructor with another Vector. Use copy() instead
	 * @param x
	 * @param y
	 */
	public Vec(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public Vec copy(){
		return new Vec(x, y);
	}
	//GET
	public double x(){
		return x;
	}
	public double y(){
		return y;
	}
	public int xInt(){
		return (int)x;
	}
	public int yInt(){
		return (int)y;
	}
//SET
	public Vec set(Vec v){
		return set(v.x, v.y);
	}
	public Vec set(double x, double y){
		this.x = x;
		this.y = y;
		return this;
	}
//SHIFT
	public Vec shift(Vec v, double s){
		return shift(v.x*s, v.y*s);
	}
	public Vec shift(Vec d){
		return shift(d.x, d.y);
	}
	public Vec shift(double dx, double dy, double scale){
		return shift(dx*scale, dy*scale);
	}
	public Vec shift(double dx, double dy){
		x += dx;
		y += dy;
		return this;
	}
//SCALE
	public Vec scale(double s){
		return scale(s, s);
	}
	public Vec scale(double sX, double sY){
		x *= sX;
		y *= sY;
		return this;
	}
	public Vec normalize(){
		return setLength(1);
	}
	public Vec invert(){
		return scale(-1);
	}
	public static double[] factors = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
	public Vec round(int numbers){
		x = Math.round(x*factors[numbers])/factors[numbers];
		y = Math.round(y*factors[numbers])/factors[numbers];
		return this;
	}
	public Vec setLength(double length){
		return scale(length/length());
	}
	public Vec intify(){
		x = Math.round(x);
		y = Math.round(y);
		return this;
	}
//OTHER
	public Vec rotate(double angle){//(radians)
		double sin = Math.sin(angle), cos = Math.cos(angle),
				x2 = (x*cos) - (y*sin),
				y2 = (x*sin) + (y*cos);
		this.x = x2;
		this.y = y2;
		return this;
		
	}
	public Vec ortho(boolean left){
		return left ? new Vec(-y, x) : new Vec(y, -x);
	}
	public Vec minus(Vec v2){
		return new Vec(x - v2.x, y - v2.y);
	}
	public Vec minus(double x, double y){
		return new Vec(this.x - x, this.y - y);
	}
	public double cross(Vec p){
		return x*p.y - y*p.x;
	}
	public double dot(Vec p){
		return x*p.x + y*p.y;
	}
	public double length(){
		return Math.sqrt((x*x) + (y*y));
	}
	public double lengthSquare() {
		return x*x + (y*y);
	}
	public double slope(){
		return y/x;
	}
	public double angle() {
		return Math.atan2(y, x);
	}
	public boolean same(Vec other){
		return x == other.x && y == other.y;
	}
	public boolean is0(){
		return x == 0 && y == 0;
	}
	public double distTo(Vec v2){
		return Math.sqrt(((v2.x - x)*(v2.x - x)) + ((v2.y - y)*(v2.y - y)));
	}
	public boolean containedBy(Rect rect){
		return containedBy(rect.pos.x(), rect.pos.y(), rect.size.x(), rect.size.y());
	}
	public boolean containedBy(double x, double y, double width, double height){
		return  ((this.x >= x && this.x <= x + width) ||
				(this.x <= x && this.x >= x + width))
				&&
				((this.y >= y && this.y <= y + height) ||
				(this.y <= y && this.y >= y + height));
	}
	
	public void vertex(){
		GL11.glVertex2d(x, y);
	}
	/**
	 * Draws a little cross on the screen
	 */
	public void drawPoint(){
		int x = xInt();
		int y = yInt();
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2i(x,		y);
			GL11.glVertex2i(x-5,	y);
			GL11.glVertex2i(x,		y);
			GL11.glVertex2i(x,		y-5);
			GL11.glVertex2i(x,		y);
			GL11.glVertex2i(x+5,	y);
			GL11.glVertex2i(x,		y);
			GL11.glVertex2i(x,		y+5);
		GL11.glEnd();
	}
	
	public void drawAt(Vec pos){
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(pos.x,		pos.y);
			GL11.glVertex2d(pos.x + x,	pos.y + y);
		GL11.glEnd();
	}
	
	public String toString(){
		return "Vec[ " + (float)x + " | " + (float)y + " ]";
	}
	
	public String toStringExact(){
		return "Vec[ " + x + " | " + y + " ]";
	}
	
	public static Vec parseVec(String string){
		if(string.equals("null")) return null;
		String[] parts = string.split(" | ");
		parts[0].replace("Vec[ ", "");
		parts[1].replace(" ]", "");
		return new Vec(Double.parseDouble(parts[0].substring(0, parts[0].length()-1)), Double.parseDouble(parts[1].substring(0, parts[1].length()-1)));
	}
}
