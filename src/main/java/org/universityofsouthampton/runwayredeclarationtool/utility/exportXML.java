package org.universityofsouthampton.runwayredeclarationtool.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
      // Create a DocumentBuilderFactory (Used to parse XML files)
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

      // Create a DocumentBuilder
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

      // Create a new XMl document
      document = dBuilder.newDocument();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void buildAirportsXML () {
    try {
    Element modelsElement = document.createElement("Models");
    modelsElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    modelsElement.setAttribute("xsi:noNamespaceSchemaLocation", "airport.xsd");
    document.appendChild(modelsElement);

    // Create the Airport elements from the objects:
    for (Airport airport : exportedAirports) {
      Element airportElement = createAirportElement(document,airport.getAirportName(),airport.getAirportCode());
      // Create the Runway element
      Element runwayElement = document.createElement("Runway");
      airportElement.appendChild(runwayElement);

      for (Runway runway : airport.getRunways()) {
        createRunwayElement(document,runwayElement,runway.getName(),"R",
            String.valueOf(runway.getTORA()),
            String.valueOf(runway.getTODA()),
            String.valueOf(runway.getASDA()),
            String.valueOf(runway.getLDA()),
            String.valueOf(runway.getDisplacedThreshold()),
            runway.getObstacles());
      }
      modelsElement.appendChild(airportElement);
      modelsElement.normalize();
    }
      // Write the DOM document to an XMl file
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
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // 4 spaces for indentation
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

          obstaclesElement.normalize();

          // Write the DOM document to an XML file
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

  private static void createRunwayElement(Document document, Element runwayElement,
      String degree, String direction, String TORA,
      String TODA, String ASDA, String LDA, String dispThresh,
      List<Obstacle> obstacles) {


    // Create the LogicalRunway element
    Element logicalRunwayElement = document.createElement("LogicalRunway");
    logicalRunwayElement.setAttribute("degree", degree);
    runwayElement.appendChild(logicalRunwayElement);

    // Create the direction element
    Element directionElement = document.createElement("direction");
    directionElement.setTextContent(direction);
    logicalRunwayElement.appendChild(directionElement);

    // Create the TORA element
    Element toraElement = document.createElement("TORA");
    toraElement.setTextContent(TORA);
    logicalRunwayElement.appendChild(toraElement);

    // Create the TODA element
    Element todaElement = document.createElement("TODA");
    todaElement.setTextContent(TODA);
    logicalRunwayElement.appendChild(todaElement);

    // Create the ASDA element
    Element asdaElement = document.createElement("ASDA");
    asdaElement.setTextContent(ASDA);
    logicalRunwayElement.appendChild(asdaElement);

    // Create the LDA element
    Element ldaElement = document.createElement("LDA");
    ldaElement.setTextContent(LDA);
    logicalRunwayElement.appendChild(ldaElement);

    // Create the dispThresh element
    Element dispThreshElement = document.createElement("dispThresh");
    dispThreshElement.setTextContent(dispThresh);
    logicalRunwayElement.appendChild(dispThreshElement);

    // Add obstacles
    if (obstacles != null && !obstacles.isEmpty()) {
      Element obstaclesElement = document.createElement("Obstacles");
      logicalRunwayElement.appendChild(obstaclesElement);

      for (Obstacle obstacle : obstacles) {
        Element obstacleElement = document.createElement("Obstacle");
        obstacleElement.setAttribute("name", obstacle.getName());

        Element heightElement = document.createElement("Height");
        heightElement.setTextContent(String.valueOf(obstacle.getHeight()));
        obstacleElement.appendChild(heightElement);

        Element distThresholdElement = document.createElement("DistThreshold");
        distThresholdElement.setTextContent(String.valueOf(obstacle.getDistanceFromThreshold()));
        obstacleElement.appendChild(distThresholdElement);

        Element distCentElement = document.createElement("DistCent");
        distCentElement.setTextContent(String.valueOf(obstacle.getDistanceFromCentreline()));
        obstacleElement.appendChild(distCentElement);

        obstaclesElement.appendChild(obstacleElement);
      }
    }
  }
}
