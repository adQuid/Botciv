package jme.gui.descriptionwrappers;

import game.UnitType;

public class BuildSpecificUnitDescriptionWrapper extends DescriptionWrapper{

	UnitType type;
	
	public BuildSpecificUnitDescriptionWrapper(UnitType type) {
		this.type = type;
	}
	
	@Override
	public String getDescription() {
		String retval = type.getTooltip() + " (costs";
		if(type.has("laborCost")) {
			retval += "  "+type.getAttribute("laborCost")+" labor";
		}
		if(type.has("laborCost")) {
			retval += "  "+type.getAttribute("materialsCost")+" materials";
		}
		
		retval += ")";
		return retval;
	}

}
