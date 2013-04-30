package variables;

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
    
    
    public static final float platformWidth = 8f;
    public static final float platformHeight = 1f;
    public static final float playerZOffset =2f;
    public static final float platformDistance = 7.0f;
    public static final float minSpeedFactor = 1.5f;
    public static final float maxSpeedFactor = 3.5f;
    public static float speedFactor = 1.5f; // player speed
        
    public static final float deathTreshold = -20.0f;
    
    public static final float chunkLength = 70.0f;
    public static final int noOfStartingChunks = 3;    


    public static final float minLeftDistance = 100;
    public static final float minRightDistance = 250;
    public static final float minDownDistance = 50;
    
    
    public static int screenWidth;
    public static int screenHeight;

    
}
