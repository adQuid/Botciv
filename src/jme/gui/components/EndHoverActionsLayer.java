package jme.gui.components;

import de.lessvoid.nifty.builder.LayerBuilder;

public class EndHoverActionsLayer {

	public static LayerBuilder getLayer() {
		return new LayerBuilder("Bottom_Layer") {{
			width("100%");
			height("100%");
			interactOnMouseOver("clearDescription()");
			interactOnRelease("click()");
		}};
	}
	
}
