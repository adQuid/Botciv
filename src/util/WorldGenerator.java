package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import game.BotcivPlayer;
import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import map.Coordinate;
import map.World;

public class WorldGenerator {

	private static int plates = 14;
	private static Random rand = new Random();
	
	public static Map<Coordinate,Tile> generateTerrain(World world) {
		
		Map<Coordinate,Tile> tiles = new HashMap<Coordinate,Tile>();
		
		//And God said, “Let the water under the sky be gathered to one place...
		for(int lat=0; lat<world.WORLD_SIZE; lat++) {
			for(int lon=0; lon<world.WORLD_SIZE; lon++) {
				Tile tile;
				if(Math.abs(lat-world.WORLD_SIZE/2) > 48) {
					 tile = new Tile(lon,lat,TileType.TYPES.get("Ice Cap"));	
					 tile.setAltitude(rand.nextInt(50)+2);
					 tile.setTemperature(-20);
				} else {
					tile = new Tile(lon,lat,TileType.TYPES.get("Sea"));
				}
				tiles.put(new Coordinate(lon,lat), tile);
			}
		}
				
		//...and let dry ground appear."
		for(int i=0; i < plates; i++) {
			Coordinate start = new Coordinate(rand.nextInt(world.WORLD_SIZE),rand.nextInt(world.WORLD_SIZE));
			int plateBase = rand.nextInt(600)-700;
			int tendrils = rand.nextInt(3) + rand.nextInt(3) + 1;
			Set<Coordinate> thisContinent = new HashSet<Coordinate>();
			
			for(int tend=0; tend<tendrils; tend++) {
				int xTravel = rand.nextInt(5) - 2;
				int yTravel = rand.nextInt(5) - 2;
				int length = rand.nextInt(30) + 1;
				int width = rand.nextInt(6)+3;

				for(int j=0; j<length; j++) {
					Coordinate nextCoord = new Coordinate(start.x+(j*xTravel),start.y+(j*yTravel)).wrap(world.WORLD_SIZE);
					int areaBonus = rand.nextInt(2000);
					for(int x=-width; x <= width; x++) {
						for(int y=-width; y <= width; y++) {
							if(Math.abs(x)+Math.abs(y) <= width) {
								Coordinate coord = new Coordinate(nextCoord.x+x,nextCoord.y+y).wrap(world.WORLD_SIZE);
								if(tiles.get(coord) != null) {
									if(tiles.get(coord).getAltitude() > -1000 
											&& !thisContinent.contains(coord)) {
										//there is a collision with another high plate, resulting in a mountain range
										setAltIfNotNull(tiles.get(coord), 
												Math.max(plateBase + (areaBonus/(Math.abs(x)+Math.abs(y)+2)),
														tiles.get(coord).getAltitude()) + rand.nextInt(400) + rand.nextInt(400));
									} else {
										setAltIfNotNull(tiles.get(coord), 
												plateBase + (areaBonus/(Math.abs(x)+Math.abs(y)+2)) + rand.nextInt(200));
									}
									thisContinent.add(coord);
								}
							}
						}
					}
					if(rand.nextFloat() < 0.2) {
						xTravel += rand.nextInt(2) - 1;
					}
					if(rand.nextFloat() < 0.2) {
						yTravel += rand.nextInt(2) - 1;
					}
					if(rand.nextFloat() < 0.6) {
						width += rand.nextInt(2) - 1;
					}
				}
			}
		}
		
		//flatten altitude a little
		for(Tile tile: tiles.values()) {
			flowAltitude(tile,tiles.get(tile.getCoordinate().left().wrap(world.WORLD_SIZE)));
			flowAltitude(tile,tiles.get(tile.getCoordinate().right().wrap(world.WORLD_SIZE)));
			flowAltitude(tile,tiles.get(tile.getCoordinate().up().wrap(world.WORLD_SIZE)));
			flowAltitude(tile,tiles.get(tile.getCoordinate().down().wrap(world.WORLD_SIZE)));
		}
		
		
		//determine mountains first
		int landTiles = 0;
		for(Tile tile: tiles.values()) {
			if(tile.getAltitude() > 800 || 
					(tile.getAltitude() > 200 &&
					cliffs(tiles,tile,world.WORLD_SIZE) >= 2)) {
				tile.setType(TileType.TYPES.get("Mountain"));
				landTiles++;
			}
		}
		
		//basic temperature by latitude
		for(Tile tile: tiles.values()) {
			tile.setTemperature(40 - (9*(Math.abs(tile.getY() - (world.WORLD_SIZE/2)))/8));
		}
		
		//checking for rainfall
		for(Tile tile: tiles.values()) {
			if(tile.getAltitude() < 0 && tile.getTemperature() > 0) {
				spreadRainfall(tile.getCoordinate(), tiles, world.WORLD_SIZE);
			}
		}
			
		//other tempurature adjustments
		for(Tile tile: tiles.values()) {
			if(tile.getTemperature() > 14) {
				tile.setTemperature(Math.max(14,tile.getTemperature() - (tile.getRainfall()/3)));
			} else {
				tile.setTemperature(Math.min(14,tile.getTemperature() + (tile.getRainfall()/3)));
			}
			if(tile.getAltitude() >= 200) {
				tile.setTemperature(tile.getTemperature() - (tile.getAltitude()/200));
			}
		}
		
		//flow temperature
		for(Tile tile: tiles.values()) {
			flowTempurature(tile,tiles.get(tile.getCoordinate().left().wrap(world.WORLD_SIZE)));
			flowTempurature(tile,tiles.get(tile.getCoordinate().right().wrap(world.WORLD_SIZE)));
			flowTempurature(tile,tiles.get(tile.getCoordinate().up().wrap(world.WORLD_SIZE)));
			flowTempurature(tile,tiles.get(tile.getCoordinate().down().wrap(world.WORLD_SIZE)));
		}
		
		//final decisions
		for(Tile tile: tiles.values()) {
			if(tile.getType() == TileType.TYPES.get("Mountain")) {
				continue;
			}
			if(tile.getTemperature() < 0) {
				tile.setType(TileType.TYPES.get("Ice Cap"));
			} else {
				if(tile.getAltitude() > -10) {
					if(tile.getRainfall() > 6) {
						tile.setType(TileType.TYPES.get("Grassland"));
					} else {
						tile.setType(TileType.TYPES.get("Plain"));
					}
					landTiles++;
				} else {

				}
			}
		}
				
		System.out.println(landTiles);
		return tiles;
	}

