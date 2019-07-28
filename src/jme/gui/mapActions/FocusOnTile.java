package jme.gui.mapActions;

import game.Tile;
import jme.gui.MainUI;
import jme.gui.components.BasicBottomPanels;
import jme.gui.components.RightPanel;
import jme.gui.components.TileFocusBottomPanels;
import jme.gui.mouseactions.MapAction;
import map.Coordinate;

public class FocusOnTile extends MapAction{

	@Override
	public void doAction(Coordinate coord) {
		Tile select = MainUI.getGame().world.getTileAt(coord);
		if(select != null) {
			MainUI.getGame().world.clearSelections();
			select.setSelected(true);
			MainUI.updateGameDisplay();
			MainUI.updateBottomPanel(TileFocusBottomPanels.focusOnTile(select));
			MainUI.updateSidePanel(RightPanel.tileFocusRightPanel(select));
		}
	}

}
