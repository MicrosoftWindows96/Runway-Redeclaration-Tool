package org.universityofsouthampton.runwayredeclarationtool.airport;

import java.util.ArrayList;

public class Airport {
    private String airportName;
    private String code;
    private final ArrayList<ParallelRunways> parallelRunwaySets; // The sets of parallel runways
    private final ArrayList<Runway> runways;
    public Airport(String name, String code){
        airportName = name;
        runways = new ArrayList<>();
        parallelRunwaySets = new ArrayList<>();
        this.code = code;
    }

    public void addNewRunway (Runway runway1, Runway runway2) { // Logic for adding runway to parallel runways
        if (parallelRunwaySets.size() == 0) {
            makeNewParallelRunwaySet(runway1,runway2);
        } else {
            for (ParallelRunways pRunway : parallelRunwaySets) { // Check every existing set if there's any matching conventions if not, add a new SET!
                pRunway.checkRunways(runway1,runway2);
                if (pRunway.checkParallelRunway(runway1) || pRunway.checkParallelRunway(runway2) &&
                    pRunway.getLogicalRunways().size() == 3) {
                    break; // TERMINATE ADDING RUNWAY AS PARALLEL RUNWAY SET IS FULL
                } else if (pRunway.equals(parallelRunwaySets.get(parallelRunwaySets.size() - 1)) && !pRunway.checkParallelRunway(runway1) && !pRunway.checkParallelRunway(runway2)) {
                    makeNewParallelRunwaySet(runway1,runway2);
                    break;
                }
            }
        }
    }

    public void makeNewParallelRunwaySet(Runway runway1, Runway runway2) {
        ParallelRunways newSet = new ParallelRunways(); // New set of parallel runways
        newSet.checkRunways(runway1,runway2);
        parallelRunwaySets.add(newSet);
    }

    // setter + getter methods
    public String getAirportName(){ return this.airportName; }
    public void setAirportName(String name) {this.airportName = name;}
    public String getAirportCode() { return this.code; }
    public void setAirportCode(String code){
        this.code = code;
    }
    public ArrayList<ParallelRunways> getParallelRunwaySets() {
        return this.parallelRunwaySets;
    }

}

