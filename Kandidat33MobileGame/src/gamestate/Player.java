/*
 *
 */
package gamestate;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import variables.P;

/* Klassen är till att börja med bara 
 * en struktur för att knyta saker 
 * till en spelare. Tanken är att det 
 * ska utvecklas till att ha ett vettigt 
 * gränssnitt och eventuellt refaktoreras 
 * till några java interfaces som ärvs.
 */

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
    
    public Player(AssetManager am) {
        playerRunSpeed = P.run_speed;
        playerJumpSpeed = P.jump_speed;
        
        playerShape = new CapsuleCollisionShape(1f,0.5f);
        playerControl = new CharacterControl(playerShape, 0.05f);
        playerControl.setWalkDirection(Vector3f.UNIT_X.multLocal(playerRunSpeed));
        playerControl.setJumpSpeed(playerJumpSpeed);
        this.addControl(playerControl);
        
        this.setLocalTranslation(new Vector3f(0, 15f, 0));
        
        //Sets the model of the player
        playerModel = (Node)am.loadModel("Models/ghost6anim/ghost6animgroups.j3o");
        this.attachChild(playerModel);
    }
    
    public void Jump() {
        playerControl.jump();
    }
    
    public void increasePlayerSpeed() {
        // TODO: Implement method
    }
    
    public CharacterControl getPlayerControl() {
        return this.playerControl;
    }
    
    public CapsuleCollisionShape getPlayerShape() {
        return this.playerShape;
    }
    
    public Node getPlayerModel() {
        return this.playerModel;
    }
}

/*
public class Player {
    // Scene Object
    private Node node;
    public Spatial spatial;
    
    // Physics connection
    public CharacterControl characterControl;
    */
    /**
     * Generates the player.
     * @param dataSource An object that generates the spatial to be used to
     * represent the player.
     */
    /*public Player(SceneObjectDataSource dataSource){
        Node node = new Node();
        // get the spatial (model, material definition etc)
        Spatial spatial = dataSource.getSceneObject();
        
        // create a CharacterControl, controlling the (physical) behaviour of
        // the player.
        CapsuleCollisionShape shape = new CapsuleCollisionShape(1f,0.5f);
        CharacterControl characterControl = new CharacterControl(shape, 0.05f);

        // set the constant walking direction and jump speed
        Vector3f walkDirection = Vector3f.UNIT_X.multLocal(P.run_speed);
        characterControl.setWalkDirection(walkDirection);
        characterControl.setJumpSpeed(P.jump_speed);
        
        // Place the player in the world
        Vector3f vt = new Vector3f(0, 15f, 0);
        spatial.setLocalTranslation(vt);
        
        
        spatial.addControl(characterControl);
        node.attachChild(spatial);
        
        this.node = node;
        this.spatial = spatial;
        this.characterControl = characterControl;
    }
    
    public void addToNode(Node node){
        node.attachChild(this.spatial);
    }
    
    public void addToPhysicsSpace(PhysicsSpace physicsSpace){
        physicsSpace.add(this.characterControl);
    }
    
    public void jump(){
        this.characterControl.jump();
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }

    public CharacterControl getCharacterControl() {
        return characterControl;
    }

    public void setCharacterControl(CharacterControl characterControl) {
        this.characterControl = characterControl;
    }
}
*/