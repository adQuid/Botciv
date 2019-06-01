package aibrain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.ListUtils;

class Hypothetical {

	public static int numMade = 0;
	
	private int iteration;
	private AIBrain parent;
	private Player self;
	private int tightForcastLength;
	private int looseForcastLength;
	private int tail;
	private Game game;
	private List<Modifier> modifiers;
	//actions that the AI thought about before
	private List<List<List<Action>>> actionMemory;
	//this is a list of the actions the parent COULD have taken, not what it did
	private List<List<Action>> possibleParentActions;
	//this is a list of the actions actually selected by the parent step, and available to try again
	private List<Action> usedParentActions;
	//actions this hypothetical is already committed to taking
	private List<List<Action>> commitedActions;
	private Plan plan;
	
	private IdeaGenerator ideaGenerator; 
	
	private Score scoreAccumulator;
	
	public Hypothetical(Game game, AIBrain brain, List<List<List<Action>>> actionMemory, List<List<Action>> plannedActions, int forcast, int lookAheadSecondary, int tailLength, Player self, int iteration, IdeaGenerator ideaGenerator) {
		this(game, new ArrayList<Modifier>(), 
				brain, actionMemory, new ArrayList<List<Action>>(), new ArrayList<Action>(), plannedActions, forcast,
                lookAheadSecondary, tailLength, self, new Score(), iteration,ideaGenerator);
	}
	
	public Hypothetical(Game game, List<Modifier> modifiers, AIBrain parent, List<List<List<Action>>> actionMemory, List<List<Action>> possibleParentActions, 
			List<Action> usedParentActions, List<List<Action>> actions, int tightForcastLength, int looseForcastLength, 
			int tail, Player empire, Score scoreAccumulator, int iteration, IdeaGenerator ideaGen){
		this(game, modifiers, parent, actionMemory, possibleParentActions, usedParentActions, actions, tightForcastLength, looseForcastLength, tail, empire, scoreAccumulator, iteration, ideaGen, new Plan());
	}
	
	public Hypothetical(Game game, List<Modifier> modifiers, AIBrain parent, List<List<List<Action>>> actionMemory, List<List<Action>> possibleParentActions, 
			List<Action> usedParentActions, List<List<Action>> actions, int tightForcastLength, int looseForcastLength, 
			int tail, Player empire, Score scoreAccumulator, int iteration,  IdeaGenerator ideaGen, Plan plan){
		
		this.game = parent.getCloner().cloneGame(game);
		this.iteration = iteration;
		this.modifiers = modifiers;
		this.parent = parent;
		this.actionMemory = new ArrayList<List<List<Action>>>(actionMemory);
		this.possibleParentActions = possibleParentActions;
		this.usedParentActions = usedParentActions;
		this.commitedActions = new ArrayList<List<Action>>(actions);
		this.tightForcastLength = tightForcastLength;
		this.looseForcastLength = looseForcastLength;
		this.tail = tail;
		this.self = empire;
		this.scoreAccumulator = scoreAccumulator;
		this.ideaGenerator = ideaGen;
		this.plan = plan;
	}
	
	public HypotheticalResult calculate() {
		return calculate(false, null);
	}
	
