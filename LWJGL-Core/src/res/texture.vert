#version 150 core

uniform mat2 rotation;
uniform vec2 translate;
uniform vec4 color;
uniform boolean mirror;
uniform vec4 texCoords;

in vec2 in_Position;
in vec2 in_TextureCoords;

out vec2 pass_TexCoords;
out vec4 pass_Color;

void main(void){
	gl_Position = vec4(rotation*in_Position + translate, 0, 1);
	
	pass_TexCoords = texCoords.xy + vec2(in_TextureCoords.x*texCoords.z, in_TextureCoords.y*texCoords.w);
	pass_Color = color;
}