package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import control.WizardControl;

/**
 * A class for a Wizard, using a
 * <code>WizardControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
@Deprecated
public class Wizard extends Node {

    public Wizard(AssetManager assetManager) {
        super("Wizard");
        Box model =
                new Box(Vector3f.ZERO, 0.2f, 2f, 0.2f);
        Geometry geometry = new Geometry("", model);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Black);
        geometry.setMaterial(material);

        this.attachChild(geometry);

        WizardControl wizardControl = new WizardControl(assetManager);
        this.addControl(wizardControl);

        this.setShadowMode(RenderQueue.ShadowMode.Cast);
    }
}