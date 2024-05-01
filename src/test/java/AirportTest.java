import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

public class AirportTest {

  Airport airport;

  @BeforeEach
  void setUp(){
    airport = new Airport("Heathrow","LHR");

  }

  @Test
  void testGetAirportNames() {
    assertEquals("Heathrow",airport.getAirportName());
    assertEquals("LHR",airport.getAirportCode());
  }

  @Test
  void testSetAirportNames() {
    airport.setAirportName("Anthony");
    airport.setAirportCode("ANT");

    assertEquals("Anthony",airport.getAirportName());
    assertEquals("ANT",airport.getAirportCode());
  }

  @Test
  void testAddNewRunway() { // Check if runways get successfully added to the airport
    Runway runway1 = new Runway("09",0,0,3660,307);
    Runway runway2 = new Runway("27",0,0,3660,0);
    airport.addNewRunway(runway1,runway2);

    assertEquals(runway1,airport.getParallelRunwaySets().get(0).getFstRunway());
    assertEquals(runway2,airport.getParallelRunwaySets().get(0).getSndRunway());
  }

  @Test
  void testMakeNewParallelRunwaySet() { // Check if runways get successfully added to the airport
    Runway runway1 = new Runway("09",0,0,3660,307);
    Runway runway2 = new Runway("27",0,0,3660,0);
    Runway runway3 = new Runway("09",0,0,3660,307);
    Runway runway4 = new Runway("27",0,0,3660,0);
    Runway runway5 = new Runway("09",0,0,3660,307);
    Runway runway6 = new Runway("27",0,0,3660,0);
    airport.addNewRunway(runway1,runway2);
    airport.addNewRunway(runway3,runway4);
    airport.addNewRunway(runway5,runway6);

    Runway runway7 = new Runway("08",0,0,3660,307);
    Runway runway8 = new Runway("26",0,0,3660,0);
    airport.addNewRunway(runway7,runway8);

    assertEquals(3,airport.getParallelRunwaySets().get(0).getLogicalRunways().size()); // Check if max parallel runways are capped at 3
    assertEquals(2,airport.getParallelRunwaySets().size()); // The other parallel runway set is in a separate set

  }

}
