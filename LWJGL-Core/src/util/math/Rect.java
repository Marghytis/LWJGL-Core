package util.math;

import org.lwjgl.opengl.GL11;


public class Rect {

	public Vec pos, size;
	
	public Rect(){
		pos = new Vec();
		size = new Vec();
	}
	
	public Rect(double x, double y, double width, double height){
		this.pos = new Vec(x, y);
		this.size = new Vec(width, height);
	}
	/**
	 * Creates the rectangle between the points orthogonal to the screen borders
	 * @param v1
	 * @param v2
	 */
	public Rect(Vec v1, Vec v2){
		this(v1.x, v1.y, v2.x - v1.x, v2.y - v1.y);
	}
	public Rect(int[] coords){
		this(coords[0], coords[1], coords[2] - coords[0], coords[3] - coords[1]);
	}
	public Rect copy(){
		return new Rect(pos.x(), pos.y(), size.x(), size.y());
	}
//SET
	public Rect set(Rect r){
		if(r == null) return set(0, 0, 0, 0);
		else return set(r.pos.x(), r.pos.y(), r.size.x(), r.size.y());
	}
	public Rect set(int[] coords){
		return set(coords[0], coords[1], coords[2] - coords[0], coords[3] - coords[1]);
	}
	public Rect set(double x, double y, double width, double height){
		pos.set(x, y);
		size.set(width, height);
		return this;
	}
//SHIFT
	public Rect shift(Vec v){
		return shift(v.x(), v.y());
	}
	public Rect shift(double x, double y){
		pos.shift(x, y);
		return this;
	}
//SCALE
	public Rect scale(double s){
		pos.scale(s);
		size.scale(s);
		return this;
	}
	public Rect scaleSizeKeepCenter(double s){
		double width = size.x() * s;
		double height = size.y() * s;
		pos.shift((size.x() - width)/2, (size.y() - height)/2);
		size.set(width, height);
		return this;
	}
	public Rect scaleSizeKeepCenter(double x, double y){
		double width = size.x() * x;
		double height = size.y() * y;
		pos.shift((size.x() - width)/2, (size.y() - height)/2);
		size.set(width, height);
		return this;
	}
	public Rect intify(){
		pos.intify();
		size.intify();
		return this;
	}
//OTHER
	public Vec middle(){
		return new Vec(pos.x() + (size.x()/2), pos.y() + (size.y()/2));
	}
	public boolean is0(){
		return pos.is0() && size.is0();
	}
	public boolean equals(int[] coords){
		return pos.x == coords[0] && pos.y == coords[1] && pos.x + size.x == coords[2] && pos.y + size.y == coords[3];
	}
	public boolean contains(Vec v){
		return contains(v.x, v.y);
	}
	public boolean contains(double x, double y){
		return  ((x >= pos.x && x <= pos.x + size.x) ||
				(x <= pos.x && x >= pos.x + size.x))
				&&
				((y >= pos.y && y <= pos.y + size.y) ||
				(y <= pos.y && y >= pos.y + size.y));
	}
	public void outline(){
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2d(pos.x(),			pos.y());
			GL11.glVertex2d(pos.x() + size.x(),	pos.y());
			GL11.glVertex2d(pos.x() + size.x(),	pos.y() + size.y());
			GL11.glVertex2d(pos.x(),			pos.y() + size.y());
		GL11.glEnd();
	}
	public String toString(){
		return "Rect[ Pos: (" + pos.x() + " | " + pos.y() + "), Size: (" + size.x() + " | " + size.y() + " ]";
	}
}
