package variables;

/**
 * On-off-switches for different graphical effects.
 * @author nina
 */
public class EffectSettings {
    public static final AmbientOcclusion ambientOcclusion = AmbientOcclusion.TEXTURE;
            
    public enum AmbientOcclusion {
        TEXTURE, FULL_POST_PROCESSING, INTERVAL_POST_PROCESSING, NONE; 
    }
}
