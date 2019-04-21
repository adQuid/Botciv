package game.actions;

import game.BotcivGame;
import game.BotcivPlayer;
import map.Coordinate;

public class ExploreTile extends BotcivAction{

	Coordinate coord;
	
	public ExploreTile(Coordinate coord) {
		this.coord = coord;
	}
	
	@Override
	public boolean isContingency() {
		return false;
	}

	@Override
	public void doAction(BotcivGame game, BotcivPlayer player) {
		player.addExploredTile(coord);
	}

}
