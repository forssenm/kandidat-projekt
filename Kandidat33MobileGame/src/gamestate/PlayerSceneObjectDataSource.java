/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestate;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author dagen
 */
public class PlayerSceneObjectDataSource implements SceneObjectDataSource{
    AssetManager assetManager;
    public PlayerSceneObjectDataSource(AssetManager assetManager){
        this.assetManager = assetManager;
    }
    public Spatial getSceneObject(){
        Spatial node = new Node();
        Box model = new Box(Vector3f.ZERO, 1f, 1f, 1f);
        model.scaleTextureCoordinates(new Vector2f(1f, .5f));
        Geometry geometry = new Geometry("player", model);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Red);
        geometry.setMaterial(material);
        return geometry;
    }
}
