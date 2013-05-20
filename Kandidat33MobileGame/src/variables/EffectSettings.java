package variables;

/**
 * On-off-switches for different graphical effects.
 * @author nina
 */
public class EffectSettings {
    public static final AmbientOcclusion ambientOcclusion = AmbientOcclusion.TEXTURE;
    public static final Score score = Score.OFF;
    public static final Light light = Light.TEXTURES_AND_WINDOW;
            
    public enum AmbientOcclusion {
        TEXTURE, FULL_POST_PROCESSING, INTERVAL_POST_PROCESSING, NONE; 
    }
    
    public enum Light {
        STANDARD_LIGHTING, NONE, TEXTURES_AND_WINDOW, TEXTURES, TEXTURES_SMALL_LIGHTS;
    }
    
    public enum Score {
        ON, OFF;
    }
}
