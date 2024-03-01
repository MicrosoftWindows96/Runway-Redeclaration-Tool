package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;

public class MenuScene extends VBox {

  public MenuScene(MainApplication app) {
    this.setAlignment(Pos.TOP_CENTER);

    var title = new Text("Runway Declaration Tool");
    title.setFont(Font.font("Arial", 24));
    title.setStyle("-fx-fill: #333;");
    VBox.setMargin(title, new Insets(10, 0, 10, 0));

    VBox buttons = new VBox(10);
    buttons.setAlignment(Pos.CENTER);

    this.setSpacing(200);

    Button airport = new Button("Airport");
    styleButton(airport);
    airport.setOnAction(e -> app.displayAirportScene());

    Button quit = new Button("Quit");
    styleButton(quit);
    quit.setOnAction(e -> System.exit(0));

    buttons.getChildren().addAll(airport, quit);

    this.getChildren().addAll(title, buttons);
  }

  private void styleButton(Button button) {
    button.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
    button.setFont(Font.font("Arial", 16));
    button.setPrefWidth(120);
    button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;"));
    button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #333; -fx-text-fill: white;"));
  }
}
