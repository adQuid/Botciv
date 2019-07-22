package jme.gui.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import jme.gui.MainUI;

public class ScrollList {

	private String id;
	private int position = 0;
	private List<ControlBuilder> items;
	
	public static Map<String,ScrollList> listDictionary = new HashMap<String,ScrollList>();
	
	public ScrollList(String id, ControlBuilder...builders) {
		this.id = id;
		this.items = new ArrayList<ControlBuilder>();
		for(ControlBuilder current: builders) {
			items.add(current);
		}
		listDictionary.put(id, this);
	}
	
	public PanelBuilder getPanel() {
		return new PanelBuilder("ScrollList"+id) {{
			childLayoutVertical();
			for(int i=0; i<3; i++) {
				control(new ButtonBuilder("Button_"+i,"Filler"+position) {{
					width("100%");
					height((100/3)+"%");
					interactOnClick("scroll("+id+":1)");
				}});
			}
		}};
	}
	
	public void scroll(int amount) {
		position = Math.max(position+amount, 0);
		MainUI.updateSidePanel(RightPanel.rightPanel());
	}
}
