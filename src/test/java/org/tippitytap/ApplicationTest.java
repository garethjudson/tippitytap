package org.tippitytap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.NamedCsvRecord;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

// Given this is a really small code base
// doing a set of black box tests that exercise the actual entry point with files seems the easiest
// we don't have to consider mocking/stubbing or dependencies. Just keep it simple.
class ApplicationTest {

  private final Application application = new Application();

  @ParameterizedTest
  @ValueSource(strings = {"provided_sample_data", "pricing_test_coverage"})
  void testCsvInput(String input) throws Exception {
    var inputFile =
        Path.of("src", "test", "resources", "csvInput", input + ".csv").toAbsolutePath().toString();
    var expectedFile =
        Path.of("src", "test", "resources", "csvInput", input + "-expected.csv").toAbsolutePath();
    var outputFile = Files.createTempFile(input, "-output.csv").toAbsolutePath();
    outputFile.toFile().deleteOnExit();

    application.run(inputFile, outputFile.toString());

    // A little verbose, different options for making this better -- I just did the quick and dirty
    // I could have just read the csv files to strings and compared the entire contents
    // Just wanted to make it obvious which fields were not matching by putting a clear set of
    // assertions with an appropriate message
    // I don't like how this looks. But I don't think there's value in refactoring if it works.
    try (
            CsvReader<NamedCsvRecord> actual = CsvReader.builder().ofNamedCsvRecord(outputFile);
            CsvReader<NamedCsvRecord> expected = CsvReader.builder().ofNamedCsvRecord(expectedFile)
    ) {
      var actualList = actual.stream().toList();
      var expectedList = expected.stream().toList();

      assertEquals(expectedList.size(), actualList.size());
      for (int i = 0; i < actualList.size(); i++) {
        var actualRow = actualList.get(i);
        var expectedRow = expectedList.get(i);

        var actualFields = actualRow.getFields();
        var expectedFields = expectedRow.getFields();

        assertEquals(expectedFields.size(), actualFields.size());
        for (int j = 0; j < actualFields.size(); j++) {
          var actualField = actualRow.getField(j);
          var expectedField = expectedRow.getField(j);
          assertEquals(
              expectedField,
              actualField,
              String.format(
                  "CSV test input %s does not produce output matching expectations. Row %d field %s, value %s not match expectation %s ",
                  inputFile, i, j, actualField, expectedField));
        }
      }
    }
  }
}
