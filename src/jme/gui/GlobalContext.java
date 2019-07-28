package jme.gui;

import game.Tile;
import jme.gui.mapActions.ZoomMap;
import jme.gui.mouseactions.ScrollAction;
import map.Coordinate;

//Proof Redux is dumb
public class GlobalContext {

	public static boolean waitingForPlayers = false;
	
	public static Coordinate selectedCoord = null;
	public static Tile getSelectedTile() {
		return MainUI.getGame().world.getTileAt(selectedCoord);
	}
	
	public static ScrollAction scrollAction = new ZoomMap();
	
	public static void clear() {
		selectedCoord = null;
		waitingForPlayers = false;
		scrollAction = new ZoomMap();
	}
}
