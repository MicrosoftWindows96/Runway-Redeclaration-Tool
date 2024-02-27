package org.universityofsouthampton.runwayredeclarationtool;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class HelloController {

    public BorderPane borderPane;
    @FXML
    private Label titleLabel;

    @FXML
    private Button actionButton;

    public void initialize() {
        // Set initial title
        titleLabel.setText("RunwayRedeclarationTool");

        // Add a hover effect to the button
        actionButton.setOnMouseEntered(event -> {
            actionButton.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
        });

        actionButton.setOnMouseExited(event -> {
            actionButton.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        });

        // Add fade transition for a smooth entry animation
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), titleLabel);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    @FXML
    private void handleAction() {
        // Add custom action for the button click
        System.out.println("Button clicked!");
    }
}
