package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import aibrain.AIBrain;
import aibrain.Action;
import aiconstructs.BitcivContingencyGenerator;
import aiconstructs.BotcivGameEvaluator;
import aiconstructs.BotcivIdeaGenerator;
import cloner.BotcivGameCloner;
import game.BotcivGame;
import game.BotcivPlayer;
import jme.gui.MainUI;

public class Controller {

	public static Controller instance = null;

	private boolean test;
	private BotcivGame activeGame;
	
	private List<AIBrain> unprocessedBrains = new ArrayList<AIBrain>();
	private List<AIBrain> processedBrains = new ArrayList<AIBrain>();
	private List<Thread> brainThreads = new ArrayList<Thread>();
	
	private List<BotcivPlayer> playersWithTurnsSubmitted = new ArrayList<BotcivPlayer>();
	
	public static void setController(BotcivGame game) {
		instance = new Controller(game,false);
	}
	
	public Controller(BotcivGame game) {
		this(game, true);
	}
	
	private Controller(BotcivGame game, boolean test) {
		this.test = test;
		activeGame = game;
		
		for(BotcivPlayer current: game.players) {
			if(current.getPlayer() == false) {
				unprocessedBrains.add(new AIBrain(new BotcivPlayer(current), 6, 0, 10, new BotcivIdeaGenerator(), new BitcivContingencyGenerator(), new BotcivGameEvaluator(), new BotcivGameCloner()));
			}
		}
		//hard-coded 4 threads for now, same as the UI
		brainThreads.add(new Thread(new BrainThread(this)));
		brainThreads.add(new Thread(new BrainThread(this)));
		brainThreads.add(new Thread(new BrainThread(this)));
		brainThreads.add(new Thread(new BrainThread(this)));
		brainThreads.get(0).start();
		brainThreads.get(1).start();
		brainThreads.get(2).start();
		brainThreads.get(3).start();
	}
	
	public synchronized void commitTurn(List<Action> actions, BotcivPlayer player) {
		System.out.println("turn committed for "+player.getName());
		activeGame.setActionsForPlayer(actions, player);
		playersWithTurnsSubmitted.add(player);
		if(allPlayersHaveSubmittedTurns()) {
			endTurn();
		}
	}
	
	public void endTurn() {
		System.out.println("ending round");
		activeGame.endRound();	
		playersWithTurnsSubmitted.clear();
		unprocessedBrains.addAll(processedBrains);
		processedBrains.clear();
		System.out.println("round "+activeGame.turn+" complete");
		if(!test) {
			MainUI.updateGameDisplay();
		}
	}
	
	private boolean allPlayersHaveSubmittedTurns() {
		for(BotcivPlayer current: activeGame.players) {
			if(!playersWithTurnsSubmitted.contains(current)) {
				return false;
			}
		}
		return true;
	}
	
	public BotcivGame getImageGame(BotcivPlayer player) {
		return (BotcivGame) activeGame.imageForPlayer(player);
	}	
	
	synchronized AIBrain getNextBrain() {
		if(unprocessedBrains.size() == 0) {
			return null;
		}
		
		AIBrain retval = unprocessedBrains.remove(0);
		processedBrains.add(retval);
		return retval;
	}
	
	public void saveGame() {
		Map<String,Object> saveState = activeGame.saveString();
		Gson gson = new Gson();
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("saves/test.savegam"));
			writer.write(gson.toJson(saveState));
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
