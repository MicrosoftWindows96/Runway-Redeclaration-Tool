package org.universityofsouthampton.runwayredeclarationtool.airport;

import java.util.ArrayList;

public class Airport {
    private String airportName;
    private String code;
    private ArrayList<Runway> runways;
    public Airport(String name, String code){
        airportName = name;
        runways = new ArrayList<>();
        this.code = code;
    }
    public void addRunway (Runway runway){
        this.runways.add(runway);
    }

    public ArrayList<Runway> getRunways(){
        return this.runways;
    }

    public String getAirportName(){ return this.airportName; }

    public void setAirportName(String name) {this.airportName = name;}

    public String getAirportCode() { return this.code; }

    public void setAirportCode(String code){
        this.code = code;
    }

}
