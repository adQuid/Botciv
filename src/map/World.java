package map;

import java.util.HashMap;
import java.util.Map;

public class World {

	static final int WORLD_SIZE = 10;
	private static Map<Coordinate,Tile> tiles = new HashMap<Coordinate,Tile>();
	
	public World() {
		
		for(int lat=0; lat<WORLD_SIZE; lat++) {
			for(int lon=0; lon<WORLD_SIZE; lon++) {
				tiles.put(new Coordinate(lon,lat), new Tile(lon,lat));				
			}
		}

	}
	
	public static Tile getTileAt(Coordinate coord) {
		Coordinate modCoord = new Coordinate(coord.x%WORLD_SIZE,coord.y);

		while(modCoord.x < 0) {
			modCoord.x += WORLD_SIZE;
		}
		
		return tiles.get(modCoord);
	}
	
}
