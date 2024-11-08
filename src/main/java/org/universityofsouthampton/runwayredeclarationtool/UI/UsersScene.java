package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.users.Account;
import org.universityofsouthampton.runwayredeclarationtool.users.AccountManager;

import java.util.ArrayList;
import java.util.Arrays;

public class
UsersScene extends BaseScene {

  private final AccountManager accountManager; // This class needs the accountManager to modify accounts
  private final ScrollPane userScroll = new ScrollPane(); // Scroll to hold user data

  public UsersScene(MainApplication app) {
    this.app = app;
    this.accountManager = app.getAccountManager();
    setSpacing(10);
    setAlignment(Pos.CENTER);

    // Set title
    Text title = new Text("User Management");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    title.setStroke(Color.WHITE);

    // Initialise user content
    updateList();

    // Set the scroll of Users
    userScroll.setFitToWidth(true);
    userScroll.setPrefHeight(500);

    // Set the main buttons
    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(addButtons());

    VBox helpBox = new VBox(10);
    helpBox.setAlignment(Pos.TOP_RIGHT);
    Button helpButton = new Button();
    styleDarkButton(helpButton, MaterialDesign.MDI_HELP, "");
    helpButton.setOnAction(e -> app.displayHelpGuideScene());
    helpButton.setLayoutX(20);
    helpButton.setLayoutY(20);
    helpButton.setPrefWidth(5);
    helpBox.getChildren().add(helpButton);




    getChildren().addAll(title,userScroll,buttonBox,helpBox);
  }

  @Override
  ArrayList<Button> addButtons() {
    Button registerButton = new Button();
    styleButton(registerButton, MaterialDesign.MDI_ACCOUNT_PLUS, "Register");
    registerButton.setOnAction(e -> promptRegistration());

    Button exportLogsButton = new Button();
    styleButton(exportLogsButton, MaterialDesign.MDI_ACCOUNT_LOCATION, "Save Logs");
    exportLogsButton.setOnAction(e -> app.getAccountLogger().exportReport());

    Button backButton = new Button();
    styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
    backButton.setOnAction(e -> {
        app.displayAirportListScene();
    });

    return new ArrayList<>(Arrays.asList(registerButton, exportLogsButton, backButton));
  }

  private HBox makeAccountBox(Account account) {
    HBox accountBox = new HBox(15);
    accountBox.setAlignment(Pos.CENTER);
    accountBox.setPadding(new Insets(5, 0, 5, 10));
    VBox accountInfo = new VBox(5);

    Text username = new Text("User: " + account.getUsername());
    username.setFont(Font.font("Arial", FontWeight.LIGHT, 15));
    Text role = new Text("Role: " + account.getRole());
    role.setFont(Font.font("Arial", FontWeight.LIGHT, 15));

    accountInfo.getChildren().addAll(username,role);

    Button editButton = new Button();
    styleButton(editButton, MaterialDesign.MDI_WRENCH, "Edit");
    editButton.setOnAction(e -> promptEditUser(account));

    Button deleteButton = new Button();
    styleButton(deleteButton, MaterialDesign.MDI_MINUS_BOX, "Delete");
    deleteButton.setOnAction(e -> {
      if (account.equals(accountManager.getLoggedInAccount())) {
        showAlert("Can't delete your OWN account");
      } else {
        accountManager.getAccounts().remove(account);
      }
      updateList();
      accountManager.saveAccountsToFile("src/main/resources/accounts.txt");
    });

    accountBox.getChildren().addAll(accountInfo, editButton, deleteButton);

    return accountBox;
  }

  private void updateList() {
    VBox users = new VBox(5);
    for (Account account : accountManager.getAccounts()) {
      users.getChildren().add(makeAccountBox(account));
    }
    userScroll.setContent(users);
  }

  private void promptRegistration() {
    Stage registrationStage = new Stage();
    registrationStage.initModality(Modality.WINDOW_MODAL);
    registrationStage.setTitle("Register");

    VBox registrationVBox = new VBox(10);
    registrationVBox.setAlignment(Pos.CENTER);
    registrationVBox.setPadding(new Insets(20));

    TextField usernameInput = new TextField();
    usernameInput.setPromptText("Username");

    PasswordField passwordInput = new PasswordField();
    passwordInput.setPromptText("Password");

    RoleCheckBoxes roleCheckBoxes = new RoleCheckBoxes();

    Button registerButton = new Button();
    styleButton(registerButton, MaterialDesign.MDI_ACCOUNT_PLUS, "Register");
    registerButton.setOnAction(e -> {
      String username = usernameInput.getText();
      String password = passwordInput.getText();

      if (username.isEmpty() || password.isEmpty()) {
        showAlert("Please enter a username and password.");
      } else if (isUsernameTaken(username)) {
        showAlert("Username is already taken.");
      } else if (roleCheckBoxes.isSelectedNull()) {
        showAlert("Please select a role.");
      } else {
        accountManager.getAccounts().add(new Account(username, password, roleCheckBoxes.getSelectedRole()));
        app.getAccountManager().saveAccountsToFile("src/main/resources/accounts.txt");
        updateList();
        app.logOperation("created a new account [" + username + "/" + roleCheckBoxes.getSelectedRole() + "] added to the system.");
        registrationStage.close();
      }
    });

    registrationVBox.getChildren().addAll(new Label("Username:"), usernameInput, new Label("Password:"), passwordInput, new Label("Role:"), roleCheckBoxes,registerButton);

    Scene registrationScene = new Scene(registrationVBox, 300, 300);
    registrationStage.setScene(registrationScene);
    registrationStage.showAndWait();
  }

  private void promptEditUser(Account account) {
    Stage registrationStage = new Stage();
    registrationStage.initModality(Modality.WINDOW_MODAL);
    registrationStage.setTitle("Edit User");

    VBox registrationVBox = new VBox(10);
    registrationVBox.setAlignment(Pos.CENTER);
    registrationVBox.setPadding(new Insets(20));

    Text username = new Text("User: " + account.getUsername());
    username.setFont(Font.font("Arial", FontWeight.BOLD, 16));

    String oldRole = account.getRole();
    RoleCheckBoxes roleCheckBoxes = new RoleCheckBoxes();
    roleCheckBoxes.setCheckBox(oldRole);

    Button confirmButton = new Button();
    styleButton(confirmButton, MaterialDesign.MDI_CHECK, "Confirm");
    confirmButton.setOnAction(e -> {
      if (roleCheckBoxes.isSelectedNull()) {
        showAlert("Please select a role.");
      } else {
        account.setRole(roleCheckBoxes.getSelectedRole());
        app.getAccountManager().saveAccountsToFile("src/main/resources/accounts.txt");
        updateList();
        if (!roleCheckBoxes.getSelectedRole().equals(oldRole)) {
          app.logOperation("edited account role of " + account.getUsername() + " " + oldRole + " -> " + roleCheckBoxes.getSelectedRole());
        }
        registrationStage.close();
      }
    });

    registrationVBox.getChildren().addAll(username, roleCheckBoxes,confirmButton);

    Scene registrationScene = new Scene(registrationVBox, 300, 200);
    registrationStage.setScene(registrationScene);
    registrationStage.showAndWait();
  }

  private boolean isUsernameTaken(String username) {
    for (Account account : accountManager.getAccounts()) {
      if (account.getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }

}
