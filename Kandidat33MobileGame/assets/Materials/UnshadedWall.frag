#if defined(HAS_GLOWMAP) || defined(HAS_COLORMAP) || (defined(HAS_LIGHTMAP) && !defined(SEPARATE_TEXCOORD))
    #define NEED_TEXCOORD1
#endif

#if defined(DISCARD_ALPHA)
    uniform float m_AlphaDiscardThreshold;
#endif

uniform vec3 m_SunColor;
#if defined(HAS_AFFECTMAP)
    uniform sampler2D m_AffectMap;
#endif

uniform vec4 m_Color;
uniform sampler2D m_ColorMap;
uniform sampler2D m_LightMap;

varying vec2 texCoord1;
varying vec2 texCoord2;

varying vec4 vertColor;

void main(){
    vec4 color = vec4(1.0);

    #ifdef HAS_COLORMAP
        color *= texture2D(m_ColorMap, texCoord1);     
    #endif

    #ifdef HAS_VERTEXCOLOR
        color *= vertColor;
    #endif

    #ifdef HAS_COLOR
        color *= m_Color;
    #endif

    #ifdef HAS_LIGHTMAP
        #ifdef SEPARATE_TEXCOORD
            color.rgb *= texture2D(m_LightMap, texCoord2).rgb;
        #else
            color.rgb *= texture2D(m_LightMap, texCoord1).rgb;
        #endif
    #endif

    #if defined(DISCARD_ALPHA)
        if(color.a < m_AlphaDiscardThreshold){
           discard;
        }
    #endif

    #if defined(HAS_AFFECTMAP)
        float affectValue = texture2D(m_AffectMap, texCoord1).r;
        color.rgb *= vec3(1-affectValue) + (affectValue * m_SunColor);
    #else
        color.rgb *= m_SunColor;
    #endif
    

    gl_FragColor = color;
}