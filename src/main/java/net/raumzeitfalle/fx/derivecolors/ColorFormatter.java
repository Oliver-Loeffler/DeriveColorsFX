package net.raumzeitfalle.fx.derivecolors;

import javafx.scene.paint.Color;

public class ColorFormatter {
	
	public static String toRGBwith(Color color, double alpha) {
		int r = (int) (color.getRed() * 255);
		int g = (int) (color.getGreen() * 255);
		int b = (int) (color.getBlue() * 255);
		int a = (int) (alpha * 255);
		return "rgba("+r+","+g+","+b+","+a+")";
	}
	
	public static String from(Color modified) {
		int r = (int) (modified.getRed() * 255);
		int g = (int) (modified.getGreen() * 255);
		int b = (int) (modified.getBlue() * 255);
		String hex = "#" + toHexString(r) + toHexString(g) + toHexString(b);
		return hex.toLowerCase();
	}
	
	public static String toHexString(int r) {
		String hex = Integer.toHexString(r);
		if (hex.length() < 2) {
			return "0" + hex;
		}
		return hex;
	}
}
