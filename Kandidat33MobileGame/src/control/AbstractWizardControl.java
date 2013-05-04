package control;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import control.AbstractPlayerInteractorControl;
import leveldata.LevelContentGenerator;
import spatial.Player;
import spatial.hazard.LinearFireball;
import state.LevelGeneratingState;

/**
 * A class for a wizard who shoots fireballs when the player is within a certain
 * range.
 *
 * @author jonatankilhamn
 */
public abstract class AbstractWizardControl extends AbstractPlayerInteractorControl implements LevelContentGenerator {

    protected boolean readyToShoot = true;
    protected LevelGeneratingState levelControl;
    protected float reloadTimer;
    protected float fireballSpeed = 15f;
    protected AssetManager assetManager;
    protected float speed = 3f;

    /**
     * Creates a new wizard with an aggro radius of 50. The assetManager is
     * needed to create fireballs.
     *
     * @param assetManager
     */
    public AbstractWizardControl(AssetManager assetManager) {
        super(new SphereCollisionShape(50f));
        this.assetManager = assetManager;
    }
    
    public AbstractWizardControl(CollisionShape s, AssetManager assetManager) {
        super(s);
        this.assetManager = assetManager;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (!readyToShoot) {
            reloadTimer += tpf;
            if (reloadTimer > 0) {
                readyToShoot = true;
            }
        }
    }

    @Override
    /**
     * Wizards do not move.
     */
    protected void positionUpdate(float tpf) {
        spatial.setLocalTranslation(spatial.getLocalTranslation().add(tpf*speed,0f,0f));
    }

    @Override
    public void collideWithPlayer(Player player) {
        if (readyToShoot) {
            shootAtPlayerAndReload(player);
        }
        lookAt(player.getLocalTranslation());
    }

    /**
     * Try to shoot at the player. Implementations of this method must take care
     * of setting readyToShoot = false and decreasing reloadTimer by an
     * appropriate amount.
     *
     * @param player Handle to the player, for aiming.
     */
    protected abstract void shootAtPlayerAndReload(Player player);

    protected void shootFireballAt(Vector3f targetPosition) {
        Vector3f direction = targetPosition.
                subtract((this.spatial).getWorldTranslation());
        //direction.setZ(0);
        direction.normalizeLocal();
        LinearFireball fireball = new LinearFireball(assetManager, direction.mult(fireballSpeed));
        this.levelControl.addToLevel(fireball,
                //shoot from the wand:
                this.spatial.getWorldTranslation().add(direction));
    }

    public void lookAt(Vector3f position) {
        Vector3f direction = position.subtract(spatial.getLocalTranslation());
        float theta = FastMath.atan2(direction.z - 5, direction.x) - FastMath.PI / 2;
        float[] angles = {-0.2f, theta, 0.0f};
        Quaternion rotation = new Quaternion(angles);
        System.out.println(((Node)this.spatial).getChildren());
        ((Node)this.spatial).getChild("wizardSpatial").setLocalRotation(rotation);
    }

    public void setLevelControl(LevelGeneratingState levelControl) {
        this.levelControl = levelControl;
    }
    
    public void collideWithStatic() {
    }
    
}