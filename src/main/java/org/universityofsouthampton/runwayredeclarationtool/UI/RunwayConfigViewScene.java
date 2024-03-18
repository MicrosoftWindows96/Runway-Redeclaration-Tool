package org.universityofsouthampton.runwayredeclarationtool.UI;

import static org.universityofsouthampton.runwayredeclarationtool.MainApplication.secondaryStage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.ParallelRunways;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

public class RunwayConfigViewScene extends BaseScene {

  private final ParallelRunways runwayManager; // This stores the list of parallel runways
  private final Runway currentRunway;
  private final Airport airport;

  public RunwayConfigViewScene(MainApplication app, Airport airport, ParallelRunways runwayManager) {
    this.app = app;
    this.airport = airport;
    this.runwayManager = runwayManager;
    currentRunway = runwayManager.getFstRunway();
    currentRunway.runCalculations();

    setPadding(new Insets(20));
    setSpacing(10);

    getChildren().add(createTitleSection(currentRunway));

    HBox parameters = new HBox(60,createParameterSection(),calculatedParameterSection());
    parameters.setAlignment(Pos.CENTER);
    getChildren().add(parameters);

    getChildren().add(drawRunway());
    if (!currentRunway.getObstacles().isEmpty()) {
      getChildren().add(createCalculationBreakdownSection());
    }

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(addButtons());
    getChildren().add(buttonBox);

    Button oppositeButton = new Button("Opposite"); // Goes to opposite runway direction
    styleButton(oppositeButton, MaterialDesign.MDI_RECYCLE, "Opposite");
    oppositeButton.setOnAction(e -> {
      runwayManager.swapRunways();
      app.displayRunwayConfigScene(airport,runwayManager);
    });

    Button nextButton = new Button("Next"); // Goes to next parallel runway
    styleButton(nextButton, MaterialDesign.MDI_ARROW_ALL, "Next");
    nextButton.setOnAction(e -> {
      runwayManager.nextCurrentRunway();
      app.displayRunwayConfigScene(airport,runwayManager);
    });

    HBox buttonBox2 = new HBox(10);
    buttonBox2.setAlignment(Pos.CENTER);
    buttonBox2.getChildren().addAll(nextButton,oppositeButton);
    getChildren().add(buttonBox2);

  }

