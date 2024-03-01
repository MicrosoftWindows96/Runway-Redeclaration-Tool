package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import javafx.stage.Stage;
import javafx.stage.Screen;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;

public class AirportScene extends VBox {

  public AirportScene (MainApplication app) {
    setAlignment(Pos.TOP_CENTER);

    var title = new Text("Airports Database");
    title.setFont(Font.font("Arial", 24));
    title.setStyle("-fx-fill: #333;");
    VBox.setMargin(title, new Insets(10, 0, 20, 0));

    VBox buttons = new VBox(10);
    buttons.setAlignment(Pos.CENTER);
    this.setSpacing(200);

    Button addAirport = new Button();
    styleButton(addAirport, MaterialDesign.MDI_PLUS_BOX, "Add");
    addAirport.setOnAction(e -> promptAddAirportForm(app));

    Button selectAirport = new Button();
    styleButton(selectAirport, MaterialDesign.MDI_VIEW_LIST, "List");
    selectAirport.setOnAction(e -> app.displayAirportListScene());

    Button back = new Button();
    styleButton(back, MaterialDesign.MDI_ARROW_LEFT, "Return");
    back.setOnAction(e -> app.displayMenu());

    buttons.getChildren().addAll(addAirport, selectAirport, back);

    this.getChildren().addAll(title, buttons);
  }

  private void styleButton(Button button, MaterialDesign icon, String text) {
    button.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
    button.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
    button.setPrefWidth(120);
    button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;"));
    button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #333; -fx-text-fill: white;"));

    FontIcon buttonIcon = new FontIcon(icon);
    buttonIcon.setIconColor(Color.WHITE);
    button.setGraphic(buttonIcon);
    button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // Only display the icon

    Label label = new Label(text);
    label.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
    label.setTextFill(Color.WHITE);
    label.setAlignment(Pos.CENTER);

    HBox hbox = new HBox(buttonIcon, label);
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.setSpacing(10);

    button.setGraphic(hbox);
  }

  private void promptAddAirportForm(MainApplication app) {
    VBox form = new VBox(10);
    form.setAlignment(Pos.CENTER);
    form.setPadding(new Insets(20));

    Label nameLabel = new Label("Airport Name:");
    TextField nameInput = new TextField();
    styleTextField(nameInput);

    Label codeLabel = new Label("Airport Code:");
    TextField codeInput = new TextField();
    styleTextField(codeInput);

    Label runwayLabel = new Label("Number of Runways:");
    TextField runwayInput = new TextField();
    styleTextField(runwayInput);

    Button submitButton = new Button();
    styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Add");
    // Add airport

    Button cancelButton = new Button();
    styleButton(cancelButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
    cancelButton.setOnAction(e -> {
      Stage stage = (Stage) form.getScene().getWindow();
      stage.close();
    });

    submitButton.setOnAction(e -> {
      String airportName = nameInput.getText();
      String airportCode = codeInput.getText();
      String runwayText = runwayInput.getText();

      // Validate
      if (airportName.isEmpty() || airportCode.isEmpty() || runwayText.isEmpty()) {
        showErrorDialog("All fields are required. Please fill in all fields.");
      } else {
        try {
          int runways = Integer.parseInt(runwayText);
          if (runways <= 0) {
            throw new IllegalArgumentException("Number of runways must be a positive integer.");
          }
          // Data valid, add airport
          // For now, just close the form
          Stage stage = (Stage) form.getScene().getWindow();
          app.addAirport(new Airport(airportName,airportCode)); // add the Airport into the list
          stage.close();

        } catch (NumberFormatException ex) {
          showErrorDialog("Invalid input for number of runways. Please enter a valid integer.");
        } catch (IllegalArgumentException ex) {
          showErrorDialog(ex.getMessage());
        }
      }
    });

    form.getChildren().addAll(nameLabel, nameInput, codeLabel, codeInput, runwayLabel, runwayInput, submitButton, cancelButton);

    Stage dialogStage = new Stage();
    dialogStage.initModality(Modality.APPLICATION_MODAL);
    dialogStage.setTitle("Add Airport");
    dialogStage.setScene(new Scene(form));

    double centerX = Screen.getPrimary().getVisualBounds().getWidth() / 2;
    double centerY = Screen.getPrimary().getVisualBounds().getHeight() / 2;
    dialogStage.setX(centerX - 150);
    dialogStage.setY(centerY - 100);
    dialogStage.showAndWait();
  }

  private void styleTextField(TextField textField) {
    textField.setStyle("-fx-background-color: white; -fx-text-fill: black;");
    textField.setFont(Font.font("Arial", 16));
  }

  private void showErrorDialog(String message) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);

    VBox dialogVbox = new VBox(20);

    Text errorMessage = new Text(message);
    Button okButton = new Button();
    styleButton(okButton, MaterialDesign.MDI_CHECK, "OK");

    okButton.setOnAction(e -> dialog.close());
    dialogVbox.setPadding(new Insets(20));

    dialogVbox.getChildren().addAll(errorMessage, okButton);
    Scene dialogScene = new Scene(dialogVbox);
    dialog.setScene(dialogScene);
    dialog.sizeToScene();
    dialog.centerOnScreen();
    dialogVbox.setAlignment(Pos.CENTER);

    dialog.showAndWait();
  }


}
