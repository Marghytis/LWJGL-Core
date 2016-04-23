package util;

import util.math.UsefulF;

public class Matrix2x2 {
	
	static float[] rotation = {1, 0, 1, 0};

	public float[] data = {
			1, 0,
			0, 1
	};
	
	public void scale(float sx, float sy){
		data[0] *= sx;
		data[1] *= sx;
		data[2] *= sy;
		data[3] *= sy;
	}
	
	/**
	 * phi in 100°
	 * @param phi
	 */
	public void rotate(int phi){
		rotation[0] = data[0]*UsefulF.cos100F[phi] - data[2]*UsefulF.sin100F[phi];
		rotation[1] = data[1]*UsefulF.cos100F[phi] - data[3]*UsefulF.sin100F[phi];
		rotation[2] = data[2]*UsefulF.cos100F[phi] + data[0]*UsefulF.sin100F[phi];
		rotation[3] = data[3]*UsefulF.cos100F[phi] + data[1]*UsefulF.sin100F[phi];
		data[0] = rotation[0];
		data[1] = rotation[1];
		data[2] = rotation[2];
		data[3] = rotation[3];
	}
}
