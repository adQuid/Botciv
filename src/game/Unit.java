package game;

import java.util.Map;

import map.Coordinate;

public class Unit {

	private static long nextID = 1;
	
	private long id;
	private UnitType type;
	private BotcivPlayer owner;
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
