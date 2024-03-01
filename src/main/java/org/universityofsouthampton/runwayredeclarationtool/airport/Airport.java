package org.universityofsouthampton.runwayredeclarationtool.airport;

import java.util.ArrayList;

public class Airport {
    String airportName;
    ArrayList<Runway> runways;
    public Airport(String name, Runway runway){
        this.airportName = name;
        this.runways = new ArrayList<>();
        addRunway(runway); //runway can't exist without airport, airport can't
    }
    public void addRunway (Runway runway){
        runways.add(runway);
    }

    public ArrayList<Runway> getRunways(){
        return runways;
    }
}
