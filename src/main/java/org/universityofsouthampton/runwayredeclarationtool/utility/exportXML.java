package org.universityofsouthampton.runwayredeclarationtool.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.ParallelRunways;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class deals with collecting the airport and obstacle arrayLists and converts back into an xml file.
 */
public class exportXML {

  private final ArrayList<Airport> exportedAirports;
  private final ArrayList<Obstacle> exportedObstacles;
  private final File filePath;
  private Document document;

  public exportXML (ArrayList<Airport> exportedAirports, ArrayList<Obstacle> exportedObstacles, File file) {
    filePath = file;
    this.exportedAirports = exportedAirports;
    this.exportedObstacles = exportedObstacles;

    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

      document = dBuilder.newDocument();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void buildAirportsXML () {
    try {
    Element modelsElement = document.createElement("Models");
    modelsElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    modelsElement.setAttribute("xsi:noNamespaceSchemaLocation", "newAirport.xsd");
    document.appendChild(modelsElement);

    for (Airport airport : exportedAirports) {
      Element airportElement = createAirportElement(document,airport.getAirportName(),airport.getAirportCode());

      if (!airport.getParallelRunwaySets().isEmpty()) {
        Element runwaysElement = document.createElement("Runways");
        airportElement.appendChild(runwaysElement);

        for (ParallelRunways parallelRunway : airport.getParallelRunwaySets()) {
          Element runwayElement = document.createElement("Runway");

          for (Pair<Runway,Runway> runwayPair : parallelRunway.getLogicalRunways()) {
            Element logicalRunways = document.createElement("LogicalRunways");
            var runway1 = runwayPair.getKey();
            var runway2 = runwayPair.getValue();
            createLogicalRunwayElement(document,runway1,logicalRunways,runway1.getObstacles());
            createLogicalRunwayElement(document,runway2,logicalRunways,runway2.getObstacles());
            runwayElement.appendChild(logicalRunways);
          }
          runwaysElement.appendChild(runwayElement);
        }
      }
      modelsElement.appendChild(airportElement);
      modelsElement.normalize();
    }
    extractedTransformerFactoryMethod();

    System.out.println("XML file successfully made!");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void extractedTransformerFactoryMethod() throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "4"); // 4 spaces for indentation
    DOMSource domSource = new DOMSource(document);
    StreamResult streamResult = new StreamResult(filePath);
    transformer.transform(domSource, streamResult);
  }

  public void buildObstaclesXML() {
      try {
          Element obstaclesElement = document.createElement("Obstacles");
          obstaclesElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
          obstaclesElement.setAttribute("xsi:noNamespaceSchemaLocation", "obstacle.xsd");
          document.appendChild(obstaclesElement);

          if (!exportedObstacles.isEmpty()) {
            for (Obstacle obstacle : exportedObstacles) {
              Element obstacleElement = document.createElement("Obstacle");
              obstacleElement.setAttribute("name", obstacle.getName());

              Element heightElement = document.createElement("Height");
              heightElement.setTextContent(Integer.toString(obstacle.getHeight()));
              obstacleElement.appendChild(heightElement);

              Element distThresholdElement = document.createElement("DistThreshold");
              distThresholdElement.setTextContent(Integer.toString(obstacle.getDistanceFromThreshold()));
              obstacleElement.appendChild(distThresholdElement);

              Element distCentElement = document.createElement("DistCent");
              distCentElement.setTextContent(Integer.toString(obstacle.getDistanceFromCentreline()));
              obstacleElement.appendChild(distCentElement);

              obstaclesElement.appendChild(obstacleElement);
            }
          }
          obstaclesElement.normalize();
          extractedTransformerFactoryMethod();

        System.out.println("XML file created successfully!");
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  private static Element createAirportElement(Document document, String name, String code) {
    // Create the Airport element
    Element airportElement = document.createElement("Airport");
    airportElement.setAttribute("name", name);
    airportElement.setAttribute("code", code);
    return airportElement;
  }

  private static void createLogicalRunwayElement(Document document, Runway runway,
      Element logicalRunwaysElement, ArrayList<Obstacle> obstacles) {

    // Create the LogicalRunway element
    Element logicalRunwayElement = document.createElement("LogicalRunway");
    logicalRunwayElement.setAttribute("degree", runway.getName());
    logicalRunwaysElement.appendChild(logicalRunwayElement);

    // Create the stopway element
    Element stopwayElement = document.createElement("stopway");
    stopwayElement.setTextContent(String.valueOf(runway.getStopway()));
    logicalRunwayElement.appendChild(stopwayElement);

    // Create the clearway element
    Element clearwayElement = document.createElement("clearway");
    clearwayElement.setTextContent(String.valueOf(runway.getClearway()));
    logicalRunwayElement.appendChild(clearwayElement);

    // Create the TORA element
    Element TORAElement = document.createElement("TORA");
    TORAElement.setTextContent(String.valueOf(runway.getTORA()));
    logicalRunwayElement.appendChild(TORAElement);

    // Create the dispThresh element
    Element dispThreshElement = document.createElement("dispThresh");
    dispThreshElement.setTextContent(String.valueOf(runway.getDisplacedThreshold()));
    logicalRunwayElement.appendChild(dispThreshElement);

    // Add obstacle
    if (obstacles != null && !runway.getObstacles().isEmpty()) {
      Obstacle obstacle = obstacles.getFirst();
      Element obstacleElement = document.createElement("Obstacle");
      obstacleElement.setAttribute("name", obstacle.getName());
      logicalRunwayElement.appendChild(obstacleElement);

      Element heightElement = document.createElement("Height");
      heightElement.setTextContent(String.valueOf(obstacle.getHeight()));
      obstacleElement.appendChild(heightElement);

      Element distThresholdElement = document.createElement("DistThreshold");
      distThresholdElement.setTextContent(String.valueOf(obstacle.getDistanceFromThreshold()));
      obstacleElement.appendChild(distThresholdElement);

      Element distCentElement = document.createElement("DistCent");
      distCentElement.setTextContent(String.valueOf(obstacle.getDistanceFromCentreline()));
      obstacleElement.appendChild(distCentElement);
      }
  }

  public void writeXML() {
    try {
      extractedTransformerFactoryMethod();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
