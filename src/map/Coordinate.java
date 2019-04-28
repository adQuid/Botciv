package map;

import game.World;
import util.MiscUtilities;

public class Coordinate {

	public int x;
	public int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coordinate(String str) {
		this.x = MiscUtilities.extractInt(str.split(",")[0]);
		this.y = MiscUtilities.extractInt(str.split(",")[1]);
	}
	
	public String toString() {
		return x+","+y;
	}
	
	public Coordinate left() {
		return new Coordinate(x-1,y).wrap(World.WORLD_SIZE);
	}
	public Coordinate right() {
		return new Coordinate(x+1,y).wrap(World.WORLD_SIZE);
	}
	public Coordinate up() {
		return new Coordinate(x,y-1).wrap(World.WORLD_SIZE);
	}
	public Coordinate down() {
		return new Coordinate(x,y+1).wrap(World.WORLD_SIZE);
	}
	
	public int distanceTo(Coordinate other) {
		return new Double(Math.ceil(Math.sqrt(Math.pow(x-other.x, 2) + Math.pow(y-other.y, 2)))).intValue();
	}

	public Coordinate wrap(int size) {
		Coordinate modCoord = new Coordinate(x%size,y);

		while(modCoord.x < 0) {
			modCoord.x += size;
		}
		
		return modCoord;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
