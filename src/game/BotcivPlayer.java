package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aibrain.Player;
import game.actions.BotcivAction;
import map.Coordinate;
import map.World;
import util.MiscUtilities;

public class BotcivPlayer implements Player{

	private String name;//must be unique
	private double labor=0;
	private double materials=0;
	private double wealth=0;
	private double influence=0;
	private double education=0;
	private List<BotcivAction> actions = new ArrayList<BotcivAction>();
	private Set<Coordinate> exploredTiles = new HashSet<Coordinate>();
	
	public BotcivPlayer(String name) {
		this.name = name;
	}
	
	public BotcivPlayer(BotcivPlayer other) {
		this.name = other.name;
		this.labor = other.labor;
		this.materials = other.materials;
		this.wealth = other.wealth;
		this.influence = other.influence;
		this.education = other.education;
		this.actions = new ArrayList<BotcivAction>(other.actions);
		this.exploredTiles = new HashSet<Coordinate>(other.exploredTiles);
	}
	
	public ResourcePortfolio getPortfolio(World world) {
		List<Unit> units = world.getAllUnitsOfTypeByPlayer(null, this);
		
		ResourcePortfolio retval = new ResourcePortfolio();
		for(Unit current: units) {
			retval.labor = MiscUtilities.addTo(retval.labor,current.getType().getAttribute("laborGeneration"));
			retval.materials = MiscUtilities.addTo(retval.labor,current.getType().getAttribute("materialsGeneration"));
		}
		
		return retval;
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
}
