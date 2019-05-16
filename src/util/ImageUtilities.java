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

import game.BotcivPlayer;

public class ImageUtilities {

	private static Map<String,BufferedImage> cachedImages = new TreeMap<String,BufferedImage>();
	
	public static BufferedImage layerImageOnImage(BufferedImage bottom, BufferedImage top) {
		return layerImageOnImage(bottom, scale(top,bottom.getWidth(),bottom.getHeight()),0,0);
	}
	
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

					if(topPixel != 0x0 && topAlpha != 0x0) {
						int fullAlpha = 0xff << 24;
						botPixel = RBGadd(RBGMult(botPixel,1-maskingFactor),RBGMult(topPixel,maskingFactor)) + fullAlpha;
						
						retval.setRGB(x, y, botPixel);
					}
				}
			}
		}
		return retval;
	}
	
	private static int RBGadd(int rbg1, int rbg2) {
		
		int red1 = (rbg1 >> 0) - (rbg1 >> 8 << 8);
		int blue1 = (((rbg1 >> 8)  << 8) - (rbg1 >> 16 << 16)) >> 8;
		int green1 = (((rbg1 >> 16) << 16) - (rbg1 >> 24 << 24)) >> 16;
		
		int red2 = (rbg2 >> 0) - (rbg2 >> 8 << 8);
		int blue2 = (((rbg2 >> 8)  << 8) - (rbg2 >> 16 << 16)) >> 8;
		int green2 = (((rbg2 >> 16) << 16) - (rbg2 >> 24 << 24)) >> 16;
		
		int red = Math.min(red1+red2, 0xff);
		int blue = Math.min(blue1+blue2, 0xff);
		int green = Math.min(green1+green2, 0xff);
		
		return (red << 0) + (blue << 8) + (green << 16);
	}
	
	private static int RBGMult(int rbg, float mult) {
		
		int red = (rbg >> 0) - (rbg >> 8 << 8);
		int blue = ((rbg >> 8)  << 8) - (rbg >> 16 << 16) >> 8;
		int green = ((rbg >> 16) << 16) - (rbg >> 24 << 24) >> 16;
		
		red *= mult;
		blue *= mult;
		green *= mult;

		return (red) + (blue<<8) + (green<<16);
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
	public static BufferedImage scale(BufferedImage sbi, int dWidth, int dHeight) {
		
		BufferedImage dbi = null;
	    if(sbi != null) {
			double fWidth = dWidth / (1.0 * sbi.getWidth());
			double fHeight = dHeight / (1.0 * sbi.getHeight());
	        dbi = new BufferedImage(dWidth, dHeight, sbi.getType());
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
			for(int i = 0; i < 50; i++) {
				retval.setRGB(i, i, 0xFFFF0000);
				retval.setRGB(i, 49-i, 0xFFFF0000);
			}
		}
		return retval;
	}
	
	public static BufferedImage cloneImage(BufferedImage img) {
		ColorModel cm = img.getColorModel();
	    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	    WritableRaster raster = img.copyData(img.getRaster().createCompatibleWritableRaster());
	    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	public static BufferedImage applyFactionColor(BufferedImage img, BotcivPlayer player) {
		BufferedImage retval = cloneImage(img);
		
		int color = 0xFF000000;
		if(player.getName().hashCode() % 5 == 0) {
			color += 0xFF;
		}
		if(player.getName().hashCode() % 5 == 1) {
			color += 0xFF00;
		}
		if(player.getName().hashCode() % 5 == 2) {
			color += 0xFFFF;
		}
		if(player.getName().hashCode() % 5 == 3) {
			color += 0xFF0000;
		}
		if(player.getName().hashCode() % 5 == 4) {
			color += 0xFF00FF;
		}
		
		for(int x=0; x<retval.getWidth(); x++) {
			for(int y=0; y<retval.getHeight(); y++) {
				if(retval.getRGB(x, y) == -65349) { //FF00BB
					retval.setRGB(x, y, color);
				}
			}
		}
		
		return retval;
	}
}
