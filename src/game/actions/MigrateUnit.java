package game.actions;

import aibrain.Action;
import game.BotcivGame;
import game.BotcivPlayer;
import game.ResourcePortfolio;
import game.Tile;
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
	public boolean equals(Object other) {
		if(other == null || !(other instanceof MigrateUnit)) {
			return false;
		}
		MigrateUnit castOther = (MigrateUnit)other;
		return unit.equals(castOther.unit) && location.equals(castOther.location);
	}
	
	@Override
	public boolean isContingency() {
		return false;
	}

	@Override
	public void doAction(BotcivGame game, BotcivPlayer player) {
		unit = game.findMatching(unit);
		if(unit == null) {
			System.err.println("Unit "+unit.getId()+" not Found!");
			return;
		}
		Tile destination = game.world.getTileAt(location);
		if(destination != null &&
				unit.getOwner().equals(player) && 
				game.world.tilesWithinRange(unit.getLocation().getCoordinate(), 1).getCoordinates().contains(location) &&
				GameLogicUtilities.unitCanTraverse(unit, destination) &&
				GameLogicUtilities.tryTopay(player, 
						new ResourcePortfolio("{I:"+(10*unit.getStackSize())+"}"))) {
			game.world.getTileAt(unit.getLocation().getCoordinate()).removeUnit(unit);
			game.world.getTileAt(location).addUnit(unit,game);
			player.addExploredTile(location);
		}		
	}

	@Override
	public int priority() {
		return 7;
	}
}
