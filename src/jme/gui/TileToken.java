/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jme.gui;

import java.util.HashMap;
import java.util.Map;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Texture.ShadowCompareMode;
import com.jme3.texture.plugins.AWTLoader;

import game.Tile;
import game.TileType;
import util.ImageUtilities;

/**
 *
 * @author b
 */
public class TileToken{
    
    private Tile tile;
    public Box box = null;
    public Geometry geo;
    public Material material;
    
    boolean rolledForest = false;
    
    static AWTLoader loader = new AWTLoader();
    
    private static Map<TileType,Material> preloadedImages = new HashMap<TileType,Material>();
    
    public static void setup(AssetManager assetManager) {
    	for(TileType current: TileType.TYPES.values()) {
    		Texture texture = new Texture2D();
    		texture.setImage(loader.load(ImageUtilities.importImage(current.getImage()),true));
    		Material mat_brick = new Material(
    		        assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    		mat_brick.setTexture("ColorMap", texture);
    		preloadedImages.put(current, mat_brick);
    	}
    }
    
    public TileToken(Tile tile, AssetManager assetManager, String name) {
        this.tile = tile;

        draw(assetManager);
    }
    
    public TileToken(TileToken other, AssetManager assetManager){
    	
        this.tile = other.tile;
        
        draw(assetManager);
    }
    
    public void draw(Tile tile, AssetManager assetManager) {
    	this.tile = tile;
    	draw(assetManager);
    }
    
    public void draw(AssetManager assetManager) {        
                
        if(MainUI.camHeight > MainUI.REDRAW_BOUNDRY) {
        	material = preloadedImages.get(tile.getType());
        } else {         	
        	Texture texture = new Texture2D();
        	texture.setImage(loader.load(tile.image(100, 100),false));
        	material = new Material(
    		        assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        	material.setTexture("ColorMap", texture);
        }
       
        if(box == null) {
        	box = new Box(0.5f,Math.max(1.0f + (0.0003f * tile.getAltitude()),1.00f),0.5f);
             
        	this.geo = new Geometry("name", box);
        }
    }
        
    public Spatial getSpatial(AssetManager assetManager) {    	
        Spatial retval = geo;
        
        retval.setMaterial(material);
        
        return retval;
    }
    
}
