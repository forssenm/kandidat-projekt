package control.fireball;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import control.AbstractHazardControl;

/**
 * A generall fireball, just hanging in the air.
 * @author jonatankilhamn
 */
public class FireballControl extends AbstractHazardControl {
    
    public FireballControl() {
        super(new SphereCollisionShape(1f));
    }
        
    @Override
    protected void positionUpdate(float tpf) {
        // do not move
    }
    
}
