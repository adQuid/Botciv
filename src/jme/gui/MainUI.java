package jme.gui;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioListenerState;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

import aibrain.Action;
import controller.Controller;
import controller.Settings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;

import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import jme.gui.components.BasicBottomPanels;
import jme.gui.components.BasicNifty;
import jme.gui.components.CornerDisplay;
import jme.gui.components.DescriptionDisplay;
import jme.gui.components.RightPanel;
import jme.gui.mapActions.FocusOnTile;
import map.Coordinate;

public class MainUI extends SimpleApplication{

	public static MainUI instance;
	public static BasicNifty nifty = new BasicNifty();
	
	private static final int MAP_SIZE = 100;
	public static float camHeight = 0.2f;
	public static final float REDRAW_BOUNDRY = 14.0f;

	private static Vector2f lastRedraw;

	private static BotcivGame imageGame;
	private static BotcivPlayer playingAs;
	private static List<Action> actionsThisTurn = new ArrayList<Action>();
	public static Coordinate focus = new Coordinate(0,0);

	int widthMult = 3;
	TileToken[][] map = new TileToken[(int)(MAP_SIZE*widthMult)][MAP_SIZE];    

	private long lastInput = 0L;
	private static int MIN_INPUT_INTERVAL = 20;
	private boolean hasUpdatedResourceDisplays = false;

	private static String[] analogMappings = new String[] {"RIGHT", "UP",  "LEFT", "DOWN", "ZOOM IN", "ZOOM OUT"};
	private static String[] actionMappings = new String[] {"QUIT", "CLICK"};

	public MainUI() {
		super(new AudioListenerState());
	}

	public static void setupGUI(BotcivPlayer player, boolean testing) {

		playingAs = player;
		imageGame = Controller.instance.getImageGame(playingAs);
		focus = player.getLastFocus();

		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		DisplayMode[] modes = device.getDisplayModes();
				
		AppSettings appSettings = new AppSettings(true);
		appSettings.setWidth(modes[modes.length-1].getWidth());
		appSettings.setHeight(modes[modes.length-1].getHeight());
		appSettings.setFullscreen(!Settings.debug());
		appSettings.setSamples(1);
		
		if(!testing) {
			instance = new MainUI();
			instance.stateManager.attach(nifty);
			instance.setSettings(appSettings);
			instance.setShowSettings(Settings.debug());
			instance.start();
		}        
	}	

