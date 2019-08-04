package jme.gui.components;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import game.Unit;
import jme.gui.MainUI;

public class UnitFocusBottomPanels {
	public static PanelBuilder focusOnUnit(Unit unit) {
				
		return new PanelBuilder("Bottom_Panel") {{
			childLayoutHorizontal(); 

			image(new ImageBuilder() {{
				childLayoutHorizontal(); 
				height("100%");
				width("20%");
				filename("images/"+unit.getType().getImage());
			}});


			panel(new PanelBuilder() {{
				childLayoutVertical(); 
				height("100%");
				width("80%");			

				text(new TextBuilder("Tile_Type_Name"){{
					alignCenter();
					height("20%");
					width("100%");
					if(unit.getOwner() == null) {
						text(unit.getType().getName());
					} else {
						text(unit.getOwner().getName()+"'s "+unit.getType().getName()+" ("+unit.getHealth()+"/"+unit.getType().getMaxHealth()+" HP)");
					}
					font("Interface/Fonts/Default.fnt");
				}});

				panel(new PanelBuilder() {{
					childLayoutHorizontal();
					height("80%");
					width("100%");

					List<ButtonBuilder> populatedButtons = populateButtons(unit);
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
	
	private static List<ButtonBuilder> populateButtons(Unit unit){

		List<ButtonBuilder> retval = new ArrayList<ButtonBuilder>();
				
		if(unit.getOwner().equals(MainUI.getPlayer()) && unit.getStackSize() > 1 && MainUI.getGame().findMatching(unit) != null) {
			retval.add(new ButtonBuilder("Split_Button","Split Stack") {{
				height("100%");
				width("20%");
				interactOnClick("splitSelected(1)");
				interactOnMouseOver("");
			}});
		}
		
		//if we can't find a matching unit, we are assuming it's already migrated
		if(unit.getOwner().equals(MainUI.getPlayer()) && unit.getType().has("migrates")) {// && MainUI.getGame().findMatching(unit) != null) {
			retval.add(new ButtonBuilder("Migrate_Button","Order Migration") {{
				height("100%");
				width("20%");
				interactOnClick("showMigrationOptions("+unit.getId()+")");
				interactOnMouseOver("updateDescription(migrate)");
			}});
		}
		
		return retval;
	}
}
