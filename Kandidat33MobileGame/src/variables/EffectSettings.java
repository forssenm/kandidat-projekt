package variables;

/**
 * On-off-switches for different graphical effects.
 * @author nina
 */
public class EffectSettings {
    public static final AmbientOcclusion ambientOcclusion = AmbientOcclusion.NONE;
    public static final Light light = Light.STANDARD_LIGHTING;
            
    public enum AmbientOcclusion {
        TEXTURE, FULL_POST_PROCESSING, INTERVAL_POST_PROCESSING, NONE; 
    }
    
    public enum Light {
        STANDARD_LIGHTING, NONE, TEXTURES_AND_WINDOW, TEXTURES, TEXTURES_SMALL_LIGHTS;
    }
}
