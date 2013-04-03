package control;

import com.jme3.scene.control.Control;
import spatial.Player;

/**
 * An interface for hazards that do something when they touch the player. An
 * object that implements this is a Control and thus attached to a
 * <code>Spatial</code>.
 *
 * @author dagen
 */
public interface HazardControl extends Control {

    /**
     * Called when this hazard collides with the player.
     * When e.g. a
     * <code>PhysicsCollisionListener</code> finds that a hazard-player-collision
     * has happened, this method is called and containts the right reaction –
     * deal damage to or move the player etc.
     *
     * @param player
     */
    public void collideWithPlayer(Player player);
}
