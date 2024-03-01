package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class AirportScene extends StackPane {

  public AirportScene (Window window) {
    setAlignment(Pos.CENTER);

    StackPane root = this;

    var airportPane = new BorderPane();
    airportPane.setMaxSize(window.getWidth(), window.getHeight());
    airportPane.setStyle("-fx-background-color: transparent;");

    var title = new Text("Airports");
    title.setFont(Font.font("Arial", 24));
    title.setStyle("-fx-fill: #333;");

    var titleBox = new HBox();
    titleBox.setAlignment(Pos.CENTER);
    titleBox.getChildren().add(title);
    titleBox.setStyle("-fx-padding: 20;");

    var buttons = new VBox(10);
    buttons.setAlignment(Pos.CENTER);

    Button addAirport = new Button("Add Airport");
    styleButton(addAirport);

    Button selectAirport = new Button("Select Airport");
    styleButton(selectAirport);

    Button back = new Button("Back");
    styleButton(back);
    back.setOnAction(e -> window.changeScene(window.menu));

    buttons.getChildren().addAll(addAirport, selectAirport, back);

    airportPane.setTop(titleBox);
    airportPane.setCenter(buttons);

    root.getChildren().addAll(AnimatedPatternBackground.getInstance(), airportPane);
  }

  private void styleButton(Button button) {
    button.setStyle("-fx-background-color: #333; -fx-text-fill: white;"); // Button style
    button.setFont(Font.font("Arial", 16)); // Font style
    button.setPrefWidth(120);
    button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;")); // Hover effect
    button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #333; -fx-text-fill: white;")); // Hover effect reset
  }
}
