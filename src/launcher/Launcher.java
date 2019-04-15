package launcher;

import game.TileType;
import game.UnitType;
import gui.MainUI;

public class Launcher {

	public static void main(String[] args) {
		UnitType.loadData();
		TileType.loadData();
		MainUI.setupGUI();
	}
	
}
