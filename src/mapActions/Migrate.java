package mapActions;

import game.Unit;
import game.actions.MigrateUnit;
import gui.MainUI;
import map.Coordinate;
import map.MainUIMapDisplay;

public class Migrate implements MapAction{

	@Override
	public void doAction(Coordinate coord) {
		MainUI.addAction(new MigrateUnit(MapActionGlobalStore.selectedUnit, coord));
		MapActionGlobalStore.selectedUnit.getLocation().removeUnit(MapActionGlobalStore.selectedUnit);
		MainUI.getGame().world.getTileAt(coord).addUnit(MapActionGlobalStore.selectedUnit);
		MainUIMapDisplay.repaintDisplay();
		new FocusOnTile().doAction(coord);
	}
}
