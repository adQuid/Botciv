package worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import game.Tile;
import game.TileType;
import game.World;
import map.Coordinate;

public class Atmosphere {

	double[] windspeed;
	double[] percipitation;
	Random rand;
	
	public Atmosphere(World world) {
		windspeed = new double[world.WORLD_SIZE];
		percipitation = new double[world.WORLD_SIZE];
		for(int i=0; i<windspeed.length; i++) {
			int lattitude = world.lattitude(new Coordinate(0,i));
			double toAdd = 0.0;
			if(lattitude >=30 && lattitude <= 60) {
				toAdd = 0.8;
			}else {
				toAdd = -0.8;
			}
			
			//toAdd *= 2.0/(0.5+(Math.min(Math.min(Math.abs(lattitude), Math.abs(lattitude-30)), Math.abs(lattitude-60))));
					
			windspeed[i] = toAdd;
			percipitation[i] = Math.min(1.0-(0.8/Math.abs((lattitude/6)-5)), 1.0);
		}
	}
	
	private static class CoordinateDirectionPair{
		public Coordinate coord;
		public Direction direction;
		public CoordinateDirectionPair(Coordinate coord, Direction direction) {
			this.coord = coord;
			this.direction = direction;
		}
		public boolean equals(Object other) { //super unsafe; I don't care
			CoordinateDirectionPair otherPair = (CoordinateDirectionPair)other;
			return this.coord.equals(otherPair.coord) && this.direction==otherPair.direction;
		}
	}
	
	public void spreadRainfall(Coordinate start, Map<Coordinate,Tile> tiles, int size, Direction direction, Random rand) {
		List<List<CoordinateDirectionPair>> arr = new ArrayList<List<CoordinateDirectionPair>>(); 
		int spread = 12;
		for(int i=0; i <= spread; i++) {
			arr.add(new ArrayList<CoordinateDirectionPair>());
		}
		
		arr.get(0).add(new CoordinateDirectionPair(start,null));
		
		for(int index=0; index < spread; index++) {
			while(arr.get(index).size() > 0) {
				Coordinate coord = arr.get(index).get(0).coord.wrap(size);

				int rainToAdd = rand.nextDouble() < (0.3 * percipitation[coord.y])?1:0;
				
				tiles.get(coord).setRainfall(tiles.get(coord).getRainfall() + rainToAdd);
				
				if(tiles.get(coord).getType() != TileType.TYPES.get("Mountain")) {
					addConditionally(tiles, size, arr.get(index+1), coord, Direction.NONE, rand);
				}
				if(direction != Direction.UP 
						&& tiles.get(coord.up()) != null 
						&& tiles.get(coord.up()).getType() != TileType.TYPES.get("Mountain")) {
					addConditionally(tiles, size, arr.get(index+1),coord.up(), Direction.UP, rand);
				} else {
					tiles.get(coord).setRainfall(tiles.get(coord).getRainfall() + (int)Math.round(rand.nextDouble()+0.1));
				}
				if(direction != Direction.DOWN 
						&& tiles.get(coord.down()) != null 
						&& tiles.get(coord.down()).getType() != TileType.TYPES.get("Mountain")) {
					addConditionally(tiles, size, arr.get(index+1),coord.down(), Direction.DOWN, rand);
				} else {
					tiles.get(coord).setRainfall(tiles.get(coord).getRainfall() + (int)Math.round(rand.nextDouble()+0.1));
				}
				if(direction != Direction.RIGHT
						&& tiles.get(coord.left()) != null 
						&& tiles.get(coord.left()).getType() != TileType.TYPES.get("Mountain")) {
					addConditionally(tiles, size, arr.get(index+1),coord.left(), Direction.LEFT, rand);
				} else {
					tiles.get(coord).setRainfall(tiles.get(coord).getRainfall() + (int)Math.round(rand.nextDouble()+0.1));
				}
				if(direction != Direction.LEFT
						&& tiles.get(coord.right()) != null 
						&& tiles.get(coord.right()).getType() != TileType.TYPES.get("Mountain")) {
					addConditionally(tiles, size, arr.get(index+1),coord.right(), Direction.RIGHT, rand);
				} else {
					tiles.get(coord).setRainfall(tiles.get(coord).getRainfall() + (int)Math.round(rand.nextDouble()+0.1));
				}

				arr.get(index).remove(0);
			}
		}				
	}
	
	public void flowTempurature(Tile tile, Tile other) {
		if(other == null) {
			return;
		}
		if(Math.abs(other.getTemperature() - tile.getTemperature()) > 5) {
			tile.setTemperature(tile.getTemperature() + (other.getTemperature() - tile.getTemperature())/5);
		}
	}
	
	private void addConditionally(Map<Coordinate,Tile> tiles, int size, List<CoordinateDirectionPair> list, Coordinate tile, Direction direction, Random rand) {
		double wind = windspeed[tile.y];
		
		if(direction == Direction.LEFT && rand.nextDouble() > (wind < 0?0.8:0.15)) {
			return;
		}
		if(direction == Direction.RIGHT && rand.nextDouble() > (wind > 0?0.8:0.15)) {
			return;
		}
		if(direction == Direction.UP && rand.nextDouble() > (wind > 0?0.8:0.15))  {
			return;
		}
		if(direction == Direction.DOWN && rand.nextDouble() > (wind < 0?0.8:0.15)) {
			return;
		}
		if(tiles.get(tile.wrap(size)) != null) {
			CoordinateDirectionPair toAdd = new CoordinateDirectionPair(tile,direction);
			if(!list.contains(toAdd)) {
				list.add(toAdd);
			} 
		}			
	}
	
}
