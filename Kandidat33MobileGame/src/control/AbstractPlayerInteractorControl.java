package control;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import spatial.Player;

/**
 *
 * @author jonatankilhamn
 */
public abstract class AbstractPlayerInteractorControl extends GhostControl implements PlayerInteractorControl {
    
    public AbstractPlayerInteractorControl(CollisionShape s) {
        super(s);
    }
    
    @Override
    public void update(float tpf){
        super.setEnabled(false);
        positionUpdate(tpf);
        super.setEnabled(true);
    }
    
    /**
     * Moves this without disrupting the physics of the GhostControl.
     * @param translation The vector of the movement.
     */
    public void move(Vector3f translation) {
        super.setEnabled(false);
        spatial.setLocalTranslation(spatial.getLocalTranslation().add(translation));
        super.setEnabled(true);
    }

    protected abstract void positionUpdate(float tpf);
    
}
