package game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jme.gui.MainUI;
import map.Coordinate;
import util.ImageUtilities;
import util.MiscUtilities;

//this kind of belongs in the game package as well
public class Tile {

	private class UnitDisplayComparator implements Comparator<UnitType>{
		@Override
		public int compare(UnitType arg0, UnitType arg1) {
			// backwards so we can just grab element 0
			return arg1.getDisplayImportance() - arg0.getDisplayImportance();
		}
	}
	
	//STATEFUL VALUES THAT ARE SAVED
	private int x;
	private static final String X_NAME = "x";
	private int y;	
	private static final String Y_NAME = "y";
	private int altitude = -2000;
	private static final String ALTITUDE_NAME = "a";
	private int temperature = 14;
	private static final String TEMPERATURE_NAME = "t";
	private int rainfall = 0;
	private static final String RAINFALL_NAME = "r";
	private TileType type;
	private static final String TYPE_NAME = "tp";
	private List<Unit> units = new ArrayList<Unit>();
	private static final String UNITS_NAME = "u";
	private BotcivPlayer owner = null;
	private static final String OWNER_NAME = "o";
	
	//STATELESS VALUES THAT ARENT SAVED
	private boolean selected;
		
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
		this.altitude = other.altitude;
		this.temperature = other.temperature;
		this.rainfall = other.rainfall;
		
		for(Unit unit: other.units) {
			Unit toAdd = new Unit(unit,game,false);
			toAdd.setLocation(this);
			addUnit(toAdd,game);
		}

