package controller;

import aibrain.AIBrain;
import aibrain.HypotheticalResult;
import game.BotcivPlayer;

public class BrainThread implements Runnable{

	@Override
	public void run() {
		while(true) {
			AIBrain brain = Controller.instance.getNextBrain();
			
			if(brain != null) {
				HypotheticalResult hypothetical = brain.runAI(Controller.instance.getImageGame((BotcivPlayer)brain.getSelf()));
				Controller.instance.commitTurn(hypothetical.getImmediateActions(), (BotcivPlayer)brain.getSelf());
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
