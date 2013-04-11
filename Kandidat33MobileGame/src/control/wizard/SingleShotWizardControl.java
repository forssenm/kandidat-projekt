package control.wizard;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import spatial.Player;
import spatial.hazard.LinearFireball;

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
