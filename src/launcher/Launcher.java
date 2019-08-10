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
			Controller.setController("test");
			BotcivPlayer player = (BotcivPlayer)Controller.instance.getPlayers().get(0);
			
			MainUI.setupGUI(player,false);
			
			Thread.sleep(2000);
			Controller.instance.startAIs();
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
		
}
