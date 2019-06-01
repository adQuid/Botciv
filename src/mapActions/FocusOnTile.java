package mapActions;

import game.Tile;
import gui.BottomDisplay;
import jme.gui.MainUI;
import gui.SideDisplay;
import map.Coordinate;

public class FocusOnTile implements MapAction{

	@Override
	public void doAction(Coordinate coord) {
		Tile select = MainUI.getGame().world.getTileAt(coord);
		if(select != null) {
			select.setSelected(true);
			MainUI.updateGameDisplay();
			SideDisplay.focusOnTile(MainUI.getGame().world.getTileAt(coord));
			
		}
	}
}
