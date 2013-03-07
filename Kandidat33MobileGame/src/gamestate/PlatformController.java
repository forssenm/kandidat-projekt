/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestate;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import java.util.LinkedList;
import java.util.Random;
import variables.P;

/**
 * A class that holds and handles all platforms.
 * A starting set of platforms are generated in the constructor, and an
 * ordered list of platforms are always accessible.
 * @author dagen
 */
public class PlatformController {
    private AssetManager assetManager;
    /**
     * A list containing all platforms in the game.
     * The initial set of platforms are ordered from left to right in space.
     * Usage: take the first platform, remake it into the next platform, and
     * move it to the end of the list.
     */
    public LinkedList<Platform> platforms;
    
    /**
     * Creates a PlatformController.
     * Creates a starting set of platforms at increasing x-distance, each
     * varying in height by a random amount.
     * @param platformFactory A PlatformFactory that generates Platforms
     * that are visible and physical.
     */
    public PlatformController(PlatformFactory platformFactory){
        platforms = new LinkedList<Platform>();
        Platform platform;
        Random random;
        int randomNumber;
        
        //Adding the first platform
        platform = platformFactory.createPlatform();
        platform.setPlatformPosition(new Vector3f(3f,10f,0));
        platforms.add(platform);
        
        //Adding all the following platforms
        for(int i = 1; i < P.platformsPerLevel; i++){
            random=new Random();
            randomNumber=(random.nextInt(9)-4);
            platform = platformFactory.createPlatform();
            platform.setPlatformPosition(new Vector3f(
                    (P.platformDistance + 2 * P.platformLength) * i,
                    (float)(platforms.getLast().getPlatformPosition().y +
                    randomNumber), 0));
            this.platforms.addLast(platform);
        }
    }
    
    /**
     * Adds a platform to the end of the list.
     * The normal usage is to always have the rightmost platform at the
     * end of the list.
     * @param platform The platform to add.
     */
    public void addPlatform(Platform platform){
        this.platforms.add(platform);
        
    }
    
    /**
     * Deletes the first (usually leftmost) platform.
     */
    public void deletePlatform() {
        platforms.remove(0);
    }
}
