/*
 *
 */
package gamestate;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import variables.P;

/**
 * Class for keeping everything relating to the player in one place.
 * Currently keeps the spatial (along with the model) and the physics control
 * object which makes the player run and fall.
 * 
 * @author dagen
 */
public class Player extends Node {
    
    private float playerRunSpeed;
    private float playerJumpSpeed;
    
    private CapsuleCollisionShape playerShape;
    private CharacterControl playerControl;
    
    private Node playerModel;
    
    
    /**
     * COnstructs a <code>Player</code> object which is a <Code>Node</code> 
     * with the players 3D-model attached to it. The <code>Player</code> also 
     * has a <code>CharacterControl</code> with a <code>CapsuleControl</code> 
     * attached. 
     * @param assetManager is used for loading the players assets i.e. 3D-model 
     * and textures. 
     */
    public Player(AssetManager assetManager) {
        playerRunSpeed = P.run_speed;
        playerJumpSpeed = P.jump_speed;
        
        playerShape = new CapsuleCollisionShape(1f,0.5f);
        playerControl = new CharacterControl(playerShape, 0.05f);
        playerControl.setWalkDirection(Vector3f.UNIT_X.mult(playerRunSpeed));
        playerControl.setJumpSpeed(playerJumpSpeed);
        this.addControl(playerControl);
        
        this.setLocalTranslation(new Vector3f(0, 15f, 0));
        
        //Sets the model of the player
        playerModel = (Node)assetManager.loadModel("Models/ghost6anim/ghost6animgroups.j3o");
        this.attachChild(playerModel);
    }
    
    /**
     * Makes the player jump in a manner predefined by 
     * <code>CharacterControl</code>.
     */
    public void jump() {
        playerControl.jump();
    }
    
    /**
     * Returns the players <code>CharacterControl</code>
     * @return the players CharacterControl.
     */
    public CharacterControl getPlayerControl() {
        return this.playerControl;
    }
    
    /**
     * 
     * @return a reference to the players 3D-model.  
     */
    public Node getPlayerModel() {
        return this.playerModel;
    }
}