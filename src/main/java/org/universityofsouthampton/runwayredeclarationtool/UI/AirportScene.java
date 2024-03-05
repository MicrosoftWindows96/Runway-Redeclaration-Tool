package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;

import static javafx.stage.Modality.APPLICATION_MODAL;

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
      AirportListScene.extractedStylingMethod(button, icon, text);
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

    Button submitButton = new Button("Add Airport");
    styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Add");

    Button cancelButton = new Button("Cancel");
    styleButton(cancelButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
    cancelButton.setOnAction(e -> ((Stage) form.getScene().getWindow()).close());

    form.getChildren().addAll(nameLabel, nameInput, codeLabel, codeInput, submitButton, cancelButton);

    submitButton.setOnAction(e -> {
      String airportName = nameInput.getText();
      String airportCode = codeInput.getText();

      if (airportName.isEmpty() || airportCode.isEmpty()) {
        showErrorDialog();
        return;
      }

      Airport newAirport = new Airport(airportName, airportCode);
      app.addAirport(newAirport);
      app.updateAirportsXML();
      ((Stage) form.getScene().getWindow()).close();
    });


    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setTitle("Add Airport");
    stage.setScene(new Scene(form));
    stage.showAndWait();
  }

  static void extractedDialogStageMethod(VBox form, Stage dialogStage) {
    ObstacleListScene.dialogGenerator(form, dialogStage);
  }

  private void styleTextField(TextField textField) {
    textField.setStyle("-fx-background-color: white; -fx-text-fill: black;");
    textField.setFont(Font.font("Arial", 16));
  }

  private void showErrorDialog() {
    Stage dialog;
      dialog = new Stage();
      dialog.initModality(APPLICATION_MODAL);

    VBox dialogVbox = new VBox(20);

    Text errorMessage = new Text("Please fill in all fields.");
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
