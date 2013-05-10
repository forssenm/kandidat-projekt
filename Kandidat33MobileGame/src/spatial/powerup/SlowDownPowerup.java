package spatial.powerup;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import control.AbstractPowerupControl;
import control.PlayerControl;
import control.PlayerInteractorControl;
import spatial.Player;
import spatial.PlayerInteractor;
import spatial.StandardParticleEmitter;

/**
 * An powerup that gives the player a boost to run and jump speed.
 *
 * @author jonatankilhamn
 */
public class SlowDownPowerup extends PlayerInteractor {

    public SlowDownPowerup(AssetManager assetManager) {
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }

    @Override
    protected Spatial createModel(AssetManager assetManager) {
        Node fireball = new Node("modelnode");
        Node model = (Node) assetManager.loadModel("Models/powerups/pyramidred/pyramidred.j3o");
        model.setName("model");
      //  model.scale(1.5f);
        fireball.attachChild(model);
        ParticleEmitter glow = getPowerupParticleEmitter(assetManager);
        fireball.attachChild(glow);

        return fireball;
    }

    private ParticleEmitter getPowerupParticleEmitter(AssetManager assetManager) {
        ParticleEmitter glow = StandardParticleEmitter.standard(assetManager);
       // glow.setName("glow");      

        glow.setStartColor(ColorRGBA.DarkGray);
        glow.setEndColor(ColorRGBA.Red);
        glow.getParticleInfluencer().setInitialVelocity(Vector3f.ZERO);
        glow.setStartSize(3f);
        glow.setEndSize(0.1f);
        glow.setGravity(0, 0, 0);
        glow.setLowLife(0.4f);
        glow.setHighLife(1f);
        glow.getParticleInfluencer().setVelocityVariation(10f);
        return glow;
    }
    
    public void destroy() {
        this.setName("");
        ParticleEmitter pe = (ParticleEmitter) this.getChild("Emitter");
        pe.setLowLife(0f);
        pe.setHighLife(0f);
        ((Node)this.getChild("modelnode")).detachChild(this.getChild("model"));
    }

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractPowerupControl() {
            private boolean hasHit;
            private float time;
            
            public void collideWithPlayer(Player player) {
                if (!hasHit) {
                    PlayerControl pc = player.getControl(PlayerControl.class);
                    pc.slowDownPowerup();
                    hasHit = true;
                    SlowDownPowerup.this.destroy();
                }
            }
            
            @Override
            protected void positionUpdate(float tpf) {
                time += tpf;
                //Spatial model = ((Node)this.spatial).getChild("model");
                //if (model != null) {
                //    model.rotate(0.09f, 0.18f, 0.04f);
                //model.setLocalTranslation(
                  //      (float)Math.cos(time*15)*2f,
                    //    (float)Math.sin(time*18)*2f,
                      //  (float)Math.sin(2+time*21)*2f);
                //}
                ParticleEmitter glow = (ParticleEmitter)((Node)this.spatial).getChild("Emitter");
                if (glow != null) {
                    glow.setStartSize(3.0f - (float)Math.cos(time*4)*1.6f);
                }
            }

        };
    }
}
