#version 150 core

uniform vec2 scale;
uniform vec3 stuff;//(height, zero, xOffset)

in float in_X;
in float in_Value;

out float y;

void main(void){
	gl_Position = vec4((in_X + stuff.z)*scale.x, (in_Value + stuff.y)*scale.y, 0, 1);
	y = in_Value/stuff.x;
}