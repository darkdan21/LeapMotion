#version 130

uniform sampler2D texture_diffuse;
uniform mat3 uvMatrix;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
	vec2 texcoord = vec3( uvMatrix*vec3( pass_TextureCoord, 1.0 )).xy;
	vec4 base = texture(texture_diffuse, texcoord);
	out_Color = pass_Color*base;
}