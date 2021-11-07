package net.raumzeitfalle.fx.derivecolors;

import java.io.File;
import java.util.Collection;
import java.util.List;

public enum FileType {
	CSV(f->new CsvReader().readFrom(f),
       (c,f)->new CsvWriter().writeToFile(c, f),
       "CSV (Comma separated values)", "*.csv", 
       "MyColors.csv", ".csv"),
	
	JSON(f->new JsonBindingsAdapter().readFrom(f),
		(c,f)->new JsonBindingsAdapter().writeToFile(c, f),
		"JSON (JavaScript object notation)", "*.json",
		"MyColors.json", ".json");
	
	private final ThrowingFunction<File,List<DerivedColor>,Exception> loader;
	private final ThrowingBiConsumer<Collection<DerivedColor>, File, Throwable> writer;
	private final String decription;
	private final String extensions;
	private final String initialName;
	private final String primaryExtension;
	
	private FileType(ThrowingFunction<File,List<DerivedColor>,Exception> loadFunction,
			        ThrowingBiConsumer<Collection<DerivedColor>,File, Throwable> saveFunction,
			        String fileTypeDescription,
			        String fileExtensions,
			        String initialFileName,
			        String extension) {
		this.loader = loadFunction;
		this.writer = saveFunction;
		this.decription = fileTypeDescription;
		this.extensions = fileExtensions;
		this.initialName = initialFileName;
		this.primaryExtension = extension;
	}
	
	public List<DerivedColor> readFrom(File toBeLoaded) throws Exception {
		return loader.apply(toBeLoaded);
	}

	public void writeToFile(Collection<DerivedColor> generatedColors, File fileToSave) throws Exception {
		writer.accept(generatedColors, fileToSave);
	}

	public String proposeFileNameUsing(String fileName) {
		int lastDot = fileName.indexOf('.');
		if (lastDot > 0) {
			return fileName.substring(0, lastDot)+getExtension();
		} else {
			return fileName+getExtension();
		}
	}
	
	public String description() {
		return decription;
	}
	
	public String fileExtensions() {
		return extensions;
	}
	
	public String initialFileName() {
		return initialName;
	}

	public String getExtension() {
		return primaryExtension;
	}
}
