package map;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.MainUI;

public class MainUIMapDisplay {
	static BufferedImage map;
	static JLabel imageDisplay = new JLabel();
	
	public static Coordinate focus = new Coordinate(0,0);
	
	public static void paintDisplay() {
		MainUI.displayPanel = new JPanel();
		
		map = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
		paintItBlack(map);
		imageDisplay.setIcon(new ImageIcon(map));
		
		MainUI.displayPanel.add(imageDisplay);
		
	}
	
	private static void paintItBlack(BufferedImage image) {
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				
				image.setRGB(x, y, 0xFF000000);
			}
		}
	}
	
	public static void repaintDisplay() {
		
		paintItBlack(map);
		
		for(int lat=focus.y; lat < focus.y+MainUI.visionDistance*2; lat++) {
			for(int lon=focus.x; lon < focus.x+MainUI.visionDistance*2; lon++) {
				Tile tile = World.getTileAt(new Coordinate(lon,lat));
				if(tile != null) {
					paintImage(tile.image(),lon,tile.getY());
				}
			}
		}
		
		imageDisplay.setIcon(new ImageIcon(map));
		imageDisplay.repaint();
	}
		
	public static void resizeDisplay() {
		map = new BufferedImage(getMapWidth(), getMapHeight(), BufferedImage.TYPE_INT_ARGB);
		repaintDisplay();
	}
	
	public static void paintImage(BufferedImage img, int xCoord, int yCoord) {
		Coordinate coord = mapToPixelCoord(xCoord,yCoord);

		
		BufferedImage scaledImage = scale(img,img.getType(),(int)Math.ceil(getMapHeight()/(1.0*MainUI.visionDistance)),(int)Math.ceil(getMapHeight()/(1.0*MainUI.visionDistance)));

		for(int x = coord.x; x < coord.x + (getMapHeight()/MainUI.visionDistance); x++) {
			for(int y = coord.y; y < coord.y + (getMapHeight()/MainUI.visionDistance); y++) {
				if(x > 0 && y > 0 && x < getMapWidth() && y < getMapHeight()) {
					try {
						int alpha = scaledImage.getRGB(x - coord.x, y - coord.y) >> 24 & 0xFF;
						if(alpha > 0) {
							map.setRGB(x, y, scaledImage.getRGB(x - coord.x, y - coord.y));
						}
					}catch(ArrayIndexOutOfBoundsException e) {
						System.err.println("drawing error!");
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
		return new Coordinate(((x * MainUI.visionDistance / getMapHeight()) + focus.x)%World.WORLD_SIZE,(y * MainUI.visionDistance / getMapHeight()) + focus.y);
	}
	
	/**
	 * scale image
	 * 
	 * @param sbi image to scale
	 * @param imageType type of image
	 * @param dWidth width of destination image
	 * @param dHeight height of destination image
	 * @param fWidth x-factor for transformation / scaling
	 * @param fHeight y-factor for transformation / scaling
	 * @return scaled image
	 */
	private static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight) {
		
		BufferedImage dbi = null;
	    if(sbi != null) {
			double fWidth = dWidth / (1.0 * sbi.getWidth());
			double fHeight = dHeight / (1.0 * sbi.getHeight());
	        dbi = new BufferedImage(dWidth, dHeight, imageType);
	        Graphics2D g = dbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
	        g.drawRenderedImage(sbi, at);
	    }

	    return dbi;
	}
	
}