  @Override
  ArrayList<Button> addButtons() {
    Button obstacleUpdateButton = new Button();
    styleButton(obstacleUpdateButton, MaterialDesign.MDI_SETTINGS, "Obstacles");
    obstacleUpdateButton.setOnAction(e -> {
      app.displayObstacleListScene(airport, runwayManager);
      if (secondaryStage != null) {
        secondaryStage.close();
      }
    });

    Button runwayUpdateButton = new Button();
    styleButton(runwayUpdateButton, MaterialDesign.MDI_WRENCH, "Modify");
    runwayUpdateButton.setOnAction(e -> {
      if (secondaryStage != null) {
        secondaryStage.close();
      }

      promptEditRunway();
    });

    Button backButton = new Button();
    styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
    backButton.setOnAction(e -> {
      app.displayAirportListScene();

      if (secondaryStage != null) {
        secondaryStage.close();
      }
    });

    Button exportButton = new Button("Export");
    styleButton(exportButton, MaterialDesign.MDI_UPLOAD, "Export");
    exportButton.setOnAction(e -> exportCalculationBreakdown());

    Button viewsButton = new Button("Render");
    styleButton(viewsButton, MaterialDesign.MDI_VIEW_AGENDA, "Render");
    viewsButton.setOnAction(e -> app.displayViewsSceneBeta(runwayManager));

    return new ArrayList<>(Arrays.asList(runwayUpdateButton, obstacleUpdateButton, viewsButton, exportButton, backButton));
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

  private void exportCalculationBreakdown() {
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
    fileChooser.getExtensionFilters().add(extFilter);
    File file = fileChooser.showSaveDialog(null);

    if (file != null) {
      try (PrintWriter writer = new PrintWriter(file)) {
        writer.println(currentRunway.getCalculationBreakdown());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Successful");
        alert.setHeaderText(null);
        alert.setContentText("Calculation breakdown has been successfully exported.");
        alert.showAndWait();
      } catch (IOException ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Could not save file");
        alert.setContentText("An error occurred while trying to save the file: " + ex.getMessage());
        alert.showAndWait();
      }
    }
  }

  private Text createTitleSection(Runway runway) {
    Text title = new Text("Runway: " + airport.getAirportCode() + " -- " + runway.getName() + runway.getDirection());
    title.setFont(Font.font("Arial", 20));
    return title;
  }

  private VBox createParameterSection() {
    VBox parameterSection = new VBox(10, new Text("Original Parameters"));
    parameterSection.setPadding(new Insets(15));
    parameterSection.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #f9f9f9;");
    parameterSection.getChildren().add(setUpParameters());

    return parameterSection;
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

  // PROMPT METHODS:
  private void promptEditRunway() {
    PromptWindow promptWindow = new PromptWindow(app);
    Text runways = new Text("Runway 1  ---  Runway 2");
    runways.setFont(Font.font("Arial", FontWeight.BOLD, 16));

    var degreeBox1 = promptWindow.editParameterField("Degree 1",runwayManager.getFstRunway().getName());
    var degreeBox2 = promptWindow.editParameterField("Degree 2",runwayManager.getSndRunway().getName());
    var deBox = new HBox(10,degreeBox1,degreeBox2);

    var stopwayBox1 = promptWindow.editParameterField("Stopway",Integer.toString(runwayManager.getFstRunway().getStopway()));
    var stopwayBox2 = promptWindow.editParameterField("Stopway",Integer.toString(runwayManager.getSndRunway().getStopway()));
    var sBox = new HBox(10,stopwayBox1,stopwayBox2);

    var clearwayBox1 = promptWindow.editParameterField("Clearway",Integer.toString(runwayManager.getFstRunway().getClearway()));
    var clearwayBox2 = promptWindow.editParameterField("Clearway",Integer.toString(runwayManager.getSndRunway().getClearway()));
    var cBox = new HBox(10,clearwayBox1,clearwayBox2);

    var TORAbox1 = promptWindow.editParameterField("TORA",Integer.toString(runwayManager.getFstRunway().getTORA()));
    var TORAbox2 = promptWindow.editParameterField("TORA",Integer.toString(runwayManager.getSndRunway().getTORA()));
    var tBox = new HBox(10,TORAbox1,TORAbox2);

    var dispThreshBox1 = promptWindow.editParameterField("Displaced Threshold:",Integer.toString(runwayManager.getFstRunway().getDisplacedThreshold()));
    var dispThreshBox2 = promptWindow.editParameterField("Displaced Threshold:",Integer.toString(runwayManager.getSndRunway().getDisplacedThreshold()));
    var diBox = new HBox(10,dispThreshBox1,dispThreshBox2);

    // Implement addButton
    Button submitButton = new Button("Add");
    styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Add");
    submitButton.setOnAction(e -> {
      String degree1 = promptWindow.getInput(degreeBox1);
      String degree2 = promptWindow.getInput(degreeBox2);
      // Try making a runway object and check for any illegal parameters
      if (!degree1.matches("\\d{2}") || !degree2.matches("\\d{2}")) {
        showErrorDialog("Invalid runway name. The name must be a valid degree.");
      } else {
        try {
          int stopway1 = Integer.parseInt(promptWindow.getInput(stopwayBox1));
          int clearway1 = Integer.parseInt(promptWindow.getInput(clearwayBox1));
          int TORA1 = Integer.parseInt(promptWindow.getInput(TORAbox1));
          int dispThresh1 = Integer.parseInt(promptWindow.getInput(dispThreshBox1));

          int stopway2 = Integer.parseInt(promptWindow.getInput(stopwayBox2));
          int clearway2 = Integer.parseInt(promptWindow.getInput(clearwayBox2));
          int TORA2 = Integer.parseInt(promptWindow.getInput(TORAbox2));
          int dispThresh2 = Integer.parseInt(promptWindow.getInput(dispThreshBox2));

          if (stopway1 < 0 || clearway1 < 0 || TORA1 < 0 || dispThresh1 < 0 ||
              stopway2 < 0 || clearway2 < 0 || TORA2 < 0 || dispThresh2 < 0) {
            throw new IllegalArgumentException("Invalid measurements for runways.");
          }

          Runway testRunway1 = new Runway(degree1,stopway1,clearway1,TORA1,dispThresh1);
          Runway testRunway2 = new Runway(degree2,stopway2,clearway2,TORA2,dispThresh2);
          runwayManager.replaceRunways(runwayManager.getCurrentRunways(),new Pair<>(testRunway1,testRunway2));

          app.displayRunwayConfigScene(airport,runwayManager);
          Stage stage = (Stage) promptWindow.getScene().getWindow();
          stage.close();

        } catch (NumberFormatException ex) {
          showErrorDialog("Invalid input for runway measurements. Please enter valid integers.");
        } catch (IllegalArgumentException ex) {
          showErrorDialog(ex.getMessage());
        }
      }
    });
    promptWindow.getChildren().addAll(runways,deBox,sBox,cBox,
        tBox,diBox,submitButton);
    promptWindow.getChildren().addAll(promptWindow.addButtons());
    dialogGenerator(promptWindow, "Add NEW Runways");
  }

}
