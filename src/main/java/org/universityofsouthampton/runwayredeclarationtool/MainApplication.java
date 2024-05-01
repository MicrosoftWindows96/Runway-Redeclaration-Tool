package org.universityofsouthampton.runwayredeclarationtool;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
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
import org.universityofsouthampton.runwayredeclarationtool.UI.AirportListScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.AnimatedPatternBackground;
import org.universityofsouthampton.runwayredeclarationtool.UI.BothViewScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.FAQScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.HelpGuideScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.HowToUseScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.MenuScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.ObstacleListScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.RunwayConfigViewScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.SideViewScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.TopDownScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.UsersScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.ViewSelectionScene;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.ParallelRunways;
import org.universityofsouthampton.runwayredeclarationtool.users.AccountManager;
import org.universityofsouthampton.runwayredeclarationtool.utility.AccountLogger;
import org.universityofsouthampton.runwayredeclarationtool.utility.exportXML;
import org.universityofsouthampton.runwayredeclarationtool.utility.importXML;

/**
 * Class that handles the scene changes (Controller)
 */
public class MainApplication extends Application {

    private static final String AIRPORTS_XML_PATH = "src/main/resources/XML/newAirports.xml";
    private static final String OBSTACLES_XML_PATH = "src/main/resources/XML/newObstacles.xml";
    private StackPane root; // The root node to hold the scenes
    private static AnimatedPatternBackground background;
    private ArrayList<Airport> airports; // Imported airports
    private ArrayList<Obstacle> obstacles; // Imported obstacles
    private AccountManager accountManager; // This class holds all account information for the system
    private AccountLogger accountLogger;

    private String currentReturnScene;

    private Airport airport;
    private ParallelRunways runwaySet;
    private ParallelRunways runwayManager;

    @Override
    public void start(Stage primaryStage) {
        playBackgroundMusic();

        root = new StackPane();
        background = AnimatedPatternBackground.getInstance();
        root.getChildren().add(background);

        accountManager = new AccountManager();
        accountManager.loadAccountsFromFile("src/main/resources/accounts.txt"); // Specify the file path relative to the project's root directory
        accountLogger = new AccountLogger();

        initialiseAirports();
        displayMenu();

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
        this.currentReturnScene = "Menu";
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
        this.airport = airport;
        this.runwaySet = runwaySet;
        ObstacleListScene obstacleListScene = new ObstacleListScene(this, airport, runwaySet);
        root.getChildren().setAll(background, obstacleListScene);
        this.currentReturnScene = "displayObstacleList";
    }

    public void displayAirportListScene() {
        AirportListScene airportListScene = new AirportListScene(this);
        root.getChildren().setAll(background, airportListScene);
        this.currentReturnScene = "displayAirportList";
    }

    public void displayRunwayConfigScene(Airport airport,  ParallelRunways runwaySet) {
        this.airport = airport;
        this.runwaySet = runwaySet;
        RunwayConfigViewScene runwayConfigScene = new RunwayConfigViewScene(this,airport, runwaySet);
        root.getChildren().setAll(background, runwayConfigScene);
        this.currentReturnScene = "displayRunwayConfig";
    }

    public void displayUsersScene() {
        UsersScene usersScene = new UsersScene(this);
        root.getChildren().setAll(background, usersScene);
        this.currentReturnScene = "displayUsers";
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
        this.runwayManager = runwayManager;
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
        this.currentReturnScene = "ViewSelection";
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
        this.currentReturnScene = "SideViewSelection";
    }



    public void display2DtopDownViewScene(ParallelRunways runwayManager, boolean isRotate) {
        if (secondaryStage == null) {
            secondaryStage = new Stage();
            secondaryStage.setTitle("2D Side View");
        }

        TopDownScene topDownScene = new TopDownScene(this, runwayManager);
        topDownScene.setSecondaryStage(secondaryStage);
        Scene topDownSceneScene = new Scene(topDownScene, 800, 600);
        secondaryStage.setScene(topDownSceneScene);
        secondaryStage.show();
        this.currentReturnScene = "TopDownSelection";
    }

    public void display2DbothViewScene(ParallelRunways runwayManager) {
        if (secondaryStage == null) {
            secondaryStage = new Stage();
            secondaryStage.setTitle("2D Side View");
        }

        BothViewScene bothViewScene = new BothViewScene(this, runwayManager);
        bothViewScene.setSecondaryStage(secondaryStage);
        Scene bothViewSceneScene = new Scene(bothViewScene, 800, 600);
        secondaryStage.setScene(bothViewSceneScene);
        secondaryStage.show();
    }

    public void toggleDarkMode() {
        background.toggleDarkMode();
    }

    public static boolean isDarkMode() {
        return background.isDarkMode;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public AccountLogger getAccountLogger() {
        return accountLogger;
    }

    public void logOperation(String operation) {
        accountLogger.writeLog(accountManager.getLoggedInAccount(), operation);
    }

    public void displayHelpGuideScene() {
        HelpGuideScene helpGuideScene = new HelpGuideScene(this);
        root.getChildren().setAll(background, helpGuideScene);

    }

    public void returnFromHelpScene(){
        switch (currentReturnScene) {
            case "displayObstacleList":
                displayObstacleListScene(this.airport, this.runwaySet);
                break;
            case "displayAirportList":
                displayAirportListScene();
                // Handle the case when the current return scene is for displaying airport list
                break;
            case "displayRunwayConfig":
                // Handle the case when the current return scene is for displaying runway configuration
                displayRunwayConfigScene(this.airport,this.runwaySet);
                break;
            case "displayUsers":
                displayUsersScene();
                // Handle the case when the current return scene is for displaying users list
                break;
            case "ViewSelection":
                displayViewsSceneBeta(this.runwayManager);
                // Handle the case when the current return scene is for view selection
                break;
            case "Menu":
                displayMenu();

                break;
            default:
                // Handle any other cases or provide a default behavior
                break;
        }

    }

    public void displayHowToUseScene() {
        HowToUseScene howToUseScene = new HowToUseScene(this);
        root.getChildren().setAll(background, howToUseScene);
    }

    public void displayFAQScene() {
        FAQScene FAQscene = new FAQScene(this);
        root.getChildren().setAll(background, FAQscene);
    }
}