package org.universityofsouthampton.runwayredeclarationtool.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class deals with parsing the XML files to convert into arrayLists of airports
 */
public class importXML {

  private final ArrayList<Airport> importedAirports = new ArrayList<>();

  public importXML () {

  }

  // Assuming the input is valid:
  public ArrayList<Airport> convertToArrayList() {

    try {
      File file = new File("src/main/resources/XML/testAirports.XML");
      InputStream inputStream = new FileInputStream(file);

      // Create a DocumentBuilderFactory (Used to parse XML files)
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

      // Create a DocumentBuilder
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

      // Parse the XMl file and create a document object
      Document doc = dBuilder.parse(inputStream);
      doc.getDocumentElement().normalize();

      // Retrieve the root element
      Element root = doc.getDocumentElement();
      // Extract the child elements
      NodeList nodeList = root.getChildNodes();

      // Iterate over the Airport nodes
      NodeList airportList = root.getElementsByTagName("Airport");
      for (int i = 0; i < airportList.getLength(); i++) {
        // New airport object to be added to the arraylist
        Airport airport;

        Node airportNode = airportList.item(i);
        if (airportNode.getNodeType() == Node.ELEMENT_NODE) {
          // Retrieve the airport elements
          Element airportElement = (Element) airportNode;
          String airportName = airportElement.getAttribute("name");
          String code = airportElement.getAttribute("code");

          // Initialise the airport object and it's runways
          airport = new Airport(airportName,code);

          // Add the Runways
          NodeList runwayList = airportElement.getElementsByTagName("LogicalRunway");
          for (int j = 0; j < runwayList.getLength(); j++) {
            // New Runway object to be added to arrayList
            Runway runway;

            Node runwayNode = runwayList.item(j);
            if (runwayNode.getNodeType() == Node.ELEMENT_NODE) {
              Element runwayElement = (Element) runwayNode;
              String runwayName = runwayElement.getAttribute("degree");
              String direction = runwayElement.getElementsByTagName("direction").item(0).getTextContent();

              int TORA = Integer.parseInt(runwayElement.getElementsByTagName("TORA").item(0).getTextContent());
              int TODA = Integer.parseInt(runwayElement.getElementsByTagName("TODA").item(0).getTextContent());
              int ASDA = Integer.parseInt(runwayElement.getElementsByTagName("ASDA").item(0).getTextContent());
              int LDA = Integer.parseInt(runwayElement.getElementsByTagName("LDA").item(0).getTextContent());
              int dispThresh = Integer.parseInt(runwayElement.getElementsByTagName("dispThresh").item(0).getTextContent());

              runway = new Runway(runwayName,TORA,TODA,ASDA,LDA,dispThresh);
              airport.addRunway(runway);
            }
          }
          // Add newly constructed airport object to arrayList
          importedAirports.add(airport);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return importedAirports;
  }

}
