#version 150 core
//SimpleShape vert

uniform vec2 scale;
uniform vec3 offset;

in vec2 in_Position;

void main(void){
	gl_Position = vec4((in_Position+offset.xy)*scale, offset.z, 1);
}