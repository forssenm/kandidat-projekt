package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import variables.P;

/**
 * A class for a non-physical window frame, purely for decoration.
 * @author jonatankilhamn
 */
public class WindowFrame extends Node {
    
    public enum Design {
        FLOWERS ("Models/window/flat-window-2.j3o"),
        BIRD ("Models/window/flat-window-2b.j3o");

        public final String modelSrc;

        Design(String modelSrc) {
            this.modelSrc = modelSrc;
        }
    }

    /**
     * This constructor creates a
     * <code>WindowFrame</code> represented by a
     * <code>Geometry</code> loaded internaly.
     *
     * @param assetManager is used to load the geometry and texture of
     * the <code>Window</code>.
     */
    public WindowFrame(AssetManager assetManager, Vector3f position, Design design ) {
        super("WindowFrame");
        
        Node window = (Node)assetManager.loadModel(design.modelSrc);
        window.scale(4.5f);
        window.rotate(90*FastMath.DEG_TO_RAD, 0f, 0f);
        
        this.attachChild(window);
        
        this.setLocalTranslation(position.x, position.y, -P.platformWidth-P.playerZOffset-3f);
        this.setShadowMode(ShadowMode.Off);   

    }
}
