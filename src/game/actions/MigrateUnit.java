package game.actions;

import aibrain.Action;
import game.BotcivGame;
import game.BotcivPlayer;
import game.ResourcePortfolio;
import game.Unit;
import map.Coordinate;
import util.GameLogicUtilities;

public class MigrateUnit extends BotcivAction{

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

	@Override
	public void doAction(BotcivGame game, BotcivPlayer player) {
		if(game.world.rangeBetween(unit.getLocation().getCoordinate(), location) <= 1 &&
				GameLogicUtilities.tryTopay(player, 
						new ResourcePortfolio("{I:10}"))) {
			game.world.getTileAt(unit.getLocation().getCoordinate()).removeUnit(unit);
			game.world.getTileAt(location).addUnit(unit);
			player.addExploredTile(location);
		}
		
	}

}