	@Override
	public void simpleInitApp() {

		assetManager.registerLocator("assets", FileLocator.class);
		TileToken.setup(assetManager);

		cam.setLocation(new Vector3f(focus.x + 0.25f,1.2f,focus.y + 0.9f));
		tiltCamera();

		inputManager.clearMappings();//Apparently application start with mappings
		inputManager.addMapping("QUIT", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addMapping("LEFT", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("RIGHT", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("UP", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("DOWN", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("ZOOM IN", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addMapping("ZOOM OUT", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		//Because of the hover layer, click actions can't be mapped here

		inputManager.addListener(actionListener, actionMappings);
		inputManager.addListener(analogListener, analogMappings);

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

	public static void clearUI() {
		
	}
	
	public static BotcivGame getGame() {
		return imageGame;
	}
	
	public static BotcivPlayer getPlayer() {
		return imageGame.playerByName(playingAs.getName());
	}
				
	public static void addAction(Action action) {
		actionsThisTurn.add(action);
	}
	
	public static void commitTurn() {
		GlobalContext.waitingForPlayers = true;
		if(instance != null) {
			Element bottomHolder = nifty.getCurrentScreen().findElementById(BasicBottomPanels.BOTTOM_BUTTON_LABEL);
			nifty.removeChildren(bottomHolder);
			updateBottomPanel(BasicBottomPanels.waitingForTurn());
			updateSidePanel(RightPanel.waitingForTurn());
		}		
		Controller.instance.commitTurn(actionsThisTurn, playingAs);
		actionsThisTurn.clear();
	}
	
	public static void clearTurn() {
		actionsThisTurn.clear();
		imageGame = Controller.instance.getImageGame(playingAs);	
		updateGameDisplay();
	}
	
	@Override
	public void destroy() { 
		System.exit(0);
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
		
		if(!hasUpdatedResourceDisplays) {
			updateResourceDisplays();
			hasUpdatedResourceDisplays = true;
		}
		
	}

	private void redraw(boolean selective) {
		rootNode.detachAllChildren();
		for(int x = 0; x < MAP_SIZE*widthMult; x++){
			for(int y = 0; y < MAP_SIZE; y++){
				if(!selective || 
						(Math.abs(cam.getLocation().x - x) < 10 && Math.abs(cam.getLocation().z - y) < 10)) {
					map[x][y].draw(imageGame.world.getTileAt(new Coordinate(x,y)),assetManager);

					Spatial wall = map[x][y].getSpatial(assetManager);

					wall.setLocalTranslation(x,-2.0f,y);
					/* This quaternion stores a 90 degree rolling rotation */
					Quaternion roll = new Quaternion();
					roll.fromAngleAxis( FastMath.PI/2.0f, new Vector3f(0,1,0) );
					/* The rotation is applied: The object rolls by 180 degrees. */
					wall.setLocalRotation(roll);
								
					rootNode.attachChild(wall);
				}
			}
		}  
	}

	private void tiltCamera(){
		float tiltFactor = 0.25f / (0.5f+(cam.getLocation().y/2));
		cam.setRotation(new Quaternion(0.0f,0.7f + tiltFactor,-0.7f,0.0f));
	}

	private final ActionListener actionListener = new ActionListener() {
		@Override
		public void onAction(String name, boolean keyPressed, float tpf) {
			if (name.equals("QUIT")) {
				System.exit(0);
			}
		}
	};

	private final AnalogListener analogListener = new AnalogListener() {
		@Override
		public void onAnalog(String name, float value, float tpf) {
			if(System.currentTimeMillis() - lastInput < MIN_INPUT_INTERVAL) {
				return;
			}
			lastInput = System.currentTimeMillis();

			Vector3f pos = cam.getLocation().clone();
			float moveSpeed = 0.05f + (0.02f * (float)Math.pow(pos.y,1.2));

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
				ButtonActions.actions.scroll(1);
			} 
			if (name.equals("ZOOM OUT")) {
				ButtonActions.actions.scroll(-1);
			}

			camHeight = cam.getLocation().y;
			if(camHeight > REDRAW_BOUNDRY && !zoomedOut) {
				redraw(false);
			}
			if(camHeight < REDRAW_BOUNDRY && (zoomedOut
					|| lastRedraw == null
					|| new Vector2f(cam.getLocation().x, cam.getLocation().z).distance(lastRedraw)>4.0f)) {
				lastRedraw = new Vector2f(cam.getLocation().x,cam.getLocation().z);
				redraw(true);
			}
		}
	};

	public void click() {
		Geometry geo = findClickedObject();
		
		for(TileToken[] row: map) {
			for(TileToken token: row) {
				if(token.geo == geo) {
					Coordinate coord = token.tile.getCoordinate();
					new FocusOnTile().doAction(coord);
				}
			}
		}
	}
	
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

	public static void newTurn() {
		imageGame = Controller.instance.getImageGame(playingAs);
		GlobalContext.clear();
		RightPanel.defaultRightPanel();
		
		if(instance != null) {
			updateBottomPanel(BasicBottomPanels.onYourTurn());
		}
		
		updateGameDisplay();
	}
	
	public static void updateGameDisplay() {
		if(instance != null) {
			instance.enqueue(new Callable<Void>() {
				public Void call() throws Exception {
					if(instance.cam.getLocation().clone().getY() < REDRAW_BOUNDRY) {
						instance.redraw(true);
					} else {
						instance.redraw(false);
					}
					return null;	            
				}
			});
			updateResourceDisplays();
			updateSidePanel();
		}
	}
	
	public void zoom(int amount) {
		Vector3f pos = cam.getLocation().clone();

		Geometry geo = findClickedObject();
		
		if (amount > 0) {
			if(geo != null){
				pos.set(pos.x * 0.7f + geo.getLocalTranslation().x * 0.3f, Math.max(pos.y / 1.25f,0.2f), pos.z * 0.7f + geo.getLocalTranslation().z * 0.3f + 0.1f);
				cam.setLocation(pos);
			}
		} 
		if (amount < 0) {
			pos.set(pos.x, Math.min((pos.y * 1.25f) + 0.05f, MAP_SIZE*1.15f), pos.z);
			cam.setLocation(pos);
		}
	}
	
	private static void updateResourceDisplays() {
		Element elementToFill = MainUI.nifty.getCurrentScreen().findElementById(CornerDisplay.LABOR);
	    elementToFill.getRenderer(TextRenderer.class).setText((int)getGame().players.get(0).getLabor()+"");
	    elementToFill = MainUI.nifty.getCurrentScreen().findElementById(CornerDisplay.MATERIALS);
	    elementToFill.getRenderer(TextRenderer.class).setText((int)getGame().players.get(0).getMaterials()+"");
	    elementToFill = MainUI.nifty.getCurrentScreen().findElementById(CornerDisplay.WEALTH);
	    elementToFill.getRenderer(TextRenderer.class).setText((int)getGame().players.get(0).getWealth()+"");
	    elementToFill = MainUI.nifty.getCurrentScreen().findElementById(CornerDisplay.INFLUENCE);
	    elementToFill.getRenderer(TextRenderer.class).setText((int)getGame().players.get(0).getInfluence()+"");
	    elementToFill = MainUI.nifty.getCurrentScreen().findElementById(CornerDisplay.EDUCATION);
	    elementToFill.getRenderer(TextRenderer.class).setText((int)getGame().players.get(0).getEducation()+"");
	}
	
	public static void updateBottomPanel(PanelBuilder newPanel) {
		Element bottomHolder = nifty.getCurrentScreen().findElementById(BasicBottomPanels.BOTTOM_BUTTON_LABEL);
		nifty.removeChildren(bottomHolder);
		newPanel.build(nifty.niftyDisplay.getNifty(), nifty.getCurrentScreen(), bottomHolder);
		nifty.cleanup();
	}
	
	public static void updateSidePanel() {
		updateSidePanel(RightPanel.currentPanel());
	}
	
	public static void updateSidePanel(PanelBuilder newPanel) {
		Element sideHolder = nifty.getCurrentScreen().findElementById("Right_Panel_Holder");
		nifty.removeChildren(sideHolder);
		newPanel.build(nifty.niftyDisplay.getNifty(), nifty.getCurrentScreen(), sideHolder);
		nifty.cleanup();
	}
}