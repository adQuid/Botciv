package jme.gui.components;

import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import game.Tile;

public class GameOptionsBottomPanels {

	public static final String BOTTOM_BUTTON_LABEL = "Bottom_Button_Holder";

	public static PanelBuilder gameOptionsPanel() {
		return new PanelBuilder("Bottom_Panel") {{
			childLayoutHorizontal(); 

			control(new ButtonBuilder("Save_Button", "Save Game"){{
				alignCenter();
				valignBottom();
				height("100%");
				width("50%");
				interactOnClick("saveGame()");
			}});

			control(new ButtonBuilder("Load_Button", "Load Game"){{
				alignCenter();
				valignBottom();
				height("100%");
				width("50%");
				interactOnClick("printstuff()");
			}});
		}};
	}

}
