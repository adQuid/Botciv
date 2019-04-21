package game.actions;

import aibrain.Action;
import game.BotcivGame;
import game.BotcivPlayer;

public abstract class BotcivAction implements Action {

	public abstract void doAction(BotcivGame game, BotcivPlayer player);
	
}
