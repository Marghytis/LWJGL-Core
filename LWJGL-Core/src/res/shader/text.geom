#version 150

uniform ivec2 offsets[1000];
uniform vec2 scale;
uniform vec2 size;

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;
 
in Vertex
{
	vec2 pass_Coords;
	vec2 pass_texCoords;
} vertex[];
 
out vec2 texCoords;
   
void main (void)
{
	ivec2 offset = offsets[gl_PrimitiveIDIn/2];
  gl_Position = vec4(scale*(vertex[0].pass_Coords*size + offset), 0.0, 1.0);
  texCoords = vertex[0].pass_texCoords;
  EmitVertex();
  
  gl_Position = vec4(scale*(vertex[1].pass_Coords*size + offset), 0.0, 1.0);
  texCoords = vertex[1].pass_texCoords;
  EmitVertex();
  
  gl_Position = vec4(scale*(vertex[2].pass_Coords*size + offset), 0.0, 1.0);
  texCoords = vertex[2].pass_texCoords;
  EmitVertex();
 
  EndPrimitive();  
}   