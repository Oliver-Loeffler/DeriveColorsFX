package net.raumzeitfalle.fx.derivecolors;

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
import javafx.scene.paint.Color;

public class DerivedColorHistoryListCell extends ListCell<DerivedColor> {
	
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
		Color derivedColor = derived.derivative(); 
		
		Region base = new Region();
		base.setBackground(new Background(new BackgroundFill(rootColor, null, null)));
		base.setMaxWidth(20);
		base.setMinWidth(10);
		base.setMinHeight(30);
		
		Region derivative = new Region();
		derivative.setBackground(new Background(new BackgroundFill(derivedColor, null, null)));
		derivative.setMaxWidth(Double.MAX_VALUE);
		derivative.setMinWidth(10);
		derivative.setMinHeight(30);
		
		Region transparent = new Region();
		transparent.setBackground(new Background(new BackgroundFill(derived.backgroundColor(),null,null)));
		transparent.setMinWidth(10);
		transparent.setMinHeight(30);
		transparent.setMaxWidth(10);
		
		GridPane.setHgrow(derivative, Priority.ALWAYS);
		GridPane.setHgrow(base, Priority.SOMETIMES);
		GridPane.setHgrow(transparent, Priority.NEVER);
		pane.add(base, 0, 0);
		pane.add(derivative, 1, 0);
		pane.add(transparent, 2, 0);
		setGraphic(pane);
	}
}
