package util;

import org.lwjgl.openal.*;

public class SoundSource {

	int handle;
	boolean playing = false;
	
	public SoundSource() {
		handle = AL10.alGenSources();
	}

	public void loadSound(Sound sound) {
		loadSound(sound, false);
	}
	public void loadSound(Sound sound, boolean loop) {

		AL10.alSourcei(handle, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
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
	
	public boolean isPlaying(){
		return AL10.alGetSourcei(handle, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	public void finalize() {
		AL10.alDeleteSources(handle);
	}
}
