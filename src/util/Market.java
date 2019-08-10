package util;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import game.BotcivGame;
import game.Tile;
import game.Unit;
import game.UnitType;
import map.Coordinate;

public class Market {

	public static MarketComparator comparator = new MarketComparator();
	
	public Tile hub;
	public Set<Node> nodes;//the hub is included in the tiles list
	private double totalGrowth;
	private double totalMigrationPopularity;
	
	
	public Market(Tile hub, Set<Node> tiles) {
		super();
		this.hub = hub;
		this.nodes = tiles;
	}
	
	private static class MarketComparator implements Comparator<Market> {
		@Override
		public int compare(Market o1, Market o2) {
			// TODO Auto-generated method stub
			return (int) (o1.hub.tradePower() - o2.hub.tradePower());
		}		
	}

	public void tradeFood(BotcivGame game) {
		UnitType pop = UnitType.TYPES.get("population");
		double totalFood = 0.0;
		int totalPopulation = 0;
		for(Node current: nodes) {
			Tile tile = game.world.getTileAt(current.coord);
			totalPopulation += tile.population();
			totalFood += tile.food();	
		}
		
		totalGrowth = Math.min((int)((totalFood - totalPopulation) * pop.getMaxHealth()), totalPopulation * 2); 
		
		//growth from internal migration
		for(Node current: nodes) {
			Tile tile = game.world.getTileAt(current.coord);
			int change = Math.max(Math.min(tile.population(), idealPopulationHealth(tile) - tile.populationHealth()), -tile.population());
			totalGrowth += tile.adjustPopulation(game, (int)Math.min(totalGrowth, change));
		}
		
		if(totalGrowth < 0) {
			while(totalGrowth < 0) {
				for(Node current: nodes) {
					if(totalGrowth < 0) {
						Tile tile = game.world.getTileAt(current.coord);
						totalGrowth -= tile.adjustPopulation(game, -1);
					}
				}
			}
		} else {
			while(totalGrowth > 0) {
				for(Node current: nodes) {
					if(totalGrowth > 0) {
						Tile tile = game.world.getTileAt(current.coord);
						totalGrowth -= tile.adjustPopulation(game, 1);
					}
				}
			}
		}
	}
		
	private int idealPopulationHealth(Tile tile) {
		return 3 * UnitType.TYPES.get("population").getMaxHealth();
	}
	
	public boolean inMarket(Coordinate coord) {
		for(Node node: nodes) {
			if(node.coord.equals(coord)) {
				return true;
			}
		}
		return false;
	}
}
