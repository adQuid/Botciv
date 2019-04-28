package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import aibrain.Action;
import aibrain.Game;
import aibrain.Player;
import game.actions.BotcivAction;
import map.Coordinate;
import util.ImageGenerator;
import util.MiscUtilities;
import util.WorldGenerator;

public class BotcivGame implements Game{

	public int turn = 1;
	private static final String TURN_NAME = "turn";
	public World world;
	private static final String WORLD_NAME = "world";
	public List<BotcivPlayer> players = new ArrayList<BotcivPlayer>();
	private static final String PLAYERS_NAME = "players";
	
	public BotcivGame() {
		this.world = new World();
		
		for(int i=0; i<1; i++) {
			BotcivPlayer toAdd = new BotcivPlayer("Player "+i);
			
			players.add(toAdd);
		}
		
		WorldGenerator.establishStartLocations(world, players);
	}
	
	public BotcivGame(BotcivGame other) {
		for(BotcivPlayer current: other.players) {
			this.players.add(current);
		}
		this.world = new World(other.world,this);
		this.turn = other.turn;
	}
	
	public BotcivGame(Map<String,Object> map) {
		turn = MiscUtilities.extractInt(map.get(TURN_NAME));
		List<Map<String,Object>> playerList = (List<Map<String,Object>>)map.get(PLAYERS_NAME);
		for(Map<String,Object> current: playerList) {
			players.add(new BotcivPlayer(current,this));
		}
		world = new World((Map<String,Object>)map.get(WORLD_NAME),this);
	}
	
	public Map<String,Object> saveString() {
		Map<String,Object> retval = new TreeMap<String,Object>();
		
		retval.put(TURN_NAME, turn);
		retval.put(WORLD_NAME, world.saveString());
		
		List<Map<String,Object>> playerList = new ArrayList<Map<String,Object>>();
		for(BotcivPlayer player: players) {
			playerList.add(player.saveString());
		}
		retval.put(PLAYERS_NAME, playerList);
		
		return retval;
	}
	
	@Override
	public void appendActionsForPlayer(List<Action> arg0, Player arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRound() {

		for(BotcivPlayer player: players) {
			for(BotcivAction action: player.getActions()) {
				action.doAction(this,player);
			}
			player.setActions(new ArrayList<BotcivAction>());
		}
		
		//end of round resource generation
		for(BotcivPlayer current: players) {
			ResourcePortfolio port = current.getResourceDeltas(world);
			
			current.setLabor(port.labor);
			current.addMaterials(port.materials);
			current.addWealth(port.wealth);
			current.addInfluence(port.influence);
			current.setEducation(port.education);
			
			//for now all governments generate an automatic 10 influence
			current.addInfluence(10);
		}
		turn++;
	}

	@Override
	public List<Player> getPlayers() {
		return null;
	}

	public BotcivPlayer playerByName(String name) {
		for(Player current: players) {
			if(((BotcivPlayer)current).getName().equals(name)) {
				return (BotcivPlayer)current;
			}
		}
		return null;
	}
	
	@Override
	public Game imageForPlayer(Player arg0) {
		BotcivGame retval = new BotcivGame(this);
		ImageGenerator.pruneImage(retval, (BotcivPlayer)arg0);
		return retval;
	}

	@Override
	public boolean isLive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Game nextRound() {
		Game retval = new BotcivGame(this);
		retval.endRound();
		return retval;
	}

	@Override
	public void setActionsForPlayer(List<Action> arg0, Player arg1) {
		BotcivPlayer player = (BotcivPlayer)arg1;
		
		List<BotcivAction> toAdd = new ArrayList<BotcivAction>();
		for(Action current: arg0) {
			toAdd.add((BotcivAction)current);
		}
		player.setActions(toAdd);
	}

	@Override
	public void setLive(boolean arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public String getTurnName() {
		return "Turn "+turn+" (Strategic Round)";
	}

}
