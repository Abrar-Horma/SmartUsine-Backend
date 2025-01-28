package com.sari.sourceapi.controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.sari.sourceapi.model.AreaInfo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CsvProcessor {

    // Define areas with floor and name
    private static final List<AreaInfo> areas = Arrays.asList(
            new AreaInfo(1, "Main Production Floor", 1),
            new AreaInfo(2, "Quality Control Lab", 1),
            new AreaInfo(3, "Conference Room A", 2),
            new AreaInfo(4, "Executive Offices", 2),
            new AreaInfo(5, "Break Room", 1),
            new AreaInfo(6, "Maintenance Workshop", 1),
            new AreaInfo(7, "Restrooms", 1),
            new AreaInfo(8, "R&D Lab", 2)
    );

    public static void main(String[] args) {
        String inputFile = "/Users/mahmoudsalah/project/ProjectStartUp/my-app/data/ai4i2020.csv"; // Path to input CSV file
        String outputFile = "/Users/mahmoudsalah/project/ProjectStartUp/my-app/data/ai4i2020_updated_v1.csv"; // Path to output CSV file

        try (
                CSVReader reader = new CSVReader(new FileReader(inputFile));
                CSVWriter writer = new CSVWriter(new FileWriter(outputFile))
        ) {
            String[] headers = reader.readNext();
            if (headers == null) {
                throw new RuntimeException("CSV file is empty");
            }

            // Add new columns for Floor and Area (use ID for Area)
            String[] newHeaders = Arrays.copyOf(headers, headers.length + 2);
            newHeaders[newHeaders.length - 2] = "Floor";
            newHeaders[newHeaders.length - 1] = "Area";
            writer.writeNext(newHeaders);

            String[] line;
            Random random = new Random();

            // Process each row and assign random Floor and Area ID
            while ((line = reader.readNext()) != null) {
                String[] newLine = Arrays.copyOf(line, line.length + 2);
                AreaInfo randomArea = areas.get(random.nextInt(areas.size()));
                newLine[newLine.length - 2] = String.valueOf(randomArea.getFloor());
                newLine[newLine.length - 1] = String.valueOf(randomArea.getId()); // Use ID for Area
                writer.writeNext(newLine);
            }

            System.out.println("Updated CSV file has been written to " + outputFile);

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    // Helper class to represent Area information
}