		this.owner = other.owner;
	}
	
	public Tile(Map<String,Object> map, BotcivGame game) {
		x = MiscUtilities.extractInt(map.get(X_NAME).toString());
		y = MiscUtilities.extractInt(map.get(Y_NAME).toString());
		altitude = MiscUtilities.extractInt(map.get(ALTITUDE_NAME).toString());
		temperature = MiscUtilities.extractInt(map.get(TEMPERATURE_NAME).toString());
		rainfall = MiscUtilities.extractInt(map.get(RAINFALL_NAME).toString());
		type = TileType.TYPES.get(map.get(TYPE_NAME).toString());
		List<Map<String,Object>> unitList = (List<Map<String,Object>>)map.get(UNITS_NAME);
		for(Map<String,Object> unitMap: unitList) {
			Unit toAdd = new Unit(unitMap,game,this);
			addUnit(toAdd,game);
		}
		if(map.get(OWNER_NAME) != null) {
			owner = game.playerByName(map.get(OWNER_NAME).toString());
		}
	}
	
	public Map<String,Object> saveString(){
		Map<String,Object> retval = new HashMap<String,Object>();
		
		retval.put(X_NAME, x);
		retval.put(Y_NAME, y);
		retval.put(ALTITUDE_NAME, altitude);
		retval.put(TEMPERATURE_NAME, temperature);
		retval.put(RAINFALL_NAME, rainfall);
		retval.put(TYPE_NAME, type.getName());
		
		List<Map<String,Object>> unitsMap = new ArrayList<Map<String,Object>>();
		for(Unit unit: units) {
			unitsMap.add(unit.toSaveString());
		}
		retval.put(UNITS_NAME, unitsMap);
		
		if(owner != null) {
			retval.put(OWNER_NAME, owner.getName());
		}
		
		return retval;
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
	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getRainfall() {
		return rainfall;
	}

	public void setRainfall(int rainfall) {
		this.rainfall = rainfall;
	}

	public Coordinate getCoordinate() {
		return new Coordinate(x,y);
	}
	
	public List<Unit> getUnits() {
		return units;
	}
		
	//if there's more than one stack, it will combine them
	public List<Unit> getUnitsByType(UnitType type) {
		List<Unit> retval = new ArrayList<Unit>();
		
		for(Unit unit: units) {
			if(unit.getType().equals(type)) {
				retval.add(unit);
			}
		}
		
		return retval;
	}
	
	public void addUnit(Unit toAdd, BotcivGame game) {
		if(getUnitsByType(toAdd.getType()).size() == 0) {
			units.add(toAdd);
		} else {
			//we just add it to the first one. Will probably bite me later
			getUnitsByType(toAdd.getType()).get(0).append(toAdd);
		}
		toAdd.setLocation(this);
	}
	
	public void addSplitUnit(Unit toAdd, BotcivGame game) {
		units.add(toAdd);
		toAdd.setLocation(this);
	}
	
	public void removeUnit(Unit toRemove) {
		Unit match = null;
		
		for(Unit current: units) {
			if(current.getId().equals(toRemove.getId())) {
				match = current;
			}
		}
		
		if(match != null) {
			units.remove(match);
		} else {
			System.err.println("Tried to remove unit "+toRemove.getId()+", but it doesn't exist!");
		}
	}
	
	public TileType getType() {
		return type;
	}
	
	public void setType(TileType type) {
		this.type = type;
	}
	
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	
	public Coordinate getRelativeCoordinate(Coordinate viewPoint) {
		return new Coordinate(x,y);
	}
		
	public BotcivPlayer getOwner() {
		return owner;
	}

	public void setOwner(BotcivPlayer owner) {
		this.owner = owner;
	}		
	
	public BufferedImage image(int width, int height) {
		BufferedImage retval;
		retval = ImageUtilities.importImage(type.getImage());	
		
		retval = ImageUtilities.scale(retval, width, height);
		
		if(units.size() > 0) {
			List<List<UnitType>> displayCategories = new ArrayList<List<UnitType>>();
			for(int i=0; i<10; i++) {
				displayCategories.add(new ArrayList<UnitType>());
			}
			for(Unit current: units) {				
				displayCategories.get(current.getType().getDisplayClass()).add(current.getType());
			}
			for(int i=0; i<10; i++) {
				Collections.sort(displayCategories.get(i),new UnitDisplayComparator());				
			}
			for(UnitType current: displayCategories.get(0)) {
				BufferedImage unit = ImageUtilities.importImage(current.getImage());
				retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.applyFactionColor(unit,getUnitsByType(current).get(0).getOwner()));
			}
			List<UnitType> layers = new ArrayList<UnitType>();
			for(int i=1; i<10; i++) {
				if(!displayCategories.get(i).isEmpty()) {
					layers.add(displayCategories.get(i).get(0));
				}
			}
			Collections.sort(layers, new UnitDisplayComparator());
			Collections.reverse(layers);//the comparator is backwards 
			for(UnitType type: layers) {
				BufferedImage unit = ImageUtilities.importImage(type.getImage());
				retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.applyFactionColor(unit,getUnitsByType(type).get(0).getOwner()));	
			}
			
		}
			
		//borders with other nations
		try {
		if(owner != null) {
			//Yes, east and west are backwards. This is required by JMonkey for some reason
			if(!owner.equals(MainUI.getGame().world.getTileAt(this.getCoordinate().left()).getOwner())) {
				retval = ImageUtilities.layerImageOnImage(retval, 
						ImageUtilities.applyFactionColor(ImageUtilities.importImage("features/East Border.png"),owner));
			}
			if(!owner.equals(MainUI.getGame().world.getTileAt(this.getCoordinate().right()).getOwner())) {
				retval = ImageUtilities.layerImageOnImage(retval, 
						ImageUtilities.applyFactionColor(ImageUtilities.importImage("features/West Border.png"),owner));
			}
			if(!owner.equals(MainUI.getGame().world.getTileAt(this.getCoordinate().up()).getOwner())) {
				retval = ImageUtilities.layerImageOnImage(retval, 
						ImageUtilities.applyFactionColor(ImageUtilities.importImage("features/North Border.png"),owner));
			}
			if(!owner.equals(MainUI.getGame().world.getTileAt(this.getCoordinate().down()).getOwner())) {
				retval = ImageUtilities.layerImageOnImage(retval, 
						ImageUtilities.applyFactionColor(ImageUtilities.importImage("features/South Border.png"),owner));
			}
		}
		} catch(Exception e) {
			if(MainUI.getGame() == null) System.out.println("game is null");
			if(MainUI.getGame().world == null) System.out.println("game is null");
			if(this.getCoordinate().left() == null) System.out.println("left is null");
			retval = ImageUtilities.importImage(TileType.TYPES.get("Sea").getImage());
		}
		
		//selection
		if(selected) {
			retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("ui/selection.png"));
		}
		
		return retval;
	}
	
	public double tradePower() {
		double retval = 0;		
		for(Unit current: getUnits()) {
			retval += MiscUtilities.extractDouble(current.getType().getAttribute("tradePower"));
		}		
		return retval;
	}
	
	public double food() {
		List<Double> foodValues = new ArrayList<Double>();
		for(Unit current: getUnits()) {
			if(current.getHealth() == current.getType().getMaxHealth()) {
				foodValues.add(MiscUtilities.extractDouble(current.getType().getAttribute("foodProduced")));
			}
		}		
		
		Collections.sort(foodValues);
		Collections.reverse(foodValues);
		double retval = 0;		
		for(int i=0; i<Math.min(type.foodValue.size(),population()); i++) {
			if(i < foodValues.size()) {
				retval += Math.max(0, foodValues.get(i) * type.foodValue.get(i));
			}
		}
		return retval;
	}
	
	public int population() {
		int retval = 0;		
		for(Unit current: getUnitsByType(UnitType.TYPES.get("population"))) {
			if(current.getHealth() == current.getType().getMaxHealth()) {
				retval += current.getStackSize();
			} else {
				retval += current.getStackSize()-1;
			}
		}		
		return retval;
	}
}
