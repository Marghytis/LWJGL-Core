#version 150 core

uniform sampler2D texture_diffuse;
uniform vec4 color;
uniform bool texture;

in vec2 pass_texCoords;

out vec4 out_Color;

void main(void){

	//out_Color = vec4(1, 0, 0, 1);
	if(texture){
		out_Color = texture(texture_diffuse, pass_texCoords)*color;
	} else {
		out_Color = color;
	}
}
