package utils;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;

public class ListUtils {

	//there has to be a better way, but that's not the point of this
	public static List<Action> combine(List<Action> list1, List<Action> list2){
		List<Action> union = new ArrayList<Action>();
		union.addAll( list1 );
		union.addAll( list2 );
		
		return union;
	}
	
	public static List<List<Action>> without(List<List<Action>> list, List<Action> toRemove){
		List<List<Action>> retval = new ArrayList<List<Action>>(list);
		retval.remove(toRemove);
		return retval;
	}
	
	public static List<List<List<Action>>> prune(List<List<List<Action>>> input, int index, List<Action> toRemove){
		List<List<List<Action>>> retval = new ArrayList<List<List<Action>>>(input);
		
		retval.set(index, without(retval.get(index),toRemove));
		
		return retval;
	}
	
}
