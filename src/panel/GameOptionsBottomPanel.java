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

import controller.Controller;
import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import game.actions.ExploreTile;
import game.actions.MigrateUnit;
import gui.DescriptionListener;
import gui.MainUI;
import layout.TableLayout;
import map.Coordinate;
import map.MainUIMapDisplay;
import mapActions.MapActionGlobalStore;
import mapActions.Migrate;
import util.ImageUtilities;

public class GameOptionsBottomPanel extends Panel{

	private List<JButton> buttons = new ArrayList<JButton>();
	
	private JPanel buttonPanel = new JPanel();
	
	public GameOptionsBottomPanel(JPanel base) {
		super(base);
	}

	@Override
	public void setup() {
		double size[][] = {{0.2,0.2,0.2,0.2,0.2},{TableLayout.FILL}};
		buttonPanel.setLayout(new TableLayout(size));
		
		for(int i=0; i<5; i++) {
			buttons.add(new JButton(""));
			buttonPanel.add(buttons.get(i),i+",0");
		}
	}
	
	public void openGameOptions() {
		super.clearPanel();
		
		double[][] size = {{0.2,0.8},{TableLayout.FILL}};		
		super.basePanel.setLayout(new TableLayout(size));
			
		double[][] rightSize = {{TableLayout.FILL},{0.5,0.5}};
		JPanel rightPanel = new JPanel();
		super.basePanel.add(rightPanel, "1,0");
		
		rightPanel.setLayout(new TableLayout(rightSize));
		
		populateActionButtons();
		
		rightPanel.add(buttonPanel,"0,1");
		
		
		
		
		super.basePanel.validate();
	}
	
	private void populateActionButtons() {
		for(int i=0; i<5; i++) {
			buttonPanel.remove(buttons.get(i));
			buttons.set(i,new JButton(""));
			buttonPanel.add(buttons.get(i),i+",0");
		}		
		
		JButton saveButton = buttons.get(0);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.instance.saveGame();
			}				
		});
		saveButton.setText("Save");
		saveButton.addMouseListener(new DescriptionListener("Send explorers here to reveal the area at a cost of 1 influence and 2 materials."));		
	}

}
