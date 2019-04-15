package game;

public class Unit {

	UnitType type;
	
	public Unit(UnitType type) {
		this.type = type;
	}
	
	public String toString() {
		return type.getName();
	}

	public UnitType getType() {
		return type;
	}
	
}
