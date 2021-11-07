package net.raumzeitfalle.fx.derivecolors;

import javafx.scene.paint.Color;

public class DerivedColorBean {
	
	// Consider opencsv with @CsvBindByName;
	
	private String derivedColor = "#ffffff";
	private String backgroundColor = "#ffffff";
	private String sourceColor = "#ffffff";
	private double hueShift = 0.0;
	private double saturation = 1.0;
	private double brightness = 1.0;
	private double opacity = 1.0;
    
	public String getDerivedColor() {
		return derivedColor;
	}
	public void setDerivedColor(String derivedColor) {
		this.derivedColor = derivedColor;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getSourceColor() {
		return sourceColor;
	}
	public void setSourceColor(String sourceColor) {
		this.sourceColor = sourceColor;
	}
	public double getHueShift() {
		return hueShift;
	}
	public void setHueShift(double hueShift) {
		this.hueShift = hueShift;
	}
	public double getSaturation() {
		return saturation;
	}
	public void setSaturation(double saturation) {
		this.saturation = saturation;
	}
	public double getBrightness() {
		return brightness;
	}
	public void setBrightness(double brightness) {
		this.brightness = brightness;
	}
	public double getOpacity() {
		return opacity;
	}
	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}
	
	public static DerivedColorBean fromRecord(DerivedColor source) {
		DerivedColorBean bean = new DerivedColorBean();
		bean.setSourceColor(source.getSourceColor());
		bean.setBackgroundColor(source.getBackgroundColor());
		bean.setDerivedColor(source.getDerivedColor());
		bean.setHueShift(source.hueShift());
		bean.setSaturation(source.saturation());
		bean.setBrightness(source.brightness());
		bean.setOpacity(source.opacity());
		return bean;
	}
	
	public static DerivedColor toRecord(DerivedColorBean bean) {
		Color source = Color.web(bean.sourceColor);
		Color background = Color.web(bean.backgroundColor);
		return new DerivedColor(source, background, bean.hueShift, bean.saturation, bean.brightness, bean.opacity);
	}
}
