package util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

public class ImageUtilities {

	private static Map<String,BufferedImage> cachedImages = new TreeMap<String,BufferedImage>();
	
	public static BufferedImage layerImageOnImage(BufferedImage bottom, BufferedImage top, int xStart, int yStart) {
		BufferedImage retval = cloneImage(bottom);
		for(int x=xStart; x<xStart+top.getWidth(); x++) {
			for(int y=yStart; y<yStart+top.getHeight(); y++) {
				if(retval.getWidth() > x && retval.getHeight() > y
						&& top.getWidth() > x && top.getHeight() > y) {

					int botPixel = retval.getRGB(x,y);
					int botAlpha = (botPixel >> 24) & 0xff;
					int topPixel = top.getRGB(x, y);
					int topAlpha = (topPixel >> 24) & 0xff;
					float maskingFactor = (float)topAlpha/0xff;
					
					
					int fullAlpha = 0xff << 24;
					botPixel = ((int)((maskingFactor * (topPixel - (topAlpha<<24))) + ((1-maskingFactor) * (botPixel - (botAlpha<<24)))) + fullAlpha);
										
					retval.setRGB(x, y, botPixel);
				}
			}
		}
		return retval;
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
		
		//if it's already in cache, forget the file read
		if(cachedImages.get(str) != null) {
			return cachedImages.get(str);
		}
		
		try {
			retval = ImageIO.read(new File("assets/images/"+str));
			cachedImages.put(str, retval);
		} catch (IOException e) {
			e.printStackTrace();
			retval = new BufferedImage(50,50, BufferedImage.TYPE_INT_ARGB);
		}
		return retval;
	}
	
	public static BufferedImage cloneImage(BufferedImage img) {
		ColorModel cm = img.getColorModel();
	    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	    WritableRaster raster = img.copyData(img.getRaster().createCompatibleWritableRaster());
	    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}
