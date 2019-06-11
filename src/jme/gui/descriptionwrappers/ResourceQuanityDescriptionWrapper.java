package jme.gui.descriptionwrappers;

import java.text.DecimalFormat;

import game.ResourcePortfolio;
import gui.BottomDisplay;
import gui.ResourceDisplay.ResourceType;
import jme.gui.MainUI;
import util.GameLogicUtilities;

public class ResourceQuanityDescriptionWrapper extends DescriptionWrapper{

	private ResourceType type;
	
	public static ResourceQuanityDescriptionWrapper labor = new ResourceQuanityDescriptionWrapper(ResourceType.labor);
	public static ResourceQuanityDescriptionWrapper materials = new ResourceQuanityDescriptionWrapper(ResourceType.materials);
	public static ResourceQuanityDescriptionWrapper wealth = new ResourceQuanityDescriptionWrapper(ResourceType.wealth);
	public static ResourceQuanityDescriptionWrapper influence = new ResourceQuanityDescriptionWrapper(ResourceType.influence);
	public static ResourceQuanityDescriptionWrapper education = new ResourceQuanityDescriptionWrapper(ResourceType.education);
	
	public ResourceQuanityDescriptionWrapper(ResourceType type) {
		this.type = type;
	}
	
	@Override
	public String getDescription() {
		
		ResourcePortfolio deltas = GameLogicUtilities.getResourceDeltas(MainUI.getGame().world,MainUI.getPlayer());
		double value = -99999; 
		double rateOfChange = -99999;
		
		switch(type){
			case labor:
				value = MainUI.getPlayer().getLabor();
				rateOfChange = deltas.labor;
				break;
			case materials:
				value = MainUI.getPlayer().getMaterials();
				rateOfChange = deltas.materials;
				break;
			case wealth:
				value = MainUI.getPlayer().getWealth();
				rateOfChange = deltas.wealth;
				break;
			case influence:
				value = MainUI.getPlayer().getInfluence();
				rateOfChange = deltas.influence;
				break;
			case education:
				value = MainUI.getPlayer().getEducation();
				rateOfChange = deltas.education;
				break;
		}
		
		if(type == ResourceType.materials || type == ResourceType.wealth || type == ResourceType.influence) {
			return new DecimalFormat("#.###").format(value)+" ("+(rateOfChange>=0?"+":"-")+new DecimalFormat("#.###").format(Math.abs(rateOfChange))+" per turn)";
		} else {
			return new DecimalFormat("#.###").format(value)+" ("+new DecimalFormat("#.###").format(Math.abs(rateOfChange))+" next turn)";
		}
	}

	
	
}
