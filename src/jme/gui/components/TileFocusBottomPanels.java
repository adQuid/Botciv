package jme.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import game.ResourcePortfolio;
import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import game.actions.ClaimTile;
import game.actions.ExploreTile;
import jme.gui.GlobalContext;
import jme.gui.MainUI;
import util.GameLogicUtilities;

public class TileFocusBottomPanels {
	public static PanelBuilder focusOnTile(Tile tile) {
		
		GlobalContext.selectedCoord = tile.getCoordinate();
		
		return new PanelBuilder("Bottom_Panel") {{
			childLayoutHorizontal(); 

			image(new ImageBuilder() {{
				childLayoutHorizontal(); 
				height("100%");
				width("20%");
				filename("images/"+tile.getType().getImage());
				image(new ImageBuilder() {{
					height("100%");
					width("100%");
					filename("images/ui/labor.png");
				}});
			}});


			panel(new PanelBuilder() {{
				childLayoutVertical(); 
				height("100%");
				width("80%");			

				text(new TextBuilder("Tile_Type_Name"){{
					alignCenter();
					height("20%");
					width("100%");
					if(tile.getOwner() == null) {
						text("Unclaimed "+tile.getType().getName()+" "+tile.getCoordinate());
					} else {
						text(tile.getOwner().getName()+"'s "+tile.getType().getName());
					}
					font("Interface/Fonts/Default.fnt");
				}});

				panel(new PanelBuilder() {{
					childLayoutHorizontal();
					height("80%");
					width("100%");

					List<ButtonBuilder> populatedButtons = populateButtons(tile);
					for(int i = 0; i < Math.max(5,populatedButtons.size()); i++) {
						if(i < populatedButtons.size()) {
							control(populatedButtons.get(i));
						}else {
							control(new ButtonBuilder("Dummy_Button_"+i,"") {{
								height("100%");
								width("20%");
							}});
						}
					}
				}});
			}});
		}};
	}
	
	private static List<ButtonBuilder> populateButtons(Tile tile){
		
		List<ButtonBuilder> retval = new ArrayList<ButtonBuilder>();
		
		if(tile.getType() == TileType.TYPES.get("Unexplored Tile")) {
			retval.add(new ButtonBuilder("Tile_Explore_Button","Explore Tile") {{
				height("100%");
				width("20%");
				interactOnClick("exploreTile()");
				interactOnMouseOver("updateDescription(explore)");
			}});
		}
		
		if(MainUI.getPlayer().getExploredTiles().contains(tile.getCoordinate()) 
				&& tile.getOwner() == null) {
			retval.add(new ButtonBuilder("Tile_Claim_Button","Claim Tile") {{
				height("100%");
				width("20%");
				interactOnClick("claimTile()");
				interactOnMouseOver("updateDescription(claim)");
			}});
		}
			
		List<UnitType> buildableUnits = GameLogicUtilities.unitsBuildableAtTile(MainUI.getPlayer(), tile);
		if(tile.getOwner() != null && tile.getOwner().equals(MainUI.getPlayer()) && buildableUnits.size() > 0) {
			retval.add(new ButtonBuilder("Tile_Build_Button","Build Unit") {{
				height("100%");
				width("20%");
				interactOnClick("displayUnitBuildList()");
				interactOnMouseOver("updateDescription(build)");
			}});
		}
		
		return retval;
	}
}
