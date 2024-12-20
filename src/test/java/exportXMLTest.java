import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.utility.exportXML;
import org.universityofsouthampton.runwayredeclarationtool.utility.importXML;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class exportXMLTest {
    exportXML export;
    ArrayList<Airport> airports; // Imported airports
    ArrayList<Obstacle> obstacles; // Imported obstacles
    String OLD_AIRPORTS_XML_PATH = "src/main/resources/test/XML/testAirports.xml"; // airport file to import from to compare
    String OLD_OBSTACLES_XML_PATH = "src/main/resources/test/XML/testObstacles.xml"; // obstacle file to import from to compare
    String NEW_AIRPORTS_XML_PATH = "src/main/resources/test/XML/newTestAirports.xml"; // airport file to export to compare
    String NEW_OBSTACLES_XML_PATH = "src/main/resources/test/XML/newTestObstacles.xml"; // obstacle file to export to compare

    @BeforeEach
    void setUp() {
        importXML airportXML = new importXML(new File(OLD_AIRPORTS_XML_PATH));
        airports = airportXML.makeAirportsXML();

        importXML obstacleXML = new importXML(new File(OLD_OBSTACLES_XML_PATH));
        obstacles = obstacleXML.makeObstaclesXML();
    }

    @Test
    void testBuildAirportXML() {
        export = new exportXML(airports,obstacles,new File(NEW_AIRPORTS_XML_PATH));
        export.buildAirportsXML(); // Write to file

        importXML checker = new importXML(new File(NEW_AIRPORTS_XML_PATH));
        ArrayList<Airport> testAirports = checker.makeAirportsXML(); // Read from exported data

        assertEquals(airports.get(0).getAirportName(),testAirports.get(0).getAirportName()); // Check if they produce the same airport
        assertEquals(airports.get(0).getAirportCode(),testAirports.get(0).getAirportCode());
        assertEquals(airports.get(0).getParallelRunwaySets().get(0).getFstRunway().getNameDirection(),
            testAirports.get(0).getParallelRunwaySets().get(0).getFstRunway().getNameDirection());
        assertEquals(airports.get(0).getParallelRunwaySets().get(0).getFstRunway().getTORA(),
            testAirports.get(0).getParallelRunwaySets().get(0).getFstRunway().getTORA());
        assertEquals(airports.get(0).getParallelRunwaySets().get(0).getSndRunway().getNameDirection(),
            testAirports.get(0).getParallelRunwaySets().get(0).getSndRunway().getNameDirection());
        assertEquals(airports.get(0).getParallelRunwaySets().get(0).getSndRunway().getTORA(),
            testAirports.get(0).getParallelRunwaySets().get(0).getSndRunway().getTORA());
    }

    @Test
    void testBuildObstaclesXML() {
        export = new exportXML(airports,obstacles,new File(NEW_OBSTACLES_XML_PATH));
        export.buildObstaclesXML(); // Write to file

        importXML checker = new importXML(new File(NEW_OBSTACLES_XML_PATH));
        ArrayList<Obstacle> testObstacles = checker.makeObstaclesXML(); // Read from exported data

        assertEquals(obstacles.get(0).getName(),testObstacles.get(0).getName());
        assertEquals(obstacles.get(0).getHeight(),testObstacles.get(0).getHeight());
        assertEquals(obstacles.get(0).getDistanceFromThreshold(),testObstacles.get(0).getDistanceFromThreshold());
        assertEquals(obstacles.get(0).getDistanceFromCentreline(),testObstacles.get(0).getDistanceFromCentreline());
    }

}