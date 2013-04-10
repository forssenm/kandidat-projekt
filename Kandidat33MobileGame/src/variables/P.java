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
    public static final float platformLength = 12.0f;
    public static final float platformWidth = 4f;
    public static final float platformHeight = 1f;
    public static final float platformDistance = 5.0f;
    public static final float runSpeed = 14f;
    public static final float pushbackSpeed = -10f;
    public static final float jumpSpeed = 25f;
    
    public static final float chunkLength = 50.0f;
    
    public static final float deathTreshold = -20.0f;  
        
    //lighting variables
    public static final Vector3f windowLightDirection = new Vector3f(0f,-10f,20f);
    
    public static final float minLeftDistance = 150;
    public static final float minRightDistance = 200;
    public static final float minDownDistance = 50;
    
    
    public static int screenWidth;
    public static int screenHeight;
    
}
