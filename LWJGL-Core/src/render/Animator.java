package render;

public class Animator {

	public Animation ani;
	public Texture tex;
	public int pos;
	public double deltaT;
	/**
	 * Has to be set every time the Texture is used (if it's needed)
	 */
	public Runnable endTask;
	public boolean instantSwitch;
	
	public Animator(){
		setTexture(Texture.emptyTexture);
	}
	
	public Animator(Texture tex){
		setTexture(tex);
	}
	
	public Animator(Animation ani) {
		setAnimation(ani);
	}
	
	public void update(double delta){
		if(instantSwitch){
			executeTask();
		} else {
			if(deltaT >= ani.frameTime){
	
				pos += (int)(deltaT/ani.frameTime);
				deltaT %= ani.frameTime;
				
				if(pos >= ani.indices.length){
					pos %= ani.indices.length;
				}
				
				if(pos >= ani.taskTime){
					executeTask();
				}
				this.tex = ani.atlas.texs[ani.indices[pos]];
			}
			deltaT += delta;
		}
	}
	
	public void setAnimation(Animation ani, Runnable endTask){
		if(ani != this.ani){
			
			lastTex = this.tex;
			lastAni = this.ani;
			lastTask = this.endTask;
			this.pos = 0;
			
			this.ani = ani;
			this.tex = ani.atlas.texs[ani.indices[pos]];
			this.endTask = endTask;
			if(ani.frameTime == -1){
				instantSwitch = true;
			} else {
				instantSwitch = false;
			}
		}
	}
	public void setAnimation(Animation ani){
		setAnimation(ani, null);
	}
	
	public void setTexture(Texture tex, Runnable endTask){
		if(tex != this.tex){
			
			lastTex = this.tex;
			lastAni = this.ani;
			lastTask = this.endTask;
					
			this.ani = null;
			this.tex = tex;
			this.endTask = endTask;
			this.pos = 0;
			
			this.instantSwitch = true;
		}
	}
	public void setTexture(Texture tex){
		setTexture(tex, null);
	}
	
	public void setLast(){
		if(lastAni != null){
			setAnimation(lastAni, lastTask);
		} else {
			setTexture(lastTex, lastTask);
		}
	}
	
	public Texture lastTex;
	public Animation lastAni;
	public Runnable lastTask;
	
	public void executeTask(){
		if(endTask != null){
			endTask.run();
			endTask = null;
		}
	}
	
	public void bindTex(){
		if(tex != null) tex.file.bind();
		else if(ani != null) ani.atlas.file.bind();
	}
}
