package org.universityofsouthampton.runwayredeclarationtool;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.kordamp.ikonli.javafx.IkonResolver;
import org.kordamp.ikonli.materialdesign.MaterialDesignIkonHandler;
import org.universityofsouthampton.runwayredeclarationtool.UI.*;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;
import org.universityofsouthampton.runwayredeclarationtool.utility.exportXML;
import org.universityofsouthampton.runwayredeclarationtool.utility.importXML;

import java.io.File;
import java.util.ArrayList;

/**
 * Class that handles the scene changes (Controller)
 */
public class MainApplication extends Application {

    private static final String AIRPORTS_XML_PATH = "src/main/resources/XML/newAirports.xml";
    private static final String OBSTACLES_XML_PATH = "src/main/resources/XML/testObstacles.xml";
    private StackPane root; // The root node to hold the scenes
    private AnimatedPatternBackground background;
    private ArrayList<Airport> airports; // Imported airports
    private ArrayList<Obstacle> obstacles; // Imported obstacles

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Registering Material Design icon pack with the IkonResolver
//        IkonResolver.getInstance().registerHandler(new MaterialDesignIkonHandler());

        // set the root node
        root = new StackPane();
        background = AnimatedPatternBackground.getInstance();
        root.getChildren().add(background);

        // Import the default XML files
        importXML airportXML = new importXML(new File(AIRPORTS_XML_PATH));
        airports = airportXML.makeAirportsXML();
        importXML obstacleXML = new importXML(new File(OBSTACLES_XML_PATH));
        obstacles = obstacleXML.makeObstaclesXML();

        displayMenu();

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Runway Redeclaration Tool");
        primaryStage.show();
    }

    public void displayMenu() {
        MenuScene menuScene = new MenuScene(this);
        root.getChildren().setAll(background, menuScene);
    }

    public void displayObstacleListScene(Airport airport, Runway runway) {
        ObstacleListScene obstacleListScene = new ObstacleListScene(this, airport, runway);
        root.getChildren().setAll(background, obstacleListScene);
    }

    public void displayAirportListScene() {
        AirportListScene airportListScene = new AirportListScene(this);
        root.getChildren().setAll(background, airportListScene);
    }

    public void displayRunwayConfigScene(Airport airport, Runway runway) {
        RunwayConfigViewScene runwayConfigScene = new RunwayConfigViewScene(this,airport, runway);
        root.getChildren().setAll(background, runwayConfigScene);
    }

    public void updateXMLs() { // Method to update XMl files in the resources folder
        exportXML airportXML = new exportXML(airports, obstacles, new File(AIRPORTS_XML_PATH));
        airportXML.buildAirportsXML();

        exportXML obstacleXML = new exportXML(airports, obstacles, new File(OBSTACLES_XML_PATH));
        obstacleXML.buildObstaclesXML();

        showNotification("System", "Airports and obstacles updated");
        System.out.println("XML files successfully updated!");
    }

    public void showNotification(String text, String text2) {
        Notifications.create()
                .title(text)
                .text(text2)
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT)
                .owner(null)
                .darkStyle()
                .showInformation();
    }

    public void mergeAirport(ArrayList<Airport> newAirports) { // Adds to arraylist from user's imported file
        airports.addAll(newAirports);
        showNotification("System", "Airports merged");
        updateXMLs();
    }

    public void addAirport(Airport airport) {
        airports.add(airport);
        showNotification("System", "Airport added");
        updateXMLs();
    }

    public ArrayList<Airport> getAirports() {
        return airports;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }
}