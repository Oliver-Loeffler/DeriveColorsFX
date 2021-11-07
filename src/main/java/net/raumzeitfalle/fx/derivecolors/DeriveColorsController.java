package net.raumzeitfalle.fx.derivecolors;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class DeriveColorsController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField brightnessField;

    @FXML
    private TextField hueField;

    @FXML
    private TextField opacityField;

    @FXML
    private TextField saturationField;
    
    @FXML
    private ColorPicker rootColorPicker;
    
    @FXML
    private ColorPicker backgroundColorPicker;
    
    @FXML
    private Pane previewBackground;
    
    @FXML
    private Region previewRegion;
    
    @FXML
    private Region sourceRegion;
    
    @FXML
    private Slider hueSlider;
    
    @FXML
    private Slider satSlider;
    
    @FXML
    private Slider opacitySlider;
    
    @FXML
    private Slider brightnessSlider;
    
    @FXML
    private DeriveColorsModel colorTemplate;
    
    @FXML
    private Label loadedFileLabel;
    
    @FXML
    private Button removeButton;
    
    @FXML
    private ListView<DerivedColor> availableColors;
    
    @FXML
    private ListView<DerivedColor> colorHistory;

    private ObservableList<DerivedColor> colorsHistory;
    
    private Set<DerivedColor> uniqueHistoryItems;
    
    private ListProperty<DerivedColor> colorHistoryProperty;
    
    private ObservableList<DerivedColor> generatedColors;
    
    private ListProperty<DerivedColor> avaliableColorsProperty;
    
    private Set<DerivedColor> uniqueColors;
    
    private ObjectProperty<Path> loadedFileProperty;
    
    private final DecimalFormat colorPropertyFieldFormat = createDecimalFormat();
        
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert availableColors != null : "fx:id=\"availableColors\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert backgroundColorPicker != null : "fx:id=\"backgroundColorPicker\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert brightnessField != null : "fx:id=\"brightnessField\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert brightnessSlider != null : "fx:id=\"brightnessSlider\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert colorHistory != null : "fx:id=\"colorHistory\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert hueField != null : "fx:id=\"hueField\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert hueSlider != null : "fx:id=\"hueSlider\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert loadedFileLabel != null : "fx:id=\"loadedFileLabel\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert opacityField != null : "fx:id=\"opacityField\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert opacitySlider != null : "fx:id=\"opacitySlider\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert previewBackground != null : "fx:id=\"previewBackground\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert previewRegion != null : "fx:id=\"previewRegion\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert rootColorPicker != null : "fx:id=\"rootColorPicker\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert satSlider != null : "fx:id=\"satSlider\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert saturationField != null : "fx:id=\"saturationField\" was not injected: check your FXML file 'DeriveColors.fxml'.";
        assert sourceRegion != null : "fx:id=\"sourceRegion\" was not injected: check your FXML file 'DeriveColors.fxml'.";
      
		colorTemplate = new DeriveColorsModel();
		
		setupColorPropertyControl(colorTemplate.hueShiftProperty(), hueSlider, hueField);
		setupColorPropertyControl(colorTemplate.saturationProperty(), satSlider, saturationField);
		setupColorPropertyControl(colorTemplate.brightnessProperty(), brightnessSlider, brightnessField);
		setupColorPropertyControl(colorTemplate.opacityProperty(), opacitySlider, opacityField);
		
		uniqueColors = new HashSet<>();
		generatedColors = FXCollections.observableArrayList();
		avaliableColorsProperty = new SimpleListProperty<>(generatedColors);
		availableColors.itemsProperty().bind(avaliableColorsProperty);
		availableColors.setCellFactory(e->new DerivedColorListCell());
		availableColors.setOnMouseClicked(event->handleMouseClicks(event, availableColors));
		
		removeButton.disableProperty().bind(avaliableColorsProperty.emptyProperty());
		
		colorsHistory = FXCollections.observableArrayList();
		uniqueHistoryItems = new HashSet<>();
		colorHistoryProperty = new SimpleListProperty<>(colorsHistory);
		colorHistory.itemsProperty().bind(colorHistoryProperty);
		colorHistory.setCellFactory(e->new DerivedColorHistoryListCell());
		colorHistory.setMinWidth(40);
		colorHistory.setPrefWidth(40);
		colorHistory.setOnMouseClicked(event->handleMouseClicks(event, colorHistory));
		
		loadedFileProperty = new SimpleObjectProperty<>(null);
		loadedFileLabel.textProperty()
					   .bind(Bindings.when(loadedFileProperty.isNull())
							         .then("")
							         .otherwise(loadedFileProperty.asString()));
		
		Platform.runLater(()->updatePreview());
	}

    private void setupColorPropertyControl(DoubleProperty property,
			                              Slider slider,
			                              TextField valueField) {
		updateField(valueField, property);
		slider.valueProperty().set(property.get());
		property.bindBidirectional(slider.valueProperty());
		property.addListener((o,p,n)->{
			updatePreview();
			updateField(valueField, property);
		});
		
		valueField.setOnKeyPressed(event->{
			if(event.getCode().equals(KeyCode.ENTER)) {
				updateValuesFromFields();
	        }
		});
		valueField.focusedProperty().addListener((o,p,n)->updateValuesFromFields());
	}
	
	private void updateValuesFromFields() {
		updateFromField(hueField, colorTemplate.hueShiftProperty(), -180, 180);
		updateFromField(saturationField, colorTemplate.saturationProperty(), 0.0, 1.0);
		updateFromField(opacityField, colorTemplate.opacityProperty(), 0.0, 1.0);
		updateFromField(brightnessField, colorTemplate.brightnessProperty(), brightnessSlider.getMin(), brightnessSlider.getMax());
	}
	
	private void updateFromField(TextField field, DoubleProperty property, double lowerBound, double upperBound) {
	    DecimalFormatSymbols formatSymbols = colorPropertyFieldFormat.getDecimalFormatSymbols();
	    char decSep = formatSymbols.getDecimalSeparator();
	    String text = field.getText().replace(decSep,'.');
		try {
			double newValue = checkBounds(lowerBound, upperBound, text);
			field.setText(colorPropertyFieldFormat.format(newValue));
			property.set(newValue);
			updatePreview();
		} catch (NumberFormatException e) {
			field.setText(colorPropertyFieldFormat.format(property.get()));
		}
	}

	private double checkBounds(double lowerBound, double upperBound, String text) {
		double newValue = Double.parseDouble(text);
		if (newValue > upperBound) {
			newValue = upperBound;
		}
		if (newValue < lowerBound) {
			newValue = lowerBound;
		}
		return newValue;
	}
	
	private void updateField(TextField field, DoubleProperty property) {
		field.textProperty().set(colorPropertyFieldFormat.format(property.get()));
	}

    private DecimalFormat createDecimalFormat() {
        return new DecimalFormat("0.00");
    }

	private void handleMouseClicks(MouseEvent mouseevent, ListView<DerivedColor> dataSource) {
		if (mouseevent.getClickCount() == 2 && MouseButton.PRIMARY.equals(mouseevent.getButton())) {
			SelectionModel<DerivedColor> selection = dataSource.getSelectionModel();
			if (!selection.isEmpty()) {
				DerivedColor selectedColor = selection.getSelectedItem();
				rootColorPicker.setValue(selectedColor.sourceColor());
				backgroundColorPicker.setValue(selectedColor.backgroundColor());
				colorTemplate.setSourceColor(selectedColor.sourceColor());
				colorTemplate.setBackground(selectedColor.backgroundColor());
				hueSlider.valueProperty().set(selectedColor.hueShift());
				satSlider.valueProperty().set(selectedColor.saturation());
				brightnessSlider.valueProperty().set(selectedColor.brightness());
				opacitySlider.valueProperty().set(selectedColor.opacity());
				updatePreview();
			}
		} else if (mouseevent.getClickCount() == 1 && MouseButton.SECONDARY.equals(mouseevent.getButton())) {
			dataSource.getSelectionModel().clearSelection();
		}
	}
	
    @FXML
    void addDerivedColorToList(ActionEvent event) {
    	DerivedColor derivative = new DerivedColor(colorTemplate.getSourceColor(),
    			                                   colorTemplate.getBackground(), 
    			                                   colorTemplate.hueShiftProperty().get(),
    			                                   colorTemplate.saturationProperty().get(),
    			                                   colorTemplate.brightnessProperty().get(),
    			                                   colorTemplate.opacityProperty().get());
    	if (!uniqueColors.contains(derivative)) {
    		uniqueColors.add(derivative);
    		Platform.runLater(()->{
    			SelectionModel<DerivedColor> selectionModel = availableColors.getSelectionModel();
    			if (!selectionModel.isEmpty()) {
    				int position = selectionModel.getSelectedIndex();
    				generatedColors.add(position+1, derivative);
    			} else {    				
    				generatedColors.add(derivative);	
    			}
    			availableColors.scrollTo(derivative);
    		});
    	}
    	if (!uniqueHistoryItems.contains(derivative)) {
    		uniqueHistoryItems.add(derivative);
    		Platform.runLater(()->colorsHistory.add(derivative));
    	}
    }

    @FXML
    void copyDerivedColorToClipboard(ActionEvent event) {
    	String colorStringToCopy = getColorStringToCopy();
		Clipboard clipboard = Clipboard.getSystemClipboard();
		clipboard.setContent(Map.of(DataFormat.PLAIN_TEXT, colorStringToCopy));
    }

    private String getColorStringToCopy() {
    	if (!availableColors.getSelectionModel().isEmpty()) {
    		DerivedColor color = availableColors.getSelectionModel().getSelectedItem();
    		return color.getDerivedColor();
    	} else {
    		Color color = colorTemplate.deriveNewColor();
    		return ColorFormatter.from(color);
    	}
	}

	@FXML
    void removeAllDerivedColors(ActionEvent event) {
    	Platform.runLater(()->{
    		generatedColors.clear();
    		uniqueColors.clear();
    	});
    }
    
    @FXML
    void clearHistory(ActionEvent event) {
    	Platform.runLater(()->{
    		colorsHistory.clear();
    		uniqueHistoryItems.clear();
    	});
    }
    
    @FXML
    void clearAll(ActionEvent event) {
    	removeAllDerivedColors(event);
    	clearHistory(event);
    }

    @FXML
    void removeSelectedDerivedColorFromList(ActionEvent event) {
    	SelectionModel<DerivedColor> selection = availableColors.getSelectionModel();
    	if (!selection.isEmpty()) {
    		DerivedColor dc = selection.getSelectedItem();
    		uniqueColors.remove(dc);
    		generatedColors.remove(dc);
    	}
    }
    
    private void loadFrom(FileType type) {
    	FileChooser fc = prepareFileChooser(type, String.format("Load Colors from %s file:", type));
    	File toBeLoaded = fc.showOpenDialog(getWindow());
    	if (toBeLoaded != null) {
    		try {
    			List<DerivedColor> colors = type.readFrom(toBeLoaded);
    			if (!colors.isEmpty()) {
    				Platform.runLater(()->{
    					loadedFileProperty.set(toBeLoaded.toPath());
    					
    					if (uniqueColors.isEmpty()) {
    					    uniqueColors.addAll(colors);
    					    generatedColors.addAll(colors);
    					} else {    					    
    					    for (DerivedColor dcolor : colors) {
    					        if (!uniqueColors.contains(dcolor)) {
    					            generatedColors.add(dcolor);
    					        }
    					    }
    					}
    				});
    			}
    		} catch (Exception anyError) {
    			String header = String.format("Failed to read %s file.",type);
                String content = String.format("Cannot read colors from %s file.\n\n%s", type, toBeLoaded);
                alertUserOnError(type, anyError, header, content);
    		}
    	}
    }

    private FileChooser prepareFileChooser(FileType type, String title) {
        FileChooser fc = new FileChooser();
    	fc.setTitle(title);
    	ExtensionFilter filter = new ExtensionFilter(type.description(), type.fileExtensions());
    	fc.getExtensionFilters().add(filter);
    	fc.setSelectedExtensionFilter(filter);
    	if (loadedFileProperty.get() != null) {
    		fc.setInitialDirectory(loadedFileProperty.get().getParent().toFile());
    		fc.setInitialFileName(loadedFileProperty.get().getFileName().toString());
    	} else {
    		fc.setInitialFileName(type.initialFileName());
    	}
        return fc;
    }
    
    private void saveTofile(FileType type) {
    	FileChooser fc = prepareFileChooser(type, String.format("Save Colors to %s file:", type));
    	if (loadedFileProperty.get() != null) {
    		String fileToSve = loadedFileProperty.get().getFileName().toString();
    		String proposedName = type.proposeFileNameUsing(fileToSve);
    		fc.setInitialFileName(proposedName);
    	}
    	File fileToSave = fc.showSaveDialog(getWindow());
    	if (null != fileToSave) {
    		try {
    			type.writeToFile(generatedColors, fileToSave);
    			Platform.runLater(()->{
    				loadedFileProperty.set(fileToSave.toPath());
    			});
    		} catch (Exception anyError) {
    		    String header = String.format("Failed to write %s file.",type);
    		    String content = String.format("Cannot write colors to %s file.\n\n%s", type, fileToSave);
    		    alertUserOnError(type, anyError, header, content);
    		}
    	}
    }

    private void alertUserOnError(FileType type, Exception anyError, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(getWindow());
        alert.setHeaderText(header);
        alert.setContentText(content);
        TextArea area = new TextArea(anyError.getMessage());
        alert.getDialogPane().setExpandableContent(area);
        Platform.runLater(()->alert.show());
    }
    
    @FXML
    void loadFromCsv(ActionEvent event) {
    	loadFrom(FileType.CSV);
    }

    @FXML
    void loadFromJson(ActionEvent event) {
    	loadFrom(FileType.JSON);
    }
    
    @FXML
    void saveToCsv(ActionEvent event) {
    	saveTofile(FileType.CSV);
    }

    @FXML
    void saveToJson(ActionEvent event) {
    	saveTofile(FileType.JSON);
    }
    
    private Window getWindow() {
    	return loadedFileLabel.getScene().getWindow();
    }
        
    @FXML
    void reset(ActionEvent event) {
    	colorTemplate.reset();
    	updatePreview();
    }

    @FXML
    void updateBackgroundColor(ActionEvent event) {
        setBackgroundColor(backgroundColorPicker.getValue());
    }

    @FXML
    void updateRootColor(ActionEvent event) {
    	setSourceColor(rootColorPicker.getValue());
    }
    
    private void setSourceColor(Color newColor) {
        colorTemplate.setSourceColor(newColor);
        adjustBrightnessScale(newColor);
        updatePreview();
    }
    
    private void setBackgroundColor(Color newColor) {
        colorTemplate.setBackground(newColor);
        updatePreview();
    }
    
    @FXML
    void pickBackgroundColorFromScreen(MouseEvent event) {
        Consumer<Color> update = newColor->{
            backgroundColorPicker.setValue(newColor);
            setBackgroundColor(backgroundColorPicker.getValue());
        };
        
        new ScreenColorPicker(update).run();
        Platform.runLater(()->backgroundColorPicker.requestFocus());
    }
    
    @FXML
    void pickSourceColorFromScreen(MouseEvent event) {
        Consumer<Color> update = newColor->{
            rootColorPicker.setValue(newColor);
            setSourceColor(rootColorPicker.getValue());
        };
        
        new ScreenColorPicker(update).run();
        Platform.runLater(()->rootColorPicker.requestFocus());
    }

	private void adjustBrightnessScale(Color newRootColor) {
		double brightness = newRootColor.getBrightness();
    	double requiredScale = 1/brightness;
    	if (Double.isInfinite(requiredScale)) {
    	    brightnessSlider.setMax(1.0);
    	} else {    	    
    	    brightnessSlider.setMax(requiredScale);
    	}
    	brightnessSlider.setMajorTickUnit(requiredScale/5);
    	brightnessSlider.setMinorTickCount(9);
	}
    
	private void updatePreview() {
		Color derivedColor = colorTemplate.deriveNewColor();
		Platform.runLater(()->{
			previewBackground.setBackground(new Background(new BackgroundFill(colorTemplate.getBackground(), null, null)));
			previewRegion.setBackground(new Background(new BackgroundFill(derivedColor, null, null)));
			sourceRegion.setBackground(new Background(new BackgroundFill(colorTemplate.getSourceColor(), null, null)));
			
			updateSliderWithMultipleStops(satSlider, saturation->colorTemplate.deriveWithSaturation(saturation));
			updateSliderWithMultipleStops(brightnessSlider, brightness->colorTemplate.deriveWithBrightness(brightness));
			updateSliderWithBackground(opacitySlider);
			updateSliderWithMultipleStops(hueSlider, hue->colorTemplate.deriveWithHue(hue));
		});
	}
	
	private void updateSliderWithMultipleStops(Slider slider, DoubleFunction<Color> colorMapper) {
		int steps = 20;
		double min = slider.getMin();
		double max = slider.getMax();
		double paramRange = max-min;
		double paramSteps = paramRange/steps;
		
		double shift = 0.0;
		if (min < 0.0) {	    
		    shift = -min;
		}
		
		List<String> stops = new ArrayList<>(steps);
		double currentParameterValue = min;
		while (currentParameterValue < max) {
			Color derived = colorMapper.apply(currentParameterValue);
			double progress = Math.round(((currentParameterValue+shift)/paramRange)*100);
			String stop = ColorFormatter.from(derived) + " " + progress +"%";
			currentParameterValue += paramSteps;
			stops.add(stop);
		}
		
		if (stops.size() == 1) {
		    Color derived = colorMapper.apply(currentParameterValue);
            double progress = Math.round(((currentParameterValue+shift)/paramRange)*100);
            String stop = ColorFormatter.from(derived) + " " + progress +"%";
            currentParameterValue += paramSteps;
            stops.add(stop);
		}
		
		String allStops = stops.stream().collect(Collectors.joining(", "));
		String style = "-track-color: linear-gradient(to right, "+allStops+");";
		slider.setStyle(style);
	}
	
	private void updateSliderWithBackground(Slider slider) {		
		Color derived = colorTemplate.deriveNewColor();
		Color back = colorTemplate.getBackground();
		String firstStop = ColorFormatter.toRGBwith(derived, slider.getMin());
		String lastStop = ColorFormatter.toRGBwith(derived, slider.getMax());
		String style = "-track-color: linear-gradient(to right, "+firstStop+" 0%, "+lastStop+" 100%);"
				     + "-track-background: "+ColorFormatter.from(back)+";";	
		slider.setStyle(style);
	}
}
