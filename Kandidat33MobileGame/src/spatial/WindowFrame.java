package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import variables.P;

/**
 * A class for a non-physical window frame, purely for decoration.
 * @author jonatankilhamn
 */
public class WindowFrame extends Geometry {

    /**
     * This constructor creates a
     * <code>WindowFrame</code> represented by a
     * <code>Geometry</code> loaded internaly.
     *
     * @param assetManager is used to load the geometry and texture of
     * the <code>Wall</code>.
     */
    public WindowFrame(AssetManager assetManager, Vector3f position // in current implementation all windows are the same size
            /*, float length, float height, float width*/) {
        super("WindowFrame");

        /* use a simple box for the model – we probably want to
         * change this to a custom model
         */
        Box model =
                new Box(new Vector3f(1, 2, -P.platformWidth / 2 + 0.1f), 1, 2, 0.1f);
        this.mesh = model;
        this.setLocalTranslation(position.x, position.y, position.z);
        
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        this.setMaterial(material);

        this.setShadowMode(ShadowMode.Off);
    }
}
