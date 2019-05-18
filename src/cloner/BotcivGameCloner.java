package cloner;

import aibrain.Game;
import aibrain.GameCloner;
import game.BotcivGame;

public class BotcivGameCloner implements GameCloner{

	@Override
	public Game cloneGame(Game game) {
		
		BotcivGame retval = new BotcivGame((BotcivGame)game);
		return retval;
	}

}
