/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package material;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.scene.Geometry;
import com.jme3.shader.Shader;
import com.jme3.shader.Uniform;
import com.jme3.shader.UniformBinding;
import com.jme3.shader.VarType;
import com.jme3.util.TempVars;
import light.MultiColoredLight;

/**
 *
 * @author Nina
 */
public class MultiColoredLightMaterial extends Material {
    
    private transient ColorRGBA ambientLightColor = new ColorRGBA(0, 0, 0, 1);
    private static final RenderState additiveLight = new RenderState();
    private static final Quaternion nullDirLight = new Quaternion(0, -1, 0, -1);
    
    static {
        additiveLight.setBlendMode(RenderState.BlendMode.AlphaAdditive);
        additiveLight.setDepthWrite(false);
    }
    
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
    
    private ColorRGBA getAmbientColor(LightList lightList) {
        System.out.println("own ambient");
        ambientLightColor.set(0, 0, 0, 1);
        for (int j = 0; j < lightList.size(); j++) {
            Light l = lightList.get(j);
            if (l instanceof AmbientLight) {
                ambientLightColor.addLocal(l.getColor());
            }
        }
        ambientLightColor.a = 1.0f;
        return ambientLightColor;
    }
    
    @Override
    protected void renderMultipassLighting(Shader shader, Geometry g, RenderManager rm) {
        System.out.println("own multipass");
        
        //------------------------------------
        
        Renderer r = rm.getRenderer();
        LightList lightList = g.getWorldLightList();
        Uniform lightDir = shader.getUniform("g_LightDirection");
        Uniform lightColor = shader.getUniform("g_LightColor");
        Uniform lightPos = shader.getUniform("g_LightPosition");
        Uniform ambientColor = shader.getUniform("g_AmbientLightColor");
        Uniform lightTexture = shader.getUniform("g_LightTexture");
        Uniform lightTextureSize = shader.getUniform("g_LightTextureSize");
        UniformBinding a = null;
        boolean isFirstLight = true;
        boolean isSecondLight = false;
        Uniform test = new Uniform();

        for (int i = 0; i < lightList.size(); i++) {
            Light l = lightList.get(i);
            if (l instanceof AmbientLight) {
                continue;
            }

            if (isFirstLight) {
                // set ambient color for first light only
                ambientColor.setValue(VarType.Vector4, getAmbientColor(lightList));
                isFirstLight = false;
                isSecondLight = true;
            } else if (isSecondLight) {
                ambientColor.setValue(VarType.Vector4, ColorRGBA.Black);
                // apply additive blending for 2nd and future lights
                r.applyRenderState(additiveLight);
                isSecondLight = false;
            }

            TempVars vars = TempVars.get();
            Quaternion tmpLightDirection = vars.quat1;
            Quaternion tmpLightPosition = vars.quat2;
            ColorRGBA tmpLightColor = vars.color;
            Vector4f tmpVec = vars.vect4f;
            Vector2f tmpRes = vars.vect2d;

            ColorRGBA color = l.getColor();
            tmpLightColor.set(color);
            tmpLightColor.a = l.getType().getId();
            lightColor.setValue(VarType.Vector4, tmpLightColor);

            switch (l.getType()) {
                case Directional:
                    DirectionalLight dl = (DirectionalLight) l;
                    Vector3f dir = dl.getDirection();

                    tmpLightPosition.set(dir.getX(), dir.getY(), dir.getZ(), -1);
                    lightPos.setValue(VarType.Vector4, tmpLightPosition);
                    tmpLightDirection.set(0, 0, 0, 0);
                    lightDir.setValue(VarType.Vector4, tmpLightDirection);
                    break;
                case Point:
                    PointLight pl = (PointLight) l;
                    Vector3f pos = pl.getPosition();
                    float invRadius = pl.getInvRadius();

                    tmpLightPosition.set(pos.getX(), pos.getY(), pos.getZ(), invRadius);
                    lightPos.setValue(VarType.Vector4, tmpLightPosition);
                    tmpLightDirection.set(0, 0, 0, 0);
                    lightDir.setValue(VarType.Vector4, tmpLightDirection);
                    
                    if(l.getClass() == MultiColoredLight.class) {
                        MultiColoredLight light = (MultiColoredLight) l;
                        tmpRes.set(new Vector2f(1,1));
                        lightTexture.setValue(VarType.Texture2D, light.getLightTexture());
                        lightTextureSize.setValue(VarType.Vector2, tmpRes);
                        test.setValue(VarType.Int, 1);
                        //System.out.println("found special: " + l);
                        //System.out.println("\t" + lightTexture + " " + light.getLightTexture());
                        //System.out.println("\t" + lightTextureSize + " " + light.getTextureDimensions() + " " + tmpRes);
                        
                        //System.out.println("\t" + test);
                    }
                    
                    break;
                case Spot:
                    SpotLight sl = (SpotLight) l;
                    Vector3f pos2 = sl.getPosition();
                    Vector3f dir2 = sl.getDirection();
                    float invRange = sl.getInvSpotRange();
                    float spotAngleCos = sl.getPackedAngleCos();

                    tmpLightPosition.set(pos2.getX(), pos2.getY(), pos2.getZ(), invRange);
                    lightPos.setValue(VarType.Vector4, tmpLightPosition);

                    //We transform the spot directoin in view space here to save 5 varying later in the lighting shader
                    //one vec4 less and a vec4 that becomes a vec3
                    //the downside is that spotAngleCos decoding happen now in the frag shader.
                    tmpVec.set(dir2.getX(), dir2.getY(), dir2.getZ(), 0);
                    rm.getCurrentCamera().getViewMatrix().mult(tmpVec, tmpVec);
                    tmpLightDirection.set(tmpVec.getX(), tmpVec.getY(), tmpVec.getZ(), spotAngleCos);

                    lightDir.setValue(VarType.Vector4, tmpLightDirection);

                    break;
                default:
                    throw new UnsupportedOperationException("Unknown type of light: " + l.getType());
            }
            //System.out.println(lightColor + " " + lightList + " " + lightTexture + " " + lightTextureSize + " " );
            vars.release();
            r.setShader(shader);
            r.renderMesh(g.getMesh(), g.getLodLevel(), 1);
        }

        if (isFirstLight && lightList.size() > 0) {
            // There are only ambient lights in the scene. Render
            // a dummy "normal light" so we can see the ambient
            ambientColor.setValue(VarType.Vector4, getAmbientColor(lightList));
            lightColor.setValue(VarType.Vector4, ColorRGBA.BlackNoAlpha);
            lightPos.setValue(VarType.Vector4, nullDirLight);
            r.setShader(shader);
            r.renderMesh(g.getMesh(), g.getLodLevel(), 1);
        }
        
        //-------------------------------------
        
        
        //LightList lightList = g.getWorldLightList();
        /*
        Uniform lightTexture = shader.getUniform("g_LightTexture");
        Uniform lightTextureSize = shader.getUniform("g_LightTextureSize");
        
        for (int i = 0; i < lightList.size(); i++) {
            Light l = lightList.get(i);
            System.out.println("checks " + l);
            if(l.getClass() == MultiColoredLight.class) {
                MultiColoredLight light = (MultiColoredLight) l;
                lightTexture.setValue(VarType.Texture2D, light.getLightTexture());
                lightTextureSize.setValue(VarType.Vector2, new Vector2f(1,1));
                System.out.println("found special: " + l);
                System.out.println("\t" + lightTexture + " " + light.getLightTexture());
                System.out.println("\t" + lightTextureSize + " " + light.getTextureDimensions());
            }
        }
        //Does nothing for result:
        //shader.getUniform("g_LightColor").setValue(VarType.Vector4, ColorRGBA.Red);
        System.out.println(shader.getUniform("g_LightColor") + " " + lightList + " " + lightTexture + " " + lightTextureSize + " " );
        super.renderMultipassLighting(shader, g, rm);
        * */
    }
    
}
