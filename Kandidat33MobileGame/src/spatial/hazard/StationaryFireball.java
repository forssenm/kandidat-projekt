package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.HazardControl;
import control.fireball.FireballControl;

/**
 * * A fireball using
 * <code>FireballControl</code> to control its behaviour.
 * 
 * @author jonatankilhamn
 */
public class StationaryFireball extends AbstractFireball {

    public StationaryFireball(AssetManager assetManager) {
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }
    
    @Override
    protected HazardControl createControl() {
        return new FireballControl();
    }
    
}
