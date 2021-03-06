package jme.gui.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import jme.gui.MainUI;
import jme.gui.mouseactions.ScrollAction;

public class ScrollList {

	public static final String PREFIX = "Scroll_List_";
	
	private String id;
	private int size;
	private int position = 0;
	private List<String> labels;
	private List<String> actions;
	private List<String> descriptions;
	private Scroll scroll = new Scroll();
	
	public static Map<String,ScrollList> listDictionary = new HashMap<String,ScrollList>();
	
	public ScrollList(String id, int size) {
		this.id = PREFIX+id;
		this.size = size;

		listDictionary.put(this.id, this);
	}
	
	public void setup(List<String> labels, List<String> actions) {
		setup(labels, actions, new ArrayList<String>());
	}
	
	public void setup(List<String> labels, List<String> actions, List<String> descriptions) {
		this.labels = labels;
		this.actions = actions;
		this.descriptions = descriptions;
	}
	
	public PanelBuilder getPanel() {
		return new PanelBuilder("ScrollList"+id) {{
			childLayoutVertical();
			interactOnMouseOver("setScroll("+id+")");
			for(int i=position; i<position+size; i++) {
				if(i < labels.size()) {
					final int j = i;
					control(new ButtonBuilder(id+labels.get(i),labels.get(i)) {{
						height((100/size)+"%");
						width("100%");
						if(j < actions.size()) {
							interactOnClick(actions.get(j));							
						}
						if(j < descriptions.size()) {
							interactOnMouseOver("updateDescription("+descriptions.get(j)+")");
						}
					}});
				} else {
					text("");
				}
			}
		}};
	}
	
	public ScrollAction getScroll() {
		return scroll;
	}
	
	private class Scroll implements ScrollAction{

		@Override
		public void doAction(int scroll) {
			position = Math.max(Math.min(position - scroll, labels.size() - 1), 0);
			MainUI.updateSidePanel();
		}
		
	}
}
