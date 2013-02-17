/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestate;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import java.util.LinkedList;
import java.util.List;
import variables.P;

/**
 *
 * @author dagen
 */
public class PlatformController {
    private AssetManager assetManager;
    public List<Platform> platforms;
    
    
    public PlatformController(PlatformFactory platformFactory){
        platforms = new LinkedList<Platform>();
        Platform platform;
        
        
        for(int i = 0; i < P.platformsPerLevel; i++){
            platform = platformFactory.createPlatform();
            platform.rigidBodyControl.setPhysicsLocation(new Vector3f((P.platformDistance+2*P.platformLength)*i,0,0));
            this.platforms.add(platform);
        }
    }
    
    public void addPlatform(Platform platform){
        this.platforms.add(platform);
        
    }
}
