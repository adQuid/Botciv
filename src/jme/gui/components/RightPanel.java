package jme.gui.components;

import de.lessvoid.nifty.builder.PanelBuilder;

public class RightPanel {

	static ScrollList list = new ScrollList("test");
	
	public static PanelBuilder rightPanel() {
		return new PanelBuilder("Scroll_List_Holder") {{
			childLayoutVertical();
			panel(list.getPanel());
		}};
	}
}
