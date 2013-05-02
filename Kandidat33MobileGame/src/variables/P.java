package variables;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * This is where you put variables. This makes for easier finding and changing
 * values for a given variable without checking the entire code.
 *
 * @author Mathias
 */
public class P {

    //level variables
    public static final float shortPlatformLength = 12.0f;
    public static final float mediumPlatformLength = 24.0f;
    public static final float longPlatformLength = 40.0f;
    
    
    public static final float platformWidth = 7f;
    public static final float platformHeight = 1f;
    public static final float playerZOffset =2f;
    public static final float platformDistance = 7.0f;
    public static final float minSpeedFactor = 1.5f;
    public static final float maxSpeedFactor = 3.5f;
    public static float speedFactor = 1.5f; // player speed
        
    public static final float deathTreshold = -20.0f;
    
    public static final float chunkLength = 70.0f;
    public static final int noOfStartingChunks = 3;    


    public static final float minLeftDistance = 130;
    public static final float minRightDistance = 250;
    public static final float minDownDistance = 50;
    
            
    //lighting variables
    public static final Vector3f windowLightDirection = new Vector3f(0f,-20f,0f);
    public static final ColorRGBA sunColor = new ColorRGBA(0.7f,0.7f,0.7f,0f);
    
    public static boolean usePlayerSpot = true;
    public static boolean useWizardLights = true;
    public static boolean useWindowLights = true;
    public static boolean useTorchLights = true;
    
    public static int screenWidth;
    public static int screenHeight;
    
    public static final float windowFreq = 0.3f;
    public static final float torchFreq = 0.3f;
    
    //strings
    
    public static final String playerSpot = "playerspot";
    
}