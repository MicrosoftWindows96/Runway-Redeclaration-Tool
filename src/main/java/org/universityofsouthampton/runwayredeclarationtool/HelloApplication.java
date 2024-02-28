package org.universityofsouthampton.runwayredeclarationtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/universityofsouthampton/runwayredeclarationtool/hello-view.fxml"));
        Parent root = loader.load();

        // Set the FXML-loaded content to the stage
        Scene scene = new Scene(root, 960, 600);

        // Load the stylesheet
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/style.css")).toExternalForm());

        // Set the scene
        stage.setScene(scene);
        stage.setTitle("RunwayRedeclarationTool");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
