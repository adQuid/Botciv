package panel;

import javax.swing.JPanel;

public abstract class Panel {

	protected JPanel basePanel;
	
	public Panel(JPanel base) {
		basePanel = base;
	}
	
	public abstract void setup();
	
	public void clearPanel() {
		basePanel.removeAll();
	}
	
	public JPanel getPanel() {
		return basePanel;
	}
}
