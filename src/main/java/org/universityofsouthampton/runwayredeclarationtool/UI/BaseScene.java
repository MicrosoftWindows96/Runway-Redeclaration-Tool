package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
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

import java.util.ArrayList;


/**
 * This class deals with the common functionality between the scenes of the application
 */
public abstract class BaseScene extends VBox {

  // Variable to access the MainApplication methods (Controller)
  protected MainApplication app;

  // Separate method to create all the buttons to be passed to a VBox or HBox
  abstract ArrayList<Button> addButtons();

  // Dialog screen to be displayed in case of any prompt screens.
  static void dialogGenerator(VBox form, String title) {
    Stage dialogStage = new Stage();
    dialogStage.initModality(Modality.APPLICATION_MODAL);
    dialogStage.setTitle(title);
    dialogStage.setScene(new Scene(form));

    double centerX = Screen.getPrimary().getVisualBounds().getWidth() / 2;
    double centerY = Screen.getPrimary().getVisualBounds().getHeight() / 2;

    dialogStage.setX(centerX - 150);
    dialogStage.setY(centerY - 100);
    dialogStage.showAndWait();
  }

  // This method opens an error window when an error occurs
  protected void showErrorDialog(String message) {
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

  // STYLING METHODS:

  // This method styles any used TextFields
  protected void styleTextField(TextField textField) {
    textField.setStyle("-fx-background-color: white; -fx-text-fill: black;");
    textField.setFont(Font.font("Arial", 16));
  }

  // Ths method styles any button, to be used in every scene
  static void styleButton(Button button, MaterialDesign icon, String text) {
    button.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
    button.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
    button.setPrefWidth(120);
    button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;"));
    button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #333; -fx-text-fill: white;"));
    button.setOnMouseClicked(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;"));

    styleIcon(button, icon, text);
  }

  void styleDarkButton(Button button, MaterialDesign icon, String text) {
    button.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
    button.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
    button.setPrefWidth(10);
    button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;"));
    button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #333; -fx-text-fill: white;"));
    button.setOnMouseClicked(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;"));

    styleIcon(button, icon, text);
  }

  // Styles the Icon in the button, used in every button on this application.
  static void styleIcon(Button button, MaterialDesign icon, String text) {
    FontIcon buttonIcon = new FontIcon(icon);
    buttonIcon.setIconColor(Color.WHITE);
    button.setGraphic(buttonIcon);
    button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

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