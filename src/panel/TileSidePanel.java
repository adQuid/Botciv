package panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.Unit;
import game.UnitType;
import gui.BottomDisplay;
import gui.LinearList;
import gui.MainUI;
import layout.TableLayout;
import map.Tile;

public class TileSidePanel extends Panel{

	private JLabel title = new JLabel("test");
	private JLabel ownObjectsTitle = new JLabel("Your Units");
	private JLabel otherObjectsTitle = new JLabel("Foreign Units");
	
	private JPanel ownObjectListPanel = new JPanel();	
	private List<Component> ownObjects = new ArrayList<Component>();
	private LinearList ownObjectLinList = new LinearList(ownObjectListPanel,ownObjects,5);
	
	private JPanel otherObjectListPanel = new JPanel();
	private List<Component> otherObjects = new ArrayList<Component>();
	private LinearList otherObjectLinList = new LinearList(otherObjectListPanel,otherObjects,5);
	
	
	private static JLabel placeholder = new JLabel("placeholder");
	
	public TileSidePanel(JPanel base) {
		super(base);

		
	}

	@Override
	public void setup() {
		double[][] size = {{TableLayout.FILL},{0.15,0.1,0.35,0.1,0.35}};
		super.basePanel.setLayout(new TableLayout(size));
		
	}
	
	public void focusOnTile(Tile tile) {
		clearPanel();
		
		super.basePanel.add(title,"0,0");
		super.basePanel.add(ownObjectsTitle,"0,1");
		super.basePanel.add(ownObjectListPanel,"0,2");
		super.basePanel.add(otherObjectsTitle, "0,3");
		super.basePanel.add(otherObjectListPanel,"0,4");
		
		title.setText(tile.getX()+","+tile.getY());		
		ownObjects.clear();
		for(Entry<UnitType,List<Unit>> current: tile.getUnits().entrySet()) {
			JButton toAdd = new JButton(current.getKey().getName()+" ("+current.getValue().size()+")");
			toAdd.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					BottomDisplay.focusOnUnit(current.getValue().get(0));
				}				
			});
			ownObjects.add(toAdd);
		}
		ownObjectLinList.updatePanel();
		
		otherObjectLinList.updatePanel();
		
		MainUI.detailPanel.validate();
	}

}
