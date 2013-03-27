/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author dagen
 */
public class Hazard extends Node {

    public Hazard(AssetManager assetManager) {
        super("hazard");
        Box model =
                new Box(Vector3f.ZERO, 1, 1, 1);

        Geometry geometry = new Geometry("", model);
        this.attachChild(geometry);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Blue);
        this.setMaterial(material);

    }
}
