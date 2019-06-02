package jme.gui.components;

import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;

public class BasicBottomPanels {

	public static final String BOTTOM_BUTTON_LABEL = "Bottom_Button_Holder";
	
	public static PanelBuilder onYourTurn() {
		return new PanelBuilder("Bottom_Panel") {{
				childLayoutVertical(); 
								
				control(new ButtonBuilder("Button_1", "Button 1"){{
					alignCenter();
					valignBottom();
					height("50%");
					width("100%");
				}});

				control(new ButtonBuilder("Button_2", "Button 2"){{
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

			control(new ButtonBuilder("Button_2", "Waiting for other players..."){{
				alignCenter();
				valignBottom();
				height("100%");
				width("100%");
				interactOnClick("printstuff()");
			}});
		}};
	}
	
}
