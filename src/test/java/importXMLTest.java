import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.utility.exportXML;
import org.universityofsouthampton.runwayredeclarationtool.utility.importXML;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class importXMLTest {
    exportXML ExportXML;
    ArrayList<Airport> airports; // Imported airports
    ArrayList<Obstacle> obstacles; // Imported obstacles
    String AIRPORTS_XML_PATH = "src/main/resources/XML/newAirports.xml";
    String OBSTACLES_XML_PATH = "src/main/resources/XML/testObstacles.xml";

    @BeforeEach
    void setUp() {
        importXML airportXML = new importXML(new File(AIRPORTS_XML_PATH));
        airports = airportXML.makeAirportsXML();
        importXML obstacleXML = new importXML(new File(OBSTACLES_XML_PATH));
        obstacles = obstacleXML.makeObstaclesXML();
        ExportXML = new exportXML(airports,obstacles,new File(AIRPORTS_XML_PATH));
    }

    @Test
    void testBuildAirportXML() {
        ExportXML.buildAirportsXML();
    }

    @Test
    void testBuildObstaclesXML() {
        ExportXML.buildObstaclesXML();
    }
    @Test
    void testCreateAirportElement() {
//        ExportXML.
    }
    @Test
    void testCreateLogicalRunwayElement() {
        Obstacle obstacle = new Obstacle("Tree",32,21,21);
        assertEquals("Tree", obstacle.getName());
        assertEquals(32, obstacle.getHeight());
        assertEquals(21, obstacle.getDistanceFromThreshold());
        assertEquals(21, obstacle.getDistanceFromCentreline());
    }

    @Test
    void obstacleShouldRetainNameAfterBeingSet() {
        Obstacle obstacle = new Obstacle("Tree",32,21,21);
        obstacle.setName("New Name");
        assertEquals("New Name", obstacle.getName());
    }

    @Test
    void obstacleShouldRetainHeightAfterBeingSet() {
        Obstacle obstacle = new Obstacle("Tree",32,21,21);
        obstacle.setHeight(50);
        assertEquals(50, obstacle.getHeight());
    }

    @Test
    void obstacleShouldRetainDistanceFromThresholdAfterBeingSet() {
        Obstacle obstacle = new Obstacle("Tree",32,21,21);
        obstacle.setDistanceFromThreshold(300);
        assertEquals(300, obstacle.getDistanceFromThreshold());
    }

    @Test
    void obstacleShouldRetainDistanceFromCentrelineAfterBeingSet() {
        Obstacle obstacle = new Obstacle("Tree",32,21,21);
        obstacle.setDistanceFromCentreline(150);
        assertEquals(150, obstacle.getDistanceFromCentreline());
    }

    @Test
    void obstacleShouldHaveCorrectAttributesAfterConstruction() {
        Obstacle newObstacle = new Obstacle("Rock", 40, 200, 100);
        assertEquals("Rock", newObstacle.getName());
        assertEquals(40, newObstacle.getHeight());
        assertEquals(200, newObstacle.getDistanceFromThreshold());
        assertEquals(100, newObstacle.getDistanceFromCentreline());
    }

}