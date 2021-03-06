package game;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import util.MiscUtilities;

public class UnitType {

	public static Map<String,UnitType> TYPES = new TreeMap<String,UnitType>();

	public static void loadData(){
		System.out.println("Loading Unit Types");
		Path unitTypeFile = Paths.get("./assets/data/unitTypes.json");
		Gson gson = new Gson();		
		Map<String, Object> map;
		try {
			map = gson.fromJson(new String(Files.readAllBytes(unitTypeFile)),Map.class);
			
			for(Map<String,Object> current: (List<Map<String,Object>>)map.get("data")) {
				UnitType toAdd = new UnitType((String)current.get("id"),
						(String)current.get("name"),
						(String)current.get("tooltip"),
						MiscUtilities.extractInt(current.get("displayClass")),
						MiscUtilities.extractInt(current.get("displayImportance")),
						(String)current.get("image"),
						MiscUtilities.extractInt(current.get("maxHealth")));
				toAdd.attributes = (Map)current.get("attributes");
				TYPES.put((String)current.get("id"), toAdd);
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String id;
	private String name;
	private String tooltip;
	//only the most important member of each display class is shown, which the exception of display class 0
	private int displayClass;
	private int displayImportance;
	private String image;
	private int maxHealth;
	//catch-all for stats that most units don't have
	private Map<String,Object> attributes;	
	

	public UnitType(String id, String name, String tooltip, int displayClass, int displayImportance, String image, int maxHealth) {
		super();
		this.id = id;
		this.name = name;
		this.tooltip = tooltip;
		this.displayClass = displayClass;
		this.displayImportance = displayImportance;
		this.image = image;
		this.maxHealth = maxHealth;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getTooltip() {
		return tooltip;
	}
	
	public int getDisplayClass() {
		return displayClass;
	}

	public int getDisplayImportance() {
		return displayImportance;
	}

	public String getImage() {
		return image;
	}	
	
	public int getMaxHealth() {
		return maxHealth;
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public boolean has(String key) {
		if(attributes.get(key)==null) {
			return false;
		}
		if(attributes.get(key).getClass() != Boolean.class) {
			return true;
		}
		return (Boolean)attributes.get(key);
	}
	
	public ResourcePortfolio getCost() {
		ResourcePortfolio retval = new ResourcePortfolio();
		if(has("laborCost")) {
			retval.labor = (Double)getAttribute("laborCost");
		}
		if(has("materialsCost")) {
			retval.materials = (Double)getAttribute("materialsCost");
		}
		if(has("wealthCost")) {
			retval.wealth = (Double)getAttribute("wealthCost");
		}
		
		return retval;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnitType other = (UnitType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}	
		
	
	
}
