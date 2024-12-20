package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.users.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuScene extends BaseScene {

  private final MainApplication app;
  private final List<Account> accounts;

  public MenuScene(MainApplication app) {
    this.app = app;
    this.accounts = app.getAccountManager().getAccounts();

    this.setAlignment(Pos.TOP_CENTER);
    this.setSpacing(200);

    // Set the title of the screen
    var title = new Text("Runway Re-declaration Tool");
    title.setFont(Font.font("Arial", 24));
    title.setStyle("-fx-fill: #333;");
    title.setStroke(Color.WHITE);
    VBox.setMargin(title, new Insets(10, 0, 10, 0));


    HBox topButtonsBox = new HBox(10);
    topButtonsBox.setAlignment(Pos.TOP_RIGHT);

    Button helpButton = new Button();
    styleDarkButton(helpButton, MaterialDesign.MDI_HELP, "");
    helpButton.setOnAction(e -> app.displayHelpGuideScene());

    Button darkModeToggle = createDarkModeToggleButton();

    topButtonsBox.getChildren().addAll(helpButton, darkModeToggle);

    // Make the screen buttons
    VBox buttons = new VBox(10);
    buttons.setAlignment(Pos.CENTER);
    buttons.getChildren().addAll(addButtons());

    // Add nodes
    this.getChildren().addAll(title, buttons, topButtonsBox);
  }

  private void toggleDarkMode() {
    app.toggleDarkMode();
  }

  private Button createDarkModeToggleButton() {
    Button darkModeToggle = new Button();
    styleDarkButton(darkModeToggle, MainApplication.isDarkMode() ? MaterialDesign.MDI_WEATHER_NIGHT : MaterialDesign.MDI_WEATHER_SUNNY, "");
    darkModeToggle.setOnAction(e -> {
      toggleDarkMode();
      // Update the icon based on the current dark mode state
      darkModeToggle.setGraphic(FontIcon.of(MainApplication.isDarkMode() ? MaterialDesign.MDI_WEATHER_NIGHT : MaterialDesign.MDI_WEATHER_SUNNY, 20));
    });
    return darkModeToggle;
  }

  @Override
  ArrayList<Button> addButtons() {

    Button login = new Button(); // Button to open the Login prompt
    styleButton(login, MaterialDesign.MDI_KEY, "Login");
    login.setOnAction(e -> promptLogin());

    Button quit = new Button(); // Button to close the Application
    styleButton(quit, MaterialDesign.MDI_CLOSE, "Quit");
    quit.setOnAction(e -> System.exit(0));

//    VBox helpBox = new VBox(10);
//    helpBox.setAlignment(Pos.TOP_CENTER);
//    Button helpButton = new Button();
//    styleDarkButton(helpButton, MaterialDesign.MDI_HELP, "");
//    helpButton.setOnAction(e -> app.displayHelpGuideScene());
//    helpButton.setLayoutX(20);
//    helpButton.setLayoutY(20);
//    helpButton.setPrefWidth(5);
//    helpBox.getChildren().add(helpButton);



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
      String username = usernameInput.getText();
      String password = passwordInput.getText();

      boolean isAuthenticated = authenticateUser(username, password);
      if (isAuthenticated) {
        app.logOperation("logged into system.");
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

  private boolean authenticateUser(String username, String password) {
    for (Account account : accounts) {
      if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
        app.getAccountManager().setLoggedInAccount(account); // Set account in MainApplication when user is authenticated!
        return true;
      }
    }
    return false;
  }

  private void showAlert() {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Login Failed");
    alert.setHeaderText(null);
    alert.setContentText("Incorrect username or password.");
    alert.showAndWait();
  }
}
