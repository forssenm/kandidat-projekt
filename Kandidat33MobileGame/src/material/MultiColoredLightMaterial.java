/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package material;

import com.jme3.asset.AssetManager;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.shader.Shader;
import com.jme3.shader.Uniform;
import com.jme3.shader.VarType;
import light.MultiColoredLight;

/**
 *
 * @author Nina
 */
public class MultiColoredLightMaterial extends Material {
    
    public MultiColoredLightMaterial(MaterialDef def) {
        super(def);
    }

    public MultiColoredLightMaterial(AssetManager contentMan, String defName) {
        super(contentMan, defName);
        System.out.println("own material");
    }

    public MultiColoredLightMaterial() {
        super();
    }
    
    @Override
    protected void renderMultipassLighting(Shader shader, Geometry g, RenderManager rm) {
        System.out.println("own multipass");
        super.renderMultipassLighting(shader, g, rm);
        
        LightList lightList = g.getWorldLightList();
        
        Uniform lightTexture = shader.getUniform("g_LightTexture");
        Uniform lightTextureSize = shader.getUniform("g_LightTextureSize");
        
        for (int i = 0; i < lightList.size(); i++) {
            Light l = lightList.get(i);
            if(l.getClass() == MultiColoredLight.class) {
                MultiColoredLight light = (MultiColoredLight) l;
                lightTexture.setValue(VarType.Texture2D, light.getLightTexture());
                lightTextureSize.setValue(VarType.Vector2, light.getTextureDimensions());
            }
        }
        System.out.println(lightList + " " + lightTexture + " " + lightTextureSize);
    }
    
}
