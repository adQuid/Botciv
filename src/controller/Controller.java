package controller;

import java.util.ArrayList;
import java.util.List;

import aibrain.AIBrain;
import aibrain.Action;
import aiconstructs.BitcivContingencyGenerator;
import aiconstructs.BotcivGameEvaluator;
import aiconstructs.BotcivIdeaGenerator;
import cloner.BotcivGameCloner;
import game.BotcivGame;
import game.BotcivPlayer;
import gui.BottomDisplay;
import gui.MainUI;
import map.MainUIMapDisplay;

public class Controller {

	public static Controller instance = null;

	private BotcivGame activeGame;
	
	private List<AIBrain> unprocessedBrains = new ArrayList<AIBrain>();
	private List<AIBrain> processedBrains = new ArrayList<AIBrain>();
	private List<Thread> brainThreads = new ArrayList<Thread>();
	
	private List<BotcivPlayer> playersWithTurnsSubmitted = new ArrayList<BotcivPlayer>();
	
	public Controller(BotcivGame game) {
		activeGame = game;
		
		for(BotcivPlayer current: game.players) {
			if(current.getPlayer() == false) {
				unprocessedBrains.add(new AIBrain(new BotcivPlayer(current), 6, 0, 10, new BotcivIdeaGenerator(), new BitcivContingencyGenerator(), new BotcivGameEvaluator(), new BotcivGameCloner()));
			}
		}
		brainThreads.add(new Thread(new BrainThread()));
		brainThreads.get(0).start();
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
		MainUI.updateGameDisplay();
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
	
}
