package core;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import util.math.Vec;

public interface Listener {
	
	public static List<Listener> listeners = new ArrayList<>();
	
	public static Vec[] buttonsPressed = {new Vec(), new Vec(), new Vec()};
	
	public static void listen(){
		while(Mouse.next()){
			int button = Mouse.getEventButton();
			if(button >= 0){
				Vec eventPos = new Vec(Mouse.getEventX(), Mouse.getEventY());
				if(Mouse.getEventButtonState()){
					//MOUSE PRESSED
					for(Listener l : listeners){
						if(l.pressed(button, eventPos)) break;
					}
					buttonsPressed[button].set(eventPos);
				} else {
					//MOUSE RELEASED
					for(Listener l : listeners){
						if(l.released(button, eventPos, eventPos.minus(buttonsPressed[button]))) break;
					}
				}
			}
		}
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				for(Listener l : listeners){
					if(l.keyPressed(Keyboard.getEventKey())) break;
				}
			} else {
				for(Listener l : listeners){
					if(l.keyReleased(Keyboard.getEventKey())) break;
				}
			}
		}
	}
	
	public static Vec getMousePos(){
		return new Vec(Mouse.getX(), Mouse.getY());
	}

	public boolean pressed(int button, Vec mousePos);
	public boolean released(int button, Vec mousePos, Vec pathSincePress);
	public boolean keyPressed(int key);
	public boolean keyReleased(int key);
}
