package Format;

public class Format {
	/*
	 * 		Format() method
	 * 		@version: 1.0-beta
	 * 		@author: Derek Sappington
	 * 		@description:  Format class to clean up System calls. (easier for debug purposes).
	 * 
	 */
	
	public static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));

	}
	
	
	/*
	 * 
	 * trims the HTML
	 * 
	 */

	public static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}
}
