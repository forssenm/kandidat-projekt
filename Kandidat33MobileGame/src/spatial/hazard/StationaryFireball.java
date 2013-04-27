package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.PlayerInteractorControl;
import control.AbstractHazardControl;

/**
 * * A fireball using
 * <code>AbstractHazardControl</code> to control its behaviour.
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
        return new AbstractHazardControl(1f) {

            @Override
            public void collideWithStatic() {
                StationaryFireball.this.destroy();
            }

            @Override
            public void afterPlayerCollision() {
                StationaryFireball.this.destroy();
            }
        };
    }
    
}
