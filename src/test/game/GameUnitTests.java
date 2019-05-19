package test.game;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import cloner.BotcivGameCloner;
import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import game.World;
import game.actions.MigrateUnit;
import map.Coordinate;

public class GameUnitTests {

	@BeforeClass
	public static void loadData() {
		TileType.loadData();
		UnitType.loadData();
	}
	
	@Test
	public void testCopy() {

		BotcivGame testGame = generateTestGame();
		
		BotcivPlayer player1 = testGame.playerByName("test1");
		BotcivPlayer player2 = testGame.playerByName("test2");
		
		Unit unit1 = new Unit(UnitType.TYPES.get("population"),player1);
		Unit unit2 = new Unit(UnitType.TYPES.get("population"),player2);
		
		Tile tile1 = testGame.world.getTileAt(new Coordinate(5,5));
		tile1.addUnit(unit1,testGame);
		Tile tile2 = testGame.world.getTileAt(new Coordinate(5,6));
		tile2.addUnit(unit2,testGame);
		
		BotcivGame copyGame = (BotcivGame)(new BotcivGameCloner().cloneGame(testGame));
		
		assert(copyGame.world.getTileAt(new Coordinate(5,5)).getAllUnits().size() == 1);
		assert(copyGame.world.getTileAt(new Coordinate(5,6)).getAllUnits().size() == 1);
		assert(copyGame.getUnits().size() == 2);
	}
	
	@Test
	public void testImage() {

		BotcivGame testGame = generateTestGame();
		
		BotcivPlayer player1 = testGame.playerByName("test1");
		BotcivPlayer player2 = testGame.playerByName("test2");
		
		player1.addExploredTile(new Coordinate(5,5));
		player1.addExploredTile(new Coordinate(5,6));
		
		Unit unit1 = new Unit(UnitType.TYPES.get("population"),player1);
		Unit unit2 = new Unit(UnitType.TYPES.get("population"),player2);
		
		Tile tile1 = testGame.world.getTileAt(new Coordinate(5,5));
		tile1.addUnit(unit1,testGame);
		Tile tile2 = testGame.world.getTileAt(new Coordinate(5,6));
		tile2.addUnit(unit2,testGame);
		
		BotcivGame imageGame1 = (BotcivGame)testGame.imageForPlayer(player1);
		BotcivGame imageGame2 = (BotcivGame)testGame.imageForPlayer(player2);
		BotcivGame copyGame = (BotcivGame)(new BotcivGameCloner()).cloneGame(testGame);
		
		assert(imageGame1.world.getTileAt(new Coordinate(5,5)).getAllUnits().size() == 1);
		assert(imageGame1.world.getTileAt(new Coordinate(5,6)).getAllUnits().size() == 1);
		assert(imageGame1.getUnits().size() == 2);
		
		assert(imageGame2.world.getTileAt(new Coordinate(5,5)).getType() == TileType.TYPES.get("Unexplored Tile"));
		assert(imageGame2.world.getTileAt(new Coordinate(5,6)).getType() == TileType.TYPES.get("Unexplored Tile"));
		assert(imageGame2.getUnits().size() == 0);
		
		assert(copyGame.world.getTileAt(new Coordinate(5,5)).getAllUnits().size() == 1);
		assert(copyGame.world.getTileAt(new Coordinate(5,6)).getAllUnits().size() == 1);
		assert(copyGame.getUnits().size() == 2);
		
	}

	private BotcivGame generateTestGame() {
		World testWorld = new World();
		
		Tile tile1 = new Tile(5, 5, TileType.TYPES.get("Grassland"));
		testWorld.setTileAt(new Coordinate(5,5), tile1);
		
		Tile tile2 = new Tile(5, 6, TileType.TYPES.get("Grassland"));
		testWorld.setTileAt(new Coordinate(5,6), tile2);
		
		BotcivGame testGame = new BotcivGame(testWorld);
		
		BotcivPlayer player1 = new BotcivPlayer("test1",true);
		testGame.players.add(player1);
		BotcivPlayer player2 = new BotcivPlayer("test2",true);
		testGame.players.add(player2);
		
		return testGame;
	}
	
}
