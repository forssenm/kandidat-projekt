package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.light.SpotLight;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import control.AbstractWizardControl;
import control.PlayerControl;
import control.PlayerInteractorControl;
import spatial.Player;

/**
 * A class for a Wizard, using a
 * <code>CalculatingWizardControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
public class CalculatingWizard extends AbstractWizard {

    private final AssetManager assetManager;

    public CalculatingWizard(AssetManager assetManager, SpotLight spotlight) {
        super(spotlight);
        this.assetManager = assetManager;
        this.attachChild(createModel(assetManager));
        this.addControl(createControl());
    }
    protected static final float aggroBoxSide = 12f;
    protected static final float aggroBoxDepth = 30;

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractWizardControl(new BoxCollisionShape(
                new Vector3f(aggroBoxSide, aggroBoxSide, aggroBoxDepth)),
                assetManager, spotlight) {
            protected static final float fireballCoolDown = 2.5f;
            private float time = 0;
            private final float radius = 5.0f;

            @Override
            protected void positionUpdate(float tpf) {
                super.positionUpdate(tpf);
                time += tpf;

                float y = tpf * radius * FastMath.sin(time);
                this.spatial.setLocalTranslation(this.spatial.getLocalTranslation().add(0f, y, 0f));
            }

            @Override
            protected void shootAtPlayerAndReload(Player player) {
                Vector3f playerPos = player.getLocalTranslation();
                Vector3f offset = player.getControl(PlayerControl.class).getVelocity().mult(1f);
                offset.setY(offset.getY() * 0.3f);
                Vector3f targetPos = playerPos.add(offset);
                float dist = (targetPos.subtract(this.spatial.getWorldTranslation())).length();
                this.fireballSpeed = dist / 1f;
                this.shootFireballAt(targetPos);
                readyToShoot = false;
                reloadTimer -= fireballCoolDown;
            }
        };
    }
    
}