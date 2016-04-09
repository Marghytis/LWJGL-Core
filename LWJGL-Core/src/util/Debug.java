package util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Debug {

	public static void print(ShortBuffer buffer){
		for(int i = 0; i < buffer.limit(); i++){
			System.out.println(buffer.get());
		}
		buffer.flip();
	}

	public static void print(FloatBuffer buffer){
		for(int i = 0; i < buffer.limit(); i++){
			System.out.println(buffer.get());
		}
		buffer.flip();
	}

	public static void print(IntBuffer buffer){
		for(int i = 0; i < buffer.limit(); i++){
			System.out.println(buffer.get());
		}
		buffer.flip();
	}
}
