package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

public class RunwayConfigViewScene extends VBox {


  private VBox parameterBox;

  /**
   * Runway currently selected
   */
  private final Runway runway;

  public RunwayConfigViewScene (MainApplication app, Airport airport, Runway runway) {
    this.runway = runway;


    setPadding(new Insets(20));
    setSpacing(10);

    Text title = new Text("Runway: " + airport.getAirportCode() + " -- " + runway.getName());
    title.setFont(Font.font("Arial", 20));

    setUpParameters();

    Button obstacleUpdateButton = new Button();
    styleButton(obstacleUpdateButton, "Obstacle");
    obstacleUpdateButton.setOnAction(e -> app.displayObstacleListScene(airport,runway));

    Button backButton = new Button();
    styleButton(backButton, "Return");
    backButton.setOnAction(e -> app.displayRunwayListScene(airport));

    HBox buttonBox = new HBox(10);
    buttonBox.getChildren().addAll(backButton,obstacleUpdateButton);


    this.getChildren().addAll(title,parameterBox,buttonBox);

  }

  private void setUpParameters() {
    parameterBox = new VBox();
    parameterBox.setSpacing(5);

    var TORA = makeDistanceHBox("TORA ", runway.getTORA());
    var TODA = makeDistanceHBox("TODA ", runway.getTODA());
    var ASDA = makeDistanceHBox("ASDA ", runway.getASDA());
    var LDA = makeDistanceHBox("LDA ", runway.getLDA());
    var disThres = makeDistanceHBox("Displaced Threshold: ",runway.getDisplacedThreshold());


    parameterBox.getChildren().addAll(TORA,TODA,ASDA,LDA,disThres);
  }

  private HBox makeDistanceHBox(String name, int value) {
    var distance = Integer.toString(value);
    return new HBox(new Text(name + distance + " m"));
  }

  private void styleButton(Button button, String text) {
    AirportListScene.extractedStylingMethod(button, MaterialDesign.MDI_KEYBOARD_RETURN, text);
  }
}
