package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.BotcivPlayer;
import layout.TableLayout;
import util.ImageUtilities;

public class CornerDisplay {

	public static ResourceDisplay laborDisplay = new ResourceDisplay(ResourceDisplay.ResourceType.labor);
	public static ResourceDisplay materialsDisplay = new ResourceDisplay(ResourceDisplay.ResourceType.materials);
	public static ResourceDisplay wealthDisplay = new ResourceDisplay(ResourceDisplay.ResourceType.wealth);
	public static ResourceDisplay influenceDisplay = new ResourceDisplay(ResourceDisplay.ResourceType.influence);
	public static ResourceDisplay educationDisplay = new ResourceDisplay(ResourceDisplay.ResourceType.education);
	
	private static JButton clearTurnButton = new JButton("Clear Turn");
	private static JButton commitTurnButton = new JButton("Commit Turn");
	
	public static void setup() {
		
		double[][] splitSize = {{TableLayout.FILL},{0.4,0.6}};
		MainUI.cornerPanel.setLayout(new TableLayout(splitSize));
		
		JPanel resourcePanel = new JPanel();
		double[][] size = {{0.15,0.15,0.15,0.15,0.15,TableLayout.FILL},{0.5,0.5}};
		resourcePanel.setLayout(new TableLayout(size));
		
		BufferedImage nextIcon = ImageUtilities.importImage("ui/labor.png");
		JLabel nextLabel = new JLabel(new ImageIcon(ImageUtilities.scale(nextIcon,40,40)));
		nextLabel.addMouseListener(new DescriptionListener("Labor represents the amount of work your civilization can get done at a time. This can be through people, animals, or machienes. Labor left over at the end of the turn is used by the people for their own needs."));
		resourcePanel.add(nextLabel,"0,0");
		resourcePanel.add(laborDisplay.getLabel(),"1,0");
		
		nextIcon = ImageUtilities.importImage("ui/materials.png");
		nextLabel = new JLabel(new ImageIcon(ImageUtilities.scale(nextIcon,40,40)));
		nextLabel.addMouseListener(new DescriptionListener("Materials are the aggregate of all common resources."));
		resourcePanel.add(nextLabel,"2,0");
		resourcePanel.add(materialsDisplay.getLabel(),"3,0");
		
		nextIcon = ImageUtilities.importImage("ui/influence.png");
		nextLabel = new JLabel(new ImageIcon(ImageUtilities.scale(nextIcon,40,40)));
		nextLabel.addMouseListener(new DescriptionListener("Influence is a measure of the stregth of your leadership and beurocracy. A power structure with more influcence can convince more people to act with the same will, and sees tasks done more efficiently. One with little to no influence will find any order ignored, and risks collapse. No single government can store more than 1000 influence."));
		resourcePanel.add(nextLabel,"4,0");
		resourcePanel.add(influenceDisplay.getLabel(),"5,0");
		
		nextIcon = ImageUtilities.importImage("ui/wealth.png");
		nextLabel = new JLabel(new ImageIcon(ImageUtilities.scale(nextIcon,40,40)));
		nextLabel.addMouseListener(new DescriptionListener("Money, jewels, toasters, anything you wouldn't eat or build a house with is wealth. Wealth won't directly help you do much, but it can often be traded for things that can. Advanced societies tend to need to pay for more things as people have higher standards of what the world owes them."));
		resourcePanel.add(nextLabel,"0,1");
		resourcePanel.add(wealthDisplay.getLabel(),"1,1");
		
		nextIcon = ImageUtilities.importImage("ui/education.png");
		nextLabel = new JLabel(new ImageIcon(ImageUtilities.scale(nextIcon,40,40)));
		nextLabel.addMouseListener(new DescriptionListener("Education is the brains to Labor's brawn, and can also be enhanced by technology. Any leftovers not used to run your society will be used to discover new science and technology, but if the upkeep of what you already know is too much, it is possible to decay into a more primative state."));
		resourcePanel.add(nextLabel,"2,1");
		resourcePanel.add(educationDisplay.getLabel(),"3,1");
		
		JPanel bigButtonsPanel = new JPanel();
		bigButtonsPanel.setLayout(new GridLayout(2,2));
		
		clearTurnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainUI.clearTurn();
			}			
		});
		
		commitTurnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainUI.commitTurn();
				laborDisplay.setValue(MainUI.getGame().players.get(0).getLabor(), MainUI.getGame().players.get(0).getPortfolio(MainUI.getGame().world).labor);
				materialsDisplay.setValue(MainUI.getGame().players.get(0).getMaterials(), MainUI.getGame().players.get(0).getPortfolio(MainUI.getGame().world).materials);
			}			
		});
		
		bigButtonsPanel.add(new JButton("Empire Options"));
		bigButtonsPanel.add(new JButton("???"));
		bigButtonsPanel.add(clearTurnButton);
		bigButtonsPanel.add(commitTurnButton);
		
		MainUI.cornerPanel.add(resourcePanel,"0,0");
		MainUI.cornerPanel.add(bigButtonsPanel,"0,1");
	}
	
}
