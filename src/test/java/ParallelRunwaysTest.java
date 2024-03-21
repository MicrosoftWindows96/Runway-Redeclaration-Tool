import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.universityofsouthampton.runwayredeclarationtool.airport.ParallelRunways;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;

import static org.junit.jupiter.api.Assertions.*;

public class ParallelRunwaysTest {

  ParallelRunways parallelRunways;

  @BeforeEach
  void setUp(){
    parallelRunways = new ParallelRunways();
    Runway runway1 = new Runway("09",0,0,3660,307);
    Runway runway2 = new Runway("27",0,0,3660,0);
    parallelRunways.checkRunways(runway1,runway2);

  }

  @Test
  void testCheckRunwaysSettingDegrees() {
    assertEquals("09",parallelRunways.getDegree1());
    assertEquals("27",parallelRunways.getDegree2()); // Check if the degrees are correctly initialised
  }

  @Test
  void testCheckRunwaysSuccessfullyAddingRunways() {
    Runway runway3 = new Runway("09",0,0,3660,307);
    Runway runway4 = new Runway("27",0,0,3660,307);
    parallelRunways.checkRunways(runway3,runway4);
    assertEquals(2,parallelRunways.getLogicalRunways().size()); // Check if both sets are added
  }

  @Test
  void testCheckParallelRunway() {
    assertTrue(parallelRunways.checkParallelRunway(new Runway("09",0,0,3660,307)));
    assertTrue(parallelRunways.checkParallelRunway(new Runway("27",0,0,3660,307)));
    assertFalse(parallelRunways.checkParallelRunway(new Runway("36",0,0,3660,307))); // check if it correctly detects mismatch in degrees
  }

  @Test
  void testPlaceObstacle() {
    Obstacle obstacle09 = new Obstacle("Rock", 25,2853,1);
    parallelRunways.setBPV(300);
    parallelRunways.placeObstacle(obstacle09);

    assertEquals(500,parallelRunways.getSndRunway().getObstacles().get(0).getDistanceFromThreshold()); // Check consistency of obstacle placement
  }

  @Test
  void testRunCalcOnBothRunways() {
    Obstacle obstacle09 = new Obstacle("Rock", 25,2853,1);
    parallelRunways.setBPV(300);
    parallelRunways.placeObstacle(obstacle09);
    parallelRunways.runCalcOnBothRunways();

    assertEquals(1850,parallelRunways.getFstRunway().getNewTORA());
    assertEquals(1850,parallelRunways.getFstRunway().getNewTODA());
    assertEquals(1850,parallelRunways.getFstRunway().getNewASDA());
    assertEquals(2553,parallelRunways.getFstRunway().getNewLDA());

    assertEquals(2860,parallelRunways.getSndRunway().getNewTORA());
    assertEquals(2860,parallelRunways.getSndRunway().getNewTODA());
    assertEquals(2860,parallelRunways.getSndRunway().getNewASDA());
    assertEquals(1850,parallelRunways.getSndRunway().getNewLDA());
  }

}
