package net.raumzeitfalle.fx.derivecolors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FileNameProposalTest {

	@ParameterizedTest
	@CsvSource({
		"CSV,  MyColors.json, MyColors.csv",
		"CSV,  MyColors.csv,  MyColors.csv",
		"JSON, MyColors.json, MyColors.json",
		"JSON, MyColors.csv,  MyColors.json",
		"CSV,  MyColors.txt,  MyColors.csv",
		"JSON, MyColors.txt,  MyColors.json",
	})
	void thatFileExtensionMatchesFormat(FileType fileType, String sourceName, String expectedName) {
		String proposedName = fileType.proposeFileNameUsing(sourceName);
		assertEquals(expectedName, proposedName);
	}

}
