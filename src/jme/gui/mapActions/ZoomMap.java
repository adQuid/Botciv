package jme.gui.mapActions;

import jme.gui.MainUI;
import jme.gui.mouseactions.ScrollAction;

public class ZoomMap implements ScrollAction{

	@Override
	public void doAction(int scroll) {
		MainUI.instance.zoom(scroll);
	}

}
