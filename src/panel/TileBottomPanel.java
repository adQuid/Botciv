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
		title.setText("Tile");
		
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

		

	}

}
