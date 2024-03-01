package org.universityofsouthampton.runwayredeclarationtool;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.universityofsouthampton.runwayredeclarationtool.UI.AirportScene;
import org.universityofsouthampton.runwayredeclarationtool.UI.AnimatedPatternBackground;
import org.universityofsouthampton.runwayredeclarationtool.UI.MenuScene;

public class MainApplication extends Application {

    private StackPane root;
    private AnimatedPatternBackground background;

    @Override
    public void start(Stage primaryStage) {
        root = new StackPane();
        background = AnimatedPatternBackground.getInstance();

        root.getChildren().add(background);

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

    public static void main(String[] args) {
        launch(args);
    }
}
