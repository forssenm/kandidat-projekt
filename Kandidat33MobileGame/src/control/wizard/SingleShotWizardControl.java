package control.wizard;

import com.jme3.asset.AssetManager;
import spatial.Player;

/**
 * A Wizard that shoots a single fireball at a time, directly at the player.
 *
 * @author jonatankilhamn
 */
public class SingleShotWizardControl extends AbstractWizardControl {

    protected static final float fireballCoolDown = 5.0f;

    public SingleShotWizardControl(AssetManager assetManager) {
        super(assetManager);
    }

    @Override
    protected void shootAtPlayerAndReload(Player player) {
        this.shootFireballAt(player.getLocalTranslation());
        readyToShoot = false;
        reloadTimer -= fireballCoolDown;
    }
}
