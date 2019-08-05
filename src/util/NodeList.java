package util;

import java.util.ArrayList;
import java.util.List;

import map.Coordinate;

public class NodeList {

	private List<Node> nodes;

	public NodeList(List<Node> nodes) {
		super();
		this.nodes = nodes;
	}
	
	public List<Node> getNodes(){
		return nodes;
	}
	
	public List<Coordinate> getCoordinates(){
		List<Coordinate> retval = new ArrayList<Coordinate>();
		
		for(Node current: nodes) {
			retval.add(current.coord);
		}
		
		return retval;
	}
	
}
