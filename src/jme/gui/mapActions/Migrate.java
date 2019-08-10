package jme.gui.mapActions;

import game.Unit;
import game.actions.MigrateUnit;
import jme.gui.GlobalContext;
import jme.gui.MainUI;
import map.Coordinate;
import jme.gui.mouseactions.MapAction;

public class Migrate extends MapAction{

	@Override
	public void doAction(Coordinate coord) {
		Unit unit = GlobalContext.getSelectedUnit();
		//if we can't find a matching unit, we will assume that it's already moved
		if(MainUI.getGame().findMatching(unit) != null &&
				MainUI.getGame().world.rangeBetween(unit.getLocation().getCoordinate(), coord) <= 1) {
			MainUI.addAction(new MigrateUnit(MainUI.getGame().findMatching(unit), coord));
			new MigrateUnit(MainUI.getGame().findMatching(unit), coord).doAction(MainUI.getGame(), MainUI.getPlayer());
		} 
		GlobalContext.clickAction = new FocusOnTile();
		GlobalContext.setSelectedUnit(null);
		new FocusOnTile().doAction(unit.getLocation().getCoordinate());
	}
}
