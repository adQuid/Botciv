package game;

import java.util.Map;

import map.Tile;

public class Unit {

	private UnitType type;
	private BotcivPlayer owner;
	private Tile location;
	
	public Unit(UnitType type, BotcivPlayer owner) {
		this.type = type;
		this.owner = owner;
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

	
}
