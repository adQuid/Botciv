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
	
	public static void paintDisplay() {		
		map = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
		paintItBlack(map);
		imageDisplay.setIcon(new ImageIcon(map));
		
		MainUI.displayPanel.add(imageDisplay);
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
		
		for(int lat=focus.y; lat < focus.y+MainUI.visionDistance*2; lat++) {
			for(int lon=focus.x; lon < focus.x+MainUI.visionDistance*2; lon++) {
				Tile tile = MainUI.getGame().world.getTileAt(new Coordinate(lon,lat));
				if(tile != null) {
					paintImage(tile.image(),lon,tile.getY());
				}
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
	
	public static void paintImage(BufferedImage img, int xCoord, int yCoord) {
		Coordinate coord = mapToPixelCoord(xCoord,yCoord);

		
		BufferedImage scaledImage = ImageUtilities.scale(img,(int)Math.ceil(getMapHeight()/(1.0*MainUI.visionDistance)),(int)Math.ceil(getMapHeight()/(1.0*MainUI.visionDistance)));

		for(int x = coord.x; x < coord.x + (getMapHeight()/MainUI.visionDistance); x++) {
			for(int y = coord.y; y < coord.y + (getMapHeight()/MainUI.visionDistance); y++) {
				if(x > 0 && y > 0 && x < getMapWidth() && y < getMapHeight()) {
					try {
						int alpha = scaledImage.getRGB(x - coord.x, y - coord.y) >> 24 & 0xFF;
						if(alpha > 0) {
							map.setRGB(x, y, scaledImage.getRGB(x - coord.x, y - coord.y));
						}
					}catch(ArrayIndexOutOfBoundsException e) {
						//System.err.println("drawing error!");
					}					
				}
			}
		}
	}
	
	private static int getMapWidth() {
		int retval = Math.max((int)(MainUI.GUI.getWidth()*0.8),400);
		return retval - (retval%MainUI.visionDistance);
	}
	
	private static int getMapHeight() {
		int retval = Math.max((int)(MainUI.GUI.getHeight()*0.75),300);
		return retval - (retval%MainUI.visionDistance);
	}
	
	private static Coordinate mapToPixelCoord(int x, int y) {
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
