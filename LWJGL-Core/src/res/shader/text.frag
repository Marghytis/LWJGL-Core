#version 150 core

uniform sampler2D texture_diffuse;
uniform vec4 color;

in vec2 texCoords;

out vec4 out_Color;

void main(void){

	//out_Color = vec4(1, 0, 0, 1);
	out_Color = texture(texture_diffuse, texCoords)*color;
}
