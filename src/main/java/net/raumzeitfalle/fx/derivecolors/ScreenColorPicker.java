package net.raumzeitfalle.fx.derivecolors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ScreenColorPicker {

    private final List<Stage> screenStages = new ArrayList<>();
    
    private final Consumer<Color> colorConsumer;
    
    public ScreenColorPicker(Consumer<Color> colorConsumer) {
        this.colorConsumer = Objects.requireNonNull(colorConsumer);
    }
    
    public void run() {
        Screen.getScreens()
              .forEach(this::createScreenShotWindow);
    }
    
    private void createScreenShotWindow(Screen screen) {
        Rectangle2D bounds = screen.getVisualBounds();
        WritableImage screenShot = createScreenShot(screen);
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        
        StackPane pane = new StackPane();
        pane.setMinWidth(bounds.getWidth());
        pane.setMinHeight(bounds.getHeight());
        pane.getChildren().add(new ImageView(screenShot));
        pane.setOnMouseClicked(this::pickColorOnMouseClick);
        pane.setCursor(Cursor.CROSSHAIR);
       
        Scene scene = new Scene(pane, bounds.getWidth(), bounds.getHeight());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        scene.setOnKeyPressed(this::abort);
        screenStages.add(stage);
        stage.show();
    }
    
    private WritableImage createScreenShot(Screen screen) {
        Rectangle2D bounds = screen.getVisualBounds();
        return new Robot().getScreenCapture(null, bounds);
    }

    private void pickColorOnMouseClick(MouseEvent event) {
        if (event.getClickCount() > 0 && MouseButton.PRIMARY.equals(event.getButton())) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (event.getTarget() instanceof ImageView) {
                ImageView target = ((ImageView) event.getTarget());
                Image image = target.getImage();
                PixelReader reader = image.getPixelReader();
                colorConsumer.accept(reader.getColor(x, y));
            }           
        } 
        closeAllWindows();
    }

    private void closeAllWindows() {
        for (Stage stage : screenStages) {
            stage.close();
        }
        screenStages.clear();
    }

    private void abort(KeyEvent keyevent) {
        closeAllWindows();
    }
}
