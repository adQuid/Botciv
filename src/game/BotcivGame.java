package game;

import java.util.List;

import aibrain.Action;
import aibrain.Game;
import aibrain.Player;
import map.World;

public class BotcivGame implements Game{

	public World world;
	
	public BotcivGame(World world) {
		this.world = world;
	}
	
	@Override
	public void appendActionsForPlayer(List<Action> arg0, Player arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRound() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Player> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Game imageForPlayer(Player arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Game nextRound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setActionsForPlayer(List<Action> arg0, Player arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLive(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

}
