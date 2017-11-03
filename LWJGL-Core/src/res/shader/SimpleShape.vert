#version 150 core
//SimpleShape vert

uniform float size;
uniform vec2 scale;
uniform vec3 offset;

in vec2 in_Position;

out vec2 pass_texCoords;

void main(void){
	gl_Position = vec4((in_Position*size+offset.xy)*scale, offset.z, 1);
	pass_texCoords = vec2(0,0);
}