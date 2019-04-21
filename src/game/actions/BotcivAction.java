package game.actions;

import aibrain.Action;
import game.BotcivGame;

public abstract class BotcivAction implements Action {

	public abstract void doAction(BotcivGame game);
	
}
