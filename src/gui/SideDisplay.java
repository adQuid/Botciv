package gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.Tile;
import game.Unit;
import game.UnitType;
import layout.TableLayout;
import panel.BuildUnitsSidePanel;
import panel.TileSidePanel;

public class SideDisplay {

	private static JPanel selectionFiller = new JPanel();
	private static TileSidePanel tileFocus = new TileSidePanel(selectionFiller);
	private static BuildUnitsSidePanel buildFocus = new BuildUnitsSidePanel(selectionFiller);
		
	public static void setupDetailUI() {
		tileFocus.setup();
		buildFocus.setup();
		
		MainUI.detailPanel.setLayout(new GridLayout(1,1));
		MainUI.detailPanel.add(selectionFiller);
	}
	
	public static void focusOnTile(Tile tile) {
		tileFocus.focusOnTile(tile);
	}
	
	public static void showBuildableUnits(Tile tile, List<UnitType> types) {
		buildFocus.focusOnTile(tile, types);
	}
}
