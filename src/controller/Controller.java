package controller;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import game.BotcivGame;
import game.BotcivPlayer;
import gui.BottomDisplay;
import map.MainUIMapDisplay;

public class Controller {

	public static Controller instance = null;

	private BotcivGame activeGame;
	
	public Controller(BotcivGame game) {
		activeGame = game;
	}
	
	public void commitTurn(List<Action> actions, BotcivPlayer player) {
		activeGame.setActionsForPlayer(actions, player);
		activeGame.endRound();	
		MainUIMapDisplay.repaintDisplay();
		BottomDisplay.resetDescription();
	}
	
	public BotcivGame getImageGame(BotcivPlayer player) {
		return (BotcivGame) activeGame.imageForPlayer(player);
	}	
}
