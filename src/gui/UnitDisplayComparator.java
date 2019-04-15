package gui;

import java.util.Comparator;

import game.UnitType;

public class UnitDisplayComparator implements Comparator<UnitType>{

	@Override
	public int compare(UnitType arg0, UnitType arg1) {
		// backwards so we can just grab element 0
		return arg1.getDisplayImportance() - arg0.getDisplayImportance();
	}

}
