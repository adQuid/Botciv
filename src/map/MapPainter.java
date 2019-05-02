package map;

import java.awt.image.BufferedImage;

import game.Tile;
import gui.MainUI;
import util.ImageUtilities;

public class MapPainter implements Runnable{

	int lowLat;
	int hiLat;
	int lowLon;
	int hiLon;
		
	public MapPainter(int lowLat, int hiLat, int lowLon, int hiLon) {
		super();
		this.lowLat = lowLat;
		this.hiLat = hiLat;
		this.lowLon = lowLon;
		this.hiLon = hiLon;
	}

	@Override
	public void run() {
		for(int lat=lowLat; lat < hiLat; lat++) {
			for(int lon=lowLon; lon < hiLon; lon++) {
				Tile tile = MainUI.getGame().world.getTileAt(new Coordinate(lon,lat));
				if(tile != null) {
					paintImage(tile.image((int)Math.ceil(MainUIMapDisplay.getMapHeight()/(1.0*MainUI.visionDistance)),(int)Math.ceil(MainUIMapDisplay.getMapHeight()/(1.0*MainUI.visionDistance)))
							,lon,tile.getY());
				}
			}
		}
		MainUIMapDisplay.threadsDone++;
	}
	
	public void paintImage(BufferedImage img, int xCoord, int yCoord) {
		Coordinate coord = MainUIMapDisplay.mapToPixelCoord(xCoord,yCoord);
		
		for(int x = coord.x; x < coord.x + (MainUIMapDisplay.getMapHeight()/MainUI.visionDistance); x++) {
			for(int y = coord.y; y < coord.y + (MainUIMapDisplay.getMapHeight()/MainUI.visionDistance); y++) {
				if(x > 0 && y > 0 && x < MainUIMapDisplay.getMapWidth() && y < MainUIMapDisplay.getMapHeight()) {
					try {
						int alpha = img.getRGB(x - coord.x, y - coord.y) >> 24 & 0xFF;
						if(alpha > 0) {
							MainUIMapDisplay.map.setRGB(x, y, img.getRGB(x - coord.x, y - coord.y));
						}
					}catch(ArrayIndexOutOfBoundsException e) {
						//System.err.println("drawing error!");
					}					
				}
			}
		}
	}
	
}
