package panel;

import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.ResourcePortfolio;
import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import game.actions.ClaimTile;
import game.actions.ExploreTile;
import game.actions.MigrateUnit;
import gui.DescriptionListener;
import gui.MainUI;
import gui.SideDisplay;
import layout.TableLayout;
import map.Coordinate;
import map.MainUIMapDisplay;
import mapActions.MapActionGlobalStore;
import mapActions.Migrate;
import util.GameLogicUtilities;
import util.ImageUtilities;

public class WaitingBottomPanel extends Panel{

	private BufferedImage picture;
	private JLabel title = new JLabel("Waiting for other players to finish turns...");
	
	public WaitingBottomPanel(JPanel base) {
		super(base);
	}

	@Override
	public void setup() {
		
	}
	
	public void display() {
		super.clearPanel();
		
		double[][] size = {{TableLayout.FILL},{TableLayout.FILL}};		
		super.basePanel.setLayout(new TableLayout(size));
		
		super.basePanel.add(title, "1,0");
	}	

}
