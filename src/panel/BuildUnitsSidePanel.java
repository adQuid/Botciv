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

import game.ResourcePortfolio;
import game.Tile;
import game.Unit;
import game.UnitType;
import game.actions.BuildUnit;
import gui.BottomDisplay;
import gui.CornerDisplay;
import gui.DescriptionListener;
import gui.LinearList;
import gui.MainUI;
import layout.TableLayout;
import map.MainUIMapDisplay;
import util.GameLogicUtilities;
import util.MiscUtilities;

public class BuildUnitsSidePanel extends Panel{
	
	private JPanel unitListPanel = new JPanel();	
	private List<Component> units = new ArrayList<Component>();
	private LinearList unitLinList = new LinearList(unitListPanel,units,8);
	
	public BuildUnitsSidePanel(JPanel base) {
		super(base);

		
	}

	@Override
	public void setup() {
		
	}
	
	public void focusOnTile(Tile tile, List<UnitType> types) {
		super.clearPanel();
		
		double[][] size = {{TableLayout.FILL},{TableLayout.FILL}};
		super.basePanel.setLayout(new TableLayout(size));
		
		super.basePanel.add(unitListPanel,"0,0");
			
		units.clear();
		for(UnitType current: types) {
			JButton toAdd = new JButton(current.getName());
			
			ResourcePortfolio cost = new ResourcePortfolio();
			cost.labor = MiscUtilities.extractDouble(current.getAttribute("laborCost"));
			cost.materials = MiscUtilities.extractDouble(current.getAttribute("materialsCost"));
			cost.wealth = MiscUtilities.extractDouble(current.getAttribute("wealthCost"));
			
			toAdd.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(GameLogicUtilities.tryTopay(MainUI.getPlayer(), cost)) {
						tile.addUnit(new Unit(current,MainUI.getPlayer()),MainUI.getGame());
						MainUI.addAction(new BuildUnit(tile.getCoordinate(),current));
						MainUIMapDisplay.repaintDisplay();
						CornerDisplay.updateResourceDisplays();
					}
				}				
			});
			toAdd.addMouseListener(new DescriptionListener(current.getTooltip()+" <br>Cost: "+cost));
			units.add(toAdd);
		}
		unitLinList.updatePanel();
				
		super.basePanel.validate();
	}

}
