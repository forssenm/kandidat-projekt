package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import material.LightTextureMaterial;
import variables.EffectSettings;
import variables.P;

/**
 * A class for a non-physical window frame, purely for decoration.
 * @author jonatankilhamn
 */
public class WindowFrame extends Node {
    
    public enum Design {
        FLOWERS ("Models/window/flat-window-2.j3o",
                "Models/window/Light/flat-window-2.j3o",
                "Models/window/Light/texture1_light.png",
                //"Models/window/Light/test1.png",
                "Models/window/Light/texture1_light_colors.png",
                new Vector2f(4,6)),
        BIRD ("Models/window/flat-window-2b.j3o",
                "Models/window/Light/flat-window-2b.j3o",
                "Models/window/Light/texture2_light.png",
                //"Models/window/Light/test2.png",
                "Models/window/Light/texture2_light_colors.png",
                new Vector2f(4,6));

        public final String modelSrc;
        public final String lightModelSrc;
        public final String wallLightSrc;
        public final String lightColorsSrc;
        public final Vector2f lightColorsDimensions;

        Design(String modelSrc, String lightModelSrc, String wallLightSrc, String lightColorsSrc, Vector2f lightColorsDimensions) {
            this.modelSrc = modelSrc;
            this.lightModelSrc = lightModelSrc;
            this.wallLightSrc = wallLightSrc;
            this.lightColorsSrc = lightColorsSrc;
            this.lightColorsDimensions = lightColorsDimensions;
        }
    }
    
    private Design design;

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
        this.design = design;
        
        Node window = null;
        if (EffectSettings.light == EffectSettings.Light.TEXTURES) {
            window = (Node)assetManager.loadModel(design.lightModelSrc);
            
        } else {
            window = (Node)assetManager.loadModel(design.modelSrc);
        }
        window.scale(4.5f);
        window.rotate(90*FastMath.DEG_TO_RAD, 0f, 0f);
        
        this.attachChild(window);
        
        this.setLocalTranslation(position.x, position.y, -P.platformWidth-P.playerZOffset-3f);
        this.setShadowMode(ShadowMode.Off);   
        
        if (EffectSettings.light == EffectSettings.Light.TEXTURES || EffectSettings.light == EffectSettings.Light.TEXTURES_AND_WINDOW || EffectSettings.light == EffectSettings.Light.TEXTURES_SMALL_LIGHTS) {
            this.attachChild(this.addWallLighting(assetManager));
        }

    }
    
    private Geometry addWallLighting(AssetManager assetManager) {
        Box wallLight = new Box(39f/2f, 63f/2f, 0f);
        Geometry wall = new Geometry("wallLighting", wallLight);
        wall.setLocalTranslation(0f, -8f/3f, 6.6f);
        Material wallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wallMaterial.setTexture("ColorMap", assetManager.loadTexture(design.wallLightSrc));
        wallMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha); // activate transparency
        wall.setMaterial(wallMaterial);
        wall.setQueueBucket(RenderQueue.Bucket.Transparent);
        return wall;
    }
}