	private static int altOrNull(Tile tile) {
		if(tile == null) {
			return -1000;//this is only used for mountains right now
		} else {
			return tile.getAltitude();
		}
	}
	
	private static void setAltIfNotNull(Tile tile, int val) {
		if(tile != null) {
			tile.setAltitude(val);
		}
	}
	
	private static int cliffs(Map<Coordinate,Tile> tiles, Tile tile, int size) {
		int retval = 0;
		
		if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().left().wrap(size))) > 200) {
			retval++;
		}
		if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().right().wrap(size))) > 200) {
			retval++;
		}
		if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().up().wrap(size))) > 200) {
			retval++;
		}
		if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().down().wrap(size))) > 200) {
			retval++;
		}
		
		return retval;
	}
	
	private static void flowAltitude(Tile tile, Tile other) {
		if(other == null) {
			return;
		}
		if(Math.abs(other.getAltitude() - tile.getAltitude()) > 50) {
			tile.setAltitude(tile.getAltitude() + (other.getAltitude() - tile.getAltitude())/5);
		}
	}
	
	private static void spreadRainfall(Coordinate start, Map<Coordinate,Tile> tiles, int size) {
		List<Coordinate> retval = new ArrayList<Coordinate>();
		List<List<Coordinate>> arr = new ArrayList<List<Coordinate>>(); 
		for(int i=0; i <= 5; i++) {
			arr.add(new ArrayList<Coordinate>());
		}
		
		arr.get(0).add(start);
		
		for(int index=0; index < 5; index++) {
			while(arr.get(index).size() > 0) {
				Coordinate coord = arr.get(index).get(0).wrap(size);

				if(tiles.get(coord).getType() != TileType.TYPES.get("Mountain")) {
					tiles.get(coord).setRainfall(tiles.get(coord).getRainfall() + rand.nextInt(2));
					addConditionally(tiles, size, arr.get(index+1), coord);
					addConditionally(tiles, size, arr.get(index+1),coord.up());
					addConditionally(tiles, size, arr.get(index+1),coord.down());
					addConditionally(tiles, size, arr.get(index+1),coord.left());
					addConditionally(tiles, size, arr.get(index+1),coord.right());
				}

				arr.get(index).remove(0);
			}
		}				
	}
	
	private static void flowTempurature(Tile tile, Tile other) {
		if(other == null) {
			return;
		}
		if(Math.abs(other.getTemperature() - tile.getTemperature()) > 5) {
			tile.setTemperature(tile.getTemperature() + (other.getTemperature() - tile.getTemperature())/5);
		}
	}
	
	private static void addConditionally(Map<Coordinate,Tile> tiles, int size, List<Coordinate> list, Coordinate tile) {
		if(tiles.get(tile.wrap(size)) != null ) {
			if(!list.contains(tile)) {
				list.add(tile);
			}
		}			
	}
	
	public static void establishStartLocations(World world, List<BotcivPlayer> players) {

		List<Coordinate> viableLocations = new ArrayList<Coordinate>();
		for(Tile tile: world.allTiles()) {
			if(tile.getType().suitableStart) {
				viableLocations.add(tile.getCoordinate());
			}
		}
		
		for(BotcivPlayer player: players) {
			Coordinate startLocation = viableLocations.get(rand.nextInt(viableLocations.size()));
			player.setLastFocus(startLocation.left().left().up());
			
			world.getTileAt(startLocation).addUnit(new Unit(UnitType.TYPES.get("Population"),player));
			player.addExploredTile(startLocation);
			player.addExploredTile(new Coordinate(startLocation.x-1,startLocation.y));
			player.addExploredTile(new Coordinate(startLocation.x+1,startLocation.y));
			player.addExploredTile(new Coordinate(startLocation.x,startLocation.y-1));
			player.addExploredTile(new Coordinate(startLocation.x,startLocation.y+1));
		}
	}
	
}
