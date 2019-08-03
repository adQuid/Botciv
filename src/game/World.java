package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import map.Coordinate;
import worldgen.WorldGenerator;

public class World {

	public final static int WORLD_SIZE = 100;
	private Map<Coordinate,Tile> tiles = new HashMap<Coordinate,Tile>();
	private static final String TILES_NAME = "tiles";	
	
	public World() {		
		
	}
	
	public World(World other, BotcivGame game) {
		for(Entry<Coordinate,Tile> entry: other.tiles.entrySet()) {
			this.tiles.put(entry.getKey(), new Tile(entry.getValue(),game));
		}
	}
	
	public World(Map<String,Object> map, BotcivGame game) {
		List<Map<String,Object>> tileList = (List<Map<String,Object>>)map.get(TILES_NAME);
		for(Map<String,Object> tileMap: tileList) {
			Tile toAdd = new Tile(tileMap,game);
			tiles.put(toAdd.getCoordinate(), toAdd);
		}
	}
	
	public Map<String,Object> saveString(){
		Map<String,Object> retval = new TreeMap<String,Object>();

		List<Map<String,Object>> tilesMap = new ArrayList<Map<String,Object>>();
		for(Tile current: tiles.values()) {
			tilesMap.add(current.saveString());
		}
		retval.put(TILES_NAME, tilesMap);
		
		return retval;
	}
	
	public Collection<Tile> allTiles() {
		return tiles.values();
	}
	
	public Tile getTileAt(Coordinate coord) {
		if(coord == null) {
			return null;
		}
						
		return tiles.get(coord.wrap(WORLD_SIZE));
	}
		
	public void setTileAt(Coordinate coord, Tile tile) {
		tiles.put(coord, tile);
	}
	
	public void generateWorld() {
		tiles = WorldGenerator.generateTerrain(this);
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
			for(Unit unit: current.getUnits()) {
				if(type == null || unit.getType() == type) {
					retval.add(unit);
				}
			}
		}
		
		return retval;
	}
	
	public List<Coordinate> tilesOwnedByPlayer(BotcivPlayer player){
		List<Coordinate> retval = new ArrayList<Coordinate>();
		
		for(Tile current: tiles.values()) {
			if(current.getOwner() != null && current.getOwner().equals(player)) {
				retval.add(current.getCoordinate());
			}
		}
		
		return retval;
	}
	
	public List<Coordinate> tilesWithinRange(Coordinate tile, int range){
		List<Coordinate> retval = new ArrayList<Coordinate>();
		List<List<Coordinate>> arr = new ArrayList<List<Coordinate>>(); 
		for(int i=0; i <= range; i++) {
			arr.add(new ArrayList<Coordinate>());
		}
		
		arr.get(0).add(tile);
		
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
	
	public int rangeBetween(Coordinate c1, Coordinate c2) {
		for(int retval = 0; retval < WORLD_SIZE; retval++) {
			if(tilesWithinRange(c1,retval).contains(c2)) {
				return retval;
			}
		}
		return -1;
	}
	
	public static int lattitude(Coordinate coord) {
		int retval = (int)(Math.abs(coord.y-(0.5*WORLD_SIZE))/(0.5*WORLD_SIZE) * 90.0);
		if(retval > 90 || retval < 0) {
			System.out.println("coord "+coord+" has lat "+retval);
		}
		return retval;
	}
	
}
