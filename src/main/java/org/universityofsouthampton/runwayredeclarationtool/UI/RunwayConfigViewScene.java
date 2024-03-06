package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

public class RunwayConfigViewScene extends VBox {

  private Runway currentRunway;
  private final Airport airport;

  public RunwayConfigViewScene(MainApplication app, Airport airport, Runway runway) {
    this.airport = airport;
    this.currentRunway = runway;

    setPadding(new Insets(20));
    setSpacing(10);

    getChildren().add(createTitleSection(runway));
    getChildren().add(createParameterSection());
    getChildren().add(drawRunway());
    getChildren().add(calculatedParameterSection());
    if (!currentRunway.getObstacles().isEmpty()) {
      getChildren().add(createCalculationBreakdownSection());
    }
    getChildren().add(createButtonSection(app));
  }

  private VBox createCalculationBreakdownSection() {
    VBox breakdownSection = new VBox(10);
    breakdownSection.setPadding(new Insets(15));
    breakdownSection.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #f9f9f9;");

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    VBox contentBox = new VBox(5);

    Text calculationDetails = new Text(currentRunway.getCalculationBreakdown());
    calculationDetails.setWrappingWidth(1000);
    contentBox.getChildren().add(calculationDetails);

    scrollPane.setContent(contentBox);
    breakdownSection.getChildren().addAll(new Text("Calculation Breakdown"), scrollPane);

    return breakdownSection;
  }

  private Text createTitleSection(Runway runway) {
    Text title = new Text("Runway: " + airport.getAirportCode() + " -- " + runway.getName());
    title.setFont(Font.font("Arial", 20));
    return title;
  }

  private VBox createParameterSection() {
    VBox parameterSection = new VBox(10);
    parameterSection.setPadding(new Insets(15));
    parameterSection.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #f9f9f9;");
    parameterSection.getChildren().add(setUpParameters());
    return parameterSection;
  }

  private HBox createButtonSection(MainApplication app) {
    Button obstacleUpdateButton = new Button();
    styleButton(obstacleUpdateButton, MaterialDesign.MDI_LIGHTBULB, "Obstacle");
    obstacleUpdateButton.setOnAction(e -> app.displayObstacleListScene(airport, currentRunway));

    Button runwayUpdateButton = new Button();
    styleButton(runwayUpdateButton, MaterialDesign.MDI_WRENCH, "Runway");
    runwayUpdateButton.setOnAction(e -> promptEditRunwayForm(app));

    Button backButton = new Button();
    styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
    backButton.setOnAction(e -> app.displayAirportListScene());

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backButton, runwayUpdateButton, obstacleUpdateButton);
    return buttonBox;
  }

  private VBox drawRunway() {
    Group runwayDiagram = new Group();
    runwayDiagram.getChildren().addAll(createRunwayLine(), createRunwayLabels());
    VBox runwaySection = new VBox(runwayDiagram);
    runwaySection.setPadding(new Insets(10, 0, 10, 0));
    runwaySection.setAlignment(Pos.CENTER);
    return runwaySection;
  }

  private Line createRunwayLine() {
    return new Line(50, 150, 750, 150) {{
      setStroke(Color.GRAY);
      setStrokeWidth(10);
    }};
  }

  private Group createRunwayLabels() {
    Text toraLabel = new Text("TORA: " + currentRunway.getTORA());
    toraLabel.setFont(Font.font("Arial", Font.getDefault().getSize()));
    toraLabel.setX(50);
    toraLabel.setY(130);

    Text todaLabel = new Text("TODA: " + currentRunway.getTODA());
    todaLabel.setFont(Font.font("Arial", Font.getDefault().getSize()));
    todaLabel.setX(150);
    todaLabel.setY(130);

    Text asdaLabel = new Text("ASDA: " + currentRunway.getASDA());
    asdaLabel.setFont(Font.font("Arial", Font.getDefault().getSize()));
    asdaLabel.setX(250);
    asdaLabel.setY(130);

    Text ldaLabel = new Text("LDA: " + currentRunway.getLDA());
    ldaLabel.setFont(Font.font("Arial", Font.getDefault().getSize()));
    ldaLabel.setX(350);
    ldaLabel.setY(130);

    Text displacedThresholdLabel = new Text("Displaced Threshold: " + currentRunway.getDisplacedThreshold());
    displacedThresholdLabel.setFont(Font.font("Arial", Font.getDefault().getSize()));
    displacedThresholdLabel.setX(450);
    displacedThresholdLabel.setY(130);

    return new Group(toraLabel, todaLabel, asdaLabel, ldaLabel, displacedThresholdLabel);
  }

  private VBox calculatedParameterSection() {
    VBox calculatedBox = new VBox(10);
    calculatedBox.setPadding(new Insets(15));
    calculatedBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #f9f9f9;");
    calculatedBox.getChildren().add(new VBox(new Text("Calculated Parameters")));

    if (!currentRunway.getObstacles().isEmpty()) {
      calculatedBox.getChildren().add(makeDistanceHBox("TORA ", currentRunway.getNewTORA()));
      calculatedBox.getChildren().add(makeDistanceHBox("TODA ", currentRunway.getNewTODA()));
      calculatedBox.getChildren().add(makeDistanceHBox("ASDA ", currentRunway.getNewASDA()));
      calculatedBox.getChildren().add(makeDistanceHBox("LDA ", currentRunway.getNewLDA()));
    } else {
      calculatedBox.getChildren().add(new Text("No obstacles present."));
    }

    return calculatedBox;
  }

  private VBox setUpParameters() {
    VBox parameterBox = new VBox(5);
    parameterBox.getChildren().addAll(makeDistanceHBox("TORA ", currentRunway.getTORA()),
            makeDistanceHBox("TODA ", currentRunway.getTODA()),
            makeDistanceHBox("ASDA ", currentRunway.getASDA()),
            makeDistanceHBox("LDA ", currentRunway.getLDA()),
            makeDistanceHBox("Displaced Threshold: ", currentRunway.getDisplacedThreshold()));
    return parameterBox;
  }

  private HBox makeDistanceHBox(String name, int value) {
    Text label = new Text(name + value + " m");
    label.setFont(Font.font("Arial", Font.getDefault().getSize()));
    HBox box = new HBox(label);
    box.setAlignment(Pos.CENTER_LEFT);
    return box;
  }

  private void styleButton(Button button, MaterialDesign icon, String text) {
    AirportListScene.extractedStylingMethod(button, icon, text);
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
    styleButton(submitButton, MaterialDesign.MDI_CHECK_CIRCLE, "Submit");

    Button cancelButton = new Button();
    styleButton(cancelButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
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

          Runway deletedRunway = currentRunway;
          airport.removeRunway(currentRunway);
          currentRunway = new Runway(degree, direction, TODA, TORA, ASDA, LDA, DisThresh);
          if (!currentRunway.getObstacles().isEmpty()) {
            Obstacle movedObstacle = deletedRunway.getObstacles().getFirst();
            currentRunway.addObstacle(movedObstacle);
          }
          airport.getRunways().add(currentRunway);
          app.updateAirportsXML();
          if (!currentRunway.getObstacles().isEmpty()) {
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
