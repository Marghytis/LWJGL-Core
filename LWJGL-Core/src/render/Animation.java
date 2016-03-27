package render;

public class Animation {

	public String name;
	public TexAtlas atlas;
	public double frameTime;
	public int taskTime;
	public int[] indices;
	public double duration;
	
	
	public Animation(TexAtlas atlas, int x, int y){
		this("", atlas, x, y);
	}
	/**
	 * Animation with a single frame
	 * @param name
	 * @param atlas
	 * @param x
	 * @param y
	 */
	public Animation(String name, TexAtlas atlas, int x, int y){
		this(name, 0, atlas, -1, y, new int[]{x});//the -1 makes the animator switch instantly
	}
	public Animation(String name, int taskTime, TexAtlas atlas, double fps, int... indices){
		this.name = name;
		this.atlas = atlas;
		this.frameTime = 1/fps;
		this.taskTime = taskTime;
		this.indices = indices;
		this.duration = indices.length*frameTime;
	}
	public Animation(String name, int taskTime, TexAtlas atlas, double fps, int y, int... x){
		this.name = name;
		this.atlas = atlas;
		this.frameTime = 1/fps;
		this.taskTime = taskTime;
		this.indices = new int[x.length];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = y*atlas.partsX + x[i];
		}
		this.duration = x.length*frameTime;
	}
	public Animation(String name, TexAtlas atlas, double fps, int y, int... x){
		this(name, x.length-1, atlas, fps, y, x);
	}
	
}
