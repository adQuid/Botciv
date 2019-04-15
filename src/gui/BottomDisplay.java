package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import layout.TableLayout;

public class BottomDisplay {

	private static JLabel description = new JLabel("Welcome!");
	
	public static void setup() {
		double[][] bigSplit = {{TableLayout.FILL},{0.2,0.8}};
		MainUI.bottomPanel.setLayout(new TableLayout(bigSplit));

		JPanel descriptionPanel = new JPanel();
		descriptionPanel.add(description);
		
		MainUI.bottomPanel.add(descriptionPanel,"0,0");
	}

	public static void setDescription(String str) {
		description.setText(str);
	}
	
}
