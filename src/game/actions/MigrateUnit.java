package game.actions;

import aibrain.Action;
import game.Unit;
import map.Coordinate;

public class MigrateUnit implements Action{

	Unit unit;
	Coordinate location;
	
	public MigrateUnit(Unit unit, Coordinate location) {
		this.unit = unit;
		this.location = location;
	}
	
	@Override
	public boolean isContingency() {
		return false;
	}

}
