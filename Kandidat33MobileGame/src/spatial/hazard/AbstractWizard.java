package spatial.hazard;

import spatial.PlayerInteractor;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
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
    
    @Override
    protected Spatial createModel(AssetManager assetManager) {
        
        if (modelForWizard == null) {
            if (EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE) {
                modelForWizard = (Node) assetManager.loadModel("Models/wizard/AO/wizard-with-ao.j3o");
            } else {
                modelForWizard = (Node) assetManager.loadModel("Models/wizard/Wizard-NoAnim-YellowbordersHair003-nolightcam.j3o");
            }
            modelForWizard.scale(1.5f);
        }
        
        Node model = (Node) modelForWizard.clone();
        model.setName("wizardSpatial");
        ParticleEmitter sparkle = getWandParticleEmitter(assetManager);
        model.attachChild(sparkle);
        sparkle.move(0.8f, 1.5f, -1f);   //what should be z effectively is x. what should be x is positive into the picture. Y is as is should be.

        return model;
    }
    
    protected Geometry addWallOcclusion(AssetManager assetManager, Vector3f localTranslation) {
        Box wallAO = new Box(3.5f, 3.5f, 0f);
        Geometry wall = new Geometry("wallOcclusion", wallAO);
        wall.setLocalTranslation(localTranslation);
        //wall.rotate(0f, 0f, -90*FastMath.DEG_TO_RAD);
        Material wallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //wallMaterial.setTexture("ColorMap", assetManager.loadTexture("Models/platform/AO/wall-ao-transparant-small.png"));
        //wallMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha); // activate transparency
        wall.setMaterial(wallMaterial);
        return wall;
    }
    
    private ParticleEmitter getWandParticleEmitter (AssetManager assetManager) {
    ParticleEmitter fire = StandardParticleEmitter.make(assetManager);
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

}