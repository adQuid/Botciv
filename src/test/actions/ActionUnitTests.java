package test.actions;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import game.World;
import game.actions.MigrateUnit;
import map.Coordinate;

public class ActionUnitTests {

	@BeforeClass
	public static void loadData() {
		TileType.loadData();
		UnitType.loadData();
	}
	
	@Test
	public void testExploreTile() {

		BotcivGame testGame = generateTestGame();
		
		BotcivPlayer player = testGame.playerByName("test");
		
		Unit toMove = new Unit(UnitType.TYPES.get("population"),player);
		
		Tile tile = testGame.world.getTileAt(new Coordinate(5,5));
		tile.addUnit(toMove,testGame);
		
		MigrateUnit action = new MigrateUnit(toMove,new Coordinate(5,6));
		
		action.doAction(testGame, player);
		
		assert(testGame.world.getTileAt(new Coordinate(5,5)).getAllUnits().size() == 0);
		assert(testGame.world.getTileAt(new Coordinate(5,6)).getAllUnits().size() == 1);
	}

	private BotcivGame generateTestGame() {
		World testWorld = new World();
		
		Tile tile1 = new Tile(5, 5, TileType.TYPES.get("Grassland"));
		testWorld.setTileAt(new Coordinate(5,5), tile1);
		
		Tile tile2 = new Tile(5, 6, TileType.TYPES.get("Grassland"));
		testWorld.setTileAt(new Coordinate(5,6), tile2);
		
		BotcivGame testGame = new BotcivGame(testWorld);
		
		BotcivPlayer player = new BotcivPlayer("test",true);
		testGame.players.add(player);
		
		return testGame;
	}
	
}
