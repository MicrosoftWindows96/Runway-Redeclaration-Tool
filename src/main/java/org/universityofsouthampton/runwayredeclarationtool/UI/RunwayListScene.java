package org.universityofsouthampton.runwayredeclarationtool.UI;

import com.almasb.fxgl.core.collection.Array;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
  private ObservableList<Airport> runwayObserve = FXCollections.observableArrayList();

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

    // Populate list of runways from Airport's arrayList
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefSize(700,500);
    updateList();


    Button backButton = new Button();
    styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
    backButton.setOnAction(e -> app.displayAirportListScene());

    HBox buttonBox = new HBox(10);
    buttonBox.getChildren().addAll(backButton);

    this.getChildren().addAll(title,buttonBox);
  }

  private void updateList() {
    runways.clear();
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

      runways.add(runway);
    }

    scrollPane.setContent(runwaysBox);
  }

  public void setSelectedRunway(Runway selectedRunway) {
    this.selectedRunway = selectedRunway;
  }

  public Runway getSelectedRunway () {
    return selectedRunway;
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

}
