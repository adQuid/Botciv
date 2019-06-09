package jme.gui.components;

import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import game.Tile;

public class BasicBottomPanels {

	public static final String BOTTOM_BUTTON_LABEL = "Bottom_Button_Holder";

	public static PanelBuilder onYourTurn() {
		return new PanelBuilder("Bottom_Panel") {{
			childLayoutVertical(); 

			control(new ButtonBuilder("Button_1", "Filler part 1"){{
				alignCenter();
				valignBottom();
				height("50%");
				width("100%");
			}});

			control(new ButtonBuilder("Button_2", "Filler part 2"){{
				alignCenter();
				valignBottom();
				height("50%");
				width("100%");
				interactOnClick("printstuff()");
			}});
		}};
	}

	public static PanelBuilder waitingForTurn() {
		return new PanelBuilder("Bottom_Panel") {{
			childLayoutVertical(); 

			control(new ButtonBuilder("Waiting_Filler", "Waiting for other players..."){{
				alignCenter();
				valignBottom();
				height("100%");
				width("100%");
				interactOnClick("printstuff()");
			}});
		}};
	}

	public static PanelBuilder focusOnTile(Tile tile) {
		return new PanelBuilder("Bottom_Panel") {{
			childLayoutHorizontal(); 

			image(new ImageBuilder() {{
				height("100%");
				width("20%");
				filename("images/"+tile.getType().getImage());
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
						text("Unclaimed "+tile.getType().getName());
					} else {
						text(tile.getOwner().getName()+"'s "+tile.getType().getName());
					}
					font("Interface/Fonts/Default.fnt");
				}});

				panel(new PanelBuilder() {{
					childLayoutHorizontal();
					height("80%");
					width("100%");
					control(new ButtonBuilder("Tile_Button_1","Tile filler") {{
						height("100%");
						width("20%");
					}});
					control(new ButtonBuilder("Tile_Button_2","Tile filler") {{
						height("100%");
						width("20%");
					}});
					control(new ButtonBuilder("Tile_Button_3","Tile filler") {{
						height("100%");
						width("20%");
					}});
					control(new ButtonBuilder("Tile_Button_4","Tile filler") {{
						height("100%");
						width("20%");
					}});
					control(new ButtonBuilder("Tile_Button_5","Tile filler") {{
						height("100%");
						width("20%");
					}});
				}});
			}});
		}};
	}
}
