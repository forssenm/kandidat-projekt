package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import variables.P;

/**
 * A class for a chunk of background wall.
 * @author jonatankilhamn
 */
public class Wall extends Node {
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
        Box model =
            new Box(new Vector3f(P.chunkLength/2,0,-P.platformWidth/2), P.chunkLength/2, 40, 0);
        
        //Loads the texture and repeats it over the chunk in its correct size 
        //(so that each brick will not be bigger in pixels, or stretched, if 
        //the resolution is higher is greater than 640x480)
        Texture texture = assetManager.loadTexture("Textures/bricks.jpg");
        texture.setWrap(Texture.WrapMode.Repeat);
        model.scaleTextureCoordinates(new Vector2f(Math.round(18f*P.screenWidth/640), Math.round(40f*P.screenHeight/480)));
        
        Geometry geometry = new Geometry("",model);
        
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.setTexture("DiffuseMap", texture);
        geometry.setMaterial(material);
 
        this.setShadowMode(ShadowMode.Receive);
        
        this.attachChild(geometry);
    }
}
