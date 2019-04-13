package util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtilities {

	public static void layerImageOnImage(BufferedImage bottom, BufferedImage top, int xStart, int yStart) {
		for(int x=xStart; x<xStart+top.getWidth(); x++) {
			for(int y=yStart; y<yStart+top.getHeight(); y++) {
				if(bottom.getWidth() > x && bottom.getHeight() > y
						&& top.getWidth() > x && top.getHeight() > y) {
					bottom.setRGB(x, y, top.getRGB(x,y));
				}
			}
		}
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
	public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight) {
		
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
	
	public static BufferedImage importImage(String str) {
		BufferedImage retval;
		try {
			retval = ImageIO.read(new File("assets/images/"+str));
		} catch (IOException e) {
			e.printStackTrace();
			retval = new BufferedImage(50,50, BufferedImage.TYPE_INT_ARGB);
		}
		return retval;
	}
}
