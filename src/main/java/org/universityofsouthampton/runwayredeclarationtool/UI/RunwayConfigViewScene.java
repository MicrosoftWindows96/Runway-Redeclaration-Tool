package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

    Button backButton = new Button();
    styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
    backButton.setOnAction(e -> app.displayRunwayListScene(airport));

    HBox buttonBox = new HBox(10);
    buttonBox.getChildren().addAll(backButton);

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
