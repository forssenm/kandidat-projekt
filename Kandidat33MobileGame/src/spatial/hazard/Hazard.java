/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import control.HazardControl;

/**
 * A class for a general hazard.
 *
 * @author dagen
 */
public abstract class Hazard extends Node {

    @Override
    public String getName() {
        if (super.getName() == null) {
            super.setName("hazard");
        }
        return super.getName();
    }

    /**
     * Creates the model for this hazard.
     *
     * @param assetManager The AssetManager used to load models and materials
     * @return A Geometry or Node that will give this hazard its look
     */
    protected abstract Spatial createModel(AssetManager assetManager);
    // a custom model would be loaded like this:
    /*
     * Node model = (Node)assetManager.loadModel("Models/wizardStuff/fireball.j3o");
     * this.attachChild(model);
     */

    /**
     * Creates the HazardControl for this hazard. A HazardControl makes sure
     * something happens when this hazard collides with the player.
     *
     * @param assetManager The AssetManager used to load models and materials
     */
    protected abstract HazardControl createControl();
}
