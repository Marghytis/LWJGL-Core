package render;

public class TexAtlas {
	public static TexAtlas emptyAtlas = new TexAtlas(TexFile.emptyTex, 0, 0, 0, 0, 1, 1, 0, 0);

	public TexFile file;
	public int partsX, partsY;
	public Texture[] texs;
	public int x1, y1, w1, h1;
	public float x2, y2, w2, h2;
	public int[] pixelCoords = new int[4];
	public TexInfo[] infos;
	
	public TexAtlas(TexFile file, int x1, int y1, int w, int h, int partsX, int partsY, double offsetX, double offsetY) {
		
		this.file = file;
		this.partsX = partsX;
		this.partsY = partsY;
		
		this.x1 = x1;
		this.y1 = y1;
		this.w1 = w/partsX;
		this.h1 = h/partsY;
		
		this.x2 = (float)x1/file.width;
		this.y2 = (float)y1/file.height;
		this.w2 = (float)w1/file.width;
		this.h2 = (float)h1/file.height;

		this.pixelCoords[0] = (int)(offsetX*w1);
		this.pixelCoords[1] = (int)(offsetY*h1);
		this.pixelCoords[2] = pixelCoords[0] + w1;
		this.pixelCoords[3] = pixelCoords[1] + h1;
		
		this.texs = new Texture[partsX*partsY];
		for(int y = 0, i = 0; y < partsY; y++){
			for(int x = 0; x < partsX; x++, i++){
				texs[i] = new Texture();
				
				texs[i].file = file;
				texs[i].pixelCoords[0] = pixelCoords[0];
				texs[i].pixelCoords[1] = pixelCoords[1];
				texs[i].pixelCoords[2] = pixelCoords[2];
				texs[i].pixelCoords[3] = pixelCoords[3];
				
				texs[i].w = w1;
				texs[i].h = h1;
				
				texs[i].texCoords[0] = x2 + (x*w2);
				texs[i].texCoords[1] = y2 + (y*h2);
				texs[i].texCoords[2] = texs[i].texCoords[0] + w2;
				texs[i].texCoords[3] = texs[i].texCoords[1] + h2;
			}
		}
	}
	
	public TexAtlas(TexFile file, int partsX, int partsY, double offsetX, double offsetY){
		this(file, 0, 0, file.width, file.height, partsX, partsY, offsetX, offsetY);
	}
	
	public TexAtlas(String filePath, int partsX, int partsY, double offsetX, double offsetY){
		this(new TexFile(filePath), partsX, partsY, offsetX, offsetY);
	}
	
	public void addInfo(TexInfo... infos){
		this.infos = infos;
	}
	
	public void applyInfos(){
		for(int j = 0; j < texs.length; j++){
			texs[j].info = new int[infos.length][];
			for(int i = 0; i < infos.length; i++){
				texs[j].info[i] = infos[i].getInfo(j);
			}
		}
	}
	
	public Texture tex(int x, int y){
		return texs[y*partsX + x];
	}
	
	@Deprecated
	/**Better use the constructor of Animation*/
	public Animation sfA(String name, int x, int y){
		return new Animation(name, this, x, y);
	}
	@Deprecated
	public Animation sfA(int x, int y){
		return sfA("", x, y);
	}
}
