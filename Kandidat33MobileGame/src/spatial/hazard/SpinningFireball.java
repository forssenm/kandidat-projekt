package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.PlayerInteractorControl;
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
    protected PlayerInteractorControl createControl() {
        return new SpinningFireballControl();
    }
}
