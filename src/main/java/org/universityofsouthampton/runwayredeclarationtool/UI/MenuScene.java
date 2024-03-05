package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;

public class MenuScene extends VBox {

  public MenuScene(MainApplication app) {
    this.setAlignment(Pos.TOP_CENTER);

    var title = new Text("Runway Re-declaration Tool");
    title.setFont(Font.font("Arial", 24));
    title.setStyle("-fx-fill: #333;");
    VBox.setMargin(title, new Insets(10, 0, 10, 0));

    VBox buttons = new VBox(10);
    buttons.setAlignment(Pos.CENTER);

    this.setSpacing(200);

    Button airport = new Button();
    styleButton(airport, MaterialDesign.MDI_DATABASE, "Airports");
    airport.setOnAction(e -> app.displayAirportScene());

//    Button obstacle = new Button();
//    styleButton(obstacle, MaterialDesign.MDI_DATABASE, "Obstacles");
//    obstacle.setOnAction(e -> app.displayObstacleScene());

    Button quit = new Button();
    styleButton(quit, MaterialDesign.MDI_EXIT_TO_APP, "Quit");
    quit.setOnAction(e -> System.exit(0));

    buttons.getChildren().addAll(airport, quit);

    this.getChildren().addAll(title, buttons);
  }

  private void styleButton(Button button, MaterialDesign icon, String text) {
      AirportListScene.extractedStylingMethod(button, icon, text);
  }
}
