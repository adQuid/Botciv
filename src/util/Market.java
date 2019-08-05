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
	public double food;
	
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
		double averageFood = 0.0;
		int totalPopulation = 0;
		for(Node current: nodes) {
			Tile tile = game.world.getTileAt(current.coord);
			averageFood += tile.food();
			totalPopulation += tile.population();
		}
			
		int totalGrowth = Math.max(0, Math.min((int)((averageFood - totalPopulation) * pop.getMaxHealth()), totalPopulation * 2)); 
		
		averageFood /= nodes.size();	
		
		for(Node current: nodes) {
			Tile tile = game.world.getTileAt(current.coord);
			int growthRate = (int)Math.round(totalGrowth * migrationPreference());
			List<Unit> unitList = tile.getUnitsByType(pop);
			if(unitList.size() > 0) {
				Unit unit = unitList.get(0);
				totalGrowth -= growthRate;
				while(growthRate > 0 && unit.getHealth() < unit.getType().getMaxHealth()) {
					unit.setHealth(unit.getHealth()+1);
					growthRate--;
				}
			}

			while(growthRate > 0) {
				Unit toAdd = new Unit(game, pop,tile.getOwner(),
						Math.min(pop.getMaxHealth(), growthRate));
				growthRate -= toAdd.getHealth();
				tile.addUnit(toAdd, game);
			}
		}
	}
	
	private double migrationPreference() {
		return 1.0 / nodes.size();
	}
}
