package test.actions;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import aibrain.Action;
import controller.Controller;
import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import game.World;
import game.actions.MigrateUnit;
import game.actions.SplitUnit;
import jme.gui.MainUI;
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
		
		Unit toMove = new Unit(testGame, UnitType.TYPES.get("population"),player);
		
		Tile tile = testGame.world.getTileAt(new Coordinate(5,5));
		tile.addUnit(toMove,testGame);
		
		MigrateUnit action = new MigrateUnit(toMove,new Coordinate(5,6));
		
		action.doAction(testGame, player);
		
		assert(testGame.world.getTileAt(new Coordinate(5,5)).getUnits().size() == 0);
		assert(testGame.world.getTileAt(new Coordinate(5,6)).getUnits().size() == 1);
	}

	@Test
	public void testMultipleSplits() {

		BotcivGame baseGame = generateTestGame();
		baseGame.players.add(new BotcivPlayer("test 2",true));
		
		Tile addTile1 = new Tile(6, 5, TileType.TYPES.get("Grassland"));
		baseGame.world.setTileAt(new Coordinate(6,5), addTile1);
		
		Tile addTile2 = new Tile(6, 6, TileType.TYPES.get("Grassland"));
		baseGame.world.setTileAt(new Coordinate(6,6), addTile2);
		
		BotcivPlayer player1 = baseGame.playerByName("test");
		BotcivPlayer player2 = baseGame.playerByName("test 2");
		
		
		Unit toMove1 = new Unit(baseGame, UnitType.TYPES.get("population"),player1);	
		toMove1.setStackSize(3);
		Tile tile = baseGame.world.getTileAt(new Coordinate(5,5));
		tile.addUnit(toMove1,baseGame);
		
		Unit toMove2 = new Unit(baseGame, UnitType.TYPES.get("population"),player2);	
		toMove2.setStackSize(7);
		Tile tile2 = baseGame.world.getTileAt(new Coordinate(6,5));
		tile2.addUnit(toMove2,baseGame);	
		
		SplitUnit split1 = new SplitUnit(toMove1,1);
		SplitUnit split2 = new SplitUnit(toMove2,5);
		
		Unit dummy1 = new Unit(toMove1);//doesn't matter I just need to mess with ids
		dummy1.setId("1s1");
		Unit dummy2 = new Unit(toMove1);//doesn't matter I just need to mess with ids
		dummy2.setId("2s1");
		
		MigrateUnit migrate1 = new MigrateUnit(dummy1, new Coordinate(5,6));
		MigrateUnit migrate2 = new MigrateUnit(dummy2, new Coordinate(6,6));

		List<Action> actions1 = new ArrayList<Action>();
		actions1.add(split1);
		actions1.add(migrate1);
		
		List<Action> actions2 = new ArrayList<Action>();
		actions2.add(migrate2);
		actions2.add(split2);
		
		baseGame.appendActionsForPlayer(actions1, player1);
		baseGame.appendActionsForPlayer(actions2, player2);
		baseGame.endRound();
		
		assert(baseGame.world.getTileAt(new Coordinate(5,5)).getUnits().size() == 1);
		assert(baseGame.world.getTileAt(new Coordinate(5,5)).getUnits().get(0).getStackSize() == 2);
		assert(baseGame.world.getTileAt(new Coordinate(5,6)).getUnits().size() == 1);
		assert(baseGame.world.getTileAt(new Coordinate(5,6)).getUnits().get(0).getStackSize() == 1);
		assert(baseGame.world.getTileAt(new Coordinate(6,5)).getUnits().size() == 1);
		assert(baseGame.world.getTileAt(new Coordinate(6,5)).getUnits().get(0).getStackSize() == 2);
		assert(baseGame.world.getTileAt(new Coordinate(6,6)).getUnits().size() == 1);
		assert(baseGame.world.getTileAt(new Coordinate(6,6)).getUnits().get(0).getStackSize() == 5);
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
