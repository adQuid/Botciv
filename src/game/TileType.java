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

public class TileType {

	public static Map<String,TileType> TYPES = new TreeMap<String,TileType>();
	
	public static void loadData(){
		System.out.println("Loading Tile Types");
		Path unitTypeFile = Paths.get("./assets/data/tileTypes.json");
		Gson gson = new Gson();		
		Map<String, Object> map;
		try {
			map = gson.fromJson(new String(Files.readAllBytes(unitTypeFile)),Map.class);
			
			for(Map<String,Object> current: (List<Map<String,Object>>)map.get("data")) {
				TYPES.put((String)current.get("name"),
						new TileType((String)current.get("name"),
								(String)current.get("image"),
								(Boolean)current.get("landPassable"),
								(Boolean)current.get("seaPassable"),
								(Boolean)current.get("suitableStart"),
								(List<Double>)current.get("foodValue")));
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String name;
	private String image;
	public boolean landPassable;
	public boolean seaPassable;
	public boolean suitableStart;
	public List<Double> foodValue;
	
	public TileType(String name, String image, boolean landPassable, boolean seaPassable, boolean suitableStart, List<Double> foodValue) {
		this.name = name;
		this.image = image;
		this.landPassable = landPassable;
		this.seaPassable = seaPassable;
		this.suitableStart = suitableStart;
		this.foodValue = foodValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
		
}
