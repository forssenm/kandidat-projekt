package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.light.SpotLight;
import control.AbstractWizardControl;
import control.PlayerInteractorControl;
import spatial.Player;

/**
 * A class for a Wizard, using a
 * <code>BurstWizardControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
public class BurstWizard extends AbstractWizard {

    private final AssetManager assetManager;

    public BurstWizard(AssetManager assetManager, SpotLight spotlight) {
        super(spotlight);
        this.assetManager = assetManager;
        this.attachChild(createModel(assetManager));
        this.addControl(createControl());
        this.getChild(0).scale(0.8f);
    }

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractWizardControl(assetManager, spotlight) {
            protected static final float burstCoolDown = 2.5f;
            protected static final float fireballCoolDown = 0.5f;
            protected int shotsFired;

            @Override
            protected void shootAtPlayerAndReload(Player player) {
                this.shootFireballAt(player.getLocalTranslation());

                readyToShoot = false;
                shotsFired++;
                if (shotsFired < 3) {
                    reloadTimer -= fireballCoolDown;
                } else {
                    shotsFired = 0;
                    reloadTimer -= burstCoolDown;

                }
            }
        };
    }
}