package map;

import java.util.HashMap;
import java.util.Map;

import game.TileType;
import game.Unit;
import game.UnitType;

public class World {

	public static final int WORLD_SIZE = 100;
	private Map<Coordinate,Tile> tiles = new HashMap<Coordinate,Tile>();
	
	public World() {
		
		for(int lat=0; lat<WORLD_SIZE; lat++) {
			for(int lon=0; lon<WORLD_SIZE; lon++) {
				Tile tile;
				if(Math.abs(lat-40)*lon + lon > 85) {
					 tile = new Tile(lon,lat,TileType.TYPES.get("Grassland"));	
					 tile.addUnit(new Unit(UnitType.TYPES.get("Road")));
					 for(int i = 0; i < lat; i++) {
						 tile.addUnit(new Unit(UnitType.TYPES.get("Population")));
					 }
				} else {
					tile = new Tile(lon,lat,TileType.TYPES.get("Sea"));
				}
				tiles.put(new Coordinate(lon,lat), tile);
			}
		}

	}
	
	public Tile getTileAt(Coordinate coord) {
		Coordinate modCoord = new Coordinate(coord.x%WORLD_SIZE,coord.y);

		while(modCoord.x < 0) {
			modCoord.x += WORLD_SIZE;
		}
		
		return tiles.get(modCoord);
	}
	
}
