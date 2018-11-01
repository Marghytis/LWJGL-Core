package util;

import org.lwjgl.openal.AL10;

public class SoundSource {

	int handle;
	boolean playing = false;
	
	public SoundSource() {
		handle = AL10.alGenSources();
	}
	
	public void loadSound(Sound sound) {

		//Assign our buffer to the source
		AL10.alSourcei(handle, AL10.AL_BUFFER, sound.getHandle());
	}

	public void play(){
		playing = true;
		AL10.alSourcePlay(handle);
	}
	
	public void pause(){
		playing = false;
		AL10.alSourcePause(handle);
	}
	
	public void stop(){
		playing = false;
		AL10.alSourceStop(handle);
	}
	
	public boolean hasFinished(){
		return AL10.alGetSourcei(handle, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED;
	}
	
	public void finalize() {
		AL10.alDeleteSources(handle);
	}
}
