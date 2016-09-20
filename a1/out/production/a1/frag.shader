#version 430
in vec4 varyingColor;
in int colorFlag;
out vec4 color;
void main(void)
{
    color = varyingColor;
}