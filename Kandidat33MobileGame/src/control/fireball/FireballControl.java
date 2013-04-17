package control.fireball;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import control.AbstractPlayerInteractorControl;
import control.PlayerControl;
import spatial.Player;

/**
 * A generall fireball, just hanging in the air.
 * @author jonatankilhamn
 */
public class FireballControl extends AbstractPlayerInteractorControl {
    
    public FireballControl() {
        super(new SphereCollisionShape(1f));
    }
    
    public void collideWithPlayer(Player player) {
            player.getControl(PlayerControl.class).pushBack();
    }
        
    @Override
    protected void positionUpdate(float tpf) {
        // do not move
    }
    
}
