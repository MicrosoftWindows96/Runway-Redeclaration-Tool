package org.universityofsouthampton.runwayredeclarationtool.UI;

import com.almasb.fxgl.core.collection.Array;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

public class RunwayListScene extends VBox {

  /**
   * Scroll pane to display the airports
   */
  private ScrollPane scrollPane = new ScrollPane();

  /**
   * Observable arraylist of airports to be displayed
   */
  private ObservableList<Runway> runwayObserve = FXCollections.observableArrayList();

  /**
   * Runways from the Airport Object
   */
  private ArrayList<Runway> runways;

  /**
   *
   */
  private Runway selectedRunway;

  /**
   * Airport Passed to this class
   */
  private Airport airport;

  public RunwayListScene (MainApplication app, Airport airport) {
    this.airport = airport;
    runways = airport.getRunways();

    setPadding(new Insets(20));
    setSpacing(10);

    Text title = new Text("List of Runways in " + this.airport.getAirportName());
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
      if (selectedRunway.getName().isEmpty()) {
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
    backButton.setOnAction(e -> app.displayAirportListScene());

    HBox buttonBox = new HBox(10);
    buttonBox.getChildren().addAll(addRunway,selectButton, backButton);

    this.getChildren().addAll(title,scrollPane,buttonBox);
  }

  private void updateList() {
    runwayObserve.clear();

    var runwaysBox = new VBox();
    runwaysBox.setSpacing(5);

    for (Runway runway : runways) {
      var name = (" -- " + runway.getName() + " -- ");
      var runwayButton = new Button(name);

      // Button to select the airport
      runwayButton.setOnMouseClicked(event -> {
        setSelectedRunway(runway);
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
    return selectedRunway;
  }

  private void promptAddRunwayForm(MainApplication app) {
    VBox form = new VBox(10);
    form.setAlignment(Pos.CENTER);
    form.setPadding(new Insets(20));

    Label nameLabel = new Label("Runway Name:");
    TextField nameInput = new TextField();
    styleTextField(nameInput);

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
    // Add airport

    Button cancelButton = new Button();
    styleButton(cancelButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
    cancelButton.setOnAction(e -> {
      Stage stage = (Stage) form.getScene().getWindow();
      stage.close();
    });

    submitButton.setOnAction(e -> {
      String name = nameInput.getText();
      String TORAname = TORAInput.getText();
      String TODAname = TODAInput.getText();
      String ASDAname = ASDAInput.getText();
      String LDAname = LDAInput.getText();
      String DisThreshname = DisThreshInput.getText();

      // Validate
      if (name.isEmpty() || TODAname.isEmpty() || TORAname.isEmpty() || ASDAname.isEmpty() || LDAname.isEmpty() || DisThreshname.isEmpty()) {
        showErrorDialog("All fields are required. Please fill in all fields.");
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
          // Data valid, add runway
          // For now, just close the form
          Stage stage = (Stage) form.getScene().getWindow();

          // add the Runway into the list
          runways.add(new Runway(name,TODA,TORA,ASDA,LDA,DisThresh));
          updateList();

          stage.close();

        } catch (NumberFormatException ex) {
          showErrorDialog("Invalid input for number of runways. Please enter a valid integer.");
        } catch (IllegalArgumentException ex) {
          showErrorDialog(ex.getMessage());
        }
      }
    });

    form.getChildren().addAll(nameLabel, nameInput, TORALabel, TORAInput, TODALabel, TODAInput,
        ASDALabel, ASDAInput, LDALabel, LDAInput, DisThreshLabel, DisThreshInput, submitButton, cancelButton);

    Stage dialogStage = new Stage();
    dialogStage.initModality(Modality.APPLICATION_MODAL);
    dialogStage.setTitle("Add Runway");
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
