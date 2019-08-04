package game.actions;

import java.util.Comparator;

import aibrain.Action;
import game.BotcivGame;
import game.BotcivPlayer;

public abstract class BotcivAction implements Action {

	public static class BotcivActionComparator implements Comparator<BotcivAction>{

		@Override
		public int compare(BotcivAction arg0, BotcivAction arg1) {
			return arg0.priority() - arg1.priority();
		}		
	}
	
	public abstract void doAction(BotcivGame game, BotcivPlayer player);
	
	//lower goes first
	public abstract int priority();
}
