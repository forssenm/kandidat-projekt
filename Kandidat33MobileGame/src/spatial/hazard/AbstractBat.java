package spatial.hazard;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Random;
import spatial.PlayerInteractor;
import variables.EffectSettings;
import variables.EffectSettings.AmbientOcclusion;

/**
 * An abstract AbstractFireball. Any class extending this one will make a
 * PlayerInteractor that looks like a fireball.
 *
 * @author jonatankilhamn
 */
public abstract class AbstractBat extends PlayerInteractor implements AnimEventListener {

    //animation
    protected AnimChannel channel;
    protected AnimControl control;

    @Override
    protected Spatial createModel(AssetManager assetManager) {

        Node model = (Node) assetManager.loadModel("Models/bat/bat2-anim3-smooth.j3o");
        ((Geometry)((Node)model.getChild("Sphere")).getChild("Sphere1")).getMaterial().setColor("Diffuse", ColorRGBA.Orange);
        
        if (EffectSettings.ambientOcclusion == AmbientOcclusion.TEXTURE) {
            ((Geometry)((Node)model.getChild("Sphere")).getChild("Sphere1")).getMaterial().setTexture("AOMap", assetManager.loadTexture("Models/bat/AO/ao-small.png"));
        }

        model.rotate(+1.6f, +1.4f, 0); //flying towards player, slightly tilted up
        
        control = model.getChild("Sphere").getControl(AnimControl.class);

        channel = control.createChannel();

        control.addListener(this);

        channel.setAnim("ArmatureAction");
        channel.setLoopMode(LoopMode.Loop);

        int mod = (new Random().nextInt(8) - 3); // from -3 to +4
        model.scale(1f - 0.05f * mod); //smaller flap faster
        channel.setSpeed(2f + 0.4f * mod);


        return model;

    }
    //animation function that must be implemented even if unused

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
    //animation function that must be implemented even if unused

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    }

    @Deprecated
    public Material getRandomColorMaterial(AssetManager assetManager) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        Random r = new Random();
        int i = r.nextInt(5);
        mat.setBoolean("UseMaterialColors", true);
        ColorRGBA[] c = {ColorRGBA.Cyan, ColorRGBA.Red, ColorRGBA.White, ColorRGBA.Orange, ColorRGBA.Yellow};
        mat.setColor("Diffuse", c[i]);
        return mat;
    }
}
