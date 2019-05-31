package jme.gui.components;

import de.lessvoid.nifty.builder.LayerBuilder;

public class UIBackgroundLayer {

	public static LayerBuilder background() {
		return new LayerBuilder("background_layer"){{
	        childLayoutCenter();
	        backgroundColor("#000f");
	        height("20%");
		}};
	}
	
}
