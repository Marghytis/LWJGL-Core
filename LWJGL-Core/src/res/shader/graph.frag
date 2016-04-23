#version 150 core

uniform vec4 colorHigh;
uniform vec4 colorLow;

in float y;//0 <= y <= 1

out vec4 out_Color;

void main(void){

	out_Color = vec4(0.0, 1.0, 0.0, 1.0);
	//out_Color = colorLow*(1.0-y) + (colorHigh*y);
	//if(y > 1000.0){
	//	out_Color = vec4(0.0, 0.0, 1.0, 1.0);
	//}
}
