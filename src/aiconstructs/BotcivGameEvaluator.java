package aiconstructs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aibrain.Game;
import aibrain.GameEvaluator;
import aibrain.Player;
import aibrain.Score;
import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import game.Unit;
import game.UnitType;

public class BotcivGameEvaluator implements GameEvaluator{

	@Override
	public Score getValue(Game arg0, Player arg1) {
		BotcivGame game = (BotcivGame)arg0;
		BotcivPlayer player = (BotcivPlayer)arg1;
		
		Map<String,BigDecimal> map = new HashMap<String,BigDecimal>();
		
		List<Unit> people = game.world.getAllUnitsOfTypeByPlayer(UnitType.TYPES.get("population"), player);
		int totalPopulation = 0;
		for(Unit unit: people) {
			totalPopulation += unit.getStackSize();
		}
		map.put("population",new BigDecimal(totalPopulation));
		
		map.put("explored tiles", new BigDecimal(player.getExploredTiles().size()));
		
		return new Score(map);
	}

}
