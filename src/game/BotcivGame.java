package game;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import aibrain.Game;
import aibrain.Player;
import map.Coordinate;
import map.World;

public class BotcivGame implements Game{

	public World world;
	public List<BotcivPlayer> players = new ArrayList<BotcivPlayer>();
	
	public BotcivGame(World world) {
		this.world = world;
		
		for(int i=0; i<1; i++) {
			BotcivPlayer toAdd = new BotcivPlayer("Player "+i);

			world.getTileAt(new Coordinate(i+3,i+3)).addUnit(new Unit(UnitType.TYPES.get("Population"),toAdd));;
			
			players.add(toAdd);
		}
	}
	
	@Override
	public void appendActionsForPlayer(List<Action> arg0, Player arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRound() {

		//end of round resource generation
		for(Player current: players) {
			BotcivPlayer civ = (BotcivPlayer)current;
			ResourcePortfolio port = civ.getPortfolio(world);
			
			civ.setLabor(port.labor);
			civ.addMaterials(port.materials);
			civ.addWealth(port.wealth);
			civ.addInfluence(port.influence);
			civ.setEducation(port.education);
			
		}
	}

	@Override
	public List<Player> getPlayers() {
		return null;
	}

	@Override
	public Game imageForPlayer(Player arg0) {
		// TODO Auto-generated method stub
		return this;
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
