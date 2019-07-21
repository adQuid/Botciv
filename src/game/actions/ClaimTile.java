package game.actions;

import java.util.HashMap;

import game.BotcivGame;
import game.BotcivPlayer;
import game.ResourcePortfolio;
import game.Tile;
import map.Coordinate;
import util.GameLogicUtilities;

public class ClaimTile extends BotcivAction{

	Coordinate coord;
	
	public ClaimTile(Coordinate coord) {
		this.coord = coord;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof ClaimTile)) {
			return false;
		}
		ClaimTile castOther = (ClaimTile)other;
		return coord.equals(castOther.coord);
	}
	
	@Override
	public boolean isContingency() {
		return false;
	}

	@Override
	public void doAction(BotcivGame game, BotcivPlayer player) {	
		Tile toClaim = game.world.getTileAt(coord);
		if(toClaim.getOwner() == null && GameLogicUtilities.tryTopay(player, 
				new ResourcePortfolio("{I:5}"))) {
			toClaim.setOwner(player);
		}
	}

}
