package org.tippitytap;

import org.tippitytap.controller.CsvCommand;
import org.tippitytap.feature.pricing.PricingService;
import org.tippitytap.feature.trip.TripService;

import java.io.IOException;

public class Application {
    private CsvCommand csvCommand;

    public Application() {
        // if this were a spring app obviously this would be done with dependency injection
        // just doing it here as it's nice and simple
        var pricingService = new PricingService();
        var tripService = new TripService(pricingService);
        this.csvCommand = new CsvCommand(tripService);
    }

    private static String printUsage() {
        return "This application requires two command line arguments, " +
                "the first is an input csv file location, and the second is an output csv file location";
    }

    public void run(String csvInputFile, String csvOutputFile) throws IOException {

        csvCommand.ProcessCsvFile(csvInputFile, csvOutputFile);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            // probably should use slf4j or other logging
            // just making a choice to keep this simple
            System.out.println(printUsage());
            return;
        }

        try {
            new Application().run(args[0], args[1]);
        } catch (Exception e) {
            // Catch all exceptions here and log, It's the entry/exit point of the application
            // so try to behave somewhat gracefully
            // should be replaced with more robust logging implementation
            System.err.println(
                    "There was an exception trying to run the application, " +
                            "please make sure you provide csv files in the correct format."
            );
            e.printStackTrace();
        }

    }
}
