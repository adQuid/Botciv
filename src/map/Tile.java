package map;

import java.awt.image.BufferedImage;
import util.ImageUtilities;

public class Tile {

	private int x;
	private int y;
		
	public Tile(int x, int y) {
		super();
		this.x = x;
		this.y = y;
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
		if(x*y + y > 20) {
			return ImageUtilities.importImage("tileTerrain/sea.png");	
		}
		return ImageUtilities.importImage("tileTerrain/grassland.png");
	}
	
}
