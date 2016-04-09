package render;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TexInfo {
	
	public int[][] info;
	
	/**
	 * @param fileName
	 */
	public TexInfo(String fileName){
		List<int[]> helper = new ArrayList<>();
		try {
			BufferedReader bf = new BufferedReader(new FileReader(fileName));
			String line = bf.readLine();
			while(line != null){
				String[] texs = line.split("~");
				for(int x = 0; x < texs.length; x++){
					String[] data = texs[x].split(",");
					int[] sprite = new int[data.length];
					for(int i = 0; i < data.length; i++){
						sprite[i] = Integer.parseInt(data[i]);
					}
					helper.add(sprite);
				}
				line = bf.readLine();
			}
			bf.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		info = helper.toArray(new int[helper.size()][]);
	}
	
	public int[] getInfo(int i){
		return info[i];
	}
}
