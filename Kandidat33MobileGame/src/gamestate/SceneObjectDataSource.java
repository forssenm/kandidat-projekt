/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestate;

import com.jme3.scene.Spatial;

/**
 * An interface for classes which generate spatials.
 * A SceneObjectDataSource can generate a spatial to use for a certain purpose,
 * such as to represent the player or a platform. Each such object has other
 * properties as well, not contained in the spatial, and DataSources make sure
 * whatever needed the spatial need not concern itself with its details.
 * @author dagen
 */
public interface SceneObjectDataSource {
    public Spatial getSceneObject();
}
