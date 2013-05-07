/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector4f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.shader.VarType;
import java.util.Arrays;

/**
 * Ambient occlusion post filter.
 * @author nina
 */
public class AmbientOcclusionFilter extends Filter {
    
    public AmbientOcclusionFilter() {
        super("AmbientOcclusionFilter");
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "PostProcessing/AmbientOcclusionMaterial.j3md");
        //material.setInt("numMoving", 1);
        Vector4f[] values = new Vector4f[1];
        Arrays.fill(values, Vector4f.ZERO);
        values[0] = new Vector4f(10, 200, 30, 60);
        //material.setParam("intervals", VarType.Vector4Array, values);
        this.updateIntervals(values);
    }

    @Override
    protected Material getMaterial() {
        return material;
    }
    
    @Override
    protected boolean isRequiresDepthTexture() {
        return true;
    }
    
    /**
     * Updates the intervals containing objects for which ambient 
     * occlusion will be calculated in real time. 
     * 
     * @param intervals The rectangle intervals containing objects for which 
     * ambient occlusion is calculated in real time. The different components 
     * of the vectors are as follows: x - minimum x value, y - maximum x value, 
     * z - minimum y value, w - maximum y value. The values are in pixels of 
     * the screen. 
     */
    public void updateIntervals(Vector4f[] intervals) {
        if(material == null || intervals == null) {
            System.out.println("No material");
        } else {
            material.setParam("intervals", VarType.Vector4Array, intervals);
            material.setInt("numMoving", intervals.length);
        }
    }
    
}
