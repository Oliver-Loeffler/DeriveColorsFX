package net.raumzeitfalle.fx.derivecolors;

import javafx.scene.paint.Color;

public record DerivedColor(Color sourceColor, Color backgroundColor, 
						   double hueShift, double saturation, 
		                   double brightness, double opacity) {
	public Color derivative() {
		return sourceColor.deriveColor(hueShift, saturation, brightness, opacity);	
	}
	
	public String getWebColor() {
		return ColorFormatter.from(derivative());
	}

	public String getDerivedColor() {
		return getWebColor();
	}

	public String getBackgroundColor() {
		return ColorFormatter.from(backgroundColor);
	}

	/**
	 * Blends the derived color with the given background color.
	 * @return blended derived {@link Color}
	 */
	public Color blendedDerivative() {
	    Color derived = derivative();
	    double topColorWeight  = derived.getOpacity();
	    double backColorweight = 1-topColorWeight;
	    double red   = topColorWeight*derived.getRed() + backColorweight*backgroundColor.getRed();
	    double green = topColorWeight*derived.getGreen() + backColorweight*backgroundColor.getGreen();
	    double blue  = topColorWeight*derived.getBlue() + backColorweight*backgroundColor.getBlue();
	    return new Color(red, green, blue, 1.0);
	}
	
	public String getSourceColor() {
		return ColorFormatter.from(sourceColor);
	}

	public double getHueShift() {
		return hueShift;
	}
	
	public double getSaturation() {
		return saturation;
	}
	
	public double getBrightness() {
		return brightness;
	}
	
	public double getOpacity() {
		return opacity;
	}
}
