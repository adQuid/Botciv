package util;

import gui.MainUI;

public class MiscUtilities {

	public static int extractInt(Object obj) {
		return (int)Double.parseDouble(obj.toString());
	}
	
	public static String formatForDescription(String input) {
		String retval = "<html>"+input+"<html>";
		
		for(int i=MainUI.GUI.getWidth()/8; i<retval.length(); i+=MainUI.GUI.getWidth()/8) {
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
