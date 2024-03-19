package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;

import java.util.ArrayList;
import java.util.Arrays;

public class MenuScene extends BaseScene {

  private MainApplication app;

  public MenuScene(MainApplication app) {
    this.app = app;
    this.setAlignment(Pos.TOP_CENTER);
    this.setSpacing(200);

    // Set the title of the screen
    var title = new Text("Runway Re-declaration Tool");
    title.setFont(Font.font("Arial", 24));
    title.setStyle("-fx-fill: #333;");
    title.setStroke(Color.WHITE);
    VBox.setMargin(title, new Insets(10, 0, 10, 0));

    VBox darkMode = new VBox(10);
    darkMode.setAlignment(Pos.TOP_RIGHT);
    Button darkModeToggle = new Button();
    styleDarkButton(darkModeToggle, MaterialDesign.MDI_WEATHER_NIGHT, "");
    darkModeToggle.setOnAction(e -> toggleDarkMode());
    darkModeToggle.setLayoutX(20);
    darkModeToggle.setLayoutY(20);
    darkMode.getChildren().add(darkModeToggle);
    this.getChildren().add(darkMode);

    // Make the screen buttons
    VBox buttons = new VBox(10);
    buttons.setAlignment(Pos.CENTER);
    buttons.getChildren().addAll(addButtons());


    // Add nodes
    this.getChildren().addAll(title, buttons);
  }

  private void toggleDarkMode() {
    app.toggleDarkMode();
  }

  @Override
  ArrayList<Button> addButtons() {

    Button login = new Button(); // Button to open the Login prompt
    styleButton(login, MaterialDesign.MDI_KEY, "Login");
    login.setOnAction(e -> promptLogin());

    Button quit = new Button(); // Button to close the Application
    styleButton(quit, MaterialDesign.MDI_CLOSE, "Quit");
    quit.setOnAction(e -> System.exit(0));

    return new ArrayList<>(Arrays.asList(login, quit));
  }

  private void promptLogin() {
    Stage loginStage = new Stage();
    loginStage.initModality(Modality.APPLICATION_MODAL);
    loginStage.setTitle("Login");

    VBox loginVBox = new VBox(10);
    loginVBox.setAlignment(Pos.CENTER);
    loginVBox.setPadding(new Insets(20));

    TextField usernameInput = new TextField();
    usernameInput.setPromptText("Username");

    PasswordField passwordInput = new PasswordField();
    passwordInput.setPromptText("Password");

    Button loginButton = new Button();
    styleButton(loginButton, MaterialDesign.MDI_KEY, "Login");
    loginButton.setOnAction(e -> {
      if ("".equals(usernameInput.getText()) && "".equals(passwordInput.getText())) {
        app.displayAirportListScene();
        loginStage.close();
      } else {
        showAlert();
      }
    });

    loginVBox.getChildren().addAll(new Label("Username:"), usernameInput, new Label("Password:"), passwordInput, loginButton);

    Scene loginScene = new Scene(loginVBox, 300, 200);
    loginStage.setScene(loginScene);
    loginStage.showAndWait();
  }

  private void showAlert() {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Login Failed");
    alert.setHeaderText(null);
    alert.setContentText("Incorrect username or password.");
    alert.showAndWait();
  }
}
