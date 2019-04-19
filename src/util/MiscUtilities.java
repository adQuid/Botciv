package util;

import gui.MainUI;

public class MiscUtilities {

	public static double addTo(double base, Object addTo) {
		try {
			return base+(double)addTo;
		} catch(Exception e) {
			return base;
		}
	}
	
	public static int extractInt(Object obj) {
		return (int)Double.parseDouble(obj.toString());
	}
	
	public static String formatForDescription(String input) {
		String retval = "<html>"+input+"<html>";
		
		for(int i=(2*MainUI.GUI.getWidth())/15; i<retval.length(); i+=(2*MainUI.GUI.getWidth())/15) {
			while(retval.charAt(i) != ' ') {
				if(++i >= retval.length()) {
					return retval;
				}
			}
			retval = retval.substring(0, i)+"<br>"+retval.substring(i+1);
		}
		return retval;
	}
	
}
