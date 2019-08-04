package game.actions;

import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import game.Unit;

public class SplitUnit extends BotcivAction{

	Unit unit;
	int splitSize;
	
	public SplitUnit(Unit unit, int splitSize) {
		this.unit = unit;
		this.splitSize = splitSize;
	}
		
	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof SplitUnit)) {
			return false;
		}
		SplitUnit castOther = (SplitUnit)other;
		return unit.equals(castOther.unit) && splitSize == castOther.splitSize;
	}
	
	@Override
	public boolean isContingency() {
		return false;
	}

	@Override
	public void doAction(BotcivGame game, BotcivPlayer player) {
		unit = game.findMatching(unit);
		Tile location = unit.getLocation();
		Unit newUnit = unit.split(splitSize, game);
		location.addSplitUnit(newUnit, game);		
	}

	@Override
	public int priority() {
		return 0;
	}
}
