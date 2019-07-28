package jme.gui.components;

import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import game.Tile;
import game.Unit;
import game.UnitType;
import jme.gui.GlobalContext;
import jme.gui.MainUI;
import util.GameLogicUtilities;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.builder.PanelBuilder;

public class RightPanel {

	private enum PanelStatus{
		DEFAULT,TILE_FOCUS,BUILD_LIST;
	}
	
	private static PanelStatus status;
	private static ScrollList list = new ScrollList("test",5);
	
	public static PanelBuilder currentPanel() {
		switch(status){
		case DEFAULT:
			return defaultRightPanel();
		case TILE_FOCUS:
			return tileFocusRightPanel(GlobalContext.selectedTile);
		case BUILD_LIST:
			return buildListRightPanel(GameLogicUtilities.unitsBuildableAtTile(MainUI.getPlayer(), GlobalContext.selectedTile));
		}
		
		return null;
	}
	
	public static PanelBuilder defaultRightPanel() {
		status = PanelStatus.DEFAULT;
		return new PanelBuilder("Scroll_List_Holder") {{
			childLayoutVertical();
			control(new ButtonBuilder("fillter_right","filler") {{
				width("100%");
				height("100%");
			}});
		}};
	}
	
	public static PanelBuilder tileFocusRightPanel(Tile tile) {
		status = PanelStatus.TILE_FOCUS;
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
	
	public static PanelBuilder buildListRightPanel(List<UnitType> units) {
		status = PanelStatus.BUILD_LIST;
		List<String> labels = new ArrayList<String>();
		List<String> actions = new ArrayList<String>();
		for(UnitType current: units) {
			labels.add(current.getName());
			actions.add("buildUnit("+current.getId()+")");
		}
		
		list.setup(labels, actions);
		
		return new PanelBuilder("Unit_List_Holder") {{
			childLayoutVertical();
			panel(list.getPanel());
		}};
	}
}
