package net.raumzeitfalle.fx.derivecolors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

public class CsvWriter {
	public File writeToFile(Collection<DerivedColor> colors, File file) {
		Collection<DerivedColorBean> payload = Objects.requireNonNull(colors, "Collection of colors must not be null").stream()
									                  .map(DerivedColorBean::fromRecord)
									                  .toList();
		CsvBindings csvb = new CsvBindings();
		try (FileWriter writer = new FileWriter(file)) {
			csvb.toCsv(payload, writer);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
		return file;
	}
}
