package org.universityofsouthampton.runwayredeclarationtool;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.universityofsouthampton.runwayredeclarationtool.UI.*;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.ParallelRunways;
import org.universityofsouthampton.runwayredeclarationtool.utility.exportXML;
import org.universityofsouthampton.runwayredeclarationtool.utility.importXML;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that handles the scene changes (Controller)
 */
public class MainApplication extends Application {

    private static final String AIRPORTS_XML_PATH = "src/main/resources/XML/newAirports.xml";
    private static final String OBSTACLES_XML_PATH = "src/main/resources/XML/testObstacles.xml";
    private StackPane root; // The root node to hold the scenes
    private static AnimatedPatternBackground background;
    private ArrayList<Airport> airports; // Imported airports
    private ArrayList<Obstacle> obstacles; // Imported obstacles

    @Override
    public void start(Stage primaryStage) {
        root = new StackPane();
        background = AnimatedPatternBackground.getInstance();
        root.getChildren().add(background);

        initialiseAirports();
        displayMenu();
        playBackgroundMusic();

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Runway Redeclaration Tool");
        primaryStage.setResizable(false);

        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.jpg"))));

        primaryStage.show();
    }

    private void initialiseAirports() {
        importXML airportXML = new importXML(new File(AIRPORTS_XML_PATH));
        airports = airportXML.makeAirportsXML();
        importXML obstacleXML = new importXML(new File(OBSTACLES_XML_PATH));
        obstacles = obstacleXML.makeObstaclesXML();
    }

    public void displayMenu() {
        MenuScene menuScene = new MenuScene(this);
        root.getChildren().setAll(background, menuScene);
    }

    private void playBackgroundMusic() {
        try {
            String musicFile = Objects.requireNonNull(getClass().getResource("/music/background_music.mp3")).toURI().toString();

            Media sound = new Media(musicFile);

            MediaPlayer mediaPlayer = new MediaPlayer(sound);

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            mediaPlayer.play();
            mediaPlayer.setVolume(0.1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not play background music");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void displayObstacleListScene(Airport airport, ParallelRunways runwaySet) {
        ObstacleListScene obstacleListScene = new ObstacleListScene(this, airport, runwaySet);
        root.getChildren().setAll(background, obstacleListScene);
    }

    public void displayAirportListScene() {
        AirportListScene airportListScene = new AirportListScene(this);
        root.getChildren().setAll(background, airportListScene);
    }

    public void displayRunwayConfigScene(Airport airport,  ParallelRunways runwaySet) {
        RunwayConfigViewScene runwayConfigScene = new RunwayConfigViewScene(this,airport, runwaySet);
        root.getChildren().setAll(background, runwayConfigScene);
    }

    public void updateXMLs() { // Method to update XMl files in the resources folder
        exportXML airportXML = new exportXML(airports, obstacles, new File(AIRPORTS_XML_PATH));
        airportXML.buildAirportsXML();

        exportXML obstacleXML = new exportXML(airports, obstacles, new File(OBSTACLES_XML_PATH));
        obstacleXML.buildObstaclesXML();

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

    public void displayViewsSceneBeta(ParallelRunways runwayManager) {
        Stage primaryStage = (Stage) root.getScene().getWindow();

        if (secondaryStage == null) {
            secondaryStage = new Stage();
            secondaryStage.setResizable(false);
        }

        ViewSelectionScene viewSelectionScene = new ViewSelectionScene(this, runwayManager);
        viewSelectionScene.setSecondaryStage(secondaryStage);

        Scene scene = new Scene(viewSelectionScene, 300, 600);
        secondaryStage.setTitle("Runway View Selection");
        secondaryStage.setScene(scene);
        secondaryStage.setResizable(false);

        secondaryStage.setX(primaryStage.getX() + primaryStage.getWidth());
        secondaryStage.setY(primaryStage.getY());

        secondaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.jpg"))));

        secondaryStage.show();
    }

    public void display2DsideViewScene(ParallelRunways runwayManager) {
        if (secondaryStage == null) {
            secondaryStage = new Stage();
            secondaryStage.setTitle("2D Side View");
        }
        SideViewScene sideViewScene = new SideViewScene(this, runwayManager);
        sideViewScene.setSecondaryStage(secondaryStage);
        Scene sideViewSceneScene = new Scene(sideViewScene, 800, 600);
        secondaryStage.setScene(sideViewSceneScene);
        secondaryStage.show();
    }



    public void display2DtopDownViewScene(ParallelRunways runwayManager, boolean isRotate) {
        if (secondaryStage == null) {
            secondaryStage = new Stage();
            secondaryStage.setTitle("2D Side View");
        }

        TopDownScene topDownScene = new TopDownScene(this, runwayManager);
        topDownScene.setSecondaryStage(secondaryStage);
        Scene topDownSceneScene = new Scene(topDownScene, 800, 800);
        secondaryStage.setScene(topDownSceneScene);
        secondaryStage.show();
    }

    public void display2DbothViewScene(ParallelRunways runwayManager) {
        if (secondaryStage == null) {
            secondaryStage = new Stage();
            secondaryStage.setTitle("2D Side View");
        }

        BothViewScene bothViewScene = new BothViewScene(this, runwayManager);
        bothViewScene.setSecondaryStage(secondaryStage);
        Scene bothViewSceneScene = new Scene(bothViewScene, 800, 510);
        secondaryStage.setScene(bothViewSceneScene);
        secondaryStage.show();
    }

    public void toggleDarkMode() {
        background.toggleDarkMode();
    }

    public static boolean isDarkMode() {
        return background.isDarkMode;
    }
}