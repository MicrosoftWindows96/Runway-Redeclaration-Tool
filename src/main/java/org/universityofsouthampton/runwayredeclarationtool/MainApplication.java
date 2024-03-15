package org.universityofsouthampton.runwayredeclarationtool;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
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

    @Override
    public void start(Stage primaryStage) {
        root = new StackPane();
        background = AnimatedPatternBackground.getInstance();
        root.getChildren().add(background);

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

    public static void main(String[] args) {
        launch(args);
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

    public static Stage secondaryStage;

    public void displayViewsSceneBeta(Runway runway) {
        Stage primaryStage = (Stage) root.getScene().getWindow();

        if (secondaryStage == null) {
            secondaryStage = new Stage();
        }

        ViewSelectionScene viewSelectionScene = new ViewSelectionScene(this, runway);
        viewSelectionScene.setSecondaryStage(secondaryStage);

        Scene scene = new Scene(viewSelectionScene, 800, 600);
        secondaryStage.setTitle("Runway View Selection");
        secondaryStage.setScene(scene);

        secondaryStage.setX(primaryStage.getX() + primaryStage.getWidth());
        secondaryStage.setY(primaryStage.getY());

        secondaryStage.show();
    }

    public void display2DsideViewScene(Runway runway) {
        if (secondaryStage == null) {
            secondaryStage = new Stage();
            secondaryStage.setTitle("2D Side View");
        }
        SideViewScene sideViewScene = new SideViewScene(this, runway);
        sideViewScene.setSecondaryStage(secondaryStage);
        Scene sideViewSceneScene = new Scene(sideViewScene, 800, 600);
        secondaryStage.setScene(sideViewSceneScene);
        secondaryStage.show();
    }



    public void display2DtopDownViewScene(Runway runway) {
        if (secondaryStage == null) {
            secondaryStage = new Stage();
            secondaryStage.setTitle("2D Side View");
        }

        TopDownScene topDownScene = new TopDownScene(this, runway);
        topDownScene.setSecondaryStage(secondaryStage);
        Scene topDownSceneScene = new Scene(topDownScene, 800, 600);
        secondaryStage.setScene(topDownSceneScene);
        secondaryStage.show();
    }

    public void display2DbothViewScene(Runway runway) {
        if (secondaryStage == null) {
            secondaryStage = new Stage();
            secondaryStage.setTitle("2D Side View");
        }

        BothViewScene bothViewScene = new BothViewScene(this, runway);
        bothViewScene.setSecondaryStage(secondaryStage);
        Scene bothViewSceneScene = new Scene(bothViewScene, 800, 600);
        secondaryStage.setScene(bothViewSceneScene);
        secondaryStage.show();
    }
}