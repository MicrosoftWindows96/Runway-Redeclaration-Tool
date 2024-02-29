package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuScene extends StackPane {

  public MenuScene (Window window) {

    var menuPane = new BorderPane();
    menuPane.setMaxSize(window.getWidth(),window.getHeight());

    var title = new Text("Runway Declaration Tool");
    var titleBox = new HBox();

    titleBox.setAlignment(Pos.BASELINE_CENTER);
    titleBox.getChildren().add(title);

    var buttons = new VBox();

    Button airport = new Button("Airport");
    airport.setOnAction(e -> window.changeScene(window.airports));

    Button quit = new Button("Quit");
    quit.setOnAction(e -> System.exit(0));

    buttons.getChildren().add(airport);
    buttons.getChildren().add(quit);
    buttons.setAlignment(Pos.CENTER);
    buttons.setSpacing(10);

    menuPane.setTop(titleBox);
    menuPane.setCenter(buttons);

    getChildren().add(menuPane);

  }

}
