package render;


public class TexAtlas extends Texture {

	public int partsX, partsY;
	public double partWidth, partHeight;
	public Texture[][] texs;
	
	public TexAtlas(TexFile file, int x1, int y1, int w, int h, int partsX, int partsY, double offsetX, double offsetY) {
		super(file, x1, y1, w/=partsX, h/=partsY, offsetX, offsetY);
		this.partsX = partsX;
		this.partsY = partsY;
		if(file.width > 0 && file.height > 0){//only for the "empty"-Texture
			this.partWidth = (double)w/file.width;
			this.partHeight = (double)h/file.height;
		}
		
		this.texs = new Texture[partsX][partsY];
	}
	
	public TexAtlas(String path, int partsX, int partsY, double offsetX, double offsetY){
		this.file = new TexFile(path);
		
		this.partsX = partsX;
		this.partsY = partsY;
		this.partWidth = 1.0/partsX;
		this.partHeight = 1.0/partsY;

		this.texCoords[0] = 0;
		this.texCoords[1] = 0;
		this.texCoords[2] = partWidth;
		this.texCoords[3] = partHeight;
		
		this.w = file.width/partsX;
		this.h = file.height/partsY;

		this.pixelCoords[0] = (int)(offsetX*w);
		this.pixelCoords[1] = (int)(offsetY*h);
		this.pixelCoords[2] = pixelCoords[0] + w;
		this.pixelCoords[3] = pixelCoords[1] + h;
		
		this.texs = new Texture[partsX][partsY];
	}
	
	public Texture tex(int x, int y){
		if(texs[x][y] != null){
			return texs[x][y];
		} else {
			Texture tex = new Texture();
			
			tex.file = file;
			tex.texCoords[0] = x*partWidth + texCoords[0];
			tex.texCoords[1] = y*partHeight + texCoords[1];
			tex.texCoords[2] = tex.texCoords[0] + partWidth;
			tex.texCoords[3] = tex.texCoords[1] + partHeight;
			
			tex.w = w;
			tex.h = h;

			tex.pixelCoords[0] = pixelCoords[0];
			tex.pixelCoords[1] = pixelCoords[1];
			tex.pixelCoords[2] = pixelCoords[2];
			tex.pixelCoords[3] = pixelCoords[3];
			
			texs[x][y] = tex;
			
			return tex;
		}
	}
	
	/**
	 * Single Frame Animation
	 * @param x
	 * @param y
	 * @return
	 */
	public Animation sfA(String name, int x, int y){
		return new Animation(name, this, -1, y, x);
	}
	
	public Animation sfA(int x, int y){
		return sfA("", x, y);
	}
}
