package control;

import com.jme3.scene.control.Control;
import spatial.Player;

/**
 * An interface for objects that do something when they touch the player. An
 * object that implements this is a Control and thus attached to a
 * <code>Spatial</code>.
 *
 * @author dagen
 */
public interface PlayerInteractorControl extends Control {

    /**
     * Called when this object collides with the player.
     * When e.g. a
     * <code>PhysicsCollisionListener</code> finds that a playerinteractor-player-collision
     * has happened, this method is called and decides on the right reaction –
     * deal damage to or move the player etc.
     *
     * @param player
     */
    public void collideWithPlayer(Player player);

    public void collideWithStatic();
}
