package mapActions;

import game.Tile;
import gui.BottomDisplay;
import gui.MainUI;
import gui.SideDisplay;
import map.Coordinate;
import map.MainUIMapDisplay;

public class FocusOnTile implements MapAction{

	@Override
	public void doAction(Coordinate coord) {
		Tile select = MainUI.getGame().world.getTileAt(coord);
		select.setSelected(true);
		MainUIMapDisplay.repaintDisplay();
		SideDisplay.focusOnTile(MainUI.getGame().world.getTileAt(coord));
		BottomDisplay.focusOnTile(select);
	}
}
