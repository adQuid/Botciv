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

import game.Unit;
import gui.DescriptionListener;
import gui.MainUI;
import layout.TableLayout;
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
		// TODO Auto-generated method stub
		double size[][] = {{0.2,0.2,0.2,0.2,0.2},{TableLayout.FILL}};
		buttonPanel.setLayout(new TableLayout(size));
	}
	
	public void selectUnit(Unit unit) {
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
		
		populateActionButtons(unit);
		
		rightPanel.add(buttonPanel,"0,1");
		
		
		
		
		super.basePanel.validate();
	}
	
	private void populateActionButtons(Unit unit) {
		if(unit.getType().has("migrates")) {
			JButton migrateButton = new JButton("Migrate");
			migrateButton.addMouseListener(new DescriptionListener("Take one movement point into a tile you own, an unclaimed tile, or a tile belonging to someone you have migration permissoins with."));
			buttons.add(migrateButton);
		}
		
		for(int i=0; i<5; i++) {
			if(buttons.size() > i) {
				buttonPanel.add(buttons.get(i),i+",0");
			}else {
				buttonPanel.add(new JButton(),i+",0");
			}
		}
	}

}
