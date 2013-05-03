package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.light.SpotLight;
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

    public SingleShotWizard(AssetManager assetManager, SpotLight spotlight) {
        super(spotlight);
        this.assetManager = assetManager;
        this.attachChild(createModel(assetManager));
        this.addControl(createControl());
        this.getChild(0).scale(1.2f);
    }

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractWizardControl(assetManager, spotlight) {
            protected static final float fireballCoolDown = 2.5f;

            @Override
            protected void shootAtPlayerAndReload(Player player) {
                this.shootFireballAt(player.getLocalTranslation());
                readyToShoot = false;
                reloadTimer -= fireballCoolDown;
            }
        };
    }
}