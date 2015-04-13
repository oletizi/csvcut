package com.orionletizi.csv;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * csvcut -- Cut columns from a CSV file. Prints modified contents to STDOUT
 *
 */
public class App {

  public static void main(String[] args) {
    final CommandLineParser parser = new GnuParser();
  
    Options options = new Options();
    options.addOption("q", "choose", false, "Engage UI for choosing columns to cut. Implies --outfile");
    options.addOption("o", "outfile", true, "Where to write the chosen columns. Implies --choose.");
    options.addOption("c", "columns", true, "Comma-separated list of column indices to cut.");
  
    final CommandLine line;
    try {
      line = parser.parse(options, args);
      
      // Check for the input file
      final String[] remaining = line.getArgs();
      
      if (remaining.length < 1) {
        printHelp(options, "Please specify an input file.");
        System.exit(0);
      }
      
      // If it doesn't exist, barf.
      File infile = new File(remaining[0]);
      if (! infile.exists()) {
        printHelp(options, "Can't find input file: " + infile.getPath());
        System.exit(0);
      }
      
      if (line.hasOption("choose")) {
        // Initiate the "choose" UI
        if (!line.hasOption("outfile")) {
          printHelp(options, "Please specify an output file.");
          System.exit(0);
        }
        // TODO: Implement me!
        throw new RuntimeException("Implement Me! -- implement the choose UI");
      } else {
        // validate the --columns option
        if (!line.hasOption("columns")) {
          printHelp(options, "Please specify the columns to cut.");
          System.exit(0);
        }
        String columnsOpt = line.getOptionValue("columns");
        String[] columnsArray = columnsOpt.split(",");
        if (columnsArray == null || columnsArray.length == 0) {
          printHelp(options, "Invalid columns option: " + columnsOpt);
          System.exit(0);
        }

        // parse the --columns option
        Integer[] columns = new Integer[columnsArray.length];
        for (int i=0; i<columnsArray.length; i++) {
          try {
            columns[i] = Integer.parseInt(columnsArray[i]);
          } catch (NumberFormatException e) {
            printHelp(options, "Invalid number format for columns option: " + columnsOpt);
            System.exit(0);
          }
        }
        
        try {
          new ColumnCutter(infile, System.out, columns).run();
        } catch (IOException e) {
          e.printStackTrace();
          System.exit(0);
        }
        
      }
    } catch (ParseException e) {
      printHelp(options, e.getLocalizedMessage());
    }
  }

  private static void printHelp(final Options options, String message) {
    System.out.println(message + "\n");
    new HelpFormatter().printHelp("java -jar csvcut.jar <options> /path/to/input/file", options); 
  }
}
