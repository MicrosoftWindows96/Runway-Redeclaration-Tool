package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginScene {

    @FXML
    public BorderPane borderPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Button actionButton;

    public void initialize() {
        FontIcon titleIcon = new FontIcon(FontAwesomeSolid.PLANE);
        titleIcon.setIconSize(24);
        titleLabel.setGraphic(titleIcon);
        titleLabel.setText(" RunwayRedeclarationTool");

        FontIcon actionIcon = new FontIcon(FontAwesomeSolid.MOUSE_POINTER);
        actionIcon.setIconSize(16);
        actionButton.setGraphic(actionIcon);
        actionButton.setText(" Click Me!");

        actionButton.setOnMouseEntered(event -> {
            actionButton.setScaleX(1.1);
            actionButton.setScaleY(1.1);
        });

        actionButton.setOnMouseExited(event -> {
            actionButton.setScaleX(1.0);
            actionButton.setScaleY(1.0);
        });

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), titleLabel);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    @FXML
    private void handleAction() {
        System.out.println("Button clicked!");
    }

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "password".equals(password)) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Login failed.");
        }
    }
}
