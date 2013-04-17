package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import control.PlayerInteractorControl;

/**
 * A class for a general player interactor.
 *
 * @author dagen
 */
public abstract class PlayerInteractor extends Node {

    @Override
    public String getName() {
        if (super.getName() == null) {
            super.setName("playerinteractor");
        }
        return super.getName();
    }

    /**
     * Creates the model for this playerinteractor.
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
     * Creates the PlayerInteractorControl for this playerinteractor. A PlayerInteractorControl makes sure
     * something happens when this hazard collides with the player.
     */
    protected abstract PlayerInteractorControl createControl();
}
