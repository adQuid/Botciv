package util;

import java.util.Comparator;
import java.util.Set;

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
	
	public void tradeFood() {
		double averageFood = 0.0;
		for(Tile current: tiles) {
			averageFood += current.food();
		}
		averageFood /= tiles.size();
		
		for(Tile current: tiles) {
			if(averageFood > 0.0) {
				System.out.println("This is making "+(current.food()-averageFood)+" food");
			}
			int growth = (int)((averageFood - current.population()) * UnitType.TYPES.get("population").getMaxHealth());
			System.out.println("Growth "+growth);
			for(Unit unit: current.getUnits().get(UnitType.TYPES.get("population"))) {
				while(growth > 0 && unit.getHealth() < unit.getType().getMaxHealth()) {
					unit.setHealth(unit.getHealth()+1);
					growth--;
				}
			}
			while(growth > 0) {
				Unit toAdd = new Unit(UnitType.TYPES.get("population"),current.getOwner(),
						Math.min(UnitType.TYPES.get("population").getMaxHealth(), growth));
				growth -= toAdd.getHealth();
				current.addUnit(toAdd);
			}
		}
	}
}
