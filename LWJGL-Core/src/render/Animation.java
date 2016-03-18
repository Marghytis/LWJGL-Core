package render;

public class Animation {

	public String name;
	public TexAtlas atlas;
	public float y1, w, y2;
	public double frameTime;
	public float[] x1;
	public int y, taskTime;
	public int[] x;
	public double duration;
	
	public Animation(String name, int taskTime, TexAtlas atlas, double fps, int y, int... x){
		this.name = name;
		this.atlas = atlas;
		this.frameTime = 1/fps;
		this.taskTime = taskTime;
		this.y = y;
		this.x = x;
		this.x1 = new float[x.length];
		for (int i = 0; i < x1.length; i++) {
			this.x1[i] = x[i]*atlas.partWidth + atlas.texCoords[0];//yes tex.pWR is relative to the file
		}
		this.y1 = y*atlas.partHeight + atlas.texCoords[1];
		this.y2 = y1 + atlas.partHeight;
		this.w = atlas.partWidth;
		this.duration = x.length*frameTime;
	}
	
	public Animation(String name, TexAtlas atlas, double fps, int y, int... x){
		this(name, x.length-1, atlas, fps, y, x);
	}
	
}
