package variables;

/**
 * On-off-switches for different graphical effects.
 * @author nina
 */
public class EffectSettings {
    public static final AmbientOcclusion ambientOcclusion = AmbientOcclusion.NONE;

    public enum Particles {
            ON, OFF;
    }
    
    public static final Particles particles = Particles.ON;
            
    public enum AmbientOcclusion {
        TEXTURE, FULL_POST_PROCESSING, INTERVAL_POST_PROCESSING, NONE, PLATFORMS; 
    }
    
    public static final Shadows shadows = Shadows.NONE;
    /*
     * If directional shadows are on, use buttons:
     * E – toggle on/off
     * Q – decrease number of splits
     * W – increase -||-
     * A – decrease resolution
     * S – increase -||-
     */
            
    public enum Shadows {
        DIRECTIONAL, NONE; 
    }
}
