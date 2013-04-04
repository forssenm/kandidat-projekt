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
public abstract class FireballControl extends GhostControl implements HazardControl {
    protected boolean hasHit = false;
        
    public FireballControl(){
        super(new SphereCollisionShape(1f));
    }
    
    public void collideWithPlayer(Player player) {
        //if (!hasHit) { //if we only want each fireball to hit once
            // code to damage player or something
            player.getControl(PlayerControl.class).pushBack();
            hasHit = true;
            //this.space.removeAll(spatial);
        //}
    }
    
    @Override
    public void update(float tpf){
        super.setEnabled(false);
        positionUpdate(tpf);
        super.setEnabled(true);
    }
    
    public void move(Vector3f translation) {
        super.setEnabled(false);
        spatial.setLocalTranslation(spatial.getLocalTranslation().add(translation));
        super.setEnabled(true);
    }

    protected abstract void positionUpdate(float tpf);
    
}
