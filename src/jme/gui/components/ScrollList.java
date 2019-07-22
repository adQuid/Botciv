package jme.gui.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import jme.gui.MainUI;
import jme.gui.mouseactions.ScrollAction;

public class ScrollList {

	public static final String PREFIX = "Scroll_List_";
	
	private String id;
	private int position = 0;
	private List<ControlBuilder> items;
	private Scroll scroll = new Scroll();
	
	public static Map<String,ScrollList> listDictionary = new HashMap<String,ScrollList>();
	
	public ScrollList(String id, ControlBuilder...builders) {
		this.id = PREFIX+id;
		this.items = new ArrayList<ControlBuilder>();
		for(ControlBuilder current: builders) {
			items.add(current);
		}
		listDictionary.put(this.id, this);
	}
	
	public PanelBuilder getPanel() {
		return new PanelBuilder("ScrollList"+id) {{
			childLayoutVertical();
			interactOnMouseOver("setScroll("+id+")");
			for(int i=0; i<3; i++) {
				control(new ButtonBuilder("Button_"+i,"Filler"+position) {{
					width("100%");
					height((100/3)+"%");
					interactOnClick("scroll(1)");
				}});
			}
		}};
	}
	
	public ScrollAction getScroll() {
		return scroll;
	}
	
	private class Scroll implements ScrollAction{

		@Override
		public void doAction(int scroll) {
			position = Math.max(position - scroll, 0);
			MainUI.updateSidePanel(RightPanel.rightPanel());
		}
		
	}
}
