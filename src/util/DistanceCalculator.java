package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.World;
import map.Coordinate;

public class DistanceCalculator {

	private class WorkingNode {//terrible name
		public Coordinate coord;
		public Coordinate origin;
		public double distance;
		public boolean checked;
		
		public WorkingNode(Coordinate coord, Coordinate origin, double distance) {
			this.coord = coord;
			this.origin = origin;
			this.distance = distance;
			this.checked = false;
		}		
	}
	
	World world;
	
	public DistanceCalculator(World world) {
		this.world = world; //a copy would be safer, but this equation is going to be running tons of times
	}
	
	public NodeList coordinatesInRange(Coordinate start, double range){
		
		List<WorkingNode> nodes = new ArrayList<WorkingNode>();
		List<WorkingNode> nodesToAdd = new ArrayList<WorkingNode>();
		
		nodesToAdd.add(new WorkingNode(start,null,0.0));
		
		while(nodesToAdd.size() > 0) {
			nodes.addAll(nodesToAdd);
			nodesToAdd.clear();
			for(WorkingNode current: nodes) {
				if(!current.checked) {
					tryToAdd(nodesToAdd, current.coord, current.coord.left(), current.distance, range);
					tryToAdd(nodesToAdd, current.coord, current.coord.right(), current.distance, range);
					tryToAdd(nodesToAdd, current.coord, current.coord.up(), current.distance, range);
					tryToAdd(nodesToAdd, current.coord, current.coord.down(), current.distance, range);
					current.checked = true;
				}
			}
		}
		
		List<Node> retval = new ArrayList<Node>();
		for(WorkingNode current: nodes) {
			retval.add(new Node(current.coord,current.origin));
		}
		
		return new NodeList(retval);
	}
	
	private void tryToAdd(List<WorkingNode> set, Coordinate origin, Coordinate toAdd, double range, double rangeMax) {
		if(world.getTileAt(toAdd) == null) {
			return;
		}
		if(world.getTileAt(toAdd).moveCost() + range <= rangeMax) {
			WorkingNode retval = new WorkingNode(toAdd, origin, world.getTileAt(toAdd).moveCost() + range);
			set.add(retval);
		}
	}
	
}
