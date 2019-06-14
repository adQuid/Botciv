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
	
	static {
		System.out.println("Loading Settings");
		Path unitTypeFile = Paths.get("./settings.txt");
		Gson gson = new Gson();	
		Map<String, Object> map;
		try {
			map = gson.fromJson(new String(Files.readAllBytes(unitTypeFile)),Map.class);
			debug = (Boolean)map.get("debug");
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
}
