package launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

import controller.Controller;
import game.BotcivGame;
import game.BotcivPlayer;
import game.TileType;
import game.UnitType;
import game.World;
import gui.MainUI;

public class Launcher {

	public static void main(String[] args) {
		UnitType.loadData();
		TileType.loadData();
		BotcivGame activeGame = loadGame();
		BotcivPlayer player = (BotcivPlayer)activeGame.getPlayers().get(0);
		Controller.instance = new Controller(activeGame);
		
		MainUI.setupGUI(player,false);
	}
	
	public static BotcivGame loadGame() {
		try {
			Scanner reader = new Scanner(new File("saves/test.savegam"));
			Gson gson = new Gson();
			String saveState = reader.nextLine();
			return new BotcivGame(gson.fromJson(saveState, Map.class));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
