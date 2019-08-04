package util;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import game.BotcivGame;
import game.Tile;
import game.Unit;
import game.UnitType;

public class Market {

	public static MarketComparator comparator = new MarketComparator();
	
	public Tile hub;
	public Set<Tile> tiles;//the hub is included in the tiles list
	public double food;
	
	public Market(Tile hub, Set<Tile> tiles) {
		super();
		this.hub = hub;
		this.tiles = tiles;
	}
	
	private static class MarketComparator implements Comparator<Market> {
		@Override
		public int compare(Market o1, Market o2) {
			// TODO Auto-generated method stub
			return (int) (o1.hub.tradePower() - o2.hub.tradePower());
		}		
	}

	public void tradeFood(BotcivGame game) {
		double averageFood = 0.0;
		for(Tile current: tiles) {
			averageFood += current.food();
		}
		averageFood /= tiles.size();

		UnitType pop = UnitType.TYPES.get("population");
		
		int totalGrowth = 0;
		for(Tile current: tiles) {
			int growth = (int)((averageFood - current.population()) * pop.getMaxHealth());

			totalGrowth += Math.min(growth, current.population()*2);	
		}
		
		for(Tile current: tiles) {
			int growthRate = (int)Math.round(totalGrowth * migrationPreference());
			List<Unit> unitList = current.getUnitsByType(pop);
			if(unitList.size() > 0) {
				Unit unit = unitList.get(0);
				totalGrowth -= growthRate;
				while(growthRate > 0 && unit.getHealth() < unit.getType().getMaxHealth()) {
					unit.setHealth(unit.getHealth()+1);
					growthRate--;
				}
			}

			while(growthRate > 0) {
				Unit toAdd = new Unit(game, pop,current.getOwner(),
						Math.min(pop.getMaxHealth(), growthRate));
				growthRate -= toAdd.getHealth();
				current.addUnit(toAdd, game);
			}
		}
	}
	
	private double migrationPreference() {
		return 1.0 / tiles.size();
	}
}
