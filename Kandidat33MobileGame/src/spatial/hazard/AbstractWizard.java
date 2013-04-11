package spatial.hazard;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * A class for a Wizard, using a
 * <code>AbstractWizardControl</code> to control its behaviour.
 *
 * @author jonatankilhamn
 */
public abstract class AbstractWizard extends Hazard {
    
    @Override
    protected Spatial createModel(AssetManager assetManager) {
        // Node playerModel = (Node) assetManager.loadModel("Models/ghost6anim/clothball.j3o");
        Node playerModel = (Node) assetManager.loadModel("Models/wizard/Wizard-NoAnim-YellowbordersHair003.j3o");

       // playerModel.setLocalRotation((new Quaternion()).fromAngles(0f,90*FastMath.DEG_TO_RAD,0f));
        playerModel.scale(1.5f);
        ParticleEmitter sparkle = getWandParticleEmitter(assetManager);
        playerModel.attachChild(sparkle);
       //denna positionering (z-led) ser konstig ut men har att göra med att modellen är roterad 90 grader. Bättre vore att ha en modell som inte behöver roteras. Jobbar på det /130410
        sparkle.move(0.8f, 1.5f, -1f);   //what should be z effectively is x. what should be x is positive into the picture. Y is as is should be.
       
         return playerModel;
    }
    
    private ParticleEmitter getWandParticleEmitter (AssetManager assetManager) {
             ParticleEmitter fire = 
            new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 10);
    Material mat_red = new Material(assetManager, 
            "Common/MatDefs/Misc/Particle.j3md");
    mat_red.setTexture("Texture", assetManager.loadTexture(
            "Effects/Explosion/flame.png"));
   // mat_red.getAdditionalRenderState().setBlendMode(BlendMode.Alpha); för att kunna göra svarta partiklar
    fire.setMaterial(mat_red);
    fire.setImagesX(2); 
    fire.setImagesY(2); // 2x2 texture animation
    fire.setStartColor( new ColorRGBA(.10f, 0.40f, 0.90f, 1f));   // bright cyan
    fire.setEndColor(new ColorRGBA(0f, 0.1f, 0.25f, 0.5f)); // dark blue
    fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -5, 0));
    fire.setStartSize(1.5f);
    fire.setEndSize(0.5f);
    fire.setGravity(0, 0f, 0);
    fire.setLowLife(0.2f);
    fire.setHighLife(0.5f);
    fire.getParticleInfluencer().setVelocityVariation(0.3f);
    return fire;
    }

}