package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import util.MiscUtilities;

public class DescriptionListener implements MouseListener{

	String message;
	
	public DescriptionListener(String message) {
		this.message = message;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		BottomDisplay.setDescription(MiscUtilities.formatForDescription(message));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
