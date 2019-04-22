package util;

import game.BotcivPlayer;
import game.ResourcePortfolio;

public class GameLogicUtilities {

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
}
