package render;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TexInfo {
	
	public int[][][] info;
	
	/**
	 * You have to do info[y][x] to get to your data!!!! not info[x][y]
	 * @param fileName
	 */
	public TexInfo(String fileName){
		List<int[][]> helper = new ArrayList<>();
		try {
			BufferedReader bf = new BufferedReader(new FileReader(fileName));
			String line = bf.readLine();
			while(line != null){
				String[] texs = line.split("~");
				int[][] row = new int[texs.length][];
				for(int x = 0; x < texs.length; x++){
					String[] data = texs[x].split(",");
					row[x] = new int[data.length];
					for(int i = 0; i < data.length; i++){
						row[x][i] = Integer.parseInt(data[i]);
					}
				}
				helper.add(row);
				line = bf.readLine();
			}
			bf.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		info = helper.toArray(new int[helper.size()][][]);
	}
	
	public int[] getInfo(int x, int y){
		return info[y][x];
	}
	
	public int[] getInfo(Animator ani){
		if(ani.ani != null){
			return info[ani.ani.y][ani.ani.x[ani.pos]];
		} else {
			return info[0][0];
		}
	}
}
