package util;

public class MultiSoundSource {

	SoundSource[] sources;
	int index;
	
	public MultiSoundSource(int nSources) {
		sources = new SoundSource[nSources];
		for(int i = 0; i < sources.length; i++) {
			sources[i] = new SoundSource();
		}
	}
	
	public void loadSound(Sound sound) {
		for(SoundSource src : sources) {
			src.loadSound(sound);
		}
	}
	
	public void play() {
		if(!sources[index].hasFinished()) {
			index = (index+1)%sources.length;
		}
		sources[index].play();
	}
	
}
