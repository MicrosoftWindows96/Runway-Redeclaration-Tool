package org.universityofsouthampton.runwayredeclarationtool.utility;

import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunwayConfigLoader {

    private static final String CONFIG_FILE_PATH = "src/main/resources/runway_config.txt";

    public static List<Runway> loadRunwayConfigurations() {
        List<Runway> runways = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    int length = Integer.parseInt(parts[1].trim());
                    int width = Integer.parseInt(parts[2].trim());
                    runways.add(new Runway());
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return runways;
    }
}
