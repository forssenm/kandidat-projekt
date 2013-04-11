package control.wizard;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import spatial.Player;
import spatial.hazard.LinearFireball;

/**
 * A Wizard that shoots three fireball in short succession, directly at the
 * player.
 *
 * @author jonatankilhamn
 */
public class BurstWizardControl extends AbstractWizardControl {

    protected static final float burstCoolDown = 5.0f;
    protected static final float fireballCoolDown = 0.5f;
    protected int shotsFired;

    public BurstWizardControl(AssetManager assetManager) {
        super(assetManager);
    }

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
}
