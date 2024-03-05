package org.universityofsouthampton.runwayredeclarationtool.utility;

import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunwayConfigLoader {
    public static final String CONFIG_FILE_PATH = "src/main/resources/runway_config.txt";


    public static List<Runway> loadRunwayConfigurations() {
        List<Runway> runways = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 7) {
                    String degrees = parts[0].trim();
                    String direction = parts[1].trim();
                    int TORA = Integer.parseInt(parts[2].trim());
                    int TODA = Integer.parseInt(parts[3].trim());
                    int ASDA = Integer.parseInt(parts[4].trim());
                    int LDA = Integer.parseInt(parts[5].trim());
                    int displacedThreshold = Integer.parseInt(parts[6].trim());
                    runways.add(new Runway(degrees, direction, TORA, TODA, ASDA, LDA, displacedThreshold));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return runways;
    }
}
