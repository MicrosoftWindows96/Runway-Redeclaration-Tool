package org.universityofsouthampton.runwayredeclarationtool;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public class HelloController {

    @FXML
    public BorderPane borderPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Button actionButton;

    public void initialize() {
        // Initial title
        FontIcon titleIcon = new FontIcon(FontAwesomeSolid.PLANE);
        titleIcon.setIconSize(24); // Adjust icon size as needed
        titleLabel.setGraphic(titleIcon);
        titleLabel.setText(" RunwayRedeclarationTool"); // Add a space for separation

        // Button w/ icon
        FontIcon actionIcon = new FontIcon(FontAwesomeSolid.MOUSE_POINTER);
        actionIcon.setIconSize(16); // Adjust icon size as needed
        actionButton.setGraphic(actionIcon);
        actionButton.setText(" Click Me!");

        // Hover effect
        actionButton.setOnMouseEntered(event -> {
            actionButton.setScaleX(1.1);
            actionButton.setScaleY(1.1);
        });

        actionButton.setOnMouseExited(event -> {
            actionButton.setScaleX(1.0);
            actionButton.setScaleY(1.0);
        });

        // Fade transition for smooth entry animation
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), titleLabel);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    @FXML
    private void handleAction() {
        // Custom action for the button click
        System.out.println("Button clicked!");
    }
}
