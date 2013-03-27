package audio;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;

/**
 * This class is used to play background music. It sets some defaults and 
 * otherwise works just like {@link AudioNode}. 
 * @author dagen
 */
public class MusicNode extends AudioNode{
    /**
     * Calls super constructor 
     * {@link AudioNode#AudioNode(com.jme3.asset.AssetManager, java.lang.String, boolean)}
     * with the input argument and false for the stream argument. The defaults 
     * are as follows: <br/> 
     * <code>looping</code> is set to <code>true</code> <br/> 
     * <code>positional</code> is set to <code>false</code> and <br/>
     * <code>volume</code> is set to <code>3</code>.
     * @param assetManager
     * @param fileName 
     */
    public MusicNode(AssetManager assetManager,String fileName){
        super(assetManager,fileName, false);
        setLooping(true);
        setPositional(false); 
        setVolume(3);
    }
}
