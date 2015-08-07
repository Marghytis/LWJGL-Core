package render;

public class Animator extends Texture {

	public Animation ani;
	public Texture tex;
	public int pos;
	public double deltaT;
	/**
	 * Has to be set every time the Texture is used (if it's needed)
	 */
	public Runnable endTask;
	public boolean instantSwitch;
	
	public Animator(Texture tex){
		setTexture(tex, null);
	}
	
	public Animator(Animation ani) {
		setAnimation(ani, null);
	}
	
	public void update(double delta){
		if(instantSwitch){
			executeTask();
		} else {
			if(deltaT >= ani.frameTime){
	
				pos += (int)(deltaT/ani.frameTime);
				deltaT %= ani.frameTime;
				
				if(pos >= ani.x1.length){
					pos %= ani.x1.length;
					executeTask();
				}
				
				this.texCoords[0] = ani.x1[pos];
				this.texCoords[2] = texCoords[0] + ani.w;
			}
			deltaT += delta;
		}
	}
	
	public void setAnimation(Animation ani, Runnable endTask){
		if(!ani.equals(this.ani)){
			this.ani = ani;
			this.tex = null;
			this.endTask = endTask;
			this.pos = 0;
			
			this.file = ani.atlas.file;
			this.texCoords[0] = ani.x1[0];
			this.texCoords[1] = ani.y1;
			this.texCoords[2] = ani.x1[0] + ani.w;
			this.texCoords[3] = ani.y2;
			
			this.w = ani.atlas.w;
			this.h = ani.atlas.h;

			this.pixelCoords[0] = ani.atlas.pixelCoords[0];
			this.pixelCoords[1] = ani.atlas.pixelCoords[1];
			this.pixelCoords[2] = ani.atlas.pixelCoords[2];
			this.pixelCoords[3] = ani.atlas.pixelCoords[3];
			
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
		if(!tex.equals(this.tex)){
			this.tex = tex;
			this.ani = null;
			this.endTask = endTask;
			this.pos = 0;
			
			this.file = tex.file;
			this.texCoords = tex.texCoords;
			this.x1 = tex.x1;
			this.y1 = tex.y1;
			this.w = tex.w;
			this.h = tex.h;
			this.pixelCoords[0] = tex.pixelCoords[0];
			this.pixelCoords[1] = tex.pixelCoords[1];
			this.pixelCoords[2] = tex.pixelCoords[2];
			this.pixelCoords[3] = tex.pixelCoords[3];
			
			this.instantSwitch = true;
		}
	}
	public void setTexture(Texture tex){
		setTexture(tex, null);
	}
	
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
