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
import util.MiscUtilities;

//this kind of belongs in the game package as well
public class Tile {

	
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
	private Map<UnitType,List<Unit>> units = new HashMap<UnitType,List<Unit>>();
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
		
		for(Entry<UnitType,List<Unit>> current: other.units.entrySet()) {
			List<Unit> toAdd = new ArrayList<Unit>();
			for(Unit unit: current.getValue()) {
				toAdd.add(new Unit(unit,game));
				toAdd.get(toAdd.size()-1).setLocation(this);
			}
			this.units.put(current.getKey(), toAdd);
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
			addUnit(toAdd);
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
		for(List<Unit> curList: units.values()) {
			for(Unit curUnit: curList) {
				unitsMap.add(curUnit.toSaveString());
			}
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
	
	public Map<UnitType,List<Unit>> getUnits() {
		return units;
	}
	
	public List<Unit> getAllUnits() {
		List<Unit> retval = new ArrayList<Unit>();
		for(List<Unit> current: units.values()) {
			retval.addAll(current);
		}		
		return retval;
	}
	
	public List<Unit> getUnitsByType(UnitType type) {
		if(units.get(type) == null) {
			return new ArrayList<Unit>();
		}
		return units.get(type);
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
		if(MainUI.visionDistance <= 15) {
			retval = ImageUtilities.importImage(type.getImage());	
		} else {
			retval = ImageUtilities.importImage(type.getImage().substring(0, type.getImage().length()-4)+"-tiny.png");
		}
		
		retval = ImageUtilities.scale(retval, width, height);
		
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
				retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.applyFactionColor(unit,units.get(current).get(0).getOwner()));
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
				retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.applyFactionColor(unit,units.get(type).get(0).getOwner()));	
			}
			
		}
			
		//borders with other nations
		if(MainUI.visionDistance <= 20 && owner != null) {
			if(!owner.equals(MainUI.getGame().world.getTileAt(this.getCoordinate().left()).getOwner())) {
				retval = ImageUtilities.layerImageOnImage(retval, 
						ImageUtilities.applyFactionColor(ImageUtilities.importImage("features/West Border.png"),owner));
			}
			if(!owner.equals(MainUI.getGame().world.getTileAt(this.getCoordinate().right()).getOwner())) {
				retval = ImageUtilities.layerImageOnImage(retval, 
						ImageUtilities.applyFactionColor(ImageUtilities.importImage("features/East Border.png"),owner));
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
		
		//selection
		if(selected) {
			retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("ui/selection.png"));
		}
		
		return retval;
	}
	
	public double tradePower() {
		double retval = 0;		
		for(Unit current: getAllUnits()) {
			retval += MiscUtilities.extractDouble(current.getType().getAttribute("tradePower"));
		}		
		return retval;
	}
	
	public double food() {
		List<Double> foodValues = new ArrayList<Double>();
		for(Unit current: getAllUnits()) {
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
		if(units.get(UnitType.TYPES.get("population")) != null) {
			for(Unit current: units.get(UnitType.TYPES.get("population"))) {
				retval++;
			}
		}
		return retval;
	}
}
