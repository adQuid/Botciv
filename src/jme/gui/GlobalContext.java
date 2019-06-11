package jme.gui;

import game.Tile;

//Proof Redux is dumb
public class GlobalContext {

	public static boolean waitingForPlayers = false;
	
	public static Tile selectedTile = null;
	
	
	public static void clear() {
		selectedTile = null;
		waitingForPlayers = false;
	}
}
