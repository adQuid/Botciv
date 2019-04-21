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
import game.Unit;
import gui.DescriptionListener;
import gui.MainUI;
import layout.TableLayout;
import map.Coordinate;
import map.MainUIMapDisplay;
import mapActions.MapActionGlobalStore;
import mapActions.Migrate;
import util.ImageUtilities;

public class UnitBottomPanel extends Panel{

	private BufferedImage picture;
	private JLabel title = new JLabel();
	private List<JButton> buttons = new ArrayList<JButton>();
	
	private JPanel buttonPanel = new JPanel();
	
	public UnitBottomPanel(JPanel base) {
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
	
	public void selectUnit(Tile tile, Unit unit) {
		super.clearPanel();
		
		double[][] size = {{0.2,0.8},{TableLayout.FILL}};		
		super.basePanel.setLayout(new TableLayout(size));
		
		picture = ImageUtilities.scale(ImageUtilities.importImage(unit.getType().getImage()),(int)(super.basePanel.getHeight()*0.8),(int)(basePanel.getHeight()*0.8));
		title.setText(unit.getType().getName());
		
		super.basePanel.add(new JLabel(new ImageIcon(picture)), "0,0");
		
		double[][] rightSize = {{TableLayout.FILL},{0.5,0.5}};
		JPanel rightPanel = new JPanel();
		super.basePanel.add(rightPanel, "1,0");
		
		rightPanel.setLayout(new TableLayout(rightSize));
		
		rightPanel.add(title,"0,0");
		
		populateActionButtons(tile, unit);
		
		rightPanel.add(buttonPanel,"0,1");
		
		
		
		
		super.basePanel.validate();
	}
	
	private void populateActionButtons(Tile tile, Unit unit) {
		for(int i=0; i<5; i++) {
			buttonPanel.remove(buttons.get(i));
			buttons.set(i,new JButton(""));
			buttonPanel.add(buttons.get(i),i+",0");
		}
		
		int nextButtonIndex = 0;
		if(unit.getType().has("migrates")) {
			JButton migrateButton = buttons.get(nextButtonIndex++);
			migrateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					List<Coordinate> inRange = MainUI.getGame().world.tilesWithinRange(tile.getCoordinate(), 1);
					for(Coordinate current: inRange) {
						MainUI.getGame().world.getTileAt(current).setSelected(true);
					}
					MainUIMapDisplay.repaintDisplay();
					MapActionGlobalStore.selectedUnit = unit;
					MainUIMapDisplay.action = new Migrate();
				}				
			});		
			migrateButton.setText("Migrate");
			migrateButton.addMouseListener(new DescriptionListener("Take one movement point into a tile you own, an unclaimed tile, or a tile belonging to someone you have migration permissoins with."));
		}
		
		this.buttonPanel.repaint();
	}

}
