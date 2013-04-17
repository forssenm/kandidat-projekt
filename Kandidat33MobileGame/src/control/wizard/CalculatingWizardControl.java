package control.wizard;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import control.PlayerControl;
import spatial.Player;
import spatial.hazard.LinearFireball;

/**
 * A Wizard that shoots a single fireball at a point just ahead of the player.
 *
 * @author jonatankilhamn
 */
public class CalculatingWizardControl extends AbstractWizardControl {

    protected static final float fireballCoolDown = 5.0f;
    protected static final float aggroBoxSide = 12f;
    protected static final float aggroBoxDepth = 30;
    private float time = 0;
    private final float radius = 5.0f;

    public CalculatingWizardControl(AssetManager assetManager) {
        super(new BoxCollisionShape(
                new Vector3f(aggroBoxSide,aggroBoxSide,aggroBoxDepth)),
                assetManager);
    }
    

    
    @Override
    protected void positionUpdate(float tpf) {
        super.positionUpdate(tpf);
        time += tpf;
                
        float y = tpf*radius*FastMath.sin(time);
        this.spatial.setLocalTranslation(this.spatial.getLocalTranslation().add(Vector3f.UNIT_Y.mult(y)));
    }

    @Override
    protected void shootAtPlayerAndReload(Player player) {
        Vector3f playerPos = player.getLocalTranslation();
        Vector3f offset = player.getControl(PlayerControl.class).getVelocity().mult(1f);
        offset.setY(offset.getY()*0.3f);
        Vector3f targetPos = playerPos.add(offset);
        float dist = (targetPos.subtract(this.spatial.getWorldTranslation())).length();
        this.fireballSpeed = dist / 1f;
        this.shootFireballAt(targetPos);
        readyToShoot = false;
        reloadTimer -= fireballCoolDown;
    }
}
