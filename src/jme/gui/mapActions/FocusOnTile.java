package jme.gui.mapActions;

import game.Tile;
import jme.gui.MainUI;
import jme.gui.components.BasicBottomPanels;
import map.Coordinate;

public class FocusOnTile implements MapAction{

	@Override
	public void doAction(Coordinate coord) {
		Tile select = MainUI.getGame().world.getTileAt(coord);
		if(select != null) {
			MainUI.getGame().world.clearSelections();
			select.setSelected(true);
			MainUI.updateGameDisplay();
			MainUI.updateBottomPanel(BasicBottomPanels.focusOnTile(select));
		}
	}
}
