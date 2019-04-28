package game.actions;

import java.util.HashMap;

import game.BotcivGame;
import game.BotcivPlayer;
import game.ResourcePortfolio;
import map.Coordinate;
import util.GameLogicUtilities;

public class ClaimTile extends BotcivAction{

	Coordinate coord;
	
	public ClaimTile(Coordinate coord) {
		this.coord = coord;
	}
	
	@Override
	public boolean isContingency() {
		return false;
	}

	@Override
	public void doAction(BotcivGame game, BotcivPlayer player) {
		if(GameLogicUtilities.tryTopay(player, 
				new ResourcePortfolio("{I:5}"))) {
			game.world.getTileAt(coord).setOwner(player);
		}
	}

}
