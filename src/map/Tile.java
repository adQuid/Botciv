package map;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import game.TileType;
import game.Unit;
import game.UnitType;
import util.ImageUtilities;

//this kind of belongs in the game package as well
public class Tile {

	private int x;
	private int y;
	
	private TileType type;
	
	private Map<UnitType,List<Unit>> units = new HashMap<UnitType,List<Unit>>();
	
	public Tile(int x, int y, TileType type) {
		super();
		this.x = x;
		this.y = y;
		this.type = type;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public Map<UnitType,List<Unit>> getUnits(){
		return units;
	}
	
	public void addUnit(Unit toAdd) {
		if(units.get(toAdd.getType()) == null) {
			units.put(toAdd.getType(), new ArrayList<Unit>());
		}
		units.get(toAdd.getType()).add(toAdd);
	}
	
	public Coordinate getRelativeCoordinate(Coordinate viewPoint) {
		return new Coordinate(x,y);
	}
	
	public Tile west() {
		return null;
	}
	
	public Tile east() {
		return null;
	}
	
	public Tile north() {
		return null;
	}
	
	public Tile south() {
		return null;
	}
	
	public BufferedImage image() {
		return ImageUtilities.importImage(type.getImage());	
	}
	
}
