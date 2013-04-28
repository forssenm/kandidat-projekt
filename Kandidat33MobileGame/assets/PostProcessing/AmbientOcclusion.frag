#import "Common/ShaderLib/MultiSample.glsllib"

uniform COLORTEXTURE m_Texture;
uniform DEPTHTEXTURE m_DepthTexture;
uniform vec4 g_ViewPort;

uniform int m_numMoving;
uniform vec4 m_intervals[10];

float radius = 5;

in vec2 texCoord;

bool isInInterval() {
    
    for (int i = 0; i < m_numMoving; i++) {
        if (gl_FragCoord.x >= m_intervals[i].x && gl_FragCoord.x <= m_intervals[i].y && gl_FragCoord.y >= m_intervals[i].z && gl_FragCoord.y <= m_intervals[i].w )
            return true;
    }
    return false;
}

void main() {
    vec4 texVal = getColor(m_Texture, texCoord);
    
    if (isInInterval()) {
        gl_FragColor = texVal * 0.6 + 
            0.1 * getColor(m_Texture, vec2((gl_FragCoord.x-radius)/g_ViewPort.z, (gl_FragCoord.y-radius)/g_ViewPort.w)) +
            0.1 * getColor(m_Texture, vec2((gl_FragCoord.x-radius)/g_ViewPort.z, (gl_FragCoord.y+radius)/g_ViewPort.w)) +
            0.1 * getColor(m_Texture, vec2((gl_FragCoord.x+radius)/g_ViewPort.z, (gl_FragCoord.y-radius)/g_ViewPort.w)) +
            0.1 * getColor(m_Texture, vec2((gl_FragCoord.x+radius)/g_ViewPort.z, (gl_FragCoord.y+radius)/g_ViewPort.w));
        //gl_FragColor = texVal * 0.8;
    } else {
        gl_FragColor = texVal;
    }
 
    //gl_FragColor = getColor(m_DepthTexture, texCoord);
     
}