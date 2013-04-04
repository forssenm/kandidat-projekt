/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import spatial.Player;

/**
 *
 * @author jonatankilhamn
 */
public abstract class FireballControl extends GhostControl implements HazardControl {
    protected boolean hasHit = false;
        
    public FireballControl(){
        super(new SphereCollisionShape(1f));
    }
    
    public void collideWithPlayer(Player player) {
        if (!hasHit) {
            // code to damage player or something
            System.out.println("You got hit!");
            hasHit = true;
        }
    }
    
    @Override
    public void update(float tpf){
        super.setEnabled(false);
        positionUpdate(tpf);
        super.setEnabled(true);
    }

    protected abstract void positionUpdate(float tpf);
    
}
