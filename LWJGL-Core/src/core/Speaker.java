package core;

import org.lwjgl.openal.*;

public class Speaker {

	private long device;
	private long context;
	
	public void init() {
		initOpenAL();
	}
	
	private void initOpenAL() {

		String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		device = ALC10.alcOpenDevice(defaultDeviceName);
		int[] attributes = {0};
		context = ALC10.alcCreateContext(device, attributes);
		ALC10.alcMakeContextCurrent(context);
		
		ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
		AL.createCapabilities(alcCapabilities);

	}
	
	public void terminate() {
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}
}
