package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Settings {

	private static boolean debug;
	
	private static String rightPanelColor;
	private static String bottomPanelColor;
	
	static {
		System.out.println("Loading Settings");
		Path unitTypeFile = Paths.get("./settings.txt");
		Gson gson = new Gson();	
		Map<String, Object> map;
		try {
			map = gson.fromJson(new String(Files.readAllBytes(unitTypeFile)),Map.class);
			debug = (Boolean)map.get("debug");
			rightPanelColor = (String)map.get("rightPanelColor");
			bottomPanelColor = (String)map.get("bottomPanelColor");
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static boolean debug() {
		return debug;
	}
	
	public static String rightPanelColor() {
		return rightPanelColor;
	}
	
	public static String bottomPanelColor() {
		return bottomPanelColor;
	}
}
