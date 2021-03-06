/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author nina
 */
public class MosaicFilter extends Filter {
    
    public MosaicFilter() {
        super("MosaicFilter");
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "PostProcessing/MosaicMaterial.j3md");
    }

    @Override
    protected Material getMaterial() {
        return material;
    }
    
}
