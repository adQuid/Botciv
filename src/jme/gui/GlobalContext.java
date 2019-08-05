package jme.gui;

import game.Tile;
import game.Unit;
import jme.gui.mapActions.FocusOnTile;
import jme.gui.mapActions.ZoomMap;
import jme.gui.mouseactions.ClickAction;
import jme.gui.mouseactions.ScrollAction;
import map.Coordinate;

//Proof Redux is dumb
public class GlobalContext {

	public static boolean waitingForPlayers = false;
	
	public static Coordinate selectedCoord = null;
	public static Tile getSelectedTile() {
		return MainUI.getGame().world.getTileAt(selectedCoord);
	}
	
	private static Unit selectedUnit = null;
	public static Unit getSelectedUnit() {
		if(selectedUnit == null) {
			return null;
		}
		return MainUI.getGame().findMatching(selectedUnit);
	}
	public static void setSelectedUnit(Unit unit) {
		selectedUnit = unit;
	}
	
	public static ScrollAction scrollAction = new ZoomMap();
	public static ClickAction clickAction = new FocusOnTile();
	
	public static class DisplayType{
		
		public String name;
		
		public static DisplayType units = new DisplayType("Units");
		public static DisplayType trade = new DisplayType("Trade");
		
		private DisplayType(String name) {
			this.name = name;
		}
	}
	public static DisplayType displayType = DisplayType.units;
	
	public static void clear() {
		selectedCoord = null;
		waitingForPlayers = false;
		scrollAction = new ZoomMap();
	}
}
