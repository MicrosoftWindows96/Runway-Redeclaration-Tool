package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

import java.util.ArrayList;

public class RunwayListScene extends VBox {

  /**
   * Scroll pane to display the airports
   */
  private final ScrollPane scrollPane = new ScrollPane();

  /**
   * Observable arraylist of airports to be displayed
   */
  private final ObservableList<Runway> runwayObserve = FXCollections.observableArrayList();

  /**
   * Runways from the Airport Object
   */
  private final ArrayList<Runway> runways;

  /**
   *
   */
  private Runway selectedRunway;

    private Button currentlySelectedButton;

  public RunwayListScene (MainApplication app, Airport airport) {
      runways = airport.getRunways();

    setPadding(new Insets(20));
    setSpacing(10);

    Text title = new Text("List of Runways in " + airport.getAirportName());
    title.setFont(Font.font("Arial", 20));

    // Populate list of runways from Airport's arrayLists
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefSize(700,500);
    updateList();

    Button selectButton = new Button();
    styleButton(selectButton, MaterialDesign.MDI_PLUS_BOX, "Select");
    selectButton.setOnAction(e -> {
      // Handle airport selection (we may need to implement database/xml func early)
      // Selection error handling
      if (this.selectedRunway == null) {
        System.out.println("Nothing Selected!");
      } else {
        app.displayRunwayConfigScene(airport, selectedRunway);
      }

    });

    Button addRunway = new Button();
    styleButton(addRunway, MaterialDesign.MDI_PLUS_BOX, "Add");
    addRunway.setOnAction(e -> promptAddRunwayForm(app));

    Button backButton = new Button();
    styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
    backButton.setOnAction(e -> {
      app.updateXMLs();
      app.displayAirportListScene();
    });

    HBox buttonBox = new HBox(10);
    buttonBox.getChildren().addAll(backButton, selectButton, addRunway);

    this.getChildren().addAll(title,scrollPane,buttonBox);
  }

  private void updateList() {
    runwayObserve.clear();
    var runwaysBox = new VBox();
    runwaysBox.setSpacing(5);

    for (Runway runway : runways) {
      var name = runway.getName();
      var runwayButton = new Button(name);
      styleButton(runwayButton, MaterialDesign.MDI_ARROW_UP, name);

      runwayButton.setOnMouseExited(e -> {
        if (runwayButton != currentlySelectedButton){
          runwayButton.setStyle(("-fx-background-color: #333; -fx-text-fill: white;"));
        }
      });

      // Button to select the runway
      runwayButton.setOnMouseClicked(e -> {
        if (currentlySelectedButton != null) {
          currentlySelectedButton.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        }
        runwayButton.setStyle(("-fx-background-color: #555; -fx-text-fill: white;"));
        setSelectedRunway(runway);
        currentlySelectedButton = runwayButton;
        System.out.println("Currently selected: " + getSelectedRunway().getName());
      });

      runwaysBox.getChildren().add(runwayButton);
      runwayObserve.add(runway);
    }

    scrollPane.setContent(runwaysBox);
  }

  public void setSelectedRunway(Runway selectedRunway) {
    this.selectedRunway = selectedRunway;
  }

  public Runway getSelectedRunway () {
    return this.selectedRunway;
  }

  private void promptAddRunwayForm(MainApplication app) {
    VBox form = new VBox(10);
    form.setAlignment(Pos.CENTER);
    form.setPadding(new Insets(20));

    Label degreeLabel = new Label("Degree:");
    TextField degreeInput = new TextField();
    styleTextField(degreeInput);
    degreeInput.setPromptText("Degree");

    Label directionLabel = new Label("Direction (L, R, C):");
    TextField directionInput = new TextField();
    styleTextField(directionInput);
    directionInput.setPromptText("Direction (L, R, C)");

    Label TORALabel = new Label("TORA:");
    TextField TORAInput = new TextField();
    styleTextField(TORAInput);

    Label TODALabel = new Label("TODA:");
    TextField TODAInput = new TextField();
    styleTextField(TODAInput);

    Label ASDALabel = new Label("ASDA:");
    TextField ASDAInput = new TextField();
    styleTextField(ASDAInput);

    Label LDALabel = new Label("LDA:");
    TextField LDAInput = new TextField();
    styleTextField(LDAInput);

    Label DisThreshLabel = new Label("Displaced Threshold:");
    TextField DisThreshInput = new TextField();
    styleTextField(DisThreshInput);

    Button submitButton = new Button();
    styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Add");

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
          runways.add(new Runway(degree, direction, TODA, TORA, ASDA, LDA, DisThresh));
          app.updateAirportsXML();
          updateList();
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
    dialogStage.setTitle("Add Runway");
    AirportScene.extractedDialogStageMethod(form, dialogStage);
  }

  private void styleTextField(TextField textField) {
    textField.setStyle("-fx-background-color: white; -fx-text-fill: black;");
    textField.setFont(Font.font("Arial", 16));
  }

  private void styleButton(Button button, MaterialDesign icon, String text) {
      AirportListScene.extractedStylingMethod(button, icon, text);
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
