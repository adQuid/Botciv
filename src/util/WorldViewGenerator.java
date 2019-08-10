package util;

import java.util.ArrayList;

import aibrain.Action;
import aibrain.Player;
import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import game.TileType;
import map.Coordinate;

public class WorldViewGenerator {

	public static void pruneImage(BotcivGame input, BotcivPlayer player) {
		
		for(Player current: input.getPlayers()) {
			if(!current.equals(player)) {
				input.setActionsForPlayer(new ArrayList<Action>(), current);
			}
		}
		
		for(Tile tile: input.world.allTiles()) {
			Coordinate coord = tile.getCoordinate();

			if(!player.getExploredTiles().contains(coord)) {
				input.world.setTileAt(coord, new Tile(coord.x, coord.y, TileType.TYPES.get("Unexplored Tile")));
			}
		}
		input.recalculateUnitList();
	}
	
}
