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
		map.put("population",new BigDecimal(people.size()));
		
		int ownedTiles = 0;
		for(Tile current: game.world.allTiles()) {
			if(player.equals(current.getOwner())) {
				ownedTiles++;
			}
		}
		map.put("owned tiles", new BigDecimal(ownedTiles*0.1));
		
		return new Score(map);
	}

}
