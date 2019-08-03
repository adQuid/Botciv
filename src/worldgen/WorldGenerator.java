package worldgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import game.World;
import map.Coordinate;

public class WorldGenerator {

	private static int plates = 18;
	private static Random rand = new Random();
	
	public static Map<Coordinate,Tile> generateTerrain(World world) {
		
		System.out.println("Building World...");
		// In the beginning God created the heaven and the earth.
		Atmosphere atmosphere = new Atmosphere(world);
		Map<Coordinate,Tile> tiles = new HashMap<Coordinate,Tile>();
		int worldTemp = 26 + rand.nextInt(24);
		
		//And God said, “Let the water under the sky be gathered to one place...
		for(int lat=0; lat<world.WORLD_SIZE; lat++) {
			for(int lon=0; lon<world.WORLD_SIZE; lon++) {
				Tile tile = new Tile(lon,lat,TileType.TYPES.get("Sea"));
				tiles.put(new Coordinate(lon,lat), tile);
			}
		}
				
		//...and let dry ground appear."
		for(int i=0; i < plates; i++) {
			Coordinate start = new Coordinate(rand.nextInt(world.WORLD_SIZE),rand.nextInt(world.WORLD_SIZE));
			int plateBase = rand.nextInt(650)-700;
			int tendrils = rand.nextInt(3) + rand.nextInt(3) + 1;
			Set<Coordinate> thisContinent = new HashSet<Coordinate>();
			
			for(int tend=0; tend<tendrils; tend++) {
				int xTravel = rand.nextInt(5) - 2;
				int yTravel = rand.nextInt(5) - 2;
				int length = rand.nextInt(34) + 1;
				int width = rand.nextInt(9)+2;

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
					if(rand.nextFloat() < 0.4) {
						xTravel += rand.nextInt(2) - 1;
					}
					if(rand.nextFloat() < 0.4) {
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
			tile.setTemperature(worldTemp - (17*(Math.abs(tile.getY() - (world.WORLD_SIZE/2)))/16));
		}
		
		//checking for rainfall
		for(Tile tile: tiles.values()) {
			if(tile.getAltitude() < 0 && tile.getTemperature() > -10) {
				atmosphere.spreadRainfall(tile.getCoordinate(), tiles, world.WORLD_SIZE, Direction.NONE, rand);
			}
		}
			
		//other tempurature adjustments
		for(Tile tile: tiles.values()) {
				
			tile.setTemperature(14 + (int)(((tile.getTemperature()-14))/(1+(Math.sqrt(tile.getRainfall()/35)))));

			if(tile.getAltitude() >= 200) {
				tile.setTemperature(tile.getTemperature() - (tile.getAltitude()/200));
			}
		}
		
		//flow temperature
		for(Tile tile: tiles.values()) {
			atmosphere.flowTempurature(tile,tiles.get(tile.getCoordinate().left()));
			atmosphere.flowTempurature(tile,tiles.get(tile.getCoordinate().right()));
			atmosphere.flowTempurature(tile,tiles.get(tile.getCoordinate().up()));
			atmosphere.flowTempurature(tile,tiles.get(tile.getCoordinate().down()));
		}
		
		//final decisions
		for(Tile tile: tiles.values()) {
			calculateBiome(tile);
			if(tile.getAltitude() > -1) {
				landTiles++;
			}
		}
				
		System.out.println(landTiles + " tiles of land, "+worldTemp+" max world temp");
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
		
		if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().left())) > 250) {
			retval++;
		}else if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().left())) < -250) {
			retval--;
		}
		if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().right())) > 250) {
			retval++;
		}else if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().right())) < -250) {
			retval--;
		}
		if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().up())) > 250) {
			retval++;
		}else if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().up())) < -250) {
			retval--;
		}
		if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().down())) > 250) {
			retval++;
		}else if(tile.getAltitude() - altOrNull(tiles.get(tile.getCoordinate().down())) < -250) {
			retval--;
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
			
	private static void calculateBiome(Tile tile) {
		
		if(tile.getType() == TileType.TYPES.get("Mountain")) {
			return;
		}
		if(tile.getTemperature() < -10) {
			tile.setType(TileType.TYPES.get("Ice Cap"));
		} else if(tile.getAltitude() > 0 && tile.getTemperature() < 5 && tile.getRainfall() > 0){
			tile.setType(TileType.TYPES.get("Tundra"));
		}else {
			if(tile.getAltitude() > -1) {
				if(tile.getRainfall() - (Math.max(0,tile.getTemperature()/6)) > 65 && tile.getTemperature() > 19) {
					tile.setType(TileType.TYPES.get("Jungle"));
				}else if(tile.getRainfall() - (Math.max(0,tile.getTemperature()/6)) > 48) {
					tile.setType(TileType.TYPES.get("Forest"));
				} else if(tile.getRainfall() - (Math.max(0,tile.getTemperature())/7) > 15) {
					tile.setType(TileType.TYPES.get("Grassland"));
				} else if(tile.getRainfall() - (Math.max(0,tile.getTemperature())/8) > 2){
					tile.setType(TileType.TYPES.get("Plain"));
				} else {
					tile.setType(TileType.TYPES.get("Desert"));
				}
			} 
		}
	}
	
	public static void establishStartLocations(World world, List<BotcivPlayer> players, BotcivGame game) {

		List<Coordinate> viableLocations = new ArrayList<Coordinate>();
		for(Tile tile: world.allTiles()) {
			if(tile.getType().suitableStart) {
				viableLocations.add(tile.getCoordinate());
			}
		}
		
		
	for(BotcivPlayer player: players) {
			Coordinate startLocation = viableLocations.remove(rand.nextInt(viableLocations.size()));
			player.setLastFocus(startLocation);
			
			world.getTileAt(startLocation).addUnit(new Unit(game, UnitType.TYPES.get("population"),player),game);
			player.addExploredTile(startLocation);
			player.addExploredTile(new Coordinate(startLocation.x-1,startLocation.y));
			player.addExploredTile(new Coordinate(startLocation.x+1,startLocation.y));
			player.addExploredTile(new Coordinate(startLocation.x,startLocation.y-1));
			player.addExploredTile(new Coordinate(startLocation.x,startLocation.y+1));
		}
	}
	
}
