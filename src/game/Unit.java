package game;

import java.util.Map;
import java.util.TreeMap;

import map.Coordinate;
import util.MiscUtilities;

public class Unit {
	
	public static class IDGenerator {	
		
		static long nextID = 1;
		
		public static String nextID(BotcivGame game, String parent) {
			if(parent == null) {
				return ""+nextID++;
			} else {
				int append = 1;
				while(game.getUnit(parent+"s"+append) != null) {
					append++;
				}
				return parent+"s"+append;
			}
		}
	}
	
	private String id; 
	private static final String ID_NAME = "id";
	private UnitType type;
	private static final String TYPE_NAME = "t";
	private int stackSize;
	private static final String STACK_SIZE_NAME = "q";
	private BotcivPlayer owner;
	private static final String OWNER_NAME = "p";
	private Tile location;
	private static final String HEALTH_NAME = "h";
	//if multiple units are in the stack, this represents the health of the unit at the "top" of the stack
	private int health; 
	
	public Unit(BotcivGame parent, UnitType type, BotcivPlayer owner) {
		this.id = IDGenerator.nextID(parent,null);
		this.type = type;
		this.stackSize = 1;
		this.owner = owner;
		this.health = type.getMaxHealth();
		parent.addUnit(this);
	}
	
	public Unit(BotcivGame parent, UnitType type, BotcivPlayer owner, int health) {
		this.id = IDGenerator.nextID(parent,null);
		this.type = type;
		this.stackSize = 1;
		this.owner = owner;
		this.health = health;
		parent.addUnit(this);
	}
	
	//makes a read-only copy of a unit. Might make this into a different class
	public Unit(Unit other) {
		this.id = other.id;
		this.type=other.type;
		this.stackSize = other.stackSize;
		this.owner = other.owner;
		this.health = other.health;
	}
	
	public Unit(Unit other, BotcivGame game, boolean isSplit) {
		if(isSplit) {
			this.id = IDGenerator.nextID(game,other.id);
		} else {
			this.id = other.id;
		}
		this.type=other.type;
		this.stackSize = other.stackSize;
		this.owner = game.playerByName(other.owner.getName());
		this.health = other.health;
		game.addUnit(this);
	}
	
	public Unit(Map<String,Object> map, BotcivGame game, Tile parent) {
		id = (String)map.get(ID_NAME);
		type = UnitType.TYPES.get(map.get(TYPE_NAME).toString());
		stackSize = MiscUtilities.extractInt(map.get(STACK_SIZE_NAME));
		owner = game.playerByName(map.get(OWNER_NAME).toString());
		health = MiscUtilities.extractInt(map.get(HEALTH_NAME).toString());
		location = parent;
		game.addUnit(this);
	}

	public Map<String,Object> toSaveString() {
		
		Map<String,Object> retval = new TreeMap<String,Object>();
		
		retval.put(ID_NAME, id);
		retval.put(TYPE_NAME, type.getId());
		retval.put(STACK_SIZE_NAME, stackSize);
		retval.put(OWNER_NAME, owner.getName());
		retval.put(HEALTH_NAME, health);
		
		return retval;
	}
	
	public void append(Unit other) {
		//I don't like that it fails silently, but I don't really want to deal with the exception passing
		if(!other.getType().equals(this.getType())) {
			System.err.println("Mismatched Types: "+getType()+" vs "+other.getType());
			return;
		}
		
		this.stackSize += other.stackSize;
		int maxHealth = type.getMaxHealth();
		int newHealth = maxHealth - (maxHealth - health) - (maxHealth - other.health);
		
		while(newHealth <= 0) {
			stackSize--;
			newHealth += maxHealth;
		}
		setHealth(newHealth);
	}
	
	public Unit split(int splitSize, BotcivGame game) {
		if(splitSize >= this.stackSize) {
			return this;
		}
		
		Unit retval = new Unit(this, game, true);		
		retval.setStackSize(splitSize);
		retval.setHealth(type.getMaxHealth());
		this.setStackSize(stackSize - splitSize);
		
		return retval;
	}
	
	public String toString() {
		return type.getName()+" "+id;
	}

	public UnitType getType() {
		return type;
	}

	public void setType(UnitType type) {
		this.type = type;
	}
	
	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
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
		this.health = Math.min(health, getType().getMaxHealth());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof Unit)) {
			return false;
		}
		Unit otherUnit = (Unit)other;
		return id.equals(otherUnit.id);
	}
	
}
