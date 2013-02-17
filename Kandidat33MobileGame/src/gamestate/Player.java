/*
 *
 */
package gamestate;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
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
 *
 * @author dagen
 */
public class Player {
    // Scene Object
    private Node node;
    public Spatial spatial;
    
    // Physics connection
    public CharacterControl characterControl;
    
    
    public Player(SceneObjectDataSource dataSource){
        Node node = new Node();
        Spatial spatial = dataSource.getSceneObject();
        
        CapsuleCollisionShape shape = new CapsuleCollisionShape(0f, 2f);
        CharacterControl characterControl = new CharacterControl(shape, 0.05f);
        characterControl.setJumpSpeed(P.jump_speed);

        Vector3f walkDirection = Vector3f.UNIT_X.multLocal(P.run_speed);
        characterControl.setWalkDirection(walkDirection);
        
        Vector3f vt = new Vector3f(0, 5, 0);
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
