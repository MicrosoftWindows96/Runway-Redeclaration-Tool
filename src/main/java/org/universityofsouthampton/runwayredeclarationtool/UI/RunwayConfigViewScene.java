package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

public class RunwayConfigViewScene extends VBox {

  /**
   * Runway currently selected
   */
  private Runway currentRunway;

  /**
   * Airport the runway is in
   */
  private final Airport airport;

  public RunwayConfigViewScene (MainApplication app, Airport airport, Runway runway) {
    this.airport = airport;
    currentRunway = runway;


    setPadding(new Insets(20));
    setSpacing(10);

    Text title = new Text("Runway: " + airport.getAirportCode() + " -- " + runway.getName());
    title.setFont(Font.font("Arial", 20));

    setUpParameters();

    Button obstacleUpdateButton = new Button();
    styleButton(obstacleUpdateButton, "Obstacle");
    obstacleUpdateButton.setOnAction(e -> app.displayObstacleListScene(airport,runway));

    Button runwayUpdateButton = new Button();
    styleButton(runwayUpdateButton,"Configure");
    runwayUpdateButton.setOnAction(e -> promptEditRunwayForm(app));

    Button backButton = new Button();
    styleButton(backButton, "Return");
    backButton.setOnAction(e -> app.displayRunwayListScene(airport));

    HBox buttonBox = new HBox(10);
    buttonBox.getChildren().addAll(backButton,runwayUpdateButton,obstacleUpdateButton);


    this.getChildren().addAll(title,setUpParameters(),buttonBox);

    if (!currentRunway.getObstacles().isEmpty()) {
      System.out.println("Obstacles present on runway!");
      this.getChildren().add(calculatedParameterSection());
    }

  }

  private VBox setUpParameters() {
    VBox parameterBox = new VBox();
    parameterBox.setSpacing(5);

    var TORA = makeDistanceHBox("TORA ", currentRunway.getTORA());
    var TODA = makeDistanceHBox("TODA ", currentRunway.getTODA());
    var ASDA = makeDistanceHBox("ASDA ", currentRunway.getASDA());
    var LDA = makeDistanceHBox("LDA ", currentRunway.getLDA());
    var disThres = makeDistanceHBox("Displaced Threshold: ",currentRunway.getDisplacedThreshold());
    parameterBox.getChildren().addAll(TORA,TODA,ASDA,LDA,disThres);

    return parameterBox;
  }

  private VBox calculatedParameterSection() {
    VBox calculatedBox = new VBox(); // New VBox section to hold calculated results!
    HBox breakdownBox = new HBox(); // Box to hold calculation steps!


    HBox comparison = new HBox();
    comparison.setSpacing(5);

    VBox parameterBox = new VBox();
    parameterBox.setSpacing(5);
    var TORA = makeDistanceHBox("TORA ", currentRunway.getNewTORA());
    var TODA = makeDistanceHBox("TODA ", currentRunway.getNewTODA());
    var ASDA = makeDistanceHBox("ASDA ", currentRunway.getNewASDA());
    var LDA = makeDistanceHBox("LDA ", currentRunway.getNewLDA());
    var disThres = makeDistanceHBox("Displaced Threshold: ",currentRunway.getDisplacedThreshold());
    parameterBox.getChildren().addAll(TORA,TODA,ASDA,LDA,disThres);

    comparison.getChildren().addAll(setUpParameters(),parameterBox); // Puts the results side by side

    calculatedBox.getChildren().addAll(comparison);

    return calculatedBox;
  }


