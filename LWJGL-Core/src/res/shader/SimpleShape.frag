#version 150 core
//SimpleShape frag

uniform sampler2D texture_diffuse;
uniform vec4 color;
uniform bool texture;

in vec2 pass_texCoords;

out vec4 out_Color;

void main(void){

	if(texture){
		out_Color = texture(texture_diffuse, pass_texCoords)*color;
	} else {
		out_Color = color;
	}
}
