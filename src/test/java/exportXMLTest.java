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
    exportXML ExportXML;
    ArrayList<Airport> airports; // Imported airports
    ArrayList<Obstacle> obstacles; // Imported obstacles
    String AIRPORTS_XML_PATH = "src/main/resources/XML/newAirportsTest.xml";
    String OBSTACLES_XML_PATH = "src/main/resources/XML/newTestObstacles.xml";

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
    }


}