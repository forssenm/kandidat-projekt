/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.GhostControl;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

/**
 *
 * @author dagen
 */
public class WizardControl extends GhostControl implements PhysicsCollisionListener {

    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        WizardControl control = new WizardControl();
        //TODO: copy parameters to new Control
        control.setSpatial(spatial);
        return control;
    }

    public void collision(PhysicsCollisionEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
