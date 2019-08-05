package util;

import game.Tile;
import map.Coordinate;

public class Node {
	public Coordinate coord;
	public Coordinate origin;
	
	public Node(Coordinate coord, Coordinate origin) {
		this.coord = coord;
		this.origin = origin;
	}
}