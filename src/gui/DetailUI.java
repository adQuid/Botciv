package gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.Unit;
import game.UnitType;
import layout.TableLayout;
import map.Tile;

public class DetailUI {

	private static JLabel title = new JLabel("test");
	
	private static JPanel objectListPanel = new JPanel();
	private static List<Component> objects = new ArrayList<Component>();
	private static LinearList objectLinList = new LinearList(objectListPanel,objects,5);
	
	private static JLabel placeholder = new JLabel("placeholder");
	
	public static void setupDetailUI() {
		double[][] size = {{TableLayout.FILL},{0.15,0.5,0.3}};
		MainUI.detailPanel.setLayout(new TableLayout(size));
		MainUI.detailPanel.add(title,"0,0");
		MainUI.detailPanel.add(objectListPanel,"0,1");
		MainUI.detailPanel.add(placeholder,"0,2");
	}
	
	public static void focusOnTile(Tile tile) {
		title.setText(tile.getX()+","+tile.getY());
		
		objects.clear();
		for(Entry<UnitType,List<Unit>> current: tile.getUnits().entrySet()) {
			objects.add(new JButton(current.getKey().getName()+" ("+current.getValue().size()+")"));
		}
		objectLinList.updatePanel();
		
		MainUI.detailPanel.validate();
	}
	
}
