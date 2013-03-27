package control;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import variables.P;

/**
 * This class is responsible for controlling the players movement when running.
 * It listens to the input mapping named "jump" to perform jumps. 
 * @author dagen
 */
public class RunningControl extends AbstractControl implements PhysicsControl, ActionListener{
    private PhysicsSpace physicsSpace;
    private CharacterControl characterControl;
    
    /** 
     * Sets the spatial of the <code>RunningControl</code>.
     */
    @Override
    public void setSpatial(Spatial spatial){
        super.setSpatial(spatial);
        
        if( this.spatial != null){
            //If there is a previous spatial then remove it's CharacterControl.
            this.spatial.removeControl(this.getCharacterControl());
        }
        if(spatial != null){
            //If we were sent a spatial then set it up with a CharacterControl. 
            this.spatial.addControl(this.getCharacterControl());
        }
    }
    
    /** 
     * Creates a clone of this <code>RunningControl</code> 
     * compatible with <code>spatial</code>.
     */
    public Control cloneForSpatial(Spatial spatial) {
        RunningControl control = new RunningControl();
        control.characterControl = 
                (CharacterControl) this.characterControl.cloneForSpatial(spatial);
        control.setSpatial(spatial); 
        return control;
    }
    
    /** 
     * Sets the <code>PhysicsSpace</code> of this <code>RunningControl</code>
     * @param space the <code>PhysicsSpace</code>
     */
    /* Connects internal CharacterControl*/
    public void setPhysicsSpace(PhysicsSpace space) {
        physicsSpace = space;
        
        /* Connects internal CharacterControl*/
        physicsSpace.add(getCharacterControl()); 
    }
    
    /** 
     * Returns the <code>PhysicsSpace</code> of this <code>RunningControl</code>
     * @return physicsSpace the <code>PhysicsSpace.
     */
    public PhysicsSpace getPhysicsSpace() {
        return physicsSpace;
    }
    
    /**
     * This method currently does nothing. 
     */
    @Override
    protected void controlUpdate(float tpf) {
        /* Currently does nothing 
         * Here we could fire a propertyChange on the position property
         * so that any one who listens could catch is.
         */
    }

    /**
     * This method currently does nothing. 
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        /* Currently does nothing */
    }

    /**
     * @deprecated 
     * Returns the jumpSpeed of internal <code>CharacterControl</code>. 
     * If <code>CharacterControl==null</code> return 
     * default value. <br/>
     * This method might be changed if the internal implementation changes.
     * @return the jumpSpeed
     */
    public float getJumpSpeed() {
        float result = P.jump_speed;
        if(characterControl != null){
            result = characterControl.getJumpSpeed();
        }
        return result;
    }

    /**
     * @deprecated 
     * Sets the jumpSpeed of the internal <code>CharacterControl</code>. <br /> 
     * This method might be changed if the internal implementation changes.
     * @param jumpSpeed the jumpSpeed to set
     */
    public void setJumpSpeed(float jumpSpeed) {
        if(characterControl != null) {
            characterControl.setJumpSpeed(jumpSpeed);
        }
    }

    /**
     * @deprecated 
     * Gets the running speed in the x-direction by
     * getting the x-component of the internal <code>CharacterControl</code>s
     * walkDirection. If <code>CharacterControl==null</code> return 
     * default value. <br/> 
     * This method might be changed if the internal implementation changes.
     * @return the runSpeed
     */
    public float getRunSpeed() {
        float result = P.run_speed;
        if(characterControl != null){
            result = characterControl.getWalkDirection().x;
        }
        return result;
    }

    /**
     * @deprecated 
     * Sets the running speed in the x-direction by setting
     * the x-component of the internal <code>CharacterControl</code>s
     * walkDirection. <br />
     * This method might be changed if the internal implementation changes.
     * @param runSpeed the runSpeed to set
     */
    public void setRunSpeed(float runSpeed) {
        if(characterControl != null) {
            characterControl.setWalkDirection(Vector3f.UNIT_X.mult(runSpeed));
        }
    }

    /** 
     * @deprecated 
     * Returns the internal <code>CharacterControl</code>. If it is null then 
     * it is created and initialized with default values. 
     * @return the characterControl
     */
    private CharacterControl getCharacterControl() {
        if(characterControl == null){
            CapsuleCollisionShape shape = new CapsuleCollisionShape(1f, 0.5f);
            characterControl = new CharacterControl(shape, 0.05f);
            setRunSpeed(P.run_speed);
            setJumpSpeed(P.jump_speed);
        }
        return characterControl;
    }
 
    /** 
     * Responds to the action "jump" by making the player jump. Currently have 
     * the same jump behaviour as <code>CharacterControl</code>.
     */
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals("jump") && isPressed){ 
            // Button down
            this.getCharacterControl().jump();
        }else{ 
            // Button up
        }
    }
}
