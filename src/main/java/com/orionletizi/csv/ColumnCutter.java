package com.orionletizi.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class ColumnCutter {

  private final List<Integer> columns;
  private final CSVParser parser;
  private final Appendable out;
  private final CSVPrinter printer;

  public ColumnCutter(File infile, Appendable out, Integer[] cols)
      throws IOException {
    this.out = out;
    this.columns = Arrays.asList(cols);
    try {
      final FileReader reader = new FileReader(infile);
      parser = new CSVParser(reader, CSVFormat.DEFAULT);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    printer = new CSVPrinter(out, CSVFormat.DEFAULT);
  }

  public void run() {
    try {
      // iterate over every line
      for (final CSVRecord record : parser) {
        // iterate over every column in the line
        for (int i = 0; i < record.size(); i++) {
          // if the column index is not in the cut columns list, print the
          // record to the output
          if (!columns.contains(i)) {
            printer.print(record.get(i));
          }
        }
        // done writing the record. Print the line separator.
        printer.println();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
