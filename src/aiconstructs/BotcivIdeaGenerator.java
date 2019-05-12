package aiconstructs;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import aibrain.Game;
import aibrain.IdeaGenerator;
import aibrain.Player;
import game.BotcivGame;
import game.BotcivPlayer;
import game.Tile;
import game.UnitType;
import game.actions.BuildUnit;
import map.Coordinate;

public class BotcivIdeaGenerator implements IdeaGenerator{

	@Override
	public List<List<Action>> generateIdeas(Game arg0, Player arg1, int iteration) {
		BotcivGame game = (BotcivGame) arg0;
		BotcivPlayer player = (BotcivPlayer) arg1;		
		List<List<Action>> retval = new ArrayList<List<Action>>();
		
		List<Coordinate> myTiles = game.world.tilesOwnedByPlayer(player);
		
		List<Action> buildAFarm = new ArrayList<Action>();
		buildAFarm.add(new BuildUnit(myTiles.get(0),UnitType.TYPES.get("farm")));
		retval.add(buildAFarm);
		
		return retval;
	}

	@Override
	public boolean hasFurtherIdeas(Game arg0, Player arg1, List<Action> committedActions, int iteration) {
		return iteration < 2;
	}

}
