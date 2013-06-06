#import "Common/ShaderLib/MultiSample.glsllib"

uniform COLORTEXTURE m_Texture;
uniform DEPTHTEXTURE m_DepthTexture;
uniform vec4 g_ViewPort;

uniform int m_numMoving;
uniform vec4 m_intervals[10];

float radius = 2.5f;
float depth = 0.0002f;

in vec2 texCoord;

bool isInInterval() {
    
    for (int i = 0; i < m_numMoving; i++) {
        if (gl_FragCoord.x >= m_intervals[i].x && gl_FragCoord.x <= m_intervals[i].y && gl_FragCoord.y >= m_intervals[i].z && gl_FragCoord.y <= m_intervals[i].w )
            return true;
    }
    return false;
}

vec4 getContribution(float x, float y, float z) {
    return 0.1 * getColor(m_Texture, vec2(x, y));
}

int getContribution(vec3 sample) {
    return (sample.z < getColor(m_DepthTexture, vec2(sample.x, sample.y)).z) ? 1 : 0;
}

float getAccessibility(vec3 s1, vec3 s2, vec3 s3, vec3 s4, vec3 s5, vec3 s6, vec3 s7, vec3 s8) {
    int numSamples = 8;
    float ratio = float(getContribution(s1) + getContribution(s2) + getContribution(s3) + getContribution(s4) + getContribution(s5)  + getContribution(s6) + getContribution(s7) + getContribution(s8)) / float(numSamples);
    return ratio >= 0.5f ? 1 : ratio;
}

vec3 toTexCoords(float x, float y, float z) {
    return vec3((gl_FragCoord.x+x)/g_ViewPort.z, (gl_FragCoord.y+y)/g_ViewPort.w, getColor(m_DepthTexture, texCoord).z+z);
}

void main() {
    vec4 texVal = getColor(m_Texture, texCoord);
    
    if (isInInterval()) {
        /*gl_FragColor = texVal * 0.6 + 
            getContribution((gl_FragCoord.x-radius)/g_ViewPort.z, (gl_FragCoord.y-radius)/g_ViewPort.w, getColor(m_DepthTexture, texCoord).z-depth) +
            getContribution((gl_FragCoord.x-radius)/g_ViewPort.z, (gl_FragCoord.y-radius)/g_ViewPort.w, getColor(m_DepthTexture, texCoord).z+depth) +
            getContribution((gl_FragCoord.x-radius)/g_ViewPort.z, (gl_FragCoord.y-radius)/g_ViewPort.w, getColor(m_DepthTexture, texCoord).z+depth) +
            getContribution((gl_FragCoord.x-radius)/g_ViewPort.z, (gl_FragCoord.y-radius)/g_ViewPort.w, getColor(m_DepthTexture, texCoord).z-depth);
        */
        //gl_FragColor = texVal * 0.8;
        gl_FragColor = texVal * getAccessibility(
            toTexCoords(-radius, -radius, -depth),
            toTexCoords(-radius, radius, depth),
            toTexCoords(radius, -radius, depth),
            toTexCoords(radius, radius, -depth),
            toTexCoords(-radius/2, -radius/2, depth/10),
            toTexCoords(-radius/2, radius/2, -depth/10),
            toTexCoords(radius/2, -radius/2, -depth/10),
            toTexCoords(radius/2, radius/2, depth/10)
            );
    } else {
        gl_FragColor = texVal;
    }
 
    //gl_FragColor = getColor(m_DepthTexture, texCoord);
     
}