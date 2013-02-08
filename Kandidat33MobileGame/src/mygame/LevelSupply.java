/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Geometry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jonatankilhamn
 */
public class LevelSupply {
    
    private BulletAppState bulletAppState;
    
    
    ArrayList<PlatformPosition> platforms;
    
    public List<PlatformPosition> getLevel(int level) {
        platforms = new ArrayList<PlatformPosition>();
        switch(level) {
            case(0):
                return getLevelZero();
            
            case (1):
                //return getLevelOne();
            default:
                return platforms;
        }
    }
    
    private List<PlatformPosition> getLevelZero() {
                for (int i = 0; i < 10; i++) {
            platforms.add(new PlatformPosition(0,i*1f,0f));
        }
        for (int i = 0; i < 5; i++) {
            platforms.add(new PlatformPosition(0,3+i*2f,i*0.3f));
        }
        return platforms;
    }
    

}
