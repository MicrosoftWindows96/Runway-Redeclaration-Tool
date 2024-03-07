package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;

public class MenuScene extends VBox {

  public MenuScene(MainApplication app) {
    this.setAlignment(Pos.TOP_CENTER);

    var title = new Text("Runway Re-declaration Tool");
    title.setFont(Font.font("Arial", 24));
    title.setStyle("-fx-fill: #333;");
    VBox.setMargin(title, new Insets(10, 0, 10, 0));

    VBox buttons = new VBox(10);
    buttons.setAlignment(Pos.CENTER);

    this.setSpacing(200);

    Button login = new Button();
    styleButton(login, MaterialDesign.MDI_LOGIN, "Login");
    login.setOnAction(e -> promptLogin(app));

    Button quit = new Button();
    styleButton(quit, MaterialDesign.MDI_EXIT_TO_APP, "Quit");
    quit.setOnAction(e -> System.exit(0));

    buttons.getChildren().addAll(login, quit);

    this.getChildren().addAll(title, buttons);
  }

  private void promptLogin(MainApplication app) {
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
      if ("admin".equals(usernameInput.getText()) && "password".equals(passwordInput.getText())) {
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


  private void styleButton(Button button, MaterialDesign icon, String text) {
      AirportListScene.extractedStylingMethod(button, icon, text);
  }
}
