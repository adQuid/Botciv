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
		if(coord == null) {
			return null;
		}
		
		
		Coordinate modCoord = new Coordinate(coord.x%WORLD_SIZE,coord.y);

		while(modCoord.x < 0) {
			modCoord.x += WORLD_SIZE;
		}
		
		return tiles.get(modCoord);
	}
		
	public void clearSelections() {
		for(Tile tile: tiles.values()) {
			tile.setSelected(false);
		}
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
	
	public List<Coordinate> tilesWithinRange(Tile tile, int range){
		List<Coordinate> retval = new ArrayList<Coordinate>();
		List<List<Coordinate>> arr = new ArrayList<List<Coordinate>>(); 
		for(int i=0; i <= range; i++) {
			arr.add(new ArrayList<Coordinate>());
		}
		
		arr.get(0).add(new Coordinate(tile.getX(),tile.getY()));
		
		for(int index=0; index < range; index++) {
			while(arr.get(index).size() > 0) {
				Coordinate coord = arr.get(index).get(0);

				addConditionally(arr.get(index+1), coord);
				addConditionally(arr.get(index+1),coord.up());
				addConditionally(arr.get(index+1),coord.down());
				addConditionally(arr.get(index+1),coord.left());
				addConditionally(arr.get(index+1),coord.right());

				arr.get(index).remove(0);
			}
		}
				
		return arr.get(range);		
	}
	
	private void addConditionally(List<Coordinate> list, Coordinate tile) {
		if(getTileAt(tile) != null ) {
			if(!list.contains(tile)) {
				list.add(tile);
			}
		}
		
			
	}
	
}
