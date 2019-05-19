package controller;

import aibrain.AIBrain;
import aibrain.HypotheticalResult;
import game.BotcivGame;
import game.BotcivPlayer;

public class BrainThread implements Runnable{

	Controller host;
	
	public BrainThread(Controller host) {
		this.host = host;
	}
	
	@Override
	public void run() {
		while(true) {
			AIBrain brain = host.getNextBrain();
			
			if(brain != null) {
				BotcivGame imageGame = host.getImageGame((BotcivPlayer)brain.getSelf());
				HypotheticalResult hypothetical = brain.runAI(imageGame);
				host.commitTurn(hypothetical.getImmediateActions(), (BotcivPlayer)brain.getSelf());
			} else {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
