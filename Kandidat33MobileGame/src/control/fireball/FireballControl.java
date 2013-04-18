package control.fireball;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import control.AbstractPlayerInteractorControl;
import control.PlayerControl;
import spatial.Player;
import spatial.hazard.AbstractFireball;

/**
 * A generall fireball, just hanging in the air.
 * @author jonatankilhamn
 */
public class FireballControl extends AbstractPlayerInteractorControl {
    protected boolean hasHit;
    
    public FireballControl() {
        super(new SphereCollisionShape(1f));
    }
    
    public void collideWithPlayer(Player player) {
        if (!hasHit) {
        player.getControl(PlayerControl.class).damage();
        hasHit = false;
        ((AbstractFireball)spatial).destroy();
        }
    }
        
    @Override
    protected void positionUpdate(float tpf) {
        // do not move
    }
    
}
