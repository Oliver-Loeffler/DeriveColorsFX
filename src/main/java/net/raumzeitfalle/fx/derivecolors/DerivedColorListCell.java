package net.raumzeitfalle.fx.derivecolors;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DerivedColorListCell extends ListCell<DerivedColor> {
	@Override
	protected void updateItem(DerivedColor item, boolean empty) {
		super.updateItem(item, empty);
		updateView();
	}
	
	private void updateView() {
		if (getItem() == null) {
			setText(null);
			setGraphic(null);
			return;
		}
		GridPane pane = new GridPane();
		DerivedColor derived = getItem();
		BorderWidths widths = new BorderWidths(2);
		BorderStroke stroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, widths);
		pane.setBorder(new Border(stroke));
		pane.setBackground(new Background(new BackgroundFill(derived.backgroundColor(), null, null)));
		
		Color rootColor = derived.sourceColor();

		Region base = new Region();
		base.setBackground(new Background(new BackgroundFill(rootColor, null, null)));
		base.setMaxWidth(Double.MAX_VALUE);
		base.setMinWidth(30);
		base.setMinHeight(30);
				
		StackPane derivative = new StackPane();
		Color blended = derived.blendedDerivative();
		Color textColor = getContrastingColor(blended);
				
		derivative.setBackground(new Background(new BackgroundFill(blended, null, null)));
		derivative.setMaxWidth(Double.MAX_VALUE);
		derivative.setMinWidth(60);
		derivative.setMinHeight(30);
		Label derivativeName = new Label(derived.getWebColor()+"\t("+String.format("%1$.0f",derived.getOpacity()*100)+"%)");
		derivativeName.textFillProperty().set(textColor);
		derivative.getChildren().add(derivativeName);
		
		Region transparent = new Region();
		transparent.setBackground(new Background(new BackgroundFill(derived.backgroundColor(),null,null)));
		transparent.setMinWidth(30);
		transparent.setMinHeight(30);
		transparent.setMaxWidth(60);
		
		GridPane.setHgrow(derivative, Priority.ALWAYS);
		GridPane.setHgrow(base, Priority.SOMETIMES);
		GridPane.setHgrow(transparent, Priority.NEVER);
		pane.add(base, 0, 0);
		pane.add(derivative, 1, 0);
		pane.add(transparent, 2, 0);
		setGraphic(pane);
	}

    private Color getContrastingColor(Color blended) {
        double whiteContrast = calculateContrast(Color.WHITE, blended);
        double blackContrast = calculateContrast(Color.BLACK, blended);
        if (whiteContrast > blackContrast) {
            return Color.WHITE;
        }
        return Color.BLACK;
    }
    
    private double calculateContrast(Color text, Color background) {
        return Math.abs(text.getRed()-background.getRed())
                + Math.abs(text.getBlue()-background.getBlue())
                + Math.abs(text.getGreen()-background.getGreen());
    }
}
