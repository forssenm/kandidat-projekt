package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import control.AbstractWizardControl;
import control.PlayerInteractorControl;
import spatial.Player;
import variables.EffectSettings;
import variables.EffectSettings.AmbientOcclusion;

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
        if(EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE) {
            this.attachChild(this.addWallOcclusion(assetManager, new Vector3f(0f, -1f, -4f)));
        }
        this.scale(1.2f);
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