package control;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import control.AbstractPlayerInteractorControl;
import control.PlayerControl;
import spatial.Player;
import spatial.hazard.AbstractFireball;

/**
 * A generall fireball, just hanging in the air.
 * @author jonatankilhamn
 */
public abstract class AbstractHazardControl extends AbstractPlayerInteractorControl {
    protected boolean hasHit;
    
    public AbstractHazardControl(float size) {
        super(new SphereCollisionShape(size));
    }
    
    public void collideWithPlayer(Player player) {
        if (!hasHit) {
        player.getControl(PlayerControl.class).damage();
        hasHit = true;
        afterPlayerCollision();
        }
    }
    
    @Override
    protected void positionUpdate(float tpf) {
        // do not move
    }

    public abstract void collideWithStatic();
    
    public abstract void afterPlayerCollision();
}
