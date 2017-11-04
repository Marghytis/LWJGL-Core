package util;

import java.io.*;
import java.nio.ByteBuffer;

import render.TexFile;
import util.math.IntVec;

public class Util {

	public static ByteBuffer readFile(String path, boolean internal, IntVec size){
		InputStream in = null;
		if ( internal ){
			in = TexFile.class.getResourceAsStream(path);
		} else try {
			in = new FileInputStream(path);
		} catch(IOException e){
			e.printStackTrace();
		}
		if(in == null) return null;
		// Open the PNG file as an InputStream
		try {
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);
			
			size.w = decoder.getWidth();
			size.h = decoder.getHeight();
			
			// Decode the PNG file in a ByteBuffer
			ByteBuffer data = ByteBuffer.allocateDirect(4 * size.w * size.h);
			decoder.decode(data, decoder.getWidth() * 4, PNGDecoder.RGBA);
			data.flip();
			
			in.close();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
