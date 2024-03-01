package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuScene extends StackPane {

  public MenuScene (Window window) {

    var menuPane = new BorderPane();
    menuPane.setMaxSize(window.getWidth(), window.getHeight());
    menuPane.setStyle("-fx-background-color: #f0f0f0;"); // Neutral background color

    var title = new Text("Runway Declaration Tool");
    title.setFont(Font.font("Arial", 24)); // Modern font and size
    title.setStyle("-fx-fill: #333;"); // Dark text for better contrast

    var titleBox = new HBox();
    titleBox.setAlignment(Pos.CENTER);
    titleBox.getChildren().add(title);
    titleBox.setStyle("-fx-padding: 20;"); // Padding for the title

    var buttons = new VBox(10); // Spacing between buttons
    buttons.setAlignment(Pos.CENTER);

    Button airport = new Button("Airport");
    styleButton(airport);
    airport.setOnAction(e -> window.changeScene(window.airports));

    Button quit = new Button("Quit");
    styleButton(quit);
    quit.setOnAction(e -> System.exit(0));

    buttons.getChildren().addAll(airport, quit);

    menuPane.setTop(titleBox);
    menuPane.setCenter(buttons);

    getChildren().add(menuPane);
  }

  private void styleButton(Button button) {
    button.setStyle("-fx-background-color: #333; -fx-text-fill: white;"); // Button style
    button.setFont(Font.font("Arial", 16)); // Font style
    button.setPrefWidth(120); // Set a preferred width
    button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;")); // Hover effect
    button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #333; -fx-text-fill: white;")); // Hover effect reset
  }

}
