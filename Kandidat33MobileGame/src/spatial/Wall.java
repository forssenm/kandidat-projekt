package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import material.WallMaterial;
import variables.EffectSettings;
import variables.P;

/**
 * A class for a chunk of background wall.
 * @author jonatankilhamn
 */
public class Wall extends Node {
    
    private static Material material;
    /**
     * This constructor creates a <code>Wall</code> represented by a 
     * <code>Geometry</code> loaded internaly. The dimensions of the 
     * <code>Wall</code> is currently loaded from the <code>P</code>-class 
     * containing some global variables. 
     * @param assetManager is used to load the geometry and 
     * texture of the <code>Wall</code>.
     */
    public Wall(AssetManager assetManager){
        super("Wall");
        
        if(material == null){
            Texture texture = assetManager.loadTexture("Textures/tegel.png");
            texture.setWrap(Texture.WrapMode.Repeat);
            
            if (EffectSettings.light == EffectSettings.Light.STANDARD_LIGHTING || EffectSettings.light == EffectSettings.Light.NONE) {
                material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
                material.setTexture("DiffuseMap", texture);
            } else {
                material =  new WallMaterial(assetManager, "Materials/WallUnshaded.j3md");
                material.setTexture("ColorMap", texture);
                /*material =  new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                material.setTexture("ColorMap", texture);*/
            }
            
        }
        
        Quad model = new Quad(P.chunkLength,WALL_HEIGHT/2);

        model.scaleTextureCoordinates(new Vector2f(11, 9));
        
        Geometry geometry1 = new Geometry("",model);
        Geometry geometry2 = new Geometry("",model);
        geometry1.setLocalTranslation(new Vector3f(0,0,-P.platformWidth/2-P.playerZOffset));
        geometry2.setLocalTranslation(new Vector3f(0,-WALL_HEIGHT/2,-P.platformWidth/2-P.playerZOffset));
        
        geometry1.setMaterial(material);
        geometry2.setMaterial(material);

       
        this.attachChild(geometry1);        
        this.attachChild(geometry2);
 
        this.setShadowMode(ShadowMode.Receive);
        
    }
    
    private static final float WALL_HEIGHT = 120.0f;
}
