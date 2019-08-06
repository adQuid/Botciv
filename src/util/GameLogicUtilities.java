package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.BotcivGame;
import game.BotcivPlayer;
import game.ResourcePortfolio;
import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import game.World;
import map.Coordinate;

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
		if(player == null || tile == null) {
			return new ArrayList<UnitType>();
		}
		
		List<UnitType> retval = new ArrayList<UnitType>();
		if(player.equals(tile.getOwner())) {
			for(UnitType type: UnitType.TYPES.values()) {
				if(type.has("alwaysBuildable")) {
					retval.add(type);
				}
			}
		}
		
		List<Unit> unitsThatCouldBuild = new ArrayList<Unit>();
		for(Unit unitList: tile.getUnits()) {
			if(unitList.getOwner() == player) {
				unitsThatCouldBuild.add(unitList);
			}
		}

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
	
	private static class MarketTileComparator implements Comparator<Tile> {
		@Override
		public int compare(Tile arg0, Tile arg1) {
			if(arg0.tradePower() < arg1.tradePower()) {
				return 1;
			} else if (arg0.tradePower() > arg1.tradePower()){
				return -1;
			} else {
				return 0;
			}
		}		
	}
	
	public static List<Market> calculateMarkets(BotcivGame game) {
		List<Tile> tiles = new ArrayList<Tile>(game.world.allTiles());
		Set<Tile> tilesInMarket = new HashSet<Tile>();
		List<Market> markets = new ArrayList<Market>();
		Collections.sort(tiles, new MarketTileComparator());
				
		for(Tile current: tiles) {
			if(current.getOwner() != null && !tilesInMarket.contains(current)) {
				Set<Node> newArea = new HashSet<Node>();
				for(Node node: game.world.tilesWithinRange(current.getCoordinate(), current.tradePower()).getNodes()) {
					Tile tile = game.world.getTileAt(node.coord);
					if(!tilesInMarket.contains(game.world.getTileAt(node.coord)) && current.getOwner().equals(tile.getOwner())) {
						newArea.add(node);
						tilesInMarket.add(game.world.getTileAt(node.coord));
					}
				}
				markets.add(new Market(current,newArea));
			}
		}
		
		return markets;
	}
	
	public static void runMarkets(BotcivGame game) {
		List<Market> markets = calculateMarkets(game);
	
		for(Market current: markets) {
			current.tradeFood(game);
		}
		
	}
	
	public static ResourcePortfolio getResourceDeltas(World world, BotcivPlayer player) {
		List<Unit> units = world.getAllUnitsOfTypeByPlayer(null, player);
		ResourcePortfolio retval = new ResourcePortfolio();
		
		retval.influence = 10;//for now all governments generate 10 influence
				
		for(Unit current: units) {
			if(player.equals(current.getOwner())) {
				if( current.getHealth() == current.getType().getMaxHealth()) {
					retval.labor = MiscUtilities.addTo(retval.labor,current.getType().getAttribute("laborGeneration"),current.getStackSize());
					retval.materials = MiscUtilities.addTo(retval.materials,current.getType().getAttribute("materialsGeneration"),current.getStackSize());
					retval.influence = MiscUtilities.addTo(retval.influence,current.getType().getAttribute("influenceGeneration"),current.getStackSize());
					retval.wealth = MiscUtilities.addTo(retval.wealth,current.getType().getAttribute("wealthGeneration"),current.getStackSize());
					retval.education = MiscUtilities.addTo(retval.education,current.getType().getAttribute("educationGeneration"),current.getStackSize());
				} else {
					retval.labor = MiscUtilities.addTo(retval.labor,current.getType().getAttribute("laborGeneration"),current.getStackSize()-1);
					retval.materials = MiscUtilities.addTo(retval.materials,current.getType().getAttribute("materialsGeneration"),current.getStackSize()-1);
					retval.influence = MiscUtilities.addTo(retval.influence,current.getType().getAttribute("influenceGeneration"),current.getStackSize()-1);
					retval.wealth = MiscUtilities.addTo(retval.wealth,current.getType().getAttribute("wealthGeneration"),current.getStackSize()-1);
					retval.education = MiscUtilities.addTo(retval.education,current.getType().getAttribute("educationGeneration"),current.getStackSize()-1);
				}
			}
		}
		
		for(Tile current: world.allTiles()) {
			if(player.equals(current.getOwner())) {
				retval.influence--;
			}
		}
		return retval;
	}
	
	public static boolean unitCanTraverse(Unit unit, Tile destination) {
		return destination.getType().landPassable;
	}
}
