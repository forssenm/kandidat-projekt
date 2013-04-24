package spatial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import util.RomanNumber;
import variables.P;

/**
 * A class for a chunk of background wall.
 * @author jonatankilhamn
 */
public class MileStone extends Node {
    private static final float LENGTH_OF_I = 1f;
    private static final float LENGTH_OF_V = 2f;
    private static final float LENGTH_OF_X = 2f;
    private static final float LENGTH_OF_D = 2f;
    private static final float LENGTH_OF_L = 1f;
    
    private static Material materialForI;
    private static Material materialForV;
    private static Material materialForX;
    private static Material materialForL;
    private static Material materialForD;
    
    
    
    
    /**
     * This constructor creates a <code>MileStone</code> represented by a 
     * <code>Geometry</code> loaded internaly.
     * @param assetManager is used to load the geometry and 
     * texture of the <code>MileStone</code>.
     */
    public MileStone(AssetManager assetManager, int progress){
        super("MileStone");
        
        if (materialForI == null) {
            initMaterials(assetManager);
        }
        
        String mileStoneText = RomanNumber.romanNumberString(progress);
        Geometry temp = new Geometry();
        float length = 0;
        for (int i = 0; i < mileStoneText.length(); i++) {
            temp = null;
            switch (mileStoneText.charAt(i)) {
                case ('I'):
                    temp = romanIBox();
                    temp.setLocalTranslation(length, 0f, 0f);
                    length += LENGTH_OF_I*2;
                    break;
                case ('V'):
                    temp = romanVBox();
                    temp.setLocalTranslation(length, 0f, 0f);
                    length += LENGTH_OF_V*2;
                    break;
                case ('X'):
                    temp = romanXBox();
                    temp.setLocalTranslation(length, 0f, 0f);
                    length += LENGTH_OF_X*2;
                    break;
                case ('L'):
                    temp = romanLBox();
                    temp.setLocalTranslation(length, 0f, 0f);
                    length += LENGTH_OF_L*2;
                    break;
                case ('C'):
                    temp = romanDBox();
                    temp.setLocalTranslation(length, 0f, 0f);
                    length += LENGTH_OF_D*2;
                    break;
                default:
                    break;
            }
            if (temp != null) {
                this.attachChild(temp);
            }
        }
       
 
        this.setShadowMode(ShadowMode.Off);
        
    }
    
    private static void initMaterials(AssetManager assetManager) {
        Texture textureForI = assetManager.loadTexture("Textures/RomanNumerals/RomanItest.jpg");
        Texture textureForV = assetManager.loadTexture("Textures/RomanNumerals/RomanVtest.jpg");
        Texture textureForX = assetManager.loadTexture("Textures/RomanNumerals/RomanXtest.jpg");
        Texture textureForL = assetManager.loadTexture("Textures/RomanNumerals/RomanLtest.jpg");
        Texture textureForD = assetManager.loadTexture("Textures/RomanNumerals/RomanDtest.jpg");
        
        textureForI.setWrap(Texture.WrapMode.EdgeClamp);
        textureForV.setWrap(Texture.WrapMode.EdgeClamp);
        textureForX.setWrap(Texture.WrapMode.EdgeClamp);
        textureForL.setWrap(Texture.WrapMode.EdgeClamp);
        textureForD.setWrap(Texture.WrapMode.EdgeClamp);
                
        materialForI = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialForI.setTexture("DiffuseMap", textureForI);
        materialForV = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialForV.setTexture("DiffuseMap", textureForV);
        materialForX = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialForX.setTexture("DiffuseMap", textureForX);
        materialForL = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialForL.setTexture("DiffuseMap", textureForL);
        materialForD = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialForD.setTexture("DiffuseMap", textureForD);
    }
    
    private Geometry romanIBox() {
        Box model =
            new Box(new Vector3f(LENGTH_OF_I,0,-P.platformWidth/2-P.playerZOffset), LENGTH_OF_I, 1f, 0.1f);
        Geometry geometry = new Geometry("",model);
        geometry.setMaterial(materialForI);
        return geometry;
    }
    
    private Geometry romanVBox() {
        Box model =
            new Box(new Vector3f(LENGTH_OF_V,0,-P.platformWidth/2-P.playerZOffset), LENGTH_OF_V, 1f, 0.1f);
        Geometry geometry = new Geometry("",model);
        geometry.setMaterial(materialForV);
        return geometry;
    }
    
    private Geometry romanXBox() {
        Box model =
            new Box(new Vector3f(LENGTH_OF_X,0,-P.platformWidth/2-P.playerZOffset), LENGTH_OF_X, 1f, 0.1f);
        Geometry geometry = new Geometry("",model);
        geometry.setMaterial(materialForX);
        return geometry;
    }
    
    private Geometry romanDBox() {
        Box model =
            new Box(new Vector3f(LENGTH_OF_D,0,-P.platformWidth/2-P.playerZOffset), LENGTH_OF_D, 1f, 0.1f);
        Geometry geometry = new Geometry("",model);
        geometry.setMaterial(materialForD);
        return geometry;
    }
    
    private Geometry romanLBox() {
        Box model =
            new Box(new Vector3f(LENGTH_OF_L,0,-P.platformWidth/2-P.playerZOffset), LENGTH_OF_L, 1f, 0.1f);
        Geometry geometry = new Geometry("",model);
        geometry.setMaterial(materialForL);
        return geometry;
    }
    
}
