package util;

import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import game.TileType;
import map.Coordinate;

public class WorldViewGenerator {

	public static void pruneImage(BotcivGame input, BotcivPlayer player) {
		for(Tile tile: input.world.allTiles()) {
			Coordinate coord = tile.getCoordinate();

			if(!player.getExploredTiles().contains(coord)) {
				input.world.setTileAt(coord, new Tile(coord.x, coord.y, TileType.TYPES.get("Unexplored Tile")));
			}
		}
		input.recalculateUnitList();
	}
	
}
