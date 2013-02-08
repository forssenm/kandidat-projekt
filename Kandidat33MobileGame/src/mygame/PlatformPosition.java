/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author jonatankilhamn
 */
public class PlatformPosition {

    private float x;
    private float y;
    private int level;

    public PlatformPosition(int level, float x, float y) {
        this.x = x;
        this.y = y;
        this.level = level;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getLevel() {
        return level;
    }
}
