module net.raumzeitfalle.fx.derivecolors {
    requires java.prefs;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    
    requires jakarta.json.bind;
    requires java.desktop;
    uses jakarta.json.bind.spi.JsonbProvider;
    
    exports net.raumzeitfalle.fx.derivecolors;
    opens net.raumzeitfalle.fx.derivecolors to javafx.graphics, javafx.fxml, jakarta.json.bind;
}