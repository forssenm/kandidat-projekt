/*
 *
 */
package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import control.PlayerControl;
import variables.P;

/**
 * Class for keeping everything relating to the player in one place. Currently
 * keeps the spatial (along with the model) and the physics control object which
 * makes the player run and fall.
 *
 * @author dagen
 */
public class Player extends Node {

    private float playerRunSpeed;
    private float playerJumpSpeed;
    private CapsuleCollisionShape playerShape;
    private PlayerControl playerControl;
    private Node playerModel;

    /**
     * Constructs a
     * <code>Player</code> object which is a
     * <Code>Node</code> with the players 3D-model attached to it. The
     * <code>Player</code> also has a
     * <code>PlayerControl</code> attached.
     * @param assetManager is used for loading the players assets i.e. 3D-model
     * and textures.
     */
    public Player(AssetManager assetManager) {
        super("player");
        playerRunSpeed = P.run_speed;
        playerJumpSpeed = P.jump_speed;

        // the player casts shadows
        this.setShadowMode(RenderQueue.ShadowMode.Cast);

        // starting position
        this.setLocalTranslation(0.0f, 5.0f, 0.0f);

        // set up the physics control
        playerControl = new PlayerControl(1f, 7f, 20f);
        playerControl.setWalkVelocity(Vector3f.UNIT_X.mult(playerRunSpeed));
        playerControl.setJumpSpeed(playerJumpSpeed);
        this.addControl(playerControl);

        //Sets the model of the player
        playerModel = (Node) assetManager.loadModel("Models/ghost6anim/ghost6animgroups.j3o");

        playerModel.setLocalRotation((new Quaternion()).fromAngles(0f,180*FastMath.DEG_TO_RAD,0f));
        playerModel.setLocalTranslation(0f,1.8f,0f);
        this.attachChild(playerModel);

    }

    /**
     *
     * @return a reference to the players 3D-model.
     */
    public Node getPlayerModel() {
        return this.playerModel;
    }
}