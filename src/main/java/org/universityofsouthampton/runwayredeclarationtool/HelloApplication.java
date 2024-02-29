package org.universityofsouthampton.runwayredeclarationtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;
import org.universityofsouthampton.runwayredeclarationtool.UI.Window;

public class HelloApplication extends Application {

    private static HelloApplication instance;
    private Stage stage;
    private Scene scene1;
    private Scene scene2;

    @Override
    public void start(Stage stage) throws Exception {
//        // Load FXML file
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/universityofsouthampton/runwayredeclarationtool/hello-view.fxml"));
//        Parent root = loader.load();
//
//        // Set the FXML-loaded content to the stage
//        Scene scene = new Scene(root, 960, 600);
//
//        // Load the stylesheet
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/style.css")).toExternalForm());
//
//        // Set the scene
//        stage.setScene(scene);
//        stage.setTitle("RunwayRedeclarationTool");
//        stage.show();

        var window = new Window(stage,1048,800);

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static HelloApplication getInstance() {
        return instance;
    }

    public void shutdown() {

        System.exit(0);
    }

}