  private void promptEditRunwayForm(MainApplication app) {
    VBox form = new VBox(10);
    form.setAlignment(Pos.CENTER);
    form.setPadding(new Insets(20));

    Label degreeLabel = new Label("Degree:");
    TextField degreeInput = new TextField(currentRunway.getDegrees());
    styleTextField(degreeInput);
    degreeInput.setPromptText("Degree");

    Label directionLabel = new Label("Direction (L, R, C):");
    TextField directionInput = new TextField(currentRunway.getDirection());
    styleTextField(directionInput);
    directionInput.setPromptText("Direction (L, R, C)");

    Label TORALabel = new Label("TORA:");
    TextField TORAInput = new TextField(Integer.toString(currentRunway.getTORA()));
    styleTextField(TORAInput);

    Label TODALabel = new Label("TODA:");
    TextField TODAInput = new TextField(Integer.toString(currentRunway.getTODA()));
    styleTextField(TODAInput);

    Label ASDALabel = new Label("ASDA:");
    TextField ASDAInput = new TextField(Integer.toString(currentRunway.getASDA()));
    styleTextField(ASDAInput);

    Label LDALabel = new Label("LDA:");
    TextField LDAInput = new TextField(Integer.toString(currentRunway.getLDA()));
    styleTextField(LDAInput);

    Label DisThreshLabel = new Label("Displaced Threshold:");
    TextField DisThreshInput = new TextField(Integer.toString(currentRunway.getDisplacedThreshold()));
    styleTextField(DisThreshInput);

    Button submitButton = new Button();
    styleButton(submitButton, "Replace");

    Button cancelButton = new Button();
    styleButton(cancelButton, "Return");
    cancelButton.setOnAction(e -> {
      Stage stage = (Stage) form.getScene().getWindow();
      stage.close();
    });

    submitButton.setOnAction(e -> {
      String degree = degreeInput.getText().trim();
      String direction = directionInput.getText().trim();
      String name = degree + direction;


      if (!name.matches("\\d{2}[LCR]")) {
        showErrorDialog("Invalid runway name. The name must consist of two digits followed by L, C, or R.");
      } else {
        try {
          int TORA = Integer.parseInt(TORAInput.getText());
          int TODA = Integer.parseInt(TODAInput.getText());
          int ASDA = Integer.parseInt(ASDAInput.getText());
          int LDA = Integer.parseInt(LDAInput.getText());
          int DisThresh = Integer.parseInt(DisThreshInput.getText());

          if (TODA <= 0 || TORA <= 0 || ASDA <= 0 || LDA <= 0 || DisThresh < 0) {
            throw new IllegalArgumentException("Invalid measurements for runway.");
          }

          Stage stage = (Stage) form.getScene().getWindow();

          airport.removeRunway(currentRunway);
          currentRunway = new Runway(degree, direction, TODA, TORA, ASDA, LDA, DisThresh);
          airport.getRunways().add(currentRunway);
          app.updateAirportsXML();
          if (!currentRunway.getObstacles().isEmpty()) { // If theres still an obstacle inside recalculate values
            currentRunway.runCalculations();
          }
          app.displayRunwayConfigScene(airport,currentRunway);
          stage.close();

        } catch (NumberFormatException ex) {
          showErrorDialog("Invalid input for number of runways. Please enter a valid integer.");
        } catch (IllegalArgumentException ex) {
          showErrorDialog(ex.getMessage());
        }
      }
    });

    form.getChildren().addAll(degreeLabel, degreeInput, directionLabel, directionInput, TORALabel, TORAInput, TODALabel, TODAInput,
        ASDALabel, ASDAInput, LDALabel, LDAInput, DisThreshLabel, DisThreshInput,
        submitButton, cancelButton);

    Stage dialogStage = new Stage();
    dialogStage.initModality(Modality.APPLICATION_MODAL);
    dialogStage.setTitle("Edit Runway");
    AirportScene.extractedDialogStageMethod(form, dialogStage);
  }

  private void showErrorDialog(String message) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);

    VBox dialogVbox = new VBox(20);

    Text errorMessage = new Text(message);
    Button okButton = new Button();
    styleButton(okButton, "OK");

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

  private HBox makeDistanceHBox(String name, int value) {
    var distance = Integer.toString(value);
    return new HBox(new Text(name + distance + " m"));
  }

  private void styleTextField(TextField textField) {
    textField.setStyle("-fx-background-color: white; -fx-text-fill: black;");
    textField.setFont(Font.font("Arial", 16));
  }

  private void styleButton(Button button, String text) {
    AirportListScene.extractedStylingMethod(button, MaterialDesign.MDI_KEYBOARD_RETURN, text);
  }
}
