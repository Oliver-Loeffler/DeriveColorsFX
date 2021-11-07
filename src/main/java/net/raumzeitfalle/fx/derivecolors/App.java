package net.raumzeitfalle.fx.derivecolors;

import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("DeriveColors"), 640, 480);
        configureStage(stage);
        placeStageAccordingToPrefs(stage);
        stage.show();
        moveStageIntoVisibleAreaWhenOutsideBounds(stage);
        rememberStagePlacementAndSizingOnExit(stage);
    }

	private void configureStage(Stage stage) {
		stage.setTitle("JavaFX Derive Colors");
        stage.initStyle(StageStyle.UNIFIED);
        stage.setScene(scene);
        stage.setMinWidth(333);
        stage.setMaxWidth(600);
        stage.setMinHeight(563);
        
        stage.getIcons().add(loadIcon("DeriveColors"));
	}

    private void rememberStagePlacementAndSizingOnExit(Stage stage) {
		stage.setOnCloseRequest(event->{
        	Preferences prefs = Preferences.userNodeForPackage(getClass());
        	prefs.putDouble("position_x", stage.getX());
        	prefs.putDouble("position_y", stage.getY());
        	prefs.putDouble("width", stage.getWidth());
        	prefs.putDouble("height", stage.getHeight());
        });
	}

	private void moveStageIntoVisibleAreaWhenOutsideBounds(Stage stage) {
		List<Screen> screens = Screen.getScreensForRectangle(0, 0, stage.getX(), stage.getY());
        if (screens.isEmpty()) {
        	stage.centerOnScreen();
        }
	}

	private void placeStageAccordingToPrefs(Stage stage) {
		Preferences prefs = Preferences.userNodeForPackage(getClass());
    	double x      = prefs.getDouble("position_x", 0d);
    	double y      = prefs.getDouble("position_y", 0d);
    	double width  = prefs.getDouble("width", 360);
    	double height = prefs.getDouble("height", 563);
        stage.setX(x);
    	stage.setY(y);
    	stage.setWidth(width);
    	stage.setHeight(height);
	}

    static void setRoot(String fxml) throws IOException {
    	Parent parent = loadFXML(fxml);
        scene.setRoot(parent);
        String css = App.class.getResource(fxml + ".css").toExternalForm();
        scene.getStylesheets().add(css);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    private static Image loadIcon(String string) {
        String resource = App.class.getResource(string+"@2x.png").toExternalForm();
        return new Image(resource);
    }

    public static void main(String[] args) {
        launch();
    }

}