#import "Common/ShaderLib/MultiSample.glsllib"

uniform COLORTEXTURE m_Texture;
uniform vec4 m_Color;
uniform vec4 g_ViewPort;

in vec2 texCoord;

void main() {
    //gl_FragColor = getColor(m_Texture, vec2((gl_FragCoord.x+20)/g_ViewPort.z, (gl_FragCoord.y+20)/g_ViewPort.w));
    //gl_FragColor = getColor(m_Texture, vec2(texCoord.x, texCoord.y));
    //gl_FragColor = getColor(m_Texture, texCoord);
    //gl_FragColor = getColor(m_Texture,vec2(,0.1f));//(gl_FragCoord*texCoord)/(gl_FragCoord);
    int xDiff = int(gl_FragCoord.x) % 8;
    int yDiff = int(gl_FragCoord.y) % 8;
    gl_FragColor = getColor(m_Texture, vec2((gl_FragCoord.x-xDiff)/g_ViewPort.z, (gl_FragCoord.y-yDiff)/g_ViewPort.w));
    
}