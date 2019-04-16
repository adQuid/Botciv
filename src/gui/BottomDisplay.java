package gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import layout.TableLayout;

public class BottomDisplay {

	private static JLabel description = new JLabel("Welcome!");
	
	private static JPanel selectionFiller = new JPanel();
	
	public static void setup() {
		double[][] bigSplit = {{TableLayout.FILL},{0.2,0.8}};
		MainUI.bottomPanel.setLayout(new TableLayout(bigSplit));

		JPanel descriptionPanel = new JPanel();
		descriptionPanel.add(description);
		
		MainUI.bottomPanel.add(descriptionPanel,"0,0");
		MainUI.bottomPanel.add(selectionFiller,"0,1");
	}

	
	public static void testSelection() {
		selectionFiller.removeAll();
		selectionFiller.add(new JLabel("HI"));
		selectionFiller.repaint();
	}
	
	public static void testSelection2() {
		selectionFiller.removeAll();
		
		JPanel otherPanel = new JPanel();
		otherPanel.add(new JLabel("bye"));
		otherPanel.add(new JLabel("bitch!"),BorderLayout.SOUTH);
		
		selectionFiller.add(otherPanel);
		selectionFiller.repaint();
	}
	
	public static void setDescription(String str) {
		description.setText(str);
	}
	
}
