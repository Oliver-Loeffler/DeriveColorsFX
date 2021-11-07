package net.raumzeitfalle.fx.derivecolors;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

public final class JsonBindingsAdapter {
	
	public final List<DerivedColor> readFrom(File file) {
		Jsonb jsonb = JsonbBuilder.create();
		try (FileReader reader = new FileReader(file)) {
			List<DerivedColorBean> colors = jsonb.fromJson(reader, new ArrayList<DerivedColorBean>(){
				private static final long serialVersionUID = 6344996180082726781L;}.getClass().getGenericSuperclass());
			return colors.stream()
					     .map(DerivedColorBean::toRecord)
					     .toList();
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
        }
	}
	
	public final File writeToFile(Collection<DerivedColor> colors, File file) {
		Collection<DerivedColorBean> payload = Objects.requireNonNull(colors, "Collection of colors must not be null").stream()
									                  .map(DerivedColorBean::fromRecord)
									                  .toList();
		JsonbConfig config = new JsonbConfig().withFormatting(true);
		Jsonb jsonb = JsonbBuilder.create(config);
		try (FileWriter writer = new FileWriter(file)) {
            jsonb.toJson(payload, writer);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
		return file;
	}
}
