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
import game.actions.BuildUnit;
import game.actions.ClaimTile;
import game.actions.ExploreTile;
import jme.gui.components.CornerDisplay;
import jme.gui.MainUI;
import jme.gui.components.DescriptionDisplay;
import jme.gui.components.RightPanel;
import jme.gui.components.ScrollList;
import jme.gui.components.TileFocusBottomPanels;
import jme.gui.components.UnitFocusBottomPanels;
import jme.gui.descriptionwrappers.ExploreDescriptionWrapper;
import jme.gui.descriptionwrappers.BuildDescriptionWrapper;
import jme.gui.descriptionwrappers.ClaimDescriptionWrapper;
import jme.gui.descriptionwrappers.DescriptionWrapper;
import jme.gui.descriptionwrappers.ResourceDescriptionWrapper;
import jme.gui.descriptionwrappers.ResourceQuanityDescriptionWrapper;
import jme.gui.mapActions.ZoomMap;
import jme.gui.mouseactions.ScrollAction;
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
		wrappers.put("laborAmount", ResourceQuanityDescriptionWrapper.labor);
		wrappers.put("materialsAmount", ResourceQuanityDescriptionWrapper.materials);
		wrappers.put("wealthAmount", ResourceQuanityDescriptionWrapper.wealth);
		wrappers.put("influenceAmount", ResourceQuanityDescriptionWrapper.influence);
		wrappers.put("educationAmount", ResourceQuanityDescriptionWrapper.education);
		wrappers.put("explore", ExploreDescriptionWrapper.explore);
		wrappers.put("claim", ClaimDescriptionWrapper.claim);
		wrappers.put("build", BuildDescriptionWrapper.build);
		
	}
	
	public void printstuff() {
		System.out.println("is this how we do this?");
	}

	public void updateDescription(String key) {
		String text = wrappers.get(key).getDescription();
		
		Element elementToFill = MainUI.nifty.getCurrentScreen().findElementById(DescriptionDisplay.descriptionID);
	    elementToFill.getRenderer(TextRenderer.class).setText(text);
	}
	
	public void endHoverActions() {
		//clear description
		Element elementToFill = MainUI.nifty.getCurrentScreen().findElementById(DescriptionDisplay.descriptionID);
	    elementToFill.getRenderer(TextRenderer.class).setText(MainUI.getGame().getTurnName());
	    
	    //set scroll to zoom
	    setScroll("zoom");
	}
	
	public void clearTurn() {
		if(!GlobalContext.waitingForPlayers) {
			MainUI.clearTurn();
		}
	}
	
	public void commitTurn() {
		if(!GlobalContext.waitingForPlayers) {
			MainUI.commitTurn();
		}
	}
	
	public void click() {
		if(!GlobalContext.waitingForPlayers) {
			MainUI.instance.click();
		}
	}
	
	public void focusOnUnit(String id) {
		MainUI.updateBottomPanel(UnitFocusBottomPanels.focusOnUnit(MainUI.getGame().getUnit(Long.parseLong(id))));
	}
	
	public void exploreTile() {
		if(GlobalContext.getSelectedTile() == null) {
			System.err.println("Explore Tile selected, but no tile was selected!");
			return;
		} 
		Tile tile = GlobalContext.getSelectedTile();
		if((tile.getUnits().get(UnitType.TYPES.get("explorer")) == null 
				|| tile.getUnits().get(UnitType.TYPES.get("explorer")).size() == 0)
				&& GameLogicUtilities.tryTopay(MainUI.getPlayer(), new ResourcePortfolio("{I:1,M:2}"))) {
			
			MainUI.addAction(new ExploreTile(tile.getCoordinate()));
			tile.addUnit(new Unit(MainUI.getGame(), UnitType.TYPES.get("explorer"), MainUI.getPlayer()),MainUI.getGame());
			MainUI.updateGameDisplay();
			
		}
	}
	
	public void claimTile() {
		if(GlobalContext.getSelectedTile() == null) {
			System.err.println("Claim Tile selected, but no tile was selected!");
			return;
		} 
		Tile tile = GlobalContext.getSelectedTile();
		if((tile.getUnits().get(UnitType.TYPES.get("claim")) == null 
				|| tile.getUnits().get(UnitType.TYPES.get("claim")).size() == 0) 
				&& GameLogicUtilities.tryTopay(MainUI.getPlayer(), new ResourcePortfolio("{I:5}"))) {
			tile.addUnit(new Unit(MainUI.getGame(), UnitType.TYPES.get("claim"), MainUI.getPlayer()),MainUI.getGame());
			MainUI.updateGameDisplay();
			MainUI.addAction(new ClaimTile(tile.getCoordinate()));
		}
	}
	
	public void displayUnitBuildList() {
		MainUI.updateSidePanel(RightPanel.buildListRightPanel(GameLogicUtilities.unitsBuildableAtTile(MainUI.getPlayer(), GlobalContext.getSelectedTile())));
	}
	
	public void buildUnit(String type) {
		UnitType toBuild = UnitType.TYPES.get(type);
		if(GameLogicUtilities.tryTopay(MainUI.getPlayer(), toBuild.getCost())) {
			GlobalContext.getSelectedTile().addUnit(new Unit(MainUI.getGame(), toBuild,MainUI.getPlayer()),MainUI.getGame());
			MainUI.addAction(new BuildUnit(GlobalContext.getSelectedTile().getCoordinate(),toBuild));
			MainUI.updateGameDisplay();
		} else {
			System.out.println("you can't afford this");
		}
	}
	
	public void setScroll(String action) {
		ScrollAction newAction = null;
		if(action.startsWith(ScrollList.PREFIX)) {
			 newAction = ScrollList.listDictionary.get(action).getScroll();
		}
		
		if(action.equals("zoom")) {
			newAction = new ZoomMap();
		}
		
		if(newAction == null) {
			System.err.println("Scroll action "+action+" not found!");
		} else {
			GlobalContext.scrollAction = newAction;
		}
		
	}
	
	public void scroll(String direction) {
		scroll(Integer.parseInt(direction));
	}	
	public void scroll(int direction) {
		GlobalContext.scrollAction.doAction(direction);
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
