package jme.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.audio.AudioListenerState;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;

import de.lessvoid.nifty.Nifty;
import game.BotcivGame;
import game.BotcivPlayer;
import game.TileType;
import game.UnitType;
import game.World;
import launcher.Launcher;
import map.Coordinate;

/** Sample 3 - how to load an OBJ model, and OgreXML model,
 * a material/texture, or text. */
public class MainUI extends SimpleApplication{

    private static final int MAP_SIZE = 100;
    public static float camHeight = 0.2f;
    public static final float REDRAW_BOUNDRY = 10.0f;
    
    private static BotcivGame imageGame;
    private static BotcivPlayer playingAs;
    public static Coordinate focus = new Coordinate(0,0);
        
    float widthMult = 3.0f;
    TileToken[][] map = new TileToken[(int)(MAP_SIZE*widthMult)][MAP_SIZE];    
    
    private long lastInput = 0L;
    
    private static String[] mappings = new String[] {
            "RIGHT", "UP",  "LEFT", "DOWN", "ZOOM IN", "ZOOM OUT", "CLICK" };

     private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("QUIT")) {
               System.exit(0);
            }
            if (name.equals("CLICK") && keyPressed) {
            	Geometry geo = findClickedObject();
            	                              
            }
        }
    };

    private final AnalogListener analogListener = new AnalogListener() {
    	@Override
    	public void onAnalog(String name, float value, float tpf) {

    		if(System.currentTimeMillis() - lastInput < 100) {
    			return;
    		}
    		lastInput = System.currentTimeMillis();
    		
    		Vector3f pos = cam.getLocation().clone();
    		float moveSpeed = 0.03f + (0.01f * (float)Math.pow(pos.y,1.5));

        	Geometry geo = findClickedObject();
    		boolean zoomedOut = camHeight > REDRAW_BOUNDRY;
    		
    		if (name.equals("RIGHT")) {

    			pos.set(pos.x + moveSpeed, pos.y, pos.z);
    			cam.setLocation(pos);
    		}
    		if (name.equals("LEFT")) {
    			pos.set(pos.x - moveSpeed, pos.y, pos.z);
    			cam.setLocation(pos);
    		} 
    		if (name.equals("UP")) {
    			pos.set(pos.x, pos.y, Math.min(pos.z + moveSpeed,MAP_SIZE - 0.5f));
    			cam.setLocation(pos);
    		} 
    		if (name.equals("DOWN")) {
    			pos.set(pos.x, pos.y, Math.max(pos.z - moveSpeed,-50.5f));
    			cam.setLocation(pos);
    		} 
    		if (name.equals("ZOOM IN")) {
    			if(geo != null){
    				pos.set(pos.x * 0.7f + geo.getLocalTranslation().x * 0.3f, Math.max(pos.y / 1.25f,0.2f), pos.z * 0.7f + geo.getLocalTranslation().z * 0.3f + 0.1f);
    				cam.setLocation(pos);
    			}
    		} 
    		if (name.equals("ZOOM OUT")) {
    			pos.set(pos.x, Math.min((pos.y * 1.25f) + 0.05f, MAP_SIZE*1.15f), pos.z);
    			cam.setLocation(pos);
    			
    		}
    		
			camHeight = cam.getLocation().y;
			if(camHeight > REDRAW_BOUNDRY && !zoomedOut) {
				redraw(false);
			}
			if(camHeight < REDRAW_BOUNDRY) {
				redraw(true);
			}
    	}
    };

    private Geometry findClickedObject() {
        // Reset results list.
        CollisionResults results = new CollisionResults();
        // Convert screen click to 3d position
                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                // Aim the ray from the clicked spot forwards.
                Ray ray = new Ray(click3d, dir);
                // Collect intersections between ray and all nodes in results list.
                rootNode.collideWith(ray, results);

                if (results.size() > 0) {
                    return results.getCollision(0).getGeometry();
                } else {
                    return null;
                }
    }
    
    public MainUI(BotcivGame game) {
        super(new StatsAppState(),new AudioListenerState());
        this.imageGame = game;
    }
    
    @Override
    public void destroy() { 
    	System.exit(0);
    }
    
    @Override
    public void simpleInitApp() {
       
    	TileToken.setup(assetManager);
    	
        cam.setLocation(new Vector3f(focus.x + 0.25f,1.2f,focus.y + 0.9f));
        tiltCamera();
        
        inputManager.addMapping("QUIT", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("LEFT", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("RIGHT", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("UP", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("DOWN", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("ZOOM IN", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("ZOOM OUT", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("CLICK", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addListener(actionListener, mappings);
        inputManager.addListener(analogListener, mappings);
        
        for(int x = 0; x < MAP_SIZE; x++){
            for(int y = 0; y < MAP_SIZE; y++){
                map[x][y] = new TileToken(imageGame.world.getTileAt(new Coordinate(x,y)),assetManager,"Box");
            }
        }
        
        for(int x = MAP_SIZE; x < MAP_SIZE*widthMult; x++){
            for(int y = 0; y < MAP_SIZE; y++){
                map[x][y] = new TileToken(map[x%MAP_SIZE][y],assetManager);
            }
        }
        
       redraw(false);
    }
        
    @Override
    public void simpleUpdate(float tpf) {
        if(cam.getLocation().x < 1.0f * MAP_SIZE){
            cam.setLocation(cam.getLocation().add(new Vector3f(MAP_SIZE,0,0)));
        }
        if(cam.getLocation().x > 2.0f * MAP_SIZE){
            cam.setLocation(cam.getLocation().add(new Vector3f(-MAP_SIZE,0,0)));
        }
        camHeight = cam.getLocation().y;
                
        tiltCamera();
    }
    
    private void tiltCamera(){
        float tiltFactor = 0.25f / (0.5f+(cam.getLocation().y/2));
        cam.setRotation(new Quaternion(0.0f,0.7f + tiltFactor,-0.7f,0.0f));
    }

    private void redraw(boolean selective) {
    	rootNode.detachAllChildren();
        for(int x = 0; x < MAP_SIZE*widthMult; x++){
            for(int y = 0; y < MAP_SIZE; y++){
            	if(!selective || 
            			(Math.abs(cam.getLocation().x - x) < 10 && Math.abs(cam.getLocation().z - y) < 10)) {
            		map[x][y].draw(assetManager);

            		Spatial wall = map[x][y].getSpatial(assetManager);

            		wall.setLocalTranslation(x,-2.0f,y);
            		/* This quaternion stores a 90 degree rolling rotation */
            		Quaternion roll = new Quaternion();
            		roll.fromAngleAxis( FastMath.PI/2, new Vector3f(0,1,0) );
            		/* The rotation is applied: The object rolls by 180 degrees. */
            		wall.setLocalRotation( roll );

            		rootNode.attachChild(wall);
            	}
            }
        }  
    }
    
	public static void setupGUI(BotcivPlayer player, boolean testing) {
       
		playingAs = player;
		focus = player.getLastFocus();
		
        if(!testing) {
    		MainUI app = new MainUI(Launcher.loadGame());
            BasicNifty nifty = new BasicNifty();
            app.stateManager.attach(nifty);
            app.start();
        }        
	}	
}