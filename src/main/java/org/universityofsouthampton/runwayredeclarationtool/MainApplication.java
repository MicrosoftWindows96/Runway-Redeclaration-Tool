package org.universityofsouthampton.runwayredeclarationtool;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.universityofsouthampton.runwayredeclarationtool.UI.*;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;
import org.universityofsouthampton.runwayredeclarationtool.utility.exportXML;
import org.universityofsouthampton.runwayredeclarationtool.utility.importXML;

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

        importXML airportXML = new importXML(new File("src/main/resources/XML/airports.xml"));
        airports = airportXML.makeAirportsXML();

        importXML obstacleXML = new importXML(new File("src/main/resources/XML/testObstacles.xml"));
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

    public void displayAirportScene() {
        AirportScene airportScene = new AirportScene(this);
        root.getChildren().setAll(background, airportScene);
    }

    public void displayObstacleListScene(Airport airport, Runway runway) {
        ObstacleListScene obstacleListScene = new ObstacleListScene(this, airport, runway);
        root.getChildren().setAll(background, obstacleListScene);
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
        RunwayConfigViewScene runwayConfigScene = new RunwayConfigViewScene(this, airport, runway);

        Line runwayLine = new Line(50, 150, 750, 150);
        runwayLine.setStroke(Color.GRAY);
        runwayLine.setStrokeWidth(10);

        Text toraLabel = new Text("TORA: " + runway.getTORA());
        toraLabel.setX(50);
        toraLabel.setY(130);

        Text todaLabel = new Text("TODA: " + runway.getTODA());
        todaLabel.setX(200);
        todaLabel.setY(130);

        Text asdaLabel = new Text("ASDA: " + runway.getASDA());
        asdaLabel.setX(350);
        asdaLabel.setY(130);

        Text ldaLabel = new Text("LDA: " + runway.getLDA());
        ldaLabel.setX(500);
        ldaLabel.setY(130);

        Text displacedThresholdLabel = new Text("Displaced Threshold: " + runway.getDisplacedThreshold());
        displacedThresholdLabel.setX(650);
        displacedThresholdLabel.setY(130);

        Group runwayDiagram = new Group();
        runwayDiagram.getChildren().addAll(runwayLine, toraLabel, todaLabel, asdaLabel, ldaLabel, displacedThresholdLabel);

        runwayDiagram.setLayoutY(600);


        runwayConfigScene.getChildren().add(runwayDiagram);

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

    public ArrayList<Airport> getAirports () {
        return airports;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public void updateXMLs() {
        exportXML airportXML = new exportXML(airports,obstacles,new File("src/main/resources/XML/airports.xml"));
        airportXML.buildAirportsXML();

        exportXML obstacleXML = new exportXML(airports,obstacles,new File("src/main/resources/XML/testObstacles.xml"));
        obstacleXML.buildObstaclesXML();
    }

    public void setAirports(ArrayList<Airport> airports) {
        this.airports = airports;
    }

    private static final String AIRPORTS_XML_PATH = "src/main/resources/XML/airports.xml";

    public void addAirport(Airport airport) {
        airports.add(airport);
        updateAirportsXML(); // Call this method every time the list changes
    }

    public void updateAirportsXML() {
        exportXML airportXML = new exportXML(airports, new ArrayList<>(), new File(AIRPORTS_XML_PATH));
        airportXML.buildAirportsXML();
    }

}