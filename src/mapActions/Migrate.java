package mapActions;

import game.Unit;
import game.actions.MigrateUnit;
import gui.MainUI;
import map.Coordinate;
import map.MainUIMapDisplay;

public class Migrate implements MapAction{

	@Override
	public void doAction(Coordinate coord) {
		Unit unit = MapActionGlobalStore.selectedUnit;
		//if we can't find a matching unit, we will assume that it's already moved
		if(MainUI.getGame().findMatching(unit) != null &&
				MainUI.getGame().world.rangeBetween(unit.getLocation().getCoordinate(), coord) <= 1) {
			MainUI.addAction(new MigrateUnit(MainUI.getGame().findMatching(unit), coord));
			MapActionGlobalStore.selectedUnit.getLocation().removeUnit(unit);
			MainUI.getGame().world.getTileAt(coord).addUnit(unit);
			MainUIMapDisplay.repaintDisplay();		
		} 
		new FocusOnTile().doAction(unit.getLocation().getCoordinate());
	}
}
