#version 330
#extension GL_ARB_explicit_attrib_location : enable

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;

uniform vec4 line_color;

layout(location = 0) in vec3 position;

out vec4 color;

void main()
{
    color = line_color;

    vec4 newpos = modelMatrix * vec4(position,1);
    gl_Position = projectionMatrix * newpos;
}
