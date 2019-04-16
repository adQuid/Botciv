package game;

import java.util.Map;

public class Unit {

	private UnitType type;
	private BotcivPlayer owner;
	
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
	

	
}
