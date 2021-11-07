package net.raumzeitfalle.fx.derivecolors;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;

public class DeriveColorsModel {
	
	private Color sourceColor;
		
	private Color background;
	
	private Color derived;
	
	private final DoubleProperty hueShiftProperty;
	
	private final DoubleProperty saturationFactor;
	
	private final DoubleProperty opacityFactor;
	
	private final DoubleProperty brightnessFactor;
	
	public DeriveColorsModel() {
		this.sourceColor = Color.rgb(100, 60, 120);
		this.background = Color.WHITE;
		this.hueShiftProperty = new SimpleDoubleProperty(0.0);
		this.saturationFactor = new SimpleDoubleProperty(1.0);
		this.opacityFactor = new SimpleDoubleProperty(1.0);
		this.brightnessFactor = new SimpleDoubleProperty(2.0);
		deriveNewColor(); 
	}
	
	public Color deriveWithSaturation(double sat) {
		double hue = hueShiftProperty.get();
		double opa = opacityFactor.get();
		double brt = brightnessFactor.get();
		return sourceColor.deriveColor(hue,sat, brt,opa);
	}
	
	public Color deriveWithBrightness(double brt) {
		double hue = hueShiftProperty.get();
		double opa = opacityFactor.get();
		double sat = saturationFactor.get();
		return sourceColor.deriveColor(hue,sat, brt,opa);
	}
	
	public Color deriveWithHue(double hue) {
		double brt = brightnessFactor.get();
		double opa = opacityFactor.get();
		double sat = saturationFactor.get();
		return sourceColor.deriveColor(hue,sat, brt,opa);
	}
	

	public Color getSourceColor() {
		return sourceColor;
	}

	public void setSourceColor(Color newSourceColor) {
		this.sourceColor = newSourceColor;
	}
	
	public DoubleProperty hueShiftProperty() {
		return hueShiftProperty;
	}
	
	public DoubleProperty saturationProperty() {
		return saturationFactor;
	}
	
	public DoubleProperty opacityProperty() {
		return opacityFactor;
	}
	
	public DoubleProperty brightnessProperty() {
		return brightnessFactor;
	}
	
	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}
	
	public Color reset() {
		derived = sourceColor.brighter();
		hueShiftProperty.set(0);
		saturationFactor.set(1);
		opacityFactor.set(1);
		brightnessFactor.set(1);
		return derived;
	}
	
	public Color deriveNewColor() {
		double hue = hueShiftProperty.get();
		double sat = saturationFactor.get();
		double opa = opacityFactor.get();
		double brt = brightnessFactor.get();
		derived = sourceColor.deriveColor(hue,sat, brt,opa);
		return derived;
	}
}
