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
import control.PlayerControl;
import control.PlayerInteractorControl;
import spatial.Player;
import spatial.PlayerInteractor;

/**
 * An powerup that gives the player the ability to double jump.
 *
 * @author jonatankilhamn
 */
public class DoubleJumpPowerup extends PlayerInteractor {

    public DoubleJumpPowerup(AssetManager assetManager) {
        this.attachChild(this.createModel(assetManager));
        this.addControl(this.createControl());
    }

    @Override
    protected Spatial createModel(AssetManager assetManager) {
        Node fireball = new Node();
        /*Sphere model =
         new Sphere(5,5,0.1f);
        
         Geometry geometry = new Geometry("", model);
         Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
         material.setColor("Color", ColorRGBA.Red);
         geometry.setMaterial(material);
        
         fireball.attachChild(geometry);*/
        ParticleEmitter glow = getPowerupParticleEmitter(assetManager);
        fireball.attachChild(glow);

        return fireball;
    }

    private ParticleEmitter getPowerupParticleEmitter(AssetManager assetManager) {
        ParticleEmitter glow =
                new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(assetManager,
                "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture(
                "Textures/Explosion/flame.png"));
        glow.setMaterial(mat_red);
        glow.setImagesX(2);
        glow.setImagesY(2); // 2x2 texture animation

        glow.setStartColor(ColorRGBA.Green);
        glow.setEndColor(ColorRGBA.DarkGray);
        glow.getParticleInfluencer().setInitialVelocity(Vector3f.ZERO);
        glow.setStartSize(3.5f);
        glow.setEndSize(0.1f);
        glow.setGravity(0, 30, 0);
        glow.setLowLife(0.4f);
        glow.setHighLife(1f);
        glow.getParticleInfluencer().setVelocityVariation(0.3f);
        return glow;
    }
    
    public void destroy() {
        this.setName("");
        ParticleEmitter pe = (ParticleEmitter) this.getChild("Emitter");
        pe.setLowLife(0f);
        pe.setHighLife(0f);
    }

    @Override
    protected PlayerInteractorControl createControl() {
        return new AbstractPlayerInteractorControl(new SphereCollisionShape(1f)) {
            private boolean hasHit;

            @Override
            protected void positionUpdate(float tpf) {
                // do not move
            }

            public void collideWithPlayer(Player player) {
                if (!hasHit) {
                    PlayerControl pc = player.getControl(PlayerControl.class);
                    pc.doubleJumpPowerup();
                    hasHit = true;
                    DoubleJumpPowerup.this.destroy();
                }
            }
        };
    }
}
