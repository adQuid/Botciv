package util;


public class MiscUtilities {

	public static double addTo(double base, Object addTo) {
		try {
			return base+(double)addTo;
		} catch(Exception e) {
			return base;
		}
	}
	
	public static int extractInt(Object obj) {
		if(obj == null) {
			return 0;
		}
		return (int)Double.parseDouble(obj.toString());
	}
	
	public static double extractDouble(Object obj) {
		if(obj == null) {
			return 0;
		}
		return (double)Double.parseDouble(obj.toString());
	}
	
	public static double orZero(Double input) {
		if(input == null) {
			return 0.0;
		} else {
			return input;
		}
	}
	
}
