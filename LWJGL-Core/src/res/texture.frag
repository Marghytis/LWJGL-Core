#version 150 core

uniform sampler2D texture_diffuse;

in vec2 pass_TexCoords;
in vec4 pass_Color;

out vec4 out_Color;

void main(void){

	//out_Color = vec4(1, 0, 0, 1);
	out_Color = texture(texture_diffuse, pass_TexCoords)*pass_Color;
}
