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

import game.Tile;
import game.TileType;
import game.Unit;
import game.UnitType;
import game.actions.ClaimTile;
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

public class TileBottomPanel extends Panel{

	private BufferedImage picture;
	private JLabel title = new JLabel();
	private List<JButton> buttons = new ArrayList<JButton>();
	
	private JPanel buttonPanel = new JPanel();
	
	public TileBottomPanel(JPanel base) {
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
	
	public void selectTile(Tile tile) {
		super.clearPanel();
		
		double[][] size = {{0.2,0.8},{TableLayout.FILL}};		
		super.basePanel.setLayout(new TableLayout(size));
		
		picture = ImageUtilities.scale(ImageUtilities.importImage("ui/selection.png"),(int)(super.basePanel.getHeight()*0.8),(int)(basePanel.getHeight()*0.8));

		String ownerStr = tile.getOwner()!=null?tile.getOwner().getName()+"'s":"Unclaimed";
		title.setText(ownerStr+" "+tile.getType().getName());
		title.addMouseListener(new DescriptionListener("Altitude: "+tile.getAltitude()+" Temperature:"+tile.getTemperature()));
		
		super.basePanel.add(new JLabel(new ImageIcon(picture)), "0,0");
		
		double[][] rightSize = {{TableLayout.FILL},{0.5,0.5}};
		JPanel rightPanel = new JPanel();
		super.basePanel.add(rightPanel, "1,0");
		
		rightPanel.setLayout(new TableLayout(rightSize));
		
		rightPanel.add(title,"0,0");
		
		populateActionButtons(tile);
		
		rightPanel.add(buttonPanel,"0,1");
		
		
		
		
		super.basePanel.validate();
	}
	
	private void populateActionButtons(Tile tile) {
		for(int i=0; i<5; i++) {
			buttonPanel.remove(buttons.get(i));
			buttons.set(i,new JButton(""));
			buttonPanel.add(buttons.get(i),i+",0");
		}		
		
		int nextButtonIndex = 0;
		if(tile.getType() == TileType.TYPES.get("Unexplored Tile")) {
			JButton exploreButton = buttons.get(nextButtonIndex++);
			exploreButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(tile.getUnits().get(UnitType.TYPES.get("Explorer")) == null 
							|| tile.getUnits().get(UnitType.TYPES.get("Explorer")).size() == 0) {
						
						MainUI.addAction(new ExploreTile(tile.getCoordinate()));
						tile.addUnit(new Unit(UnitType.TYPES.get("Explorer"), MainUI.getPlayer()));
						MainUIMapDisplay.repaintDisplay();
					}
				}				
			});
			exploreButton.setText("Explore");
			exploreButton.addMouseListener(new DescriptionListener("Send explorers here to reveal the area at a cost of 1 influence and 2 materials."));
		}
		if(MainUI.getPlayer().getExploredTiles().contains(tile.getCoordinate()) 
				&& MainUI.getPlayer() != tile.getOwner()) {
			JButton claimButton = buttons.get(nextButtonIndex++);
			claimButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(tile.getUnits().get(UnitType.TYPES.get("Claim")) == null 
							|| tile.getUnits().get(UnitType.TYPES.get("Claim")).size() == 0) {
						tile.addUnit(new Unit(UnitType.TYPES.get("Claim"), MainUI.getPlayer()));
						MainUIMapDisplay.repaintDisplay();
						MainUI.addAction(new ClaimTile(tile.getCoordinate()));
					}
				}				
			});
			claimButton.setText("Claim");
			claimButton.addMouseListener(new DescriptionListener("."));
		}
	}

}
