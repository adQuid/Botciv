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
	private static final String HEALTH_NAME = "h";
	private int health;
	
	public Unit(UnitType type, BotcivPlayer owner) {
		this.type = type;
		this.owner = owner;
		this.id = nextID++;
		this.health = type.getMaxHealth();
	}
	
	public Unit(UnitType type, BotcivPlayer owner, int health) {
		this.type = type;
		this.owner = owner;
		this.id = nextID++;
		this.health = health;
	}
	
	public Unit(Unit other, BotcivGame game) {
		this.type=other.type;
		this.owner = game.playerByName(other.owner.getName());
		this.id = other.id;
		this.health = other.health;
	}
	
	public Unit(Map<String,Object> map, BotcivGame game, Tile parent) {
		id = MiscUtilities.extractInt(map.get(ID_NAME));
		type = UnitType.TYPES.get(map.get(TYPE_NAME).toString());
		owner = game.playerByName(map.get(OWNER_NAME).toString());
		health = MiscUtilities.extractInt(map.get(HEALTH_NAME).toString());
		location = parent;
	}

	public Map<String,Object> toSaveString() {
		
		Map<String,Object> retval = new TreeMap<String,Object>();
		
		retval.put(ID_NAME, id);
		retval.put(TYPE_NAME, type.getId());
		retval.put(OWNER_NAME, owner.getName());
		retval.put(HEALTH_NAME, health);
		
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
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof Unit)) {
			return false;
		}
		Unit otherUnit = (Unit)other;
		return id == otherUnit.id;
	}
	
}
