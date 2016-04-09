#version 150 core

in vec2 in_Position;
in vec2 in_TexCoords;

out Vertex {
	vec2 pass_Coords;
	vec2 pass_texCoords;
	} vertex;

void main(void){
	vertex.pass_Coords = in_Position;
	vertex.pass_texCoords = in_TexCoords;
}