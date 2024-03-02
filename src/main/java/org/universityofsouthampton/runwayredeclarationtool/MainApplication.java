package org.universityofsouthampton.runwayredeclarationtool;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.universityofsouthampton.runwayredeclarationtool.UI.*;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

/**
 * Class that handles the scene changes (Controller)
 */
public class MainApplication extends Application {

    private StackPane root;
    private AnimatedPatternBackground background;
    private ArrayList<Airport> airports;
    private ArrayList<Obstacle> obstacles;

    @Override
    public void start(Stage primaryStage) {
        root = new StackPane();
        background = AnimatedPatternBackground.getInstance();

        root.getChildren().add(background);

        initialiseAirportsXML();
//        initialiseObstaclesXML();

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

    public void displayAirportScene() {
        AirportScene airportScene = new AirportScene(this);
        root.getChildren().setAll(background, airportScene);
    }

    public void displayObstacleScene(Airport airport, Runway runway) {
        ObstacleUpdateScene obstacleScene = new ObstacleUpdateScene(this, airport, runway);
        root.getChildren().setAll(background, obstacleScene);
    }

    public void displayAirportListScene() {
        AirportListScene airportListScene = new AirportListScene(this);
        root.getChildren().setAll(background, airportListScene);
    }

    public void displayRunwayListScene(Airport airport) { // Airport object needed to be passed to scene
        RunwayListScene runwayListScene = new RunwayListScene(this, airport);
        root.getChildren().setAll(background, runwayListScene);
    }

    public void displayRunwayConfigScene(Airport airport, Runway runway) {
        RunwayConfigViewScene runwayConfigScene = new RunwayConfigViewScene(this,airport,runway);
        root.getChildren().setAll(background, runwayConfigScene);
    }

    /**
     * This method collects the created / creates the airport objects from the XML file
     * (Objects are manually made for now!)
     */
    private void initialiseAirportsXML () {
        airports = new ArrayList<>();

        Runway runway1LHR = new Runway("09",3902,3902,3902,3595,306);
        Airport heathrow = new Airport("Heathrow", "LHR");
        heathrow.addRunway(runway1LHR);
        Obstacle obstacle1 = new Obstacle("Obstacle1",25,306,241);
        Obstacle obstacle2 = new Obstacle("Obstacle2",40,320,250);
        runway1LHR.addObstacle(obstacle1);
        runway1LHR.addObstacle(obstacle2);

        Runway runway1SOU = new Runway("09",3902,3902,3902,3595,306);
        Runway runway2SOU = new Runway("27",3884,3962,3884,4884,0);
        Airport southampton = new Airport("Southampton", "SOU");
        southampton.addRunway(runway1SOU);
        southampton.addRunway(runway2SOU);

        airports.add(heathrow);
        airports.add(southampton);

    }

//    private void initialiseObstaclesXML () {
//        obstacles = new ArrayList<>();
//
//        obstacles.add(obstacle1);
//        obstacles.add(obstacle2);
//
//
//    }

    public void addAirport(Airport airport) {
        airports.add(airport);
    }

    public ArrayList<Airport> getAirports () {
        return airports;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
