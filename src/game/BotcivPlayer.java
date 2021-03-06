package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aibrain.Player;
import game.actions.BotcivAction;
import map.Coordinate;
import util.MiscUtilities;

public class BotcivPlayer implements Player{

	private boolean player = false;
	private static final String PLAYER_NAME = "pl";
	private String name;//must be unique
	private static final String NAME_NAME = "nme";
	private double labor=0;
	private static final String LABOR_NAME = "l";
	private double materials=0;
	private static final String MATERIALS_NAME = "m";
	private double wealth=0;
	private static final String WEALTH_NAME = "w";
	private double influence=0;
	private static final String INFLUENCE_NAME = "i";
	private double education=0;
	private static final String EDUCATION_NAME = "e";
	private List<BotcivAction> actions = new ArrayList<BotcivAction>();
	private Set<Coordinate> exploredTiles = new HashSet<Coordinate>();
	private static final String EXPLORED_NAME = "et";
	
	private Coordinate lastFocus = new Coordinate(0,0);
	private static final String LAST_FOCUS_NAME = "f";
	
	public BotcivPlayer(String name, boolean player) {
		this.name = name;
		this.player = player;
		this.influence = 10;
		this.labor = 1;
		this.materials = 15;
	}
	
	public BotcivPlayer(BotcivPlayer other) {
		this.player = other.player;
		this.name = other.name;
		this.labor = other.labor;
		this.materials = other.materials;
		this.wealth = other.wealth;
		this.influence = other.influence;
		this.education = other.education;
		this.actions = new ArrayList<BotcivAction>(other.actions);
		this.exploredTiles = new HashSet<Coordinate>(other.exploredTiles);
		this.lastFocus = other.lastFocus;
	}
	
	public BotcivPlayer(Map<String, Object> map, BotcivGame game) {
		player = (Boolean)map.get(PLAYER_NAME);
		name = map.get(NAME_NAME).toString();
		labor = MiscUtilities.extractDouble(map.get(LABOR_NAME).toString());
		materials = MiscUtilities.extractDouble(map.get(MATERIALS_NAME).toString());
		wealth = MiscUtilities.extractDouble(map.get(WEALTH_NAME).toString());
		influence = MiscUtilities.extractDouble(map.get(INFLUENCE_NAME).toString());
		education = MiscUtilities.extractDouble(map.get(EDUCATION_NAME).toString());
		
		List<String> exploreList = (List<String>)(map.get(EXPLORED_NAME));
		for(String current: exploreList) {
			exploredTiles.add(new Coordinate(current));
		}
		
		lastFocus = new Coordinate(map.get(LAST_FOCUS_NAME).toString());
	}
	
	public Map<String, Object> saveString() {
		Map<String,Object> retval = new HashMap<String,Object>();
		
		retval.put(PLAYER_NAME, player);
		retval.put(NAME_NAME, name);
		retval.put(LABOR_NAME, labor);
		retval.put(MATERIALS_NAME, materials);
		retval.put(WEALTH_NAME, wealth);
		retval.put(INFLUENCE_NAME, influence);
		retval.put(EDUCATION_NAME, education);
		
		List<String> exploredTilesList = new ArrayList<String>();
		for(Coordinate coord: exploredTiles) {
			exploredTilesList.add(coord.toString());
		}
		retval.put(EXPLORED_NAME, exploredTilesList);
				
		retval.put(LAST_FOCUS_NAME, lastFocus.toString());		
		
		return retval;
	}
	
	public boolean equals(Object other) {
		if(other == null || !(other instanceof BotcivPlayer)) {
			return false;
		}
		return this.name.equals(((BotcivPlayer)other).name);
	}
	
	public String toString() {
		return name;
	}
	
	public boolean getPlayer() {
		return player;
	}
	
	public String getName() {
		return name;
	}
	
	public double getLabor() {
		return labor;
	}

	public void setLabor(double labor) {
		this.labor = labor;
	}
	
	public void addLabor(double labor) {
		this.labor += labor;
	}

	public double getMaterials() {
		return materials;
	}

	public void addMaterials(double materials) {
		this.materials += materials;
	}

	public double getWealth() {
		return wealth;
	}

	public void addWealth(double wealth) {
		this.wealth += wealth;
	}

	public double getInfluence() {
		return influence;
	}

	public void setInfluence(double influence) {
		this.influence = influence;
	}
	
	public void addInfluence(double influence) {
		this.influence += influence;
	}

	public double getEducation() {
		return education;
	}

	public void setEducation(double education) {
		this.education = education;
	}
	
	public void addEducation(double education) {
		this.education += education;
	}
	
	public List<BotcivAction> getActions() {
		return actions;
	}
	
	public void setActions(List<BotcivAction> actions) {
		this.actions = actions;
	}
	
	public void addAction(BotcivAction action) {
		actions.add(action);
	}
		
	public Set<Coordinate> getExploredTiles(){
		return exploredTiles;
	}
	
	public void addExploredTile(Coordinate coord) {
		exploredTiles.add(coord);
	}
	
	public Coordinate getLastFocus() {
		return lastFocus;
	}

	public void setLastFocus(Coordinate lastFocus) {
		this.lastFocus = lastFocus;
	}
	
	
}
