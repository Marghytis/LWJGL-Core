package util;

import java.util.Random;

import render.Shader;

public class Color {

	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f);
	public static final Color WHITE = new Color(1, 1, 1);
	public static final Color RED = new Color(1, 0, 0);
	public static final Color GREEN = new Color(0, 1, 0);
	public static final Color BLUE = new Color(0, 0, 1);
	public static final Color YELLOW = new Color(1, 1, 0);
	public static final Color BROWN = new Color(0.68f, 0.44f, 0.26f);
	public static final Color INVISIBLE = new Color(0, 0, 0, 0);

	public static float boundAlpha = 1, boundR = 1, boundG = 1, boundB = 1;
	public float r, g, b, a;
	
	public Color(Random random, float minimum){
		this(minimum + (random.nextFloat()*(1-minimum)), minimum + (random.nextFloat()*(1-minimum)), minimum + (random.nextFloat()*(1-minimum)));
	}
	
	public Color(){
		this(1, 1, 1, 1);
	}
	
	public Color(String string){
		String[] args = string.split("_");
		r = Float.parseFloat(args[0]);
		g = Float.parseFloat(args[1]);
		b = Float.parseFloat(args[2]);
		a = Float.parseFloat(args[3]);
	}
	
	public Color(float r, float g, float b){
		this(r, g, b, 1);
	}
	
	public Color(float r, float g, float b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public Color(Color color) {
		this(color.r, color.g, color.b, color.a);
	}

	public void set(float r, float g, float b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public void set(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void set(Color c){
		set(c.r, c.g, c.b, c.a);
	}
	
	public Color minus(Color c){
		return new Color(r - c.r, g - c.g, b - c.b, a - c.a);
	}
	
	public Color add(Color c){
		r += c.r;
		g += c.g;
		b += c.b;
		a += c.a;
		return this;
	}
	
	public Color scale(float scale){
		r *= scale;
		g *= scale;
		b *= scale;
		a *= scale;
		return this;
	}
	
	public void bind(float a){
		boundAlpha = this.a*a;
		boundR = r;
		boundG = g;
		boundB = b;
		Shader.current.set("color", r, g, b, boundAlpha);
	}
	
	public void bindKeepAlpha(){
		Shader.current.set("color", r, g, b, boundAlpha);
		boundR = r;
		boundG = g;
		boundB = b;
	}
	
	public void bind(){
		Shader.current.set("color", r, g, b, a);
		boundAlpha = a;
		boundR = r;
		boundG = g;
		boundB = b;
	}
	
	public static void bind(float r, float g, float b, float a){
		Shader.current.set("color", r, g, b, a);
		boundAlpha = a;
		boundR = r;
		boundG = g;
		boundB = b;
	}
	
	public static void bindAlpha(float alpha){
		bind(boundR, boundG, boundB, alpha);
		boundAlpha = alpha;
	}
	
	public byte[] bytes(byte[] bytes){
		if(bytes == null) bytes = new byte[4];
		bytes[0] = (byte)(r*Byte.MAX_VALUE);
		bytes[1] = (byte)(g*Byte.MAX_VALUE);
		bytes[2] = (byte)(b*Byte.MAX_VALUE);
		bytes[3] = (byte)(a*Byte.MAX_VALUE);
		return bytes;
	}
	
	@Override
	public String toString(){
		return "Color[" + r + "f, " + g + "f, " + b + "f, " + a + "f]";
	}
	
	public String toString2(){
		return r + "_" + g + "_" + b + "_" + a;
	}
}
