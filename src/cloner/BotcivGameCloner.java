package cloner;

import aibrain.Game;
import aibrain.GameCloner;
import game.BotcivGame;

public class BotcivGameCloner implements GameCloner{

	@Override
	public Game cloneGame(Game game) {
		
		return new BotcivGame((BotcivGame)game);
	}

}
