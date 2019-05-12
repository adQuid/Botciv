package aibrain;

import java.util.ArrayList;
import java.util.List;

import aibrain.Game;

/**
 * A representation of a path the AI could take, as well as the expected score from taking that path.
 */
public class HypotheticalResult {

	private Score score;
	private Plan plan = new Plan();
	private List<List<List<Action>>> actionMemory;
	
	HypotheticalResult(Game game, Player empire, GameEvaluator evaluator) {
		this.score = evaluator.getValue(game, empire);
	}
	
	HypotheticalResult(Game game, Player empire, Plan plan, List<List<List<Action>>> actionMemory, GameEvaluator evaluator) {
		this.score = evaluator.getValue(game, empire);
		this.plan = plan;				
		this.actionMemory = actionMemory;
	}	
	
	HypotheticalResult(Game game, List<Action> actions, Player empire, List<Action> nextActions, Reasoning reason, GameEvaluator evaluator) {
		this.score = evaluator.getValue(game, empire);
		this.plan.addActionListToEnd(actions);
		this.plan.addActionListToEnd(nextActions);
	}
	
	HypotheticalResult(HypotheticalResult other){
		this.score = new Score(other.score);
		this.plan = new Plan(other.plan);	
		this.actionMemory = new ArrayList<List<List<Action>>>(other.actionMemory);
	}
	
	public Score getScore() {
		return score;
	}
	
	void setScore(Score score) {
		this.score = score;
	}
	
	public List<List<Action>> getActions() {
		return plan.getPlannedActions();
	}
	
	/**
	 * @return The actions from this plan that the AI would take this upcoming turn.
	 */
	public List<Action> getImmediateActions() {
		//I don't like this, but I'm going to put off fixing this for now
		if(plan.getPlannedActions().size() > 0) {
		  return plan.getPlannedActions().get(0);
		} else {
		  return new ArrayList<Action>();
		}
	}
	
	void appendActionsFront(List<Action> actions) {
		this.plan.addActionListFront(actions);
	}
	
	void appendActionsEnd(List<Action> actions) {
		this.plan.addActionListToEnd(actions);;
	}

	void appendActionsEnd(List<Action> actions, Reasoning reasonings) {
		this.plan.addActionListToEnd(actions);
	}
	
	public Plan getPlan() {
		return plan;
	}

	void setPlan(Plan plan) {
		this.plan = plan;
	}

	public List<List<List<Action>>> getActionMemory() {
		return actionMemory;
	}
	
	public void clearActionMemory() {
		for(List<List<Action>> current: actionMemory) {
			current.clear();
		}
	}

	public void removeActionListFromFront() {
		plan.removeActionListFromFront();
		actionMemory.remove(0);
	}
}
