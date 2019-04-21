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
import panel.TileSidePanel;

public class SideDisplay {

	private static TileSidePanel tileFocus = new TileSidePanel(MainUI.detailPanel);
	
	public static void setupDetailUI() {
		tileFocus.setup();
		
	}
	
	public static void focusOnTile(Tile tile) {
		tileFocus.focusOnTile(tile);
	}
	
}
