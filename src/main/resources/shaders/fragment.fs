#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec4 texture_color;

void main()
{
    fragColor = texture(texture_sampler, outTexCoord)*texture_color;
}