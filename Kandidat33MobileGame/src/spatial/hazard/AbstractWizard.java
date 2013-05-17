package spatial.hazard;

import spatial.PlayerInteractor;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import spatial.StandardParticleEmitter;
import variables.P;
import com.jme3.scene.shape.Box;
import spatial.StandardParticleEmitter;
import variables.EffectSettings;
import variables.EffectSettings.AmbientOcclusion;

/**
 * A class for a general Wizard. Any class extending this will have the wizard
 * model and particle effect on the wand.
 *
 * @author jonatankilhamn
 */
public abstract class AbstractWizard extends PlayerInteractor {
    
    private static Node modelForWizard;
    protected SpotLight spotlight;
    
    public AbstractWizard(SpotLight spotlight) {
        super();
        this.spotlight = spotlight;
    }
    
    @Override
    protected Spatial createModel(AssetManager assetManager) {
        
        if (modelForWizard == null) {
            if (EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE) {
                modelForWizard = (Node) assetManager.loadModel("Models/wizard/AO/wizard-with-ao.j3o");
            } else {
                //modelForWizard = (Node) assetManager.loadModel("Models/wizard/Wizard-NoAnim-YellowbordersHair003-nolightcam.j3o"); // Nina's
                //modelForWizard = (Node) assetManager.loadModel("Models/wizard/Wizard-NoAnim-YellowbordersHair004MergeGreen.j3o");
                //modelForWizard = (Node) assetManager.loadModel("Models/wizard/wizard3/untitled21.j3o");
                modelForWizard = (Node) assetManager.loadModel("Models/wizard/wizard3/new/untitled21b-textures.j3o");
            }
            modelForWizard.scale(1.5f);
            modelForWizard.rotate(0, FastMath.DEG_TO_RAD*180, 0);
        }
        
        Node model = (Node) modelForWizard.clone();
        //model.rotate(0,3.1f,0);
        model.setName("wizardSpatial");
        if (EffectSettings.particles == EffectSettings.Particles.ON) {
        ParticleEmitter sparkle = getWandParticleEmitter(assetManager);
        model.attachChild(sparkle);
        sparkle.move(-1.2f, 1.65f, 1.4f);  //tested in scenecomposer. Good fit for wizard3.
        }
        return model;

    }
    
    protected Geometry addWallOcclusion(AssetManager assetManager, Vector3f localTranslation) {
        Box wallAO = new Box(3.5f, 3.5f, 0f);
        Geometry wall = new Geometry("wallOcclusion", wallAO);
        wall.setLocalTranslation(localTranslation);
        Material wallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wallMaterial.setTexture("ColorMap", assetManager.loadTexture("Models/wizard/AO/wall-ao-transparent-small.png"));
        wallMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha); // activate transparency
        wall.setMaterial(wallMaterial);
        return wall;
    }
    
    private ParticleEmitter getWandParticleEmitter (AssetManager assetManager) {
    ParticleEmitter sparkle = StandardParticleEmitter.standard(assetManager);
    sparkle.setName("spark");
    sparkle.setNumParticles(5);
    sparkle.getMaterial().setTexture("Texture", assetManager.loadTexture(
            "Textures/Explosion/flash.png"));
    
    sparkle.setStartColor( new ColorRGBA(.10f, 0.40f, 0.90f, 1f));   // bright cyan
    sparkle.setEndColor(new ColorRGBA(0f, 0.1f, 0.25f, 0.5f)); // dark blue
    sparkle.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -5, 0));
    sparkle.setStartSize(1.5f);
    sparkle.setEndSize(0.5f);
    sparkle.setGravity(0, 0f, 0);
    sparkle.setLowLife(0.2f);
    sparkle.setHighLife(0.6f);
    sparkle.getParticleInfluencer().setVelocityVariation(0.3f);
    return sparkle;
    }
    
    protected void redress(AssetManager a, ColorRGBA cloth, ColorRGBA particle) { //Changes wizard to black clothes and red particle
        if (cloth != null) {
        Material mat = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors",true);
        //saknas något här för att materialet ska reflektera ljus på bra sätt. det blir"torrt"
        mat.setColor("Diffuse", cloth);
        this.getChild("Cone.0002").setMaterial(mat);
        this.getChild("Cone.0012").setMaterial(mat);
            this.getChild("Cone.0021").setMaterial(mat);
        }
        if (EffectSettings.particles == EffectSettings.Particles.ON) {

            if (particle != null) {
                ((ParticleEmitter) this.getChild("spark")).setStartColor(particle);
            }
        }
    }
}