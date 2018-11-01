package util;

import java.nio.*;

import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

public class Sound {
	  
  private int handle;

	public Sound(String fileName){

		//Allocate space to store return information from the function
		MemoryStack.stackPush();
		IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);
		MemoryStack.stackPush();
		IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);

		ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(fileName, channelsBuffer, sampleRateBuffer);

		//Retreive the extra information that was stored in the buffers by the function
		int channels = channelsBuffer.get();
		int sampleRate = sampleRateBuffer.get();
		//Free the space we allocated earlier
		MemoryStack.stackPop();
		MemoryStack.stackPop();
		
		//Find the correct OpenAL format
		int format = -1;
		if(channels == 1) {
		    format = AL10.AL_FORMAT_MONO16;
		} else if(channels == 2) {
		    format = AL10.AL_FORMAT_STEREO16;
		}

		//Request space for the buffer
		handle = AL10.alGenBuffers();

		//Send the data to OpenAL
		AL10.alBufferData(handle, format, rawAudioBuffer, sampleRate);

		//Free the memory allocated by STB
		LibCStdlib.free(rawAudioBuffer);
	}
	public int getHandle() {
		return handle;
	}
	
	public void finalize() {
	    AL10.alDeleteBuffers(handle);
	}
}
