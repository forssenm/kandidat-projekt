package control;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.math.Vector3f;
import leveldata.LevelContentGenerator;
import spatial.Player;
import spatial.hazard.LinearFireball;

/**
 * A class for a wizard who shoots fireballs when the player is within a certain
 * range.
 *
 * @author jonatankilhamn
 */
public class WizardControl extends AbstractHazardControl implements LevelContentGenerator {

    protected boolean readyToShoot = true;
    protected static final float fireballCoolDown = 6.0f;
    protected static final float fireballSpeed = 15.0f;
    protected LevelControl levelControl;
    protected float time;
    protected AssetManager assetManager;

    /**
     * Creates a new wizard with an aggro radius of 10. The assetManager is
     * needed to create fireballs.
     *
     * @param assetManager
     */
    public WizardControl(AssetManager assetManager) {
        super(new SphereCollisionShape(50f));
        this.assetManager = assetManager;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (!readyToShoot) {
            time += tpf;
            if (time > 0) {
                time -= fireballCoolDown;
                readyToShoot = true;
            }
        }
    }

    @Override
    /**
     * Wizards do not move.
     */
    protected void positionUpdate(float tpf) {
    }

    @Override
    public void collideWithPlayer(Player player) {
        if (readyToShoot) {
            Vector3f direction = player.getLocalTranslation().
                    subtract(this.spatial.getWorldTranslation());
            direction.normalizeLocal();
            LinearFireball fireball = new LinearFireball(assetManager, direction.mult(fireballSpeed));
            this.levelControl.addToLevel(fireball, this.spatial.getWorldTranslation());
            readyToShoot = false;
        }
    }

    public void setLevelControl(LevelControl levelControl) {
        this.levelControl = levelControl;
    }
}