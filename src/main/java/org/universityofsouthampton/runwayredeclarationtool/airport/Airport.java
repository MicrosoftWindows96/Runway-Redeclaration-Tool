package org.universityofsouthampton.runwayredeclarationtool.airport;

import java.util.ArrayList;

public class Airport {
    private String airportName;
    private String code;
    private final ArrayList<Runway> runways;
    public Airport(String name, String code){
        airportName = name;
        runways = new ArrayList<>();
        this.code = code;
    }

    public ArrayList<Integer> getParallelRunways() { // Method to get any parallel runways
        int numberOfRunways = runways.size();
        ArrayList<Integer> parallelRunwayIndex = new ArrayList<>();

        for (int i = 0; i < numberOfRunways; i++) {
            for (int j = i + 1; j < numberOfRunways; j++) {
                if (runways.get(i).equals(runways.get(j))) {
                    parallelRunwayIndex.add(i);
                    parallelRunwayIndex.add(j);
                }
            }
        }
        return parallelRunwayIndex;
    }

    public void addRunway (Runway runway){
        this.runways.add(runway);
    }
    public void removeRunway (Runway deleteRunway) {
        runways.remove(deleteRunway);
    }

    // setter + getter methods
    public String getAirportName(){ return this.airportName; }
    public void setAirportName(String name) {this.airportName = name;}
    public String getAirportCode() { return this.code; }
    public void setAirportCode(String code){
        this.code = code;
    }
    public ArrayList<Runway> getRunways(){
        return this.runways;
    }
}

