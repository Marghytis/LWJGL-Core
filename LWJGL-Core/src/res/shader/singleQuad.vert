#version 150 core

uniform vec2 scale;
uniform vec4 texCoords;

in vec2 in_Position;
in vec2 in_TexCoords;

out vec2 pass_texCoords;

void main(void){
	gl_Position = vec4(in_Position*scale, 0, 1);
	pass_texCoords = in_TexCoords*texCoords.zw + texCoords.xy;
}