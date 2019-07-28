package jme.gui.components;

import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import game.Tile;
import game.Unit;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.builder.PanelBuilder;

public class RightPanel {

	static ScrollList list = new ScrollList("test",5);
	
	public static PanelBuilder defaultRightPanel() {
				
		return new PanelBuilder("Scroll_List_Holder") {{
			childLayoutVertical();
			control(new ButtonBuilder("fillter_right","filler") {{
				width("100%");
				height("100%");
			}});
		}};
	}
	
	public static PanelBuilder listRightPanel(Tile tile) {
		List<String> labels = new ArrayList<String>();
		for(Unit current: tile.getAllUnits()) {
			labels.add(current.getType().getName());
		}
		
		list.setup(labels, new ArrayList<String>());
		
		return new PanelBuilder("Unit_List_Holder") {{
			childLayoutVertical();
			panel(list.getPanel());
		}};
	}
}
