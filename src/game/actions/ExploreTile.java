package game.actions;

import java.util.HashMap;

import game.BotcivGame;
import game.BotcivPlayer;
import game.ResourcePortfolio;
import map.Coordinate;
import util.GameLogicUtilities;

public class ExploreTile extends BotcivAction{

	Coordinate coord;
	
	public ExploreTile(Coordinate coord) {
		this.coord = coord;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof ExploreTile)) {
			return false;
		}
		ExploreTile castOther = (ExploreTile)other;
		return coord.equals(castOther.coord);
	}
	
	@Override
	public boolean isContingency() {
		return false;
	}

	@Override
	public void doAction(BotcivGame game, BotcivPlayer player) {
		if(GameLogicUtilities.tryTopay(player, 
				new ResourcePortfolio("{I:1,M:2}"))) {
			player.addExploredTile(coord);
		}
	}

}
