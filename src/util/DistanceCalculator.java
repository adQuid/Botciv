package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.World;
import map.Coordinate;

public class DistanceCalculator {

	private class Node {
		public Coordinate coord;
		public double distance;
		public boolean checked;
		
		public Node(Coordinate coord, double distance) {
			this.coord = coord;
			this.distance = distance;
			this.checked = false;
		}		
	}
	
	World world;
	
	public DistanceCalculator(World world) {
		this.world = world; //a copy would be safer, but this equation is going to be running tons of times
	}
	
	public List<Coordinate> coordinatesInRange(Coordinate start, double range){
		
		Set<Node> nodes = new HashSet<Node>();
		Set<Node> nodesToAdd = new HashSet<Node>();
		
		nodesToAdd.add(new Node(start,0.0));
		
		while(nodesToAdd.size() > 0) {
			nodes.addAll(nodesToAdd);
			nodesToAdd.clear();
			for(Node current: nodes) {
				if(!current.checked) {
					tryToAdd(nodesToAdd, current.coord.left(), current.distance, range);
					tryToAdd(nodesToAdd, current.coord.right(), current.distance, range);
					tryToAdd(nodesToAdd, current.coord.up(), current.distance, range);
					tryToAdd(nodesToAdd, current.coord.down(), current.distance, range);
					current.checked = true;
				}
			}
		}
		
		List<Coordinate> retval = new ArrayList<Coordinate>();
		for(Node current: nodes) {
			retval.add(current.coord);
		}
		
		return retval;
	}
	
	private void tryToAdd(Set<Node> set, Coordinate toAdd, double range, double rangeMax) {
		if(world.getTileAt(toAdd) == null) {
			return;
		}
		if(world.getTileAt(toAdd).moveCost() + range <= rangeMax) {
			Node retval = new Node(toAdd, world.getTileAt(toAdd).moveCost() + range);
			set.add(retval);
		}
	}
	
}
