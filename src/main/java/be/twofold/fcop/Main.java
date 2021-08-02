package be.twofold.fcop;

import org.apache.commons.cli.*;

import java.io.*;
import java.nio.file.*;

public class Main {

    private static final Options OPTIONS = new Options()
        .addOption("x", "extract", false, "Extract the contents of the specified file")
        .addOption("i", "input", true, "The source file to extract")
        .addOption("o", "output", true, "The output directory to extract to")
        .addOption("h", "help", false, "Show this help");

    public static void main(String[] args) throws IOException {
        CommandLine cmd;
        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(OPTIONS, args);
        } catch (ParseException e) {
            System.err.println("Could not parse options: " + e.getMessage());
            return;
        }

        if (cmd.hasOption('x')) {
            String i = cmd.getOptionValue('i');
            String o = cmd.getOptionValue('o');
            if (i == null || o == null) {
                System.err.println("Need input and output parameters");
                return;
            }

            FileExtractor extractor = new FileExtractor(Paths.get(i), Paths.get(o));
            extractor.extract();
            return;
        }

        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("fcop", OPTIONS);
        }
    }

}
