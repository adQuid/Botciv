package game;

import java.util.Map;
import java.util.TreeMap;

import map.Coordinate;
import util.MiscUtilities;

public class Unit {

	private static long nextID = 1;
	
	private long id; 
	private static final String ID_NAME = "id";
	private UnitType type;
	private static final String TYPE_NAME = "t";
	private BotcivPlayer owner;
	private static final String OWNER_NAME = "p";
	private Tile location;
	
	public Unit(UnitType type, BotcivPlayer owner) {
		this.type = type;
		this.owner = owner;
		this.id = nextID++;
	}
	
	public Unit(Unit other, BotcivGame game) {
		this.type=other.type;
		this.owner = game.playerByName(other.owner.getName());
		this.id = other.id;
	}
	
	public Unit(Map<String,Object> map, BotcivGame game, Tile parent) {
		id = MiscUtilities.extractInt(map.get(ID_NAME));
		type = UnitType.TYPES.get(map.get(TYPE_NAME).toString());
		owner = game.playerByName(map.get(OWNER_NAME).toString());
		location = parent;
	}

	public Map<String,Object> toSaveString() {
		
		Map<String,Object> retval = new TreeMap<String,Object>();
		
		retval.put(ID_NAME, id);
		retval.put(TYPE_NAME, type.getName());
		retval.put(OWNER_NAME, owner.getName());
		
		return retval;
	}
	
	public String toString() {
		return type.getName();
	}

	public UnitType getType() {
		return type;
	}

	public void setType(UnitType type) {
		this.type = type;
	}

	public BotcivPlayer getOwner() {
		return owner;
	}

	public void setOwner(BotcivPlayer owner) {
		this.owner = owner;
	}

	public Tile getLocation() {
		return location;
	}

	public void setLocation(Tile location) {
		this.location = location;
	}

	//doesn't quite meet the contract for equals
	public boolean matches(Unit other) {
		return id == other.id;
	}
	
}
