package control.fireball;

import control.AbstractPlayerInteractorControl;
import com.jme3.math.Vector3f;

/**
 * A fireball moving in a straight line.
 * @author jonatankilhamn
 */
public class LinearFireballControl extends FireballControl {

    private Vector3f velocity;

    /**
     * @param velocity The velocity of the fireball. Never changes once set.
     */
    public LinearFireballControl(Vector3f velocity) {
        this.velocity = velocity;
    }
    
    @Override
    protected void positionUpdate(float tpf) {
        Vector3f oldPos = spatial.getLocalTranslation();
        spatial.setLocalTranslation(oldPos.add(velocity.mult(tpf)));
    }
    
}
