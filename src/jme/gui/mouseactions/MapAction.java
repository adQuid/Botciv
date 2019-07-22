package jme.gui.mouseactions;

import map.Coordinate;

public abstract class MapAction implements ClickAction{

	public void doAction(Object input) {
		Coordinate coord = (Coordinate)input;
		doAction(coord);
	}
	
	public abstract void doAction(Coordinate coord);
	
}
