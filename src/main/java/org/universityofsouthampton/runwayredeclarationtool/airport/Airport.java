package org.universityofsouthampton.runwayredeclarationtool.airport;

import java.util.ArrayList;

public class Airport {
    String airportName;
    String airportCode;
    ArrayList<Runway> runways;
    public Airport(String name, String code){
        airportName = name;
        runways = new ArrayList<>();
        airportCode = code;
    }
    public void addRunway (Runway runway){
        runways.add(runway);
    }

    public ArrayList<Runway> getRunways(){
        return runways;
    }

    public String getAirportName(){ return airportName; }

    public void setAirportName(String name) {this.airportName = name;}

    public String getAirportCode() { return airportCode; }

    public void setAirportCode(String code){
        airportCode = code;
    }

}
