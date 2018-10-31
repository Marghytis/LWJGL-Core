#version 150 core
//singleQuad

uniform sampler2D texture_diffuse;
uniform bool textured;
uniform vec4 color;

in vec2 pass_texCoords;

out vec4 out_Color;

void main(void){

	if(textured){
		out_Color = texture(texture_diffuse, pass_texCoords)*color;
	} else {
		out_Color = color;
	}
}
