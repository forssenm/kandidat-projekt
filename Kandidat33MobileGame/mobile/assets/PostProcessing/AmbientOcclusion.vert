uniform mat4 g_WorldViewProjectionMatrix;

in vec4 inPosition;
in vec2 inTexCoord;

out vec2 texCoord;

void main() {
    vec4 pos = g_WorldViewProjectionMatrix * inPosition;
    gl_Position = vec4(pos.x, pos.y, 0.0, 1.0);
    texCoord = inTexCoord;
}