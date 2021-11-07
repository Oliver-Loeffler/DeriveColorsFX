package net.raumzeitfalle.fx.derivecolors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.scene.paint.Color;

class JsonBindingsTest {
	
	private JsonBindingsAdapter jsonBindingsAdapter = new JsonBindingsAdapter();
	
	@Test
	void that_colors_are_written_to_file(@TempDir Path tempDir) {
		Path file = tempDir.resolve("Colors.json");
		List<DerivedColor> colors = List.of(
				new DerivedColor(Color.BEIGE, Color.RED, 1.0,1.0,1.0,1.0),
				new DerivedColor(Color.GREEN, Color.BLUE, 0.5,0.2,0.9,0.8));
		
		assertDoesNotThrow(()->jsonBindingsAdapter.writeToFile(colors, file.toFile()));
		assertTrue(Files.exists(file));
	}
	
	@Test
	void that_colors_are_read_from_json(@TempDir Path tempDir) {
		Path file = tempDir.resolve("Colors.json");
		List<DerivedColor> colors = List.of(
				new DerivedColor(Color.BEIGE, Color.RED, 1.0,1.0,1.0,1.0),
				new DerivedColor(Color.GREEN, Color.BLUE, 0.5,0.2,0.9,0.8));
	
		assertDoesNotThrow(()->jsonBindingsAdapter.writeToFile(colors,file.toFile()));

		List<DerivedColor> loaded = assertDoesNotThrow(()->jsonBindingsAdapter.readFrom(file.toFile()));
		assertEquals(colors, loaded);
	}
}
