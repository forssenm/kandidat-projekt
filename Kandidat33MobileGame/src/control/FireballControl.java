/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import spatial.Player;

/**
 *
 * @author jonatankilhamn
 */
public class FireballControl extends GhostControl implements HazardControl {
    boolean hasHit = false;
    
    Vector3f velocity = new Vector3f( 10.0f, -1.0f, 0.0f );
    
    public FireballControl(){
        super(new SphereCollisionShape(1.0f));
    }
    
    public void collideWithPlayer(Player player) {
        if (!hasHit) {
            // code to damage player or something
            hasHit = true;
        }
    }
    @Override
    public void update(float tpf){
        super.setEnabled(false);
        Vector3f oldTranslation = this.spatial.getLocalTranslation();
        this.spatial.setLocalTranslation( oldTranslation.add( velocity.mult(tpf) ) );
        super.setEnabled(true);
    }
}
