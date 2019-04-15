package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;

public class ResourceDisplay implements MouseListener{

	JLabel label = new JLabel("0");
	ResourceType type;
	double value = 0;
	double rateOfChange = 0;
	
	public enum ResourceType{
		labor,materials,influence,wealth,education;
	}
	
	public ResourceDisplay(ResourceType type) {
		this.type = type;
		label.addMouseListener(this);
	}
	
	public void setValue(double value, double rateOfChange) {
		this.value = value;
		this.rateOfChange = rateOfChange;
		label.setText((int)this.value+"");
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		BottomDisplay.setDescription(new DecimalFormat("#.###").format(value)+" ("+(rateOfChange>=0?"+":"-")+new DecimalFormat("#.###").format(Math.abs(rateOfChange))+" per turn)");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
