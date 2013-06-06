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
    
    protected SpotLight spotlight;
    
    public AbstractWizard(SpotLight spotlight) {
        super();
        this.spotlight = spotlight;
    }
    
    @Override
    protected Spatial createModel(AssetManager assetManager) {
        
        //Node model = (Node) assetManager.loadModel("Models/wizard/wizard3/new/untitled21b-textures.j3o");
       Node model = (Node) assetManager.loadModel("Models/wizard/wizardcloth005.j3o");
       
        //modelForWizard = (Node) assetManager.loadModel("Models/wizard/Wizard-NoAnim-YellowbordersHair003-nolightcam.j3o"); // Nina's
        //modelForWizard = (Node) assetManager.loadModel("Models/wizard/Wizard-NoAnim-YellowbordersHair004MergeGreen.j3o");
        //modelForWizard = (Node) assetManager.loadModel("Models/wizard/wizard3/untitled21.j3o");
        
        model.scale(1.5f);
        model.rotate(0, FastMath.DEG_TO_RAD * 180, 0);
        
        if (EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE) {
           // this.addAOTextures(model, assetManager);
        }

        //model.rotate(0,3.1f,0);
        model.setName("wizardSpatial");
        ParticleEmitter sparkle = getWandParticleEmitter(assetManager);
        model.attachChild(sparkle);
        sparkle.move(-1.2f, 1.65f, 1.4f);  //tested in scenecomposer. Good fit for wizard3.

        return model;

    }
    
    protected Geometry addWallOcclusion(AssetManager assetManager, Vector3f localTranslation) {
        Box wallAO = new Box(3.5f, 3.5f, 0f);
        Geometry wall = new Geometry("wallOcclusion", wallAO);
        wall.setLocalTranslation(localTranslation);
        Material wallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wallMaterial.setTexture("ColorMap", assetManager.loadTexture("Models/ghost/AO/wall-ao-small.png"));
        wallMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha); // activate transparency
        wallMaterial.getAdditionalRenderState().setDepthWrite(false);
        wall.setMaterial(wallMaterial);
        return wall;
    }
    
    private ParticleEmitter getWandParticleEmitter (AssetManager assetManager) {
    ParticleEmitter fire = StandardParticleEmitter.standard(assetManager);
    fire.setName("spark");
    fire.setNumParticles(5);
    fire.getMaterial().setTexture("Texture", assetManager.loadTexture(
            "Textures/Explosion/flash.png"));
    
    fire.setStartColor( new ColorRGBA(.10f, 0.40f, 0.90f, 1f));   // bright cyan
    fire.setEndColor(new ColorRGBA(0f, 0.1f, 0.25f, 0.5f)); // dark blue
    fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -5, 0));
    fire.setStartSize(1.5f);
    fire.setEndSize(0.5f);
    fire.setGravity(0, 0f, 0);
    fire.setLowLife(0.2f);
    fire.setHighLife(0.6f);
    fire.getParticleInfluencer().setVelocityVariation(0.3f);
    return fire;
    }
    
    protected void redress(AssetManager a, ColorRGBA cloth, ColorRGBA particle) { //Changes wizard to black clothes and red particle
        if (cloth != null) {
            //saknas något här för att materialet ska reflektera ljus på bra sätt. det blir"torrt"
            ((Geometry)this.getChild("Cone.0002")).getMaterial().setColor("Diffuse", cloth);
            ((Geometry)this.getChild("Cone.0012")).getMaterial().setColor("Diffuse", cloth);
            ((Geometry)this.getChild("Cone.0021")).getMaterial().setColor("Diffuse", cloth);
        }
        if (particle != null) {
            ((ParticleEmitter)this.getChild("spark")).setStartColor(particle);
        }
    }

    private void addAOTextures(Node model, AssetManager assetManager) {
        ((Geometry)((Node)model.getChild("Cone.000")).getChild("Cone.0001")).getMaterial().setTexture("AOMap", assetManager.loadTexture("Models/wizard/wizard3/new/AO/hat-ao-small.png"));
        ((Geometry)((Node)model.getChild("Cone.000")).getChild("Cone.0002")).getMaterial().setTexture("AOMap", assetManager.loadTexture("Models/wizard/wizard3/new/AO/hat-ao-small.png"));
        ((Geometry)((Node)model.getChild("Cone.001")).getChild("Cone.0011")).getMaterial().setTexture("AOMap", assetManager.loadTexture("Models/wizard/wizard3/new/AO/dress-ao-small.png"));
        ((Geometry)((Node)model.getChild("Cone.001")).getChild("Cone.0012")).getMaterial().setTexture("AOMap", assetManager.loadTexture("Models/wizard/wizard3/new/AO/dress-ao-small.png"));
        ((Geometry)((Node)model.getChild("Cone.002")).getChild("Cone.0021")).getMaterial().setTexture("AOMap", assetManager.loadTexture("Models/wizard/wizard3/new/AO/arm-ao-small.png"));
        ((Geometry)((Node)model.getChild("Cone.001")).getChild("Cone.0012")).getMaterial().setTexture("AOMap", assetManager.loadTexture("Models/wizard/wizard3/new/AO/dress-ao-small.png"));
        ((Geometry)((Node)model.getChild("Cylinder.001")).getChild("Cylinder.0011")).getMaterial().setTexture("DiffuseMap", assetManager.loadTexture("Models/wizard/wizard3/new/AO/hair-ao-small.png"));
        ((Geometry)((Node)model.getChild("Plane.004")).getChild("Plane.0061")).getMaterial().setTexture("DiffuseMap", assetManager.loadTexture("Models/wizard/wizard3/new/AO/beard-ao-small.png"));
        ((Geometry)((Node)model.getChild("Sphere")).getChild("Sphere1")).getMaterial().setTexture("DiffuseMap", assetManager.loadTexture("Models/wizard/wizard3/new/AO/head-ao-small.png"));
    }
}