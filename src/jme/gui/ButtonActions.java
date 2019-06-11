package jme.gui;

import java.util.HashMap;
import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import game.ResourcePortfolio;
import game.Tile;
import game.Unit;
import game.UnitType;
import game.actions.ClaimTile;
import game.actions.ExploreTile;
import jme.gui.MainUI;
import jme.gui.components.DescriptionDisplay;
import jme.gui.descriptionwrappers.ExploreDescriptionWrapper;
import jme.gui.descriptionwrappers.ClaimDescriptionWrapper;
import jme.gui.descriptionwrappers.DescriptionWrapper;
import jme.gui.descriptionwrappers.ResourceDescriptionWrapper;
import map.MainUIMapDisplay;
import util.GameLogicUtilities;

public class ButtonActions implements ScreenController{

	public static ButtonActions actions = new ButtonActions();
	
	public Map<String,DescriptionWrapper> wrappers = new HashMap<String,DescriptionWrapper>();
	
	{
		wrappers.put("labor", ResourceDescriptionWrapper.labor);
		wrappers.put("materials", ResourceDescriptionWrapper.materials);
		wrappers.put("wealth", ResourceDescriptionWrapper.wealth);
		wrappers.put("influence", ResourceDescriptionWrapper.influence);
		wrappers.put("education", ResourceDescriptionWrapper.education);
		wrappers.put("explore", ExploreDescriptionWrapper.explore);
		wrappers.put("claim", ClaimDescriptionWrapper.claim);
		
	}
	
	public void printstuff() {
		System.out.println("is this how we do this?");
	}

	public void updateDescription(String key) {
		String text = wrappers.get(key).getDescription();
		
		Element elementToFill = MainUI.nifty.getCurrentScreen().findElementById(DescriptionDisplay.descriptionID);
	    elementToFill.getRenderer(TextRenderer.class).setText(text);
	}
	
	public void clearDescription() {
		Element elementToFill = MainUI.nifty.getCurrentScreen().findElementById(DescriptionDisplay.descriptionID);
	    elementToFill.getRenderer(TextRenderer.class).setText(MainUI.getGame().getTurnName());
	}
	
	public void clearTurn() {
		MainUI.clearTurn();
	}
	
	public void commitTurn() {
		MainUI.commitTurn();
	}
	
	public void click() {
		MainUI.instance.click();
	}
	
	public void exploreTile() {
		if(GlobalContext.selectedTile == null) {
			System.err.println("Explore Tile selected, but no tile was selected!");
			return;
		} 
		Tile tile = GlobalContext.selectedTile;
		if((tile.getUnits().get(UnitType.TYPES.get("explorer")) == null 
				|| tile.getUnits().get(UnitType.TYPES.get("explorer")).size() == 0)
				&& GameLogicUtilities.tryTopay(MainUI.getPlayer(), new ResourcePortfolio("{I:1,M:2}"))) {
			
			MainUI.addAction(new ExploreTile(tile.getCoordinate()));
			tile.addUnit(new Unit(UnitType.TYPES.get("explorer"), MainUI.getPlayer()),MainUI.getGame());
			MainUI.updateGameDisplay();
			
		}
	}
	
	public void claimTile() {
		if(GlobalContext.selectedTile == null) {
			System.err.println("Claim Tile selected, but no tile was selected!");
			return;
		} 
		Tile tile = GlobalContext.selectedTile;
		if((tile.getUnits().get(UnitType.TYPES.get("claim")) == null 
				|| tile.getUnits().get(UnitType.TYPES.get("claim")).size() == 0) 
				&& GameLogicUtilities.tryTopay(MainUI.getPlayer(), new ResourcePortfolio("{I:5}"))) {
			tile.addUnit(new Unit(UnitType.TYPES.get("claim"), MainUI.getPlayer()),MainUI.getGame());
			MainUIMapDisplay.repaintDisplay();
			MainUI.addAction(new ClaimTile(tile.getCoordinate()));
		}
	}
	
	@Override
	public void bind(Nifty arg0, Screen arg1) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub		
	}	

}
