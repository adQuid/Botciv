package util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import game.BotcivGame;
import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import jme.gui.GlobalContext;
import jme.gui.MainUI;

public class MapUtilities {

	private static class UnitDisplayComparator implements Comparator<UnitType>{
		@Override
		public int compare(UnitType arg0, UnitType arg1) {
			// backwards so we can just grab element 0
			return arg1.getDisplayImportance() - arg0.getDisplayImportance();
		}
	}
	
	
	public static BufferedImage image(BotcivGame game, Tile tile, int width, int height) {

		//Always start with terrain
		BufferedImage retval;
		retval = ImageUtilities.importImage(tile.getType().getImage());	

		retval = ImageUtilities.scale(retval, width, height);

		if(GlobalContext.displayType == GlobalContext.DisplayType.units) {
			if(tile.getUnits().size() > 0) {
				List<List<UnitType>> displayCategories = new ArrayList<List<UnitType>>();
				for(int i=0; i<10; i++) {
					displayCategories.add(new ArrayList<UnitType>());
				}
				for(Unit current: tile.getUnits()) {				
					displayCategories.get(current.getType().getDisplayClass()).add(current.getType());
				}
				for(int i=0; i<10; i++) {
					Collections.sort(displayCategories.get(i),new UnitDisplayComparator());				
				}
				for(UnitType current: displayCategories.get(0)) {
					BufferedImage unit = ImageUtilities.importImage(current.getImage());
					retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.applyFactionColor(unit,tile.getUnitsByType(current).get(0).getOwner()));
				}
				List<UnitType> layers = new ArrayList<UnitType>();
				for(int i=1; i<10; i++) {
					if(!displayCategories.get(i).isEmpty()) {
						layers.add(displayCategories.get(i).get(0));
					}
				}
				Collections.sort(layers, new UnitDisplayComparator());
				Collections.reverse(layers);//the comparator is backwards 
				for(UnitType type: layers) {
					BufferedImage unit = ImageUtilities.importImage(type.getImage());
					retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.applyFactionColor(unit,tile.getUnitsByType(type).get(0).getOwner()));	
				}

			}

		}
		//borders with other nations
		try {
			if(tile.getOwner() != null) {
				//Yes, east and west are backwards. This is required by JMonkey for some reason
				if(!tile.getOwner().equals(MainUI.getGame().world.getTileAt(tile.getCoordinate().left()).getOwner())) {
					retval = ImageUtilities.layerImageOnImage(retval, 
							ImageUtilities.applyFactionColor(ImageUtilities.importImage("features/East Border.png"),tile.getOwner()));
				}
				if(!tile.getOwner().equals(MainUI.getGame().world.getTileAt(tile.getCoordinate().right()).getOwner())) {
					retval = ImageUtilities.layerImageOnImage(retval, 
							ImageUtilities.applyFactionColor(ImageUtilities.importImage("features/West Border.png"),tile.getOwner()));
				}
				if(!tile.getOwner().equals(MainUI.getGame().world.getTileAt(tile.getCoordinate().up()).getOwner())) {
					retval = ImageUtilities.layerImageOnImage(retval, 
							ImageUtilities.applyFactionColor(ImageUtilities.importImage("features/North Border.png"),tile.getOwner()));
				}
				if(!tile.getOwner().equals(MainUI.getGame().world.getTileAt(tile.getCoordinate().down()).getOwner())) {
					retval = ImageUtilities.layerImageOnImage(retval, 
							ImageUtilities.applyFactionColor(ImageUtilities.importImage("features/South Border.png"),tile.getOwner()));
				}
			}
		} catch(Exception e) {
			if(MainUI.getGame() == null) System.out.println("game is null");
			if(MainUI.getGame().world == null) System.out.println("game is null");
			if(tile.getCoordinate().left() == null) System.out.println("left is null");
			retval = ImageUtilities.importImage(TileType.TYPES.get("Sea").getImage());
		}


		if(GlobalContext.displayType == GlobalContext.DisplayType.trade && tile.getType() != TileType.TYPES.get("Unexplored Tile")) {
			List<Market> markets = GameLogicUtilities.calculateMarkets(MainUI.getGame());

			for(Market market: markets) {
				 for(Node current: market.nodes) {
					Tile curTile = game.world.getTileAt(current.coord);
					if(curTile.equals(tile)) {
						if(current.origin == null) {
							retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("features/Hub Primary Trade.png"));
						}else if(current.origin.equals(tile.getCoordinate().up())) {
							retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("features/Up Primary Trade.png"));
						} else if(current.origin.equals(tile.getCoordinate().down())) {
							retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("features/Down Primary Trade.png"));
						} else if(current.origin.equals(tile.getCoordinate().left())) {
							retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("features/Left Primary Trade.png"));
						} else if(current.origin.equals(tile.getCoordinate().right())) {
							retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("features/Right Primary Trade.png"));
						}						
					} else if(current.origin != null){
						if(current.origin.up().equals(tile.getCoordinate())) {
							retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("features/Down Primary Trade.png"));
						} else if(current.origin.down().equals(tile.getCoordinate())) {
							retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("features/Up Primary Trade.png"));
						} else if(current.origin.left().equals(tile.getCoordinate())) {
							retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("features/Right Primary Trade.png"));
						} else if(current.origin.right().equals(tile.getCoordinate())) {
							retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("features/Left Primary Trade.png"));
						}
					}
					
				}
			}
		}

		//selection
		if(tile.isSelected()) {
			retval = ImageUtilities.layerImageOnImage(retval, ImageUtilities.importImage("ui/selection.png"));
		}

		return retval;
	}
	
}
