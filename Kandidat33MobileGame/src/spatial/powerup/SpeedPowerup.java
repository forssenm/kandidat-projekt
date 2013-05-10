package spatial.powerup;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import control.AbstractPlayerInteractorControl;
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
public class SpeedPowerup extends PlayerInteractor {

    public SpeedPowerup(AssetManager assetManager) {
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }

    @Override
    protected Spatial createModel(AssetManager assetManager) {
        Node modelNode = new Node("modelnode");
        
        Node model = (Node)assetManager.loadModel("Models/powerups/icosphere/ico001.j3o");
        model.setName("model");
        ParticleEmitter glow = getPowerupParticleEmitter(assetManager);
        modelNode.attachChild(glow);
        modelNode.attachChild(model);

        return modelNode;
    }

    private ParticleEmitter getPowerupParticleEmitter(AssetManager assetManager) {
        ParticleEmitter glow = StandardParticleEmitter.standard(assetManager);
                
        glow.setStartColor(ColorRGBA.Cyan);
        glow.setEndColor(ColorRGBA.White);
        glow.getParticleInfluencer().setInitialVelocity(Vector3f.ZERO);
        glow.setStartSize(3.0f);
        glow.setEndSize(0.1f);
        glow.setGravity(0, 0, 0);
        glow.setLowLife(0.4f);
        glow.setHighLife(1f);
        glow.getParticleInfluencer().setVelocityVariation(3f);
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
                    pc.speedBoostPowerup();
                    hasHit = true;
                    SpeedPowerup.this.destroy();
                }
            }
            
            @Override
            protected void positionUpdate(float tpf) {
                time += tpf;
                Spatial model = ((Node)this.spatial).getChild("model");
                if (model != null) {
                model.setLocalTranslation(
                        (float)Math.cos(time*10)*2f,
                        (float)Math.sin(time*12)*2f,
                        (float)Math.sin(2+time*14)*2f);
                }
            }

        };
    }
}
