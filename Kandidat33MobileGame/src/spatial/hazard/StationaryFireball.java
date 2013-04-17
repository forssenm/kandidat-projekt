package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.PlayerInteractorControl;
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
    protected PlayerInteractorControl createControl() {
        return new FireballControl();
    }
    
}
