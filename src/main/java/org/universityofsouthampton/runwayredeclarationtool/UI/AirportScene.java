package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AirportScene extends StackPane {

  public AirportScene (Window window) {

    setAlignment(Pos.CENTER);

    var airportPane = new BorderPane();
    airportPane.setMaxSize(window.getWidth(),window.getHeight());

    var title = new Text("Airports");
    var titleBox = new HBox();

    titleBox.setAlignment(Pos.BASELINE_CENTER);
    titleBox.getChildren().add(title);

    var buttons = new VBox();

    Button addAirport = new Button("Add Airport");
    Button selectAirport = new Button("Select Airport");
    Button back = new Button("Back");
    back.setOnAction(e -> window.changeScene(window.menu));

    buttons.getChildren().add(addAirport);
    buttons.getChildren().add(selectAirport);
    buttons.getChildren().add(back);
    buttons.setAlignment(Pos.CENTER);
    buttons.setSpacing(10);

    airportPane.setTop(titleBox);
    airportPane.setCenter(buttons);

    getChildren().add(airportPane);

  }

}