	public HypotheticalResult calculate(boolean debug, List<List<Action>> script) {
		
		//base case where I'm out of time
		Game futureGame = parent.getCloner().cloneGame(game);
		if(tightForcastLength == 0 && looseForcastLength == 0) {
			for(int index = 0; index < tail; index++) {
				futureGame.endRound();
				scoreAccumulator.addLayer(parent.getEvaluator().getValue(futureGame, self).getFirstLayer());
			}
			
			HypotheticalResult retval = new HypotheticalResult(futureGame, this.self,plan,actionMemory,parent.getEvaluator());
			retval.setScore(scoreAccumulator);
			return retval;
		}
		
		//these are the actions I look at when trying out new actions this turn
		List<List<Action>> possibleActions = ideaGenerator.generateIdeas(game, self, iteration);
		List<List<Action>> actionsToCheck = new ArrayList<List<Action>>(possibleActions);
		//these are the actions I pass down to the next level to not consider if I don't do them at this level
		List<List<Action>> passdownActions = ideaGenerator.generateIdeas(game, self, iteration);
		
		List<List<Action>> ideas = new ArrayList<List<Action>>();
		if(script != null) {
			ideas.add(script.remove(0));			
		} else {
			//remove all actions that the parent could have done, but didn't do
			possibleParentActions.remove(usedParentActions);		
			actionsToCheck.removeAll(possibleParentActions);
			//remove all actions that the AIBrain looked at before
			actionsToCheck.removeAll(actionMemory.get(depthInForecast()));

			ideas = actionsToCheck;
			//always include the empty idea
			ideas.add(0,new ArrayList<Action>());
		}
		//dangerously overwrite actionMemory
		actionMemory.set(depthInForecast(), possibleActions);
		
		//add score from this round
		HypotheticalResult thisLevelResult = new HypotheticalResult(game,self,plan,actionMemory,parent.getEvaluator());
		scoreAccumulator.addLayer(thisLevelResult.getScore().getFirstLayer());
																				
		parent.applyContingencies(futureGame,this.self,1);
			
		//try adding a new action
		List<HypotheticalResult> allOptions = new ArrayList<HypotheticalResult>();
		for(List<Action> current: ideas) {
			Score scoreToPass = new Score(scoreAccumulator);
			Game branchGame = parent.getCloner().cloneGame(futureGame);
			List<Action> combinedIdeas = ListUtils.combine(commitedActions.get(0),current);
			branchGame.setActionsForPlayer(combinedIdeas, self);
			branchGame.endRound();
			//skip a round when we are in loose forecasting
			if(isInLooseForcastPhase()) {
				scoreToPass.addLayer(new HypotheticalResult(branchGame, self,parent.getEvaluator()).getScore().decay(parent.getDecayRate()).getFirstLayer());
				branchGame.endRound();
			}
			List<List<Action>> toPass = current.size()==0?passdownActions:ideaGenerator.generateIdeas(futureGame, self, iteration);
			Plan planToPass = new Plan(plan);
			planToPass.addActionListToEnd(combinedIdeas);
			allOptions.add(packResult((tightForcastLength == 0?
						new Hypothetical(branchGame,modifiers,parent,ListUtils.prune(actionMemory,depthInForecast(),current),toPass,
							new ArrayList<Action>(current), commitedActions.subList(1, commitedActions.size()),tightForcastLength,
							looseForcastLength-1,tail,self,
							scoreToPass.decay(parent.getDecayRate()),iteration,ideaGenerator,planToPass).calculate(debug,script)
						:new Hypothetical(branchGame,modifiers,parent,ListUtils.prune(actionMemory,depthInForecast(),current),toPass,
							new ArrayList<Action>(current), commitedActions.subList(1, commitedActions.size()),tightForcastLength-1,
							looseForcastLength,tail,self,
							scoreToPass.decay(parent.getDecayRate()), iteration,ideaGenerator,planToPass).calculate(debug,script))
					,combinedIdeas));	
		}
		
		//pick best option
		BigDecimal bestScore = new BigDecimal(0);
		HypotheticalResult retval = null;
		for(HypotheticalResult current: allOptions) {
			if(retval == null || current.getScore().totalScore().compareTo(bestScore) > 0) {
				bestScore = current.getScore().totalScore();
				retval = current;
			}
		}
				
		return retval;
	}
		
	private HypotheticalResult packResult(HypotheticalResult result, List<Action> actions) {
		return result;
	}
	
	private boolean isInLooseForcastPhase() {
		return tightForcastLength == 0;
	}
	
	private int depthInForecast() {
		return parent.getMaxTtl() - tightForcastLength - looseForcastLength;
	}
}
