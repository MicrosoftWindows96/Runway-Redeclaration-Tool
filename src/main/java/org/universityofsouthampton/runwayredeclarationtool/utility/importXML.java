package org.universityofsouthampton.runwayredeclarationtool.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class deals with parsing the XML files to convert into arrayLists of airports and obstacles
 */
public class importXML {

    private final ArrayList<Airport> importedAirports = new ArrayList<>();
    private final ArrayList<Obstacle> importedObstacles = new ArrayList<>();
    private Element root;

    public importXML (File file) {

        try {
            InputStream inputStream = new FileInputStream(file);

            // Create a DocumentBuilderFactory (Used to parse XML files)
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            // Create a DocumentBuilder
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Parse the XMl file and create a document object
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();

            // Retrieve the root element of the passed file
            root = doc.getDocumentElement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Airport> makeAirportsXML() {
        try {
            // Get all Airport elements
            NodeList airportList = root.getElementsByTagName("Airport");

            for (int i = 0; i < airportList.getLength(); i++) {
                Element airportElement = (Element) airportList.item(i);

                // Retrieve attributes of Airport element
                String airportCode = airportElement.getAttribute("code");
                String airportName = airportElement.getAttribute("name");
                System.out.println(airportName + " " + airportCode);
                Airport airport = new Airport(airportName,airportCode);

                // Get the "Runways" element
                Element runwaysElement = (Element) airportElement.getElementsByTagName("Runways").item(0);

                if (runwaysElement != null) {
                    // Get all the "Runway" elements in the "Runways" element
                    NodeList runwayList = runwaysElement.getElementsByTagName("Runway");

                    for (int j = 0; j < runwayList.getLength(); j++) {
                        Element runwayElement = (Element) runwayList.item(j);

                        // Get all the "LogicalRunways" elements in the "ParallelRunways" element
                        NodeList logicalRunwaysList = runwayElement.getElementsByTagName("LogicalRunways");

                        for (int k = 0; k < logicalRunwaysList.getLength(); k ++) {
                            Element logicalRunwaysElement = (Element) logicalRunwaysList.item(k);

                            // Get ALL the "LogicalRunway" elements in each "LogicalRunways"
                            NodeList logicalRunwayList = logicalRunwaysElement.getElementsByTagName("LogicalRunway");
                            Runway runway1 = makeRunwayObject((Element) logicalRunwayList.item(0));
                            Runway runway2 = makeRunwayObject((Element) logicalRunwayList.item(1));
                            airport.addNewRunway(runway1,runway2);
                        }
                    }
                }
                importedAirports.add(airport);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return importedAirports;
    }

    // Since there's a pair of runway objects can call this method twice!
    public Runway makeRunwayObject(Element logicalRunwayElement) {
        // Retrieve data for LogicalRunway
        String degree = logicalRunwayElement.getAttribute("degree");
        int stopway = Integer.parseInt(
            logicalRunwayElement.getElementsByTagName("stopway").item(0).getTextContent());
        int clearway = Integer.parseInt(
            logicalRunwayElement.getElementsByTagName("clearway").item(0).getTextContent());
        int tora = Integer.parseInt(
            logicalRunwayElement.getElementsByTagName("TORA").item(0).getTextContent());
        int dispThresh = Integer.parseInt(
            logicalRunwayElement.getElementsByTagName("dispThresh").item(0).getTextContent());

        // Make runway object
        Runway runway = new Runway(degree,stopway,clearway,tora,dispThresh);

        // Check for Obstacle Element
        Element obstacleElement = (Element) logicalRunwayElement.getElementsByTagName("Obstacle").item(0);
        if (obstacleElement != null) {
            String name = obstacleElement.getAttribute("name");
            int height = Integer.parseInt(
                obstacleElement.getElementsByTagName("Height").item(0).getTextContent());
            int distThreshold = Integer.parseInt(
                obstacleElement.getElementsByTagName("DistThreshold").item(0).getTextContent());
            int distCent = Integer.parseInt(
                obstacleElement.getElementsByTagName("DistCent").item(0).getTextContent());
            runway.addObstacle(new Obstacle(name,height,distThreshold,distCent));
        }
        return runway;
    }

    public ArrayList<Obstacle> makeObstaclesXML() {
        try {
            // Iterate over the Obstacle nodes
            NodeList obstacleList = root.getElementsByTagName("Obstacle");

            if (obstacleList.getLength() > 0 ) {
                for (int i = 0; i < obstacleList.getLength(); i++) {
                    // New Obstacle object to be added to the arraylist
                    Element obstacleElement = (Element) obstacleList.item(i);

                    String name = obstacleElement.getAttribute("name");
                    int height = Integer.parseInt(obstacleElement.getElementsByTagName("Height").item(0).getTextContent());
                    int disThreshold = Integer.parseInt(obstacleElement.getElementsByTagName("DistThreshold").item(0).getTextContent());
                    int distCent = Integer.parseInt(obstacleElement.getElementsByTagName("DistCent").item(0).getTextContent());

                    Obstacle obstacle = new Obstacle(name,height,disThreshold,distCent);
                    importedObstacles.add(obstacle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return importedObstacles;
    }


}