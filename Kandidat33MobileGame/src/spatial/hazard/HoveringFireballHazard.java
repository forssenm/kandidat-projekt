package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.HazardControl;
import control.fireball.HoveringFireballControl;

/**
 * * A fireball using
 * <code>HoveringFireballControl</code> to control its behaviour.
 * 
 * @author jonatankilhamn
 */
public class HoveringFireballHazard extends FireballHazard {

    public HoveringFireballHazard(AssetManager assetManager) {
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }
    
    @Override
    protected HazardControl createControl() {
        return new HoveringFireballControl();
    }
    
}
