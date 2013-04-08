package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import control.HazardControl;
import control.WizardControl;
import control.WizardHazardControl;
import spatial.hazard.Hazard;

/**
 * A class for a WizardHazard, using a
 * <code>WizardControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
public class WizardHazard extends Hazard {
    private final AssetManager assetManager;

    public WizardHazard(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.attachChild(createModel(assetManager));
        this.addControl(createControl());
    }

    @Override
    protected Spatial createModel(AssetManager assetManager) {
        Box model =
                new Box(Vector3f.ZERO, 0.2f, 2f, 0.2f);
        Geometry geometry = new Geometry("", model);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Black);
        geometry.setMaterial(material);
        geometry.setShadowMode(RenderQueue.ShadowMode.Cast);
        return geometry;
    }

    @Override
    protected HazardControl createControl() {
        WizardHazardControl wizardControl = new WizardHazardControl(assetManager);
        return wizardControl;
    }
    
}