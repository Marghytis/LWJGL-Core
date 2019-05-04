package core;

import java.util.*;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import util.Time;
import util.math.IntVec;

public class Core implements Runnable {

	public IntVec SIZE_HALF, SIZE;
	public final int fps;
	public final double frameTime;
	
	private Thread gameLoopThread;
	private Runnable doAfterTheRest;
	private Window window;
	private Speaker speaker;
	private Game game;
//	private 

	public Core(Window window, Speaker speaker, int fps) {
		gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		this.window = window;
		this.speaker = speaker;
		if(this instanceof Game)
			this.game = (Game)this;
		else
			throw new RuntimeException("Only call this constructor if this core is an instance of Game!");
		this.fps = fps;
		this.frameTime = 1.0/fps;
		this.SIZE_HALF = window.SIZE_HALF;
		this.SIZE = window.SIZE;
	}
	
	public Core(Window window, Speaker speaker, Game game, int fps) {
		gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		this.window = window;
		this.speaker = speaker;
		this.game = game;
		this.fps = fps;
		this.frameTime = 1.0/fps;
		this.SIZE_HALF = window.SIZE_HALF;
		this.SIZE = window.SIZE;
	}
	
	public void setUpdaters(Updater... updaters) {
		this.updaters.clear();
		for(Updater u : updaters) {
			this.updaters.add(u);
		}
	}

	public void start() {
		start(false);
	}
	public void start(boolean sameThread) {
		String osName = System.getProperty("os.name");
        if ( sameThread || osName.contains("Mac") ) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
	}
	
	public void run() {
        init();
        coreLoop();
	}
	
	private void init(){
		window.init();
		speaker.init();
		game.initGame();
		window.show();
	}
	
	public static boolean renderedToLong, updatedToLong;
	public static int ticks;
	private void coreLoop(){
		
		Time.update(0);//this is used to get the time delta for moving objects
		Time.update(1);
		double interval = 1000000000.0/fps;
		double intervalSec = interval/1000000000;
		double currentTime = System.nanoTime();
		double targetTickTime = currentTime;
		double lastTickTime = currentTime - interval;
		
		while(!(glfwWindowShouldClose(window.getHandle())))//TODO remove double functionality for exiting
		{

			window.listen();//LISTEN INPUT
			
			int ticksLocal = 0;
			currentTime = System.nanoTime();
			Time.update(3);
			while(currentTime >= targetTickTime) {

				tick(intervalSec);//UPDATE GAME LOGIC
				
				lastTickTime = targetTickTime;
				targetTickTime += interval;
				ticksLocal++;
			}
			Time.update(1);
			Time.update(3);
			updatedToLong = Time.delta[3] > 0.02;
			ticks = ticksLocal;
			
			Time.update(2);
			double inter = (System.nanoTime() - lastTickTime)/interval;
			if(inter < 0) {
				System.out.println("WHAAT: " + inter);
			}
			window.render(inter);//RENDER SCENE

			Time.update(2);
			renderedToLong = Time.delta[2] > 0.02;

//			if(updatedToLong) System.out.println("updated too long (" + Time.delta[3] + " s, " + ticksLocal + " ticks)  ");
//			if(renderedToLong) System.out.println("rendered too long (" + Time.delta[2] + " s)  ");
			Time.update(4);
			//some other action can be performed after this cycle
			if(doAfterTheRest != null){
				doAfterTheRest.run();
				doAfterTheRest = null;
			}

			window.swapBuffers();// swap the color buffers
//			sync(60);
			Time.update(4);
		}
		
		//When the WHILE-loop has ended, exit the program
		exit();
	}
	
	public double timeLeft() {
		return frameTime - Time.peakDelta(1);
	}
	
	List<Updater> updaters = new ArrayList<>();
	
	public void tick(double delta){
		for(Updater u : updaters){
			if(u.update(delta))
				break;
		}
	}
	
	private long variableYieldTime, lastTime2;
	
	/**
     * An accurate sync method that adapts automatically
     * to the system it runs on to provide reliable results.
     *
     * @param fps The desired frame rate, in frames per second
     * @author kappa (On the LWJGL Forums)
     */
    private void sync(int fps) {
        if (fps <= 0) return;
         
        long sleepTime = 1000000000 / fps; // nanoseconds to sleep this frame
        // yieldTime + remainder micro & nano seconds if smaller than sleepTime
        long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
        long overSleep = 0; // time the sync goes over by
         
        try {
            while (true) {
                long t = System.nanoTime() - lastTime2;
                 
                if (t < sleepTime - yieldTime) {
                    Thread.sleep(1);
                } else if (t < sleepTime) {
                    // burn the last few CPU cycles to ensure accuracy
                    Thread.yield();
                } else {
                    overSleep = t - sleepTime;
                    break; // exit while loop
                }
            }
        } catch (InterruptedException e) {
        	e.printStackTrace();
        } finally {
        	lastTime2 = System.nanoTime() - Math.min(overSleep, sleepTime);
           
            // auto tune the time sync should yield
            if (overSleep > variableYieldTime) {
                // increase by 200 microseconds (1/5 a ms)
                variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
            }
            else if (overSleep < variableYieldTime - 200*1000) {
                // decrease by 2 microseconds
                variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
            }
        }
    }
	
	public void doAfterTheRest(Runnable doAfterTheRest) {
		this.doAfterTheRest = doAfterTheRest;
	}
	
	//TODO add resource deletion
	private void exit(){
		
		window.terminate();
		speaker.terminate();
		System.exit(0);
	}
	
	public Window getWindow(){
		return window;
	}
}
