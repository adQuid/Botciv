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
import jme.gui.GlobalContext;
import jme.gui.MainUI;
import map.Coordinate;
import jme.gui.mapActions.Migrate;

public class ActionIntegrationTests {

	@BeforeClass
	public static void loadData() {
		TileType.loadData();
		UnitType.loadData();
	}
	
	@Test
	public void testExploreTile() {

		BotcivGame baseGame = generateTestGame();		
		BotcivPlayer player = baseGame.playerByName("test");
		
		Unit toMove = new Unit(baseGame, UnitType.TYPES.get("population"),player);		
		Tile tile = baseGame.world.getTileAt(new Coordinate(5,5));
		tile.addUnit(toMove,baseGame);
		
		Controller.setTestController(baseGame);
		MainUI.setupGUI(player, true);
		player = MainUI.getGame().playerByName("test");
						
		//TODO: Make this use the UI methods
		Unit selected = MainUI.getGame().findMatching(toMove);
		Migrate mapAction = new Migrate();
		GlobalContext.setSelectedUnit(selected);
		mapAction.doAction(new Coordinate(5,6));
		
		MainUI.commitTurn();
		
		assert(baseGame.world.getTileAt(new Coordinate(5,5)).getUnits().size() == 0);
		assert(baseGame.world.getTileAt(new Coordinate(5,6)).getUnits().size() == 1);
	}
	
	@Test
	public void testSplitAndMigrate() {

		BotcivGame baseGame = generateTestGame();		
		BotcivPlayer player = baseGame.playerByName("test");
		
		Unit toMove = new Unit(baseGame, UnitType.TYPES.get("population"),player);	
		toMove.setStackSize(3);
		Tile tile = baseGame.world.getTileAt(new Coordinate(5,5));
		tile.addUnit(toMove,baseGame);
		
		Controller.setTestController(baseGame);
		MainUI.setupGUI(player, true);
		player = MainUI.getGame().playerByName("test");
						
		
		SplitUnit split = new SplitUnit(toMove,1);
		MigrateUnit migrate = new MigrateUnit(toMove, new Coordinate(5,6));

		MainUI.addAction(migrate);
		MainUI.addAction(split);
		MainUI.commitTurn();
		
		assert(baseGame.world.getTileAt(new Coordinate(5,5)).getUnits().size() == 1);
		assert(baseGame.world.getTileAt(new Coordinate(5,5)).getUnits().get(0).getStackSize() == 1);
		assert(baseGame.world.getTileAt(new Coordinate(5,6)).getUnits().size() == 1);
		assert(baseGame.world.getTileAt(new Coordinate(5,6)).getUnits().get(0).getStackSize() == 2);
	}

	private BotcivGame generateTestGame() {
		World testWorld = new World();
		
		Tile tile1 = new Tile(5, 5, TileType.TYPES.get("Grassland"));
		testWorld.setTileAt(new Coordinate(5,5), tile1);
		
		Tile tile2 = new Tile(5, 6, TileType.TYPES.get("Grassland"));
		testWorld.setTileAt(new Coordinate(5,6), tile2);
		
		BotcivGame testGame = new BotcivGame(testWorld);
		
		BotcivPlayer player = new BotcivPlayer("test",true);
		player.setInfluence(100);
		player.addExploredTile(new Coordinate(5,5));
		player.addExploredTile(new Coordinate(5,6));
		testGame.players.add(player);
		
		return testGame;
	}
	
}
