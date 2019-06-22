package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import aibrain.Action;
import aibrain.Game;
import aibrain.Player;
import game.actions.BotcivAction;
import map.Coordinate;
import util.WorldViewGenerator;
import worldgen.WorldGenerator;
import util.GameLogicUtilities;
import util.MiscUtilities;

public class BotcivGame implements Game{

	public boolean isLive = true;
	
	public int turn = 1;
	private static final String TURN_NAME = "turn";
	public World world;
	private static final String WORLD_NAME = "world";
	public List<BotcivPlayer> players = new ArrayList<BotcivPlayer>(); //public to avoid casting madness
	private static final String PLAYERS_NAME = "players";
	
	//This is a short list that isn't saved
	private Set<Unit> units = new HashSet<Unit>();
	
	public BotcivGame() {
		this.world = new World();
		world.generateWorld();
		
		for(int i=0; i<6; i++) {
			BotcivPlayer toAdd = new BotcivPlayer("Player "+i,i==0);
			
			players.add(toAdd);
		}
		
		WorldGenerator.establishStartLocations(world, players,this);
		System.out.println(units.size());
	}
	
	public BotcivGame(World world) {
		this.world = world;
	}
	
	public BotcivGame(BotcivGame other) {
		for(BotcivPlayer current: other.players) {
			this.players.add(new BotcivPlayer(current));
		}
		this.world = new World(other.world,this);
		this.turn = other.turn;
		this.isLive = false;
		if(units.size() == 1 && other.units.size() == 2) {
			System.out.println(units.size());
		}
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
		for(Action current: arg0) {
			playerByName(((BotcivPlayer)arg1).getName()).addAction((BotcivAction)current);	
		}
	}

	@Override
	public void endRound() {

		for(BotcivPlayer player: players) {
			for(BotcivAction action: player.getActions()) {
				action.doAction(this,player);
			}
			player.setActions(new ArrayList<BotcivAction>());
		}
		
		GameLogicUtilities.calculateMarkets(this);
		
		//end of round resource generation
		for(BotcivPlayer current: players) {
			ResourcePortfolio port = GameLogicUtilities.getResourceDeltas(world, current);
			
			current.setLabor(port.labor);
			current.addMaterials(port.materials);
			current.addWealth(port.wealth);
			current.addInfluence(port.influence);
			current.setEducation(port.education);
		}
		turn++;
		recalculateUnitList();
	}

	@Override
	public List<Player> getPlayers() {
		List<Player> retval = new ArrayList<Player>();
		for(BotcivPlayer current: players) {
			retval.add(current);
		}
		return retval;
	}

	public BotcivPlayer playerByName(String name) {
		for(Player current: players) {
			if(((BotcivPlayer)current).getName().equals(name)) {
				return (BotcivPlayer)current;
			}
		}
		return null;
	}
	
	public Unit findMatching(Unit unit) {
		for(Tile tile: world.allTiles()) {
			List<Unit> matchingUnitList = tile.getUnits().get(unit.getType());

			if(matchingUnitList != null) {
				for(Unit matchingUnit: matchingUnitList) {
					if(matchingUnit.equals(unit)) {
						return matchingUnit;
					}
				}
			}
		}
		System.err.println("Failed to find matching unit!");
		return null;
	}
	
	@Override
	public Game imageForPlayer(Player arg0) {
		BotcivGame retval = new BotcivGame(this);
		WorldViewGenerator.pruneImage(retval, (BotcivPlayer)arg0);
		return retval;
	}

	@Override
	public boolean isLive() {
		return isLive;
	}

	@Override
	public Game nextRound() {
		Game retval = new BotcivGame(this);
		retval.endRound();
		return retval;
	}

	@Override
	public void setActionsForPlayer(List<Action> arg0, Player arg1) {
		BotcivPlayer player = playerByName(((BotcivPlayer)arg1).getName());
		
		List<BotcivAction> toAdd = new ArrayList<BotcivAction>();
		for(Action current: arg0) {
			toAdd.add((BotcivAction)current);
		}
		player.setActions(toAdd);
	}

	@Override
	public void setLive(boolean isLive) {
		this.isLive = isLive;
		
	}
	
	public Set<Unit> getUnits() {
		return units;
	}
	
	public void addUnit(Unit unit) {
		units.add(unit);
	}
	
	public void recalculateUnitList() {
		units.clear();
		for(Tile tile: world.allTiles()) {
			for(Unit unit: tile.getAllUnits()) {
				units.add(unit);
			}
		}
	}
	
	public String getTurnName() {
		return "Turn "+turn+" (Strategic Round)";
	}

}
