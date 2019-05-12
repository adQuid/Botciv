package game.actions;

import java.util.HashMap;

import game.BotcivGame;
import game.BotcivPlayer;
import game.ResourcePortfolio;
import game.Unit;
import game.UnitType;
import map.Coordinate;
import util.GameLogicUtilities;
import util.MiscUtilities;

public class BuildUnit extends BotcivAction{

	Coordinate coord;
	UnitType type;
	
	public BuildUnit(Coordinate coord, UnitType type) {
		this.coord = coord;
		this.type = type;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof BuildUnit)) {
			return false;
		}
		BuildUnit castOther = (BuildUnit)other;
		return type == castOther.type && coord.equals(castOther.coord);
	}
	
	@Override
	public boolean isContingency() {
		return false;
	}

	@Override
	public void doAction(BotcivGame game, BotcivPlayer player) {
		ResourcePortfolio cost = new ResourcePortfolio();
		cost.materials = MiscUtilities.extractDouble(type.getAttribute("materialsCost"));
		if(GameLogicUtilities.tryTopay(player, cost)) {
			game.world.getTileAt(coord).addUnit(new Unit(type,player));
		}
	}

}
