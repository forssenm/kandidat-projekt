/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;

/**
 * A general PowerupControl. Has the right collisionshape and doesn't move or
 * do anything when touching a platform.
 * @author jonatankilhamn
 */
public abstract class AbstractPowerupControl extends AbstractPlayerInteractorControl {
            public AbstractPowerupControl() {
                super(new SphereCollisionShape(2f));
            }

            @Override
            protected void positionUpdate(float tpf) {
                // do not move
            }


            public void collideWithStatic() {
                // do nothing when colliding with platforms
            }
}
