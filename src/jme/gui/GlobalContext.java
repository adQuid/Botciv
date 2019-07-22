package jme.gui;

import game.Tile;
import jme.gui.mapActions.ZoomMap;
import jme.gui.mouseactions.ScrollAction;

//Proof Redux is dumb
public class GlobalContext {

	public static boolean waitingForPlayers = false;
	
	public static Tile selectedTile = null;
		
	public static ScrollAction scrollAction = new ZoomMap();
	
	public static void clear() {
		selectedTile = null;
		waitingForPlayers = false;
	}
}
