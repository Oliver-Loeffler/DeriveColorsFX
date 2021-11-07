package net.raumzeitfalle.fx.derivecolors;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvReader {
	public List<DerivedColor> readFrom(File file) {
		CsvBindings csvb = new CsvBindings();
		try (FileReader reader = new FileReader(file)) {
			List<DerivedColorBean> colors = csvb.fromCsv(reader);
			return colors.stream()
					     .map(DerivedColorBean::toRecord)
					     .toList();
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
        }
	}
}
