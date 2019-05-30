package gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import game.Tile;
import game.Unit;
import layout.TableLayout;
import panel.GameOptionsBottomPanel;
import panel.TileBottomPanel;
import panel.UnitBottomPanel;
import panel.WaitingBottomPanel;

public class BottomDisplay {

	private static JLabel description = new JLabel(MainUI.getGame().getTurnName());
	private static JPanel selectionFiller = new JPanel();
	
	private static UnitBottomPanel unitPanel = new UnitBottomPanel(selectionFiller);
	private static TileBottomPanel tilePanel = new TileBottomPanel(selectionFiller);
	private static GameOptionsBottomPanel gameOptionsPanel = new GameOptionsBottomPanel(selectionFiller);
	private static WaitingBottomPanel waitPanel = new WaitingBottomPanel(selectionFiller);	
	
	public static void setup() {
		double[][] bigSplit = {{TableLayout.FILL},{0.2,0.8}};
		MainUI.bottomPanel.setLayout(new TableLayout(bigSplit));

		JPanel descriptionPanel = new JPanel();
		descriptionPanel.add(description);
		
		gameOptionsPanel.setup();
		waitPanel.setup();
		unitPanel.setup();
		tilePanel.setup();
		
		MainUI.bottomPanel.add(descriptionPanel,"0,0");
		MainUI.bottomPanel.add(selectionFiller,"0,1");
	}
	
	public static void setDescription(String str) {
		description.setText(str);
	}
	
	public static void resetDescription() {
		description.setText(MainUI.getGame().getTurnName());
	}
	
	public static void openGameOptions() {
		gameOptionsPanel.openGameOptions();
	}
	
	public static void waitForEndTurn() {
		waitPanel.display();
	}
	
	public static void focusOnTile(Tile tile) {
		tilePanel.selectTile(tile);
	}
	
	public static void focusOnUnit(Tile tile, Unit focus) {
		unitPanel.selectUnit(tile, focus);
	}
	
}
