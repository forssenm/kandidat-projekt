package spatial.hazard;

import com.jme3.asset.AssetManager;
import control.AbstractWizardControl;
import control.PlayerInteractorControl;
import spatial.Player;

/**
 * A class for a Wizard, using a
 * <code>SingleShotWizardControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
public class SingleShotWizard extends AbstractWizard {

    private final AssetManager assetManager;

    public SingleShotWizard(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.attachChild(createModel(assetManager));
        this.addControl(createControl());
    }

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractWizardControl(assetManager) {
            protected static final float fireballCoolDown = 5.0f;

            @Override
            protected void shootAtPlayerAndReload(Player player) {
                this.shootFireballAt(player.getLocalTranslation());
                readyToShoot = false;
                reloadTimer -= fireballCoolDown;
            }
        };
    }
}