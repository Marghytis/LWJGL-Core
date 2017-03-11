#version 150 core
//singleQuad

uniform sampler2D texture_diffuse;
uniform bool texture;
uniform vec4 color;

in vec2 pass_texCoords;

out vec4 out_Color;

void main(void){

	if(texture){
		out_Color = texture2D(texture_diffuse, pass_texCoords)*color;
	} else {
		out_Color = color;
	}
}
