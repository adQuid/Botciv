package launcher;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import com.google.gson.Gson;

import controller.Controller;
import game.BotcivGame;
import game.BotcivPlayer;
import game.TileType;
import game.UnitType;
import jme.gui.MainUI;

public class Launcher {

	public static void main(String[] args) {
		JFrame test = new JFrame("Error");
		test.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		try {
			UnitType.loadData();
			TileType.loadData();
			BotcivGame activeGame = loadGame();
			BotcivPlayer player = (BotcivPlayer)activeGame.getPlayers().get(0);
			Controller.setController(activeGame);

			MainUI.setupGUI(player,false);
		}catch(Exception e) {
			e.printStackTrace();
			test.add(new JLabel(e.getStackTrace()[0].toString()+":"+e.getClass()+(e.getMessage()!=null?":"+e.getMessage():"")),BorderLayout.CENTER);

			test.pack();
			test.setVisible(true);
		}
	}
	
	public static BotcivGame newGame() {
		return new BotcivGame();
	}
	
	public static BotcivGame loadGame() {
		try {
			System.out.println("loading game");
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
