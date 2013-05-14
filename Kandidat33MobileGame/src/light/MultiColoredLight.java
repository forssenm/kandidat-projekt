/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package light;

import com.jme3.asset.AssetManager;
import com.jme3.light.PointLight;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture2D;
import spatial.WindowFrame.Design;

/**
 *
 * @author Nina
 */
public class MultiColoredLight extends PointLight {
    
    private Texture2D lightTexture;
    private Vector2f dimensions; 

    public MultiColoredLight(AssetManager assetManager, Design design) {
        super();
        //this.lightTexture = (Texture2D) assetManager.loadTexture(design.lightColorsSrc);
        this.lightTexture = (Texture2D) assetManager.loadTexture("Models/window/Light/light_colors_test.png");
        this.dimensions = design.lightColorsDimensions;
    }
    
    public Texture2D getLightTexture() {
        return lightTexture;
    }
    
    public Vector2f getTextureDimensions() {
        return dimensions;
    }
    
}
