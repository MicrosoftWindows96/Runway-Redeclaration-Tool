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
            // Collect Airport nodes
            NodeList airportNodes = root.getElementsByTagName("Airport");

            // Loop through each "Airport" element
            for (int i = 0; i < airportNodes.getLength(); i++) {
                Element airportElement = (Element) airportNodes.item(i);

                // Get Object parameters
                String airportName = airportElement.getAttribute("name");
                String codeName = airportElement.getAttribute("code");

                // New airport object to be added to the arraylist
                Airport airport = new Airport(airportName,codeName);

                // Get the "Runway" element
                Element runwayElement = (Element) airportElement.getElementsByTagName("Runway").item(0);

                if (runwayElement != null) {

                    // Collect Logical Runway nodes
                    NodeList logicalRunwayNodes = runwayElement.getElementsByTagName("LogicalRunway");

                    for (int j = 0; j < logicalRunwayNodes.getLength(); j++ ) {
                        Element logicalRunwayElement = (Element) logicalRunwayNodes.item(j);

                        // Get Object Parameters
                        String degree = logicalRunwayElement.getAttribute("degree");
                        String direction = logicalRunwayElement.getElementsByTagName("direction").item(0).getTextContent();
                        int stopway = Integer.parseInt(logicalRunwayElement.getElementsByTagName("stopway").item(0).getTextContent());
                        int clearway = Integer.parseInt(logicalRunwayElement.getElementsByTagName("clearway").item(0).getTextContent());
                        int TORA = Integer.parseInt(logicalRunwayElement.getElementsByTagName("TORA").item(0).getTextContent());
                        int dispThresh = Integer.parseInt(logicalRunwayElement.getElementsByTagName("dispThresh").item(0).getTextContent());

                        Runway runway = new Runway(degree,direction,stopway,clearway,TORA,dispThresh);

                        // Get the "Obstacles" Element
                        Element obstaclesElement = (Element) logicalRunwayElement.getElementsByTagName("Obstacles").item(0);

                        if (obstaclesElement != null) {
                            // Collect all "Obstacle" Element is not null
                            NodeList obstacleNodes = obstaclesElement.getElementsByTagName("Obstacle");

                            for (int k = 0; k < obstacleNodes.getLength(); k ++) {
                                Element obstacleElement = (Element) obstacleNodes.item(k);

                                // Get Obstacle Object parameters
                                String obstacleName = obstacleElement.getAttribute("name");
                                int height = Integer.parseInt(logicalRunwayElement.getElementsByTagName("Height").item(0).getTextContent());
                                int distThreshold = Integer.parseInt(logicalRunwayElement.getElementsByTagName("DistThreshold").item(0).getTextContent());
                                int distCent = Integer.parseInt(logicalRunwayElement.getElementsByTagName("DistCent").item(0).getTextContent());

                                Obstacle obstacle = new Obstacle(obstacleName,height,distThreshold,distCent);
                                runway.addObstacle(obstacle);

                            }
                        }
                        airport.addRunway(runway);
                    }
                }
                // Add newly constructed airport object to arrayList
                importedAirports.add(airport);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return importedAirports;
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