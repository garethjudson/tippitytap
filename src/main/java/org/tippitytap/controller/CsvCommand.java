package org.tippitytap.controller;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.NamedCsvRecord;
import de.siegmar.fastcsv.writer.CsvWriter;
import org.tippitytap.feature.model.Tap;
import org.tippitytap.feature.model.TapBuilder;
import org.tippitytap.feature.model.TapType;
import org.tippitytap.feature.model.Trip;
import org.tippitytap.feature.trip.TripService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// In bound entrypoint to service which takes in a csv and returns a csv
public class CsvCommand {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final String[] csvOutputHeader = new String[] {
            "Started",
            "Finished",
            "DurationSecs",
            "FromStopId",
            "ToStopId",
            "ChargeAmount",
            "CompanyId",
            "BusId",
            "PAN",
            "Status"
    };

    public final TripService tripService;

    public CsvCommand(TripService tripService) {
        this.tripService = tripService;
    }

    // In normal process I would limit the amount of exceptions I throw, and look to handle them where they are thrown
    // I would usually like to wrap exceptions in a result object which specifies when processes completed successfully vs with errors
    // this allows the caller to use the Error value for logic without having to use exception handling--which is a general smell.
    // This can have some effects depending on framework, e.g. exceptions and hibernate transactions when exceptions get thrown
    // in this case I don't have as much time as I would like to set this up. So...right now we're not handling errors well
    public void ProcessCsvFile(String csvFileIn, String csvFileOut) throws IOException {
        try (CsvReader<NamedCsvRecord> csvInput =
                     CsvReader.builder().ofNamedCsvRecord(Paths.get(csvFileIn));
             CsvWriter csvOutput = CsvWriter.builder().build(Paths.get(csvFileOut))
        ) {
            var trips = tripService.processTaps(
                    csvInput.stream().map(this::tapFromCsvRow).toList()
            );

            csvOutput.writeRecord(csvOutputHeader);

            trips.stream().sorted().map(this::csvRecordFromTrip).forEach(csvOutput::writeRecord);
        }
    }

    // if there were more code I would likely extract the following methods to separate classes
    // when you have ~100 lines, readability is not really an issue
    private Tap tapFromCsvRow(NamedCsvRecord row) {
        // ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
        // 22-01-2023 13:00:00,
        // The row names should be constants, maybe, only use here
        // might make sense to create a method which will
        var utcDateTime = parseCsvDate(row.getField("DateTimeUTC"));
        return new TapBuilder()
                .withId(row.getField("ID"))
                .withTimestamp(utcDateTime)
                .withType(TapType.parse(row.getField("TapType")))
                .withStopId(row.getField("StopId"))
                .withCompanyId(row.getField("CompanyId"))
                .withBusId(row.getField("BusId"))
                .withPan(row.getField("PAN"))
                .build();
    }


    private String[] csvRecordFromTrip(Trip trip) {
        return new String[] {
                formatInstantAsUTCDate(trip.getStarted()),
                formatInstantAsUTCDate(trip.getFinished()),
                String.format("%d", trip.getDurationSeconds()),
                trip.getFromStopId(),
                trip.getToStopId(),
                formatCents(trip.getChargeAmountCents()),
                trip.getCompanyId(),
                trip.getBusId(),
                trip.getPan(),
                trip.getStatus().toString()
        };
    }


    // These could be static utilities and extracted from here
    // I have not done this as, for the most part it will only be used here and this is just an example
    private static String formatCents(long cents) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        return currencyFormat.format(BigDecimal.valueOf(cents).movePointLeft(2));
    }

    private static Instant parseCsvDate(String date) {
        return LocalDateTime.parse(date, dateTimeFormatter)
                .toInstant(ZoneOffset.UTC);
    }

    private static String formatInstantAsUTCDate(Instant instant) {
        String formatedDate = null;
        if (instant != null) {
            formatedDate = dateTimeFormatter.format(instant.atZone(ZoneOffset.UTC));
        }
        return formatedDate;
    }
}
