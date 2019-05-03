package util;

import java.util.ArrayList;
import java.util.List;

import game.BotcivPlayer;
import game.ResourcePortfolio;
import game.Tile;
import game.Unit;
import game.UnitType;

public class GameLogicUtilities {

	public static boolean canAfford(BotcivPlayer player, ResourcePortfolio port) {
		return (player.getLabor() >= port.labor &&
				player.getMaterials() >= port.materials &&
				player.getInfluence() >= port.influence &&
				player.getWealth() >= port.wealth &&
				player.getEducation() >= port.education);
	}
	
	//returns true and deducts payment if the player could afford it
	public static boolean tryTopay(BotcivPlayer player, ResourcePortfolio port) {
		if(player.getLabor() >= port.labor &&
				player.getMaterials() >= port.materials &&
				player.getInfluence() >= port.influence &&
				player.getWealth() >= port.wealth &&
				player.getEducation() >= port.education) {
			player.addLabor(-port.labor);
			player.addMaterials(-port.materials);
			player.addInfluence(-port.influence);
			player.addWealth(-port.wealth);
			player.addEducation(-port.education);
			return true;
		} else {
			return false;
		}		
	}	
	
	public static List<UnitType> unitsBuildableAtTile(BotcivPlayer player, Tile tile) {
		List<Unit> unitsThatCouldBuild = new ArrayList<Unit>();
		for(List<Unit> unitList: tile.getUnits().values()) {
			if(unitList.get(0).getOwner() == player) {
				unitsThatCouldBuild.add(unitList.get(0));
			}
		}

		List<UnitType> retval = new ArrayList<UnitType>();
		for(Unit current: unitsThatCouldBuild) {
			if(current.getType().getAttribute("builds") != null) {
				List<String> types = (List<String>)current.getType().getAttribute("builds");
				for(String type: types) {//add tech requirements here
					retval.add(UnitType.TYPES.get(type));
				}
			}
		}
		
		
		return retval;
	}
}
