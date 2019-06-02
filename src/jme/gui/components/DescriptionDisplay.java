package jme.gui.components;

import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.render.NiftyImage;
import jme.gui.MainUI;

public class DescriptionDisplay {

	public static final String descriptionID = "Description_Text";
	
	public static PanelBuilder panel() {
		return new PanelBuilder("Description_Panel"){{
			childLayoutVertical(); 
			valignBottom();

			text(new TextBuilder(descriptionID){{
				height("100%");
				width("100%");
				text(MainUI.getGame().getTurnName());
				font("fonts/TimesNewRoman.fnt");	
			
				wrap(true);
			}});
		}};
	}
}
