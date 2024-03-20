package org.universityofsouthampton.runwayredeclarationtool.airport;

import java.util.ArrayList;
import javafx.util.Pair;

/**
 * This class deals with the parallel and logical cases of runways as well as the runway's calculations
 */
public class ParallelRunways {

  protected ArrayList<Pair<Runway,Runway>> logicalRunways; // This is the list of parallel runways
  private int pointer = 0;
  private Pair<Runway,Runway> currentRunways;
  private String degree1; // LOWER DEGREE
  private String degree2; // HIGHER DEGREE
  // Pair of one runway and it's opposite direction

  public ParallelRunways () {
    logicalRunways = new ArrayList<>();
  }

  public void checkRunways (Runway runway1, Runway runway2) { // Check runway details to then add to arrayList of parallel runways
    if (logicalRunways.size() == 0) { // SET the parallel runway degrees
      degree1 = getStrDegree(Math.min(getIntDegree(runway1.getName()), getIntDegree(runway2.getName())));
      degree2 = getStrDegree(Math.max(getIntDegree(runway1.getName()), getIntDegree(runway2.getName())));
      updateLogicalRunways(runway1,runway2);
    } else {
      if (checkParallelRunway(runway1) || checkParallelRunway(runway2)) {
        if (logicalRunways.size() != 3) {
          updateLogicalRunways(runway1,runway2);
        } else {
          System.out.println("Runway set " + runway1.getName() + "/" + runway2.getName() + " is FULL!");
        }
      }
    }
  }
  public boolean checkParallelRunway(Runway runway) { // This method checks if a newly added runway is parallel to existing runways AND can be added
    // IF runway matches with any of the degrees AND the current # of parallel runways hasn't exceeded 3
    return (runway.getName().equals(degree1) || runway.getName().equals(degree2));
  }

  public void placeObstacle(Obstacle obstacle) { // ADDS the obstacle to both runway objects from their perspective thresholds
    var fstRunway = getFstRunway();
    var sndRunway = getSndRunway();
    fstRunway.addObstacle(obstacle);
    // TORA = Obstacle's disp from runway1 threshold + Obstacle's disp from runway2 threshold + runway1 displacedThreshold + runway 2 displacedThreshold + difference in TORAs
    int difference = Math.max(0, fstRunway.getTORA() - sndRunway.getTORA());

    int distFromSndRunwayThres = sndRunway.getTORA() - sndRunway.getDisplacedThreshold() - fstRunway.getDisplacedThreshold() -
        obstacle.getDistanceFromThreshold() + difference;
    System.out.println(sndRunway.getTORA() + " - " + sndRunway.getDisplacedThreshold() + " - " + fstRunway.getDisplacedThreshold() + " - " +
        obstacle.getDistanceFromThreshold() + " + " + difference +" = " + distFromSndRunwayThres);

    // Make another obstacle object but with the distances from the other side
    Obstacle obstacle2 = new Obstacle(obstacle.getName(),obstacle.getHeight(),distFromSndRunwayThres,obstacle.getDistanceFromCentreline());
    sndRunway.addObstacle(obstacle2);
  }

  public void removeObstacle(Obstacle obstacle1) {
    getFstRunway().removeObstacle(obstacle1);
    getSndRunway().removeObstacle(getSndRunway().getObstacles().get(0));
  }

  public void runCalcOnBothRunways() {
    getFstRunway().runCalculations();
    getSndRunway().runCalculations();
  }

  private void updateLogicalRunways(Runway runway1, Runway runway2) { // This will update the arrayList with consistent naming conventions (ARG: new runway pair to be added)
    Pair<Runway,Runway> pair = new Pair<>(runway1,runway2);
    switch (logicalRunways.size()) {
      case 0 -> nameDirection(pair,"_"); // Don't give any direction indicator
      case 1 -> {
        nameDirection(logicalRunways.get(0),"L"); // Establish L and R for 2 runways
        nameDirection(pair,"R");
      }
      case 2 -> nameDirection(pair,"C"); // Add the Centre runway for 3 runways
    }
    logicalRunways.add(pair);
    currentRunways = logicalRunways.get(pointer);
  }

  public void nameDirection(Pair<Runway,Runway> runwayPair, String direction) { // This gets a runway pair renames them appropriately
    switch (direction) {
      case "L" -> {
        runwayPair.getKey().setDirection(direction);
        runwayPair.getValue().setDirection("R");
      }
      case "C" -> {
        runwayPair.getKey().setDirection(direction);
        runwayPair.getValue().setDirection("C");
      }
      case "R" -> {
        runwayPair.getKey().setDirection(direction);
        runwayPair.getValue().setDirection("L");
      }
      case "_" -> {
        runwayPair.getKey().setDirection(direction);
        runwayPair.getValue().setDirection("_");
      }
    }
  }

  public int getIntDegree(String degree) {
    String parsedValue = degree;
    if (degree.matches("0[1-9]")) {
      parsedValue = String.valueOf((degree.charAt(1)));
    }
    return Integer.parseInt(parsedValue);
  }

  private String getStrDegree(int intDegree) {
    String degree = String.valueOf(intDegree);
    if (intDegree <= 9) {
      degree = "0" + intDegree;
    }
    return degree;
  }

  // Methods for UI
  public void setBPV(int BPV) {
    getFstRunway().setBlastProtectionValue(BPV);
    getSndRunway().setBlastProtectionValue(BPV);
  }

  public Runway getFstRunway() {
    return currentRunways.getKey();
  }

  public Runway getSndRunway() {
    return currentRunways.getValue();
  }

  public void nextCurrentRunway() { // Sets the currently viewed runway pair, increments pointer in the cyclic arrayList
    if (pointer == logicalRunways.size() - 1) {
      pointer = 0;
    } else {
      pointer++;
    }
    currentRunways = logicalRunways.get(pointer);
  }

  public void swapRunways() { // This swaps the runways and their perspective logical runway in the pair (UI Access method)
    var newLogicalRunways = new ArrayList<Pair<Runway,Runway>>();
    for (Pair<Runway,Runway> runwayPair : logicalRunways) {
      newLogicalRunways. add(new Pair<>(runwayPair.getValue(), runwayPair.getKey()));
    }
    logicalRunways = newLogicalRunways;
    currentRunways = logicalRunways.get(pointer);
  }

  public void setNewRunwayParameters(Runway oldParameters, Runway newParameters) {
    if (!oldParameters.equals(newParameters)) {
      oldParameters.setName(newParameters.getName());
      oldParameters.setTORA(newParameters.getTORA());
      oldParameters.setStopway(newParameters.getStopway());
      oldParameters.setClearway(newParameters.getClearway());
      oldParameters.setDisplacedThreshold(newParameters.getDisplacedThreshold());
    }
  }

  public Pair<Runway,Runway> getCurrentRunways() {
    return currentRunways;
  }

  public Runway getHigherDegreeRunway() {
    Runway runway = getFstRunway();
    if (getSndRunway().getName().equals(degree2)) {
      runway = getSndRunway();
    }
    return runway;
  }

  public Runway getLowerDegreeRunway() {
    Runway runway = getFstRunway();
    if (getSndRunway().getName().equals(degree1)) {
      runway = getSndRunway();
    }
    return runway;
  }

  public String getDegree1() {
    return degree1;
  }

  public String getDegree2() {
    return degree2;
  }

  public ArrayList<Pair<Runway,Runway>> getLogicalRunways() { // getter method for THIS set of logical runways
    return logicalRunways;
  }

}
