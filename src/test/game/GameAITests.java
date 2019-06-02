package test.game;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import aibrain.Action;
import cloner.BotcivGameCloner;
import controller.Controller;
import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import game.World;
import game.actions.MigrateUnit;
import map.Coordinate;

public class GameAITests {

	@BeforeClass
	public static void loadData() {
		TileType.loadData();
		UnitType.loadData();
	}
	
	@Test
	public void testClaimAndFarm() throws InterruptedException {

		BotcivGame testGame = generateTestGame();
		
		BotcivPlayer gatePlayer = testGame.playerByName("gate");
		
		BotcivPlayer player1 = testGame.playerByName("test1");
				
		Unit unit1 = new Unit(UnitType.TYPES.get("population"),player1);
		
		Tile tile1 = testGame.world.getTileAt(new Coordinate(5,5));
		tile1.addUnit(unit1,testGame);		
		
		//This starts four threads, which isn't IDEAL in a unit test...
		Controller controller = new Controller(testGame);
		controller.startAIs();		
		
		//use this as a simple way to make the controller do only one action
		controller.commitTurn(new ArrayList<Action>(), gatePlayer);
		
		//since the AI is in a different thread, busy wait
		while(testGame.turn < 2) {
			Thread.sleep(100);
		}
		
		assert(testGame.world.getTileAt(new Coordinate(5,5)).getOwner().equals(player1));
		
		player1 = testGame.playerByName("test1");
		player1.addMaterials(100);
		
		//use this as a simple way to make the controller do only one action
		controller.commitTurn(new ArrayList<Action>(), gatePlayer);
		
		//being a little unspecific here in order to not be super volatile as unit rules change
		assert(testGame.world.getTileAt(new Coordinate(5,5)).getAllUnits().size() > 0);
	}
	
	private BotcivGame generateTestGame() {
		World testWorld = new World();
		
		Tile tile1 = new Tile(5, 5, TileType.TYPES.get("Grassland"));
		testWorld.setTileAt(new Coordinate(5,5), tile1);
		
		Tile tile2 = new Tile(5, 6, TileType.TYPES.get("Grassland"));
		testWorld.setTileAt(new Coordinate(5,6), tile2);
		
		BotcivGame testGame = new BotcivGame(testWorld);
		
		BotcivPlayer gatePlayer = new BotcivPlayer("gate",true);//used to keep brain threads from going mad
		testGame.players.add(gatePlayer);
		BotcivPlayer player1 = new BotcivPlayer("test1",false);
		player1.addMaterials(20);
		player1.addInfluence(100);
		player1.addExploredTile(new Coordinate(5,5));
		testGame.players.add(player1);
		
		return testGame;
	}
	
}
