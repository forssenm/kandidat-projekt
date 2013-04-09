package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

/**
 * An abstract FireballHazard. Any class extending this one will make a Hazard
 * that looks like a fireball.
 * @author jonatankilhamn
 */
public abstract class FireballHazard extends Hazard {

    @Override
    protected Spatial createModel(AssetManager assetManager) {
        Sphere model =
                new Sphere(5,5,0.1f);
        
                Geometry geometry = new Geometry("", model);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Red);
        geometry.setMaterial(material);
        return geometry;
    }
    
}
