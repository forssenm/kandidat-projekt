/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestate;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import variables.P;

/**
 *
 * @author dagen
 */
public class PlatformSceneObjectDataSource implements SceneObjectDataSource{
    AssetManager assetManager;

    private static int counter = 0;
    private static Geometry geometry;

    public PlatformSceneObjectDataSource(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    public Spatial getSceneObject(){
        Geometry geometry;
        if(this.geometry == null){
            Box model = new Box(Vector3f.ZERO,P.platformLength,P.platformHeight,P.platformWidth);
            
            geometry = new Geometry("Platform" , model);
        
            Material material = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
           // Material material = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            material.setTexture("DiffuseMap", assetManager.loadTexture("Textures/BrickWall.jpg"));
            /*ColorRGBA color = ColorRGBA.Blue;
            material.setBoolean("UseMaterialColors", true);
            material.setColor("Ambient", color);
            material.setColor("Diffuse", color);
            material.setColor("Specular", ColorRGBA.White);
        */
          //  material.setTexture("ColorMap", this.assetManager.loadTexture("Textures/BrickWall.jpg"));
            
            geometry.setMaterial(material);
            
            return (this.geometry = geometry);
        }
        return (geometry = this.geometry.clone(true));
    }
}
