package render;

public class Animation {

	public TexAtlas atlas;
	public double y1, w, y2, frameTime;
	public double[] x1;
	public int y;
	public int[] x;
	
	public Animation(TexAtlas atlas, double fps, int y, int... x){
		this.atlas = atlas;
		this.frameTime = 1/fps;
		this.y = y;
		this.x = x;
		this.x1 = new double[x.length];
		for (int i = 0; i < x1.length; i++) {
			this.x1[i] = x[i]*atlas.partWidth + atlas.texCoords[0];//yes tex.pWR is relative to the file
		}
		this.y1 = y*atlas.partHeight + atlas.texCoords[1];
		this.y2 = y1 + atlas.partHeight;
		this.w = atlas.partWidth;
	}
	
}
