package gui;

import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.google.gson.Gson;

import aibrain.Action;
import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import game.Unit;
import game.World;
import layout.TableLayout;
import map.Coordinate;
import map.MainUIMapDisplay;

public class MainUI {

	public static int visionDistance = 3;
	public static JFrame GUI = new JFrame("Botciv");
	public static JPanel displayPanel = new JPanel();
	public static JPanel detailPanel = new JPanel();
	public static JPanel cornerPanel = new JPanel();
	public static JPanel bottomPanel = new JPanel();
	
	//should this be here?
	private static BotcivGame activeGame;
	private static BotcivPlayer playingAs; 
	private static BotcivGame imageGame;
	private static List<Action> actionsThisTurn = new ArrayList<Action>();
	
	public static void setupGUI(BotcivGame game, BotcivPlayer player) {
	
		activeGame = game;
		playingAs = player;
		imageGame = (BotcivGame) activeGame.imageForPlayer(playingAs);
		
		SideDisplay.setupDetailUI();
		CornerDisplay.setup();
		BottomDisplay.setup();
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		  .addKeyEventDispatcher(new KeyEventDispatcher() {
		      @Override
		      public boolean dispatchKeyEvent(KeyEvent e) {
		    	  	if(e.getID() != KeyEvent.KEY_PRESSED) {
		    	  		return false;
		    	  	}
		    	  	if(e.getKeyCode() == KeyEvent.VK_UP) {
		    	  		if(MainUIMapDisplay.focus.y > -5) {
		    	  			for(int i = 0; i <= MainUI.visionDistance/10; i++) {
		    	  				MainUIMapDisplay.focus = MainUIMapDisplay.focus.up();
		    	  			}
		    	  			MainUIMapDisplay.repaintDisplay();
		    	  		}
					}
					if(e.getKeyCode() == KeyEvent.VK_DOWN) {
						if(MainUIMapDisplay.focus.y+MainUI.visionDistance < activeGame.world.WORLD_SIZE + 5) {
							for(int i = 0; i <= MainUI.visionDistance/10; i++) {
								MainUIMapDisplay.focus = MainUIMapDisplay.focus.down();
							}
							MainUIMapDisplay.repaintDisplay();
						}
					}
					if(e.getKeyCode() == KeyEvent.VK_LEFT) {
						for(int i = 0; i <= MainUI.visionDistance/10; i++) {
							MainUIMapDisplay.focus = MainUIMapDisplay.focus.left();
						}
						MainUIMapDisplay.repaintDisplay();
					}
					if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
						for(int i = 0; i <= MainUI.visionDistance/10; i++) {
							MainUIMapDisplay.focus = MainUIMapDisplay.focus.right();
						}
						MainUIMapDisplay.repaintDisplay();
					}
		        return false;
		      }
		});
		
		displayPanel.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(arg0.getWheelRotation() > 0) {
					if(visionDistance < activeGame.world.WORLD_SIZE) {
						visionDistance+=2;
						MainUIMapDisplay.focus = MainUIMapDisplay.focus.up().left();
					}
				}else if(visionDistance > 4){
					visionDistance-=2;
					MainUIMapDisplay.focus = MainUIMapDisplay.focus.down().right();
				}
				MainUIMapDisplay.repaintDisplay();
			}			
		});
				
		//resize on dragging the window
		GUI.addComponentListener(new ComponentAdapter( ) {
			public void componentResized(ComponentEvent ev) {
			  MainUIMapDisplay.resizeDisplay();
			}
		});
		
		MainUIMapDisplay.paintDisplay();
		
		double[][] size = {{0.8,0.2},{0.75,0.25}};
		GUI.setLayout(new TableLayout(size));
		GUI.add(displayPanel,"0,0");
		GUI.add(detailPanel,"1,0");
		GUI.add(bottomPanel,"0,1");
		GUI.add(cornerPanel,"1,1");
		
		GUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		GUI.pack();
		GUI.setVisible(true);
	}
	
	public static void clearUI() {
		SideDisplay.focusOnTile(getGame().world.getTileAt(MainUIMapDisplay.focus));		
		BottomDisplay.focusOnTile(getGame().world.getTileAt(MainUIMapDisplay.focus));
	}
	
	public static BotcivGame getGame() {
		return imageGame;
	}
	
	public static BotcivPlayer getPlayer() {
		return imageGame.playerByName(playingAs.getName());
	}
	
	public static Unit findMatching(Unit unit) {
		Tile tile = activeGame.world.getTileAt(unit.getLocation().getCoordinate());
		
		List<Unit> matchingUnitList = tile.getUnits().get(unit.getType());
		
		if(matchingUnitList == null) {
			return null;
		}
		
		for(Unit matchingUnit: matchingUnitList) {
			if(matchingUnit.matches(unit)) {
				return matchingUnit;
			}
		}
		System.err.println("Failed to find matching unit!");
		return null;
	}
	
	public static void saveGame() {
		Map<String,Object> saveState = activeGame.saveString();
		Gson gson = new Gson();
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("saves/test.savegam"));
			writer.write(gson.toJson(saveState));
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void commitTurn() {
		activeGame.setActionsForPlayer(actionsThisTurn, playingAs);
		actionsThisTurn.clear();
		activeGame.endRound();
		imageGame = (BotcivGame)activeGame.imageForPlayer(playingAs);		
		MainUIMapDisplay.repaintDisplay();
		BottomDisplay.resetDescription();
	}
	
	public static void addAction(Action action) {
		actionsThisTurn.add(action);
	}
	
	public static void clearTurn() {
		actionsThisTurn.clear();
		imageGame = (BotcivGame)activeGame.imageForPlayer(playingAs);		
		MainUIMapDisplay.repaintDisplay();
	}
	
}
