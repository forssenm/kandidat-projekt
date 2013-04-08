package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.HazardControl;
import control.fireball.SpinningFireballControl;

/**
 * A fireball using
 * <code>SpinningFireballControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
public class SpinningFireball extends AbstractFireball {

    public SpinningFireball(AssetManager assetManager) {
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }

    @Override
    protected HazardControl createControl() {
        return new SpinningFireballControl();
    }
}
