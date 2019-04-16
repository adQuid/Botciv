package map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.BotcivPlayer;
import game.TileType;
import game.Unit;
import game.UnitType;

public class World {

	public final int WORLD_SIZE = 100;
	private Map<Coordinate,Tile> tiles = new HashMap<Coordinate,Tile>();
	
	public World() {
		
		for(int lat=0; lat<WORLD_SIZE; lat++) {
			for(int lon=0; lon<WORLD_SIZE; lon++) {
				Tile tile;
				if(Math.abs(lat-40)*lon + lon > 85) {
					 tile = new Tile(lon,lat,TileType.TYPES.get("Grassland"));	
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
	
	/**
	 * 
	 * @param type can be set to null for wildcard
	 * @param player
	 * @return
	 */
	public List<Unit> getAllUnitsOfTypeByPlayer(UnitType type, BotcivPlayer player){
		List<Unit> retval = new ArrayList<Unit>();
		
		for(Tile current: tiles.values()) {
			for(List<Unit> unitList: current.getUnits().values()) {
				for(Unit unit: unitList) {
					if(type == null || unit.getType() == type) {
						retval.add(unit);
					}
				}
			}
		}
		
		return retval;
	}
	
}
