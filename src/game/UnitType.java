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
		Path unitTypeFile = Paths.get("./assets/data/unitTypes.json");
		Gson gson = new Gson();		
		Map<String, Object> map;
		try {
			map = gson.fromJson(new String(Files.readAllBytes(unitTypeFile)),Map.class);
			
			for(Map<String,Object> current: (List<Map<String,Object>>)map.get("data")) {
				UnitType toAdd = new UnitType((String)current.get("id"),
						(String)current.get("name"),
						MiscUtilities.extractInt(current.get("displayClass")),
						MiscUtilities.extractInt(current.get("displayImportance")),
						(String)current.get("image"));
				toAdd.attributes = (Map)current.get("attributes");
				TYPES.put((String)current.get("name"), toAdd);
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
	//only the most important member of each display class is shown, which the exception of display class 0
	private int displayClass;
	private int displayImportance;
	private String image;
	//catch-all for stats that most units don't have
	private Map<String,Object> attributes;	
	

	public UnitType(String id, String name, int displayClass, int displayImportance, String image) {
		super();
		this.id = id;
		this.name = name;
		this.displayClass = displayClass;
		this.displayImportance = displayImportance;
		this.image = image;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
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
