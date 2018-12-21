package core;

import util.math.Vec;

public interface Listener {

	public boolean pressed(int button, Vec mousePos);
	public boolean released(int button, Vec mousePos, Vec mouseDelta);
	public boolean keyPressed(int key);
	public boolean keyReleased(int key);
	public boolean charTyped(char ch);
}
