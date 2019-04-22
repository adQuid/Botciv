package game;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import util.MiscUtilities;

public class ResourcePortfolio {

	String[] keysArr = {"L","M","I","W","E"};
	List<String> keys = Arrays.asList(keysArr);
	
	public double labor=0;
	public double materials=0;
	public double influence=0;
	public double wealth=0;
	public double education=0;
	
	public ResourcePortfolio() {
		
	}
	
	//shorter names actually reduce chance of misspelling
	public ResourcePortfolio(String inputs) {
		Gson gson = new Gson();	
		Map<String, Object> map = gson.fromJson(inputs,Map.class);
		//check for typos anyway
		for(String key: map.keySet()) {
			if(!keys.contains(key)) {
				System.err.println("Key "+key+" Is not a resource!!!");
			}
		}
		this.labor = MiscUtilities.extractDouble(map.get("L"));
		this.materials = MiscUtilities.extractDouble(map.get("M"));
		this.influence = MiscUtilities.extractDouble(map.get("I"));
		this.wealth = MiscUtilities.extractDouble(map.get("W"));
		this.education = MiscUtilities.extractDouble(map.get("E"));
	}
	
}
