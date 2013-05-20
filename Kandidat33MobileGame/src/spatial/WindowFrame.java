package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import variables.EffectSettings;
import variables.P;

/**
 * A class for a non-physical window frame, purely for decoration.
 * @author jonatankilhamn
 */
public class WindowFrame extends Node {
    
    public enum Design {
        FLOWERS ("Models/window/flat-window-2-texture.png",
                "Models/window/Light/texture1_light.png",
                //"Models/window/Light/texture1_light-small-round.png",
                //"Models/window/Light/test1.png",
                "Models/window/Light/texture1_light_colors.png",
                "Models/window/Light/flat-window-2-texture.png"),
        BIRD ("Models/window/flat-window-2-texture-2.png",
                "Models/window/Light/texture2_light.png",
                //"Models/window/Light/texture2_light-small-round.png",
                //"Models/window/Light/test2.png",
                "Models/window/Light/texture2_light_colors.png",
                "Models/window/Light/flat-window-2-texture-2.png");

        public final String wallLightSrc;
        public final String lightColorsSrc;
        public final String modelTexture;
        public final String lightModelTexture;

        Design(String modelTexture, String wallLightSrc, String lightColorsSrc, String lightModelTexture) {
            this.modelTexture = modelTexture;
            this.wallLightSrc = wallLightSrc;
            this.lightColorsSrc = lightColorsSrc;
            this.lightModelTexture = lightModelTexture;
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
        
        Node window = (Node)assetManager.loadModel("Models/window/flat-window-2.j3o");
        if (EffectSettings.light == EffectSettings.Light.TEXTURES || EffectSettings.light == EffectSettings.Light.TEXTURES_AND_WINDOW || EffectSettings.light == EffectSettings.Light.TEXTURES_SMALL_LIGHTS) {
            Material newMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            //Material newMaterial = new WallMaterial(assetManager, "Materials/WallUnshaded.j3md");
            newMaterial.setTexture("ColorMap", assetManager.loadTexture(design.lightModelTexture));
            //newMaterial.setTexture("AffectMap", assetManager.loadTexture("Models/window/Light/affectionMap.png"));
            ((Geometry)((Node)window.getChild("Cylinder")).getChild("Cylinder1")).setMaterial(newMaterial);
        } else {
            ((Geometry)((Node)window.getChild("Cylinder")).getChild("Cylinder1")).getMaterial().setTexture("DiffuseMap", assetManager.loadTexture(design.modelTexture));
        }
        window.scale(4.5f);
        window.rotate(90*FastMath.DEG_TO_RAD, 0f, 0f);
        
        this.attachChild(window);
        
        this.setLocalTranslation(position.x, position.y, -P.platformWidth-P.playerZOffset-3f);
        this.setShadowMode(ShadowMode.Off);   
        
        if (EffectSettings.light == EffectSettings.Light.TEXTURES || EffectSettings.light == EffectSettings.Light.TEXTURES_AND_WINDOW || EffectSettings.light == EffectSettings.Light.TEXTURES_SMALL_LIGHTS) {
            this.attachChild(this.addWallLighting(assetManager, design));
        }
    }
    
    private Geometry addWallLighting(AssetManager assetManager, Design design) {
        Box wallLight = new Box(39f/2f, 63f/2f, 0f);
        //Box wallLight = new Box(30, 30, 0f);
        Geometry wall = new Geometry("wallLighting", wallLight);
        wall.setLocalTranslation(0f, -8f/3f, 6.6f);
        Material wallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wallMaterial.setTexture("ColorMap", assetManager.loadTexture(design.wallLightSrc));
        wallMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha); // activate transparency
        wallMaterial.getAdditionalRenderState().setDepthWrite(false);
        wall.setMaterial(wallMaterial);
        wall.setQueueBucket(RenderQueue.Bucket.Transparent);
        return wall;
    }
}
