package game;

import java.util.List;

import aibrain.Player;
import map.World;
import util.MiscUtilities;

public class BotcivPlayer implements Player{

	String name;
	private double labor=0;
	private double materials=0;
	private double wealth=0;
	private double influence=0;
	private double education=0;
	
	public BotcivPlayer(String name) {
		this.name = name;
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
		
}
