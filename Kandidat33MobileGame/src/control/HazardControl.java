package control;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import spatial.Player;

/**
 *
 * @author dagen
 */
public interface HazardControl extends Control {

    public void collideWithPlayer(Spatial player);
}
