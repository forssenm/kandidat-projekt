/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package audio;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;

/**
 *
 * @author dagen
 */
public class MusicNode extends AudioNode{
    public MusicNode(AssetManager assetManager,String fileName){
        super(assetManager,fileName, false);
        setLooping(true);  // activate continuous playing
        setPositional(false);
        setVolume(3);
    }
}
