/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

/**
 *
 * @author dagen
 */
public class HazardControl extends GhostControl implements PhysicsCollisionListener {
    
    public HazardControl() {
        super();
    }
    
    public HazardControl(CollisionShape c) {
        super(c);
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        HazardControl control = new HazardControl();
        //TODO: copy parameters to new Control
        control.setSpatial(spatial);
        return control;
    }

    public void collision(PhysicsCollisionEvent event) {
        if (event.getNodeA().getName().equals("player") &&
                event.getNodeB().getName().equals("hazard")) {
            final Spatial spatial = event.getNodeA();
            System.out.println("Hit player");
        } else if (event.getNodeB().getName().equals("player") &&
                event.getNodeA().getName().equals("hazard")) {
            final Spatial spatial = event.getNodeB();
            System.out.println("Hit player");
        }
        
        if (event.getNodeA().getName().equals("player")
                && event.getNodeB().getName().equals("nothazard")) {
            final Spatial spatial = event.getNodeA();
            System.out.println("foo");
        } else if (event.getNodeB().getName().equals("player") &&
                event.getNodeA().getName().equals("nothazard")) {
            final Spatial spatial = event.getNodeB();
            System.out.println("bar");
        }
    }
}
