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
						text(unit.getOwner().getName()+"'s "+unit.getType().getName());
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
		
					/*JButton migrateButton = buttons.get(nextButtonIndex++);
			migrateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					List<Coordinate> inRange = MainUI.getGame().world.tilesWithinRange(tile.getCoordinate(), 1);
					for(Coordinate current: inRange) {
						MainUI.getGame().world.getTileAt(current).setSelected(true);
					}
					MainUIMapDisplay.repaintDisplay();
					MapActionGlobalStore.selectedUnit = unit;
					MainUIMapDisplay.action = new Migrate();
				}				
			});		
			migrateButton.setText("Migrate");
			migrateButton.addMouseListener(new DescriptionListener("Take one movement point into a tile you own, an unclaimed tile, or a tile belonging to someone you have migration permissoins with."));
		*/
		
		//if we can't find a matching unit, we are assuming it's already migrated
		if(unit.getOwner().equals(MainUI.getPlayer()) && unit.getType().has("migrates") && MainUI.getGame().findMatching(unit) != null) {
			retval.add(new ButtonBuilder("Migrate_Button","Migrate") {{
				height("100%");
				width("20%");
				interactOnClick("showMigrationOptions("+unit.getId()+")");
				interactOnMouseOver("updateDescription(migrate)");
			}});
		}
		
		return retval;
	}
}
