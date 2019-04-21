package game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import gui.MainUI;
import gui.UnitDisplayComparator;
import map.Coordinate;
import util.ImageUtilities;

//this kind of belongs in the game package as well
public class Tile {

	private int x;
	private int y;	
	private TileType type;
	private Map<UnitType,List<Unit>> units = new HashMap<UnitType,List<Unit>>();
	
	private boolean selected;
	
	//debug
	private boolean copied = false;
	
	public Tile(int x, int y, TileType type) {
		super();
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public Tile(Tile other, BotcivGame game) {
		this.x = other.x;
		this.y = other.y;
		this.type = other.type;
		
		for(Entry<UnitType,List<Unit>> current: other.units.entrySet()) {
			List<Unit> toAdd = new ArrayList<Unit>();
			for(Unit unit: current.getValue()) {
				toAdd.add(new Unit(unit,game));
				toAdd.get(toAdd.size()-1).setLocation(this);
			}
			this.units.put(current.getKey(), toAdd);
		}
		
		//debug
		this.copied = true;
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
	
	public Coordinate getCoordinate() {
		return new Coordinate(x,y);
	}
	
	public Map<UnitType,List<Unit>> getUnits(){
		return units;
	}
	
	public void addUnit(Unit toAdd) {
		if(units.get(toAdd.getType()) == null) {
			units.put(toAdd.getType(), new ArrayList<Unit>());
		}
		units.get(toAdd.getType()).add(toAdd);
		toAdd.setLocation(this);
	}
	
	public void removeUnit(Unit toRemove) {
		for(Unit current: units.get(toRemove.getType())) {
			if(current == toRemove) {
				units.get(toRemove.getType()).remove(toRemove);
				if(units.get(toRemove.getType()).size() == 0) {
					units.remove(toRemove.getType());
				}
				return;
			}
		}
	}
	
	public TileType getType() {
		return type;
	}
	
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	
	public Coordinate getRelativeCoordinate(Coordinate viewPoint) {
		return new Coordinate(x,y);
	}
		
	public BufferedImage image() {
		BufferedImage retval = ImageUtilities.importImage(type.getImage());	
		
		if(MainUI.visionDistance <= 10 && units.size() > 0) {
			List<List<UnitType>> displayCategories = new ArrayList<List<UnitType>>();
			for(int i=0; i<10; i++) {
				displayCategories.add(new ArrayList<UnitType>());
			}
			for(UnitType current: units.keySet()) {
				displayCategories.get(current.getDisplayClass()).add(current);
			}
			for(int i=0; i<10; i++) {
				Collections.sort(displayCategories.get(i),new UnitDisplayComparator());				
			}
			for(UnitType current: displayCategories.get(0)) {
				BufferedImage unit = ImageUtilities.importImage(current.getImage());
				retval = ImageUtilities.layerImageOnImage(retval, unit);
			}
			for(int i=1; i<10; i++) {
				if(!displayCategories.get(i).isEmpty()) {
					BufferedImage unit = ImageUtilities.importImage(displayCategories.get(i).get(0).getImage());
					retval = ImageUtilities.layerImageOnImage(retval, unit);	
				}
			}
			
		}
				
		if(selected) {
			retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("ui/selection.png"));
		}
		
		return retval;
	}
	
}
