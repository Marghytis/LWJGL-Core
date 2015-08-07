package util;

public class Anim {
	
	public double time, duration;
	public AnimPart[] parts;
	public int iA, iM;
	public boolean dir;
	
	/**
	 * parts should be ordered after their startTime
	 * @param parts
	 */
	public Anim(AnimPart... parts){
		this.parts = parts;
		this.iM = parts.length - 1;
		for(int i = 0; i < parts.length; i++){
			duration += parts[i].startIntervall;
		}
		duration += parts[iM].length;
	}
	
	public void update(double delta){
		if(dir){
			//reset everything if time is at left end
			if(time <= 0){
				iA = -1;
				time = 0;
			} else if(iA > iM){
				iA = iM;
			}
			
			//if nothing has started yet check for the first part
			if(iA == -1 && time >= parts[0].startIntervall){
				iA = 0;
				parts[iA].clock = 0;
			}
			
			//check if the parts following parts[iA] should start
			if(iA >= 0)
			for(int i = iA + 1;i < parts.length && parts[iA].clock >= parts[i].startIntervall; i++){
				parts[i].clock = parts[iA].clock - parts[i].startIntervall;
				iA++;
			}
			
			//update the values of all active parts
			for(int i = 0; i <= iA; i++){
				parts[i].value.v = parts[i].func.x(Math.min(1, parts[i].clock/parts[i].length));
			}
			
			//Update the clock of the last active part
			for(int i = 0; i <= iA; i++){
				parts[i].clock += delta;
			}
			time += delta;
		} else {
			
			//reset everything if time at right end
			if(time >= duration){
				iA = iM;
				parts[iM].clock = parts[iM].length;
				time = duration;
			} else if(iA < 0){
				iA = 0;
			}
			
			//check if the parts following parts[iA] should start
			for(int i = iA - 1;i >= 0 && parts[iA].clock <= parts[i].length - parts[iA].startIntervall; i--){
				parts[i].clock = parts[iA].startIntervall + parts[iA].clock;
				iA--;
			}
			
			//update the values of all active parts
			for(int i = iM; i >= iA; i--){
				parts[i].value.v = parts[i].func.x(parts[i].clock/parts[i].length);//is allowed to be negative as an indicator to not use the value
			}
			
			//Update the clock of the last active part
			for(int i = iM; i >= iA; i--){
				parts[i].clock -= delta;
			}
			time -= delta;
		}
	}
	
	public static class AnimPart {
		Func func;
		Value value;
		double startIntervall, length, clock;
		
		public AnimPart(Value value, Func x, double startIntervall, double length){
			this.value = value;
			this.func = x;
			this.startIntervall = startIntervall;
			this.length = length;
		}
	}
	
	public interface Func {
		/**
		 * @param t in range of [0;1]
		 * @return x
		 */
		public double x(double t);
	}
	public static class Value {
		public double v = -1;
	}
}
