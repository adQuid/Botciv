package map;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.Tile;
import gui.SideDisplay;
import mapActions.FocusOnTile;
import mapActions.MapAction;
import gui.MainUI;
import util.ImageUtilities;

public class MainUIMapDisplay {
	static BufferedImage map;
	static JLabel imageDisplay = new JLabel();
	
	public static Coordinate focus = new Coordinate(0,0);
	
	private static long lastUpdateTime = 0L;
	
	public static MapAction action = new FocusOnTile();
	
	public static int threadsDone = 0;
	
	public static void paintDisplay() {		
		map = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
		paintItBlack(map);
		imageDisplay.setIcon(new ImageIcon(map));
		
		MainUI.displayPanel.add(imageDisplay);
		focus = MainUI.getPlayer().getLastFocus();
		establishSelectAction();
	}
	
	private static void paintItBlack(BufferedImage image) {
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				
				image.setRGB(x, y, 0xFF000000);
			}
		}
	}
	
	public static void repaintDisplay() {
		if(System.currentTimeMillis()-lastUpdateTime < 400) {
			return;
		} else {
			lastUpdateTime = System.currentTimeMillis();
		}
		
		paintItBlack(map);
		
		threadsDone = 0;
		Thread painter1 = new Thread(new MapPainter(focus.y, focus.y+MainUI.visionDistance, focus.x, focus.x+MainUI.visionDistance));
		Thread painter2 = new Thread(new MapPainter(focus.y+MainUI.visionDistance, focus.y+MainUI.visionDistance*2, focus.x, focus.x+MainUI.visionDistance));
		Thread painter3 = new Thread(new MapPainter(focus.y, focus.y+MainUI.visionDistance, focus.x+MainUI.visionDistance, focus.x+MainUI.visionDistance*2));
		Thread painter4 = new Thread(new MapPainter(focus.y+MainUI.visionDistance, focus.y+MainUI.visionDistance*2, focus.x+MainUI.visionDistance, focus.x+MainUI.visionDistance*2));
		
		painter1.start();
		painter2.start();
		painter3.start();
		painter4.start();
		
		while(threadsDone < 4) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		imageDisplay.setIcon(new ImageIcon(map));
		imageDisplay.repaint();
	}
		
	public static void resizeDisplay() {
		if(System.currentTimeMillis()-lastUpdateTime < 400) {
			return;
		} 
		
		map = new BufferedImage(getMapWidth(), getMapHeight(), BufferedImage.TYPE_INT_ARGB);
		repaintDisplay();
	}
		
	public static int getMapWidth() {
		int retval = Math.max((int)(MainUI.GUI.getWidth()*0.8),400);
		return retval - (retval%MainUI.visionDistance);
	}
	
	public static int getMapHeight() {
		int retval = Math.max((int)(MainUI.GUI.getHeight()*0.75),300);
		return retval - (retval%MainUI.visionDistance);
	}
	
	public static Coordinate mapToPixelCoord(int x, int y) {
		return new Coordinate((x - focus.x) * getMapHeight() / MainUI.visionDistance,(y - focus.y) * getMapHeight() / MainUI.visionDistance);
	}
	
	public static Coordinate pixelToMapCoord(int x, int y) {
		return new Coordinate(((x * MainUI.visionDistance / getMapHeight()) + focus.x)%MainUI.getGame().world.WORLD_SIZE,(y * MainUI.visionDistance / getMapHeight()) + focus.y);
	}
	
	private static void establishSelectAction() {
		imageDisplay.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				MainUI.getGame().world.clearSelections();
				Coordinate newSelect = MainUIMapDisplay.pixelToMapCoord(arg0.getX(), arg0.getY());
				action.doAction(newSelect);
				action = new FocusOnTile();
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}			
		});
	}
}
