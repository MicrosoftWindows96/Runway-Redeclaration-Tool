package org.universityofsouthampton.runwayredeclarationtool.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;

/**
 * This class deals with building prompt windows
 */
public class PromptWindow extends BaseScene {

  public PromptWindow(MainApplication app) { // Window Box default settings
    this.app = app;
    setSpacing(10);
    setAlignment(Pos.CENTER);
    setPadding(new Insets(20));
  }

  @Override
  ArrayList<Button> addButtons() {
    Button cancelButton = new Button();
    styleButton(cancelButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
    cancelButton.setOnAction(e -> ((Stage) getScene().getWindow()).close());
    return new ArrayList<>(List.of(cancelButton));
  }

  // Adding a new object
  public VBox addParameterField(String label) {
    Label paraLabel = new Label(label + ":");
    TextField paraInput = new TextField();
    styleTextField(paraInput);
    paraInput.setPromptText(label);
    VBox vBox = new VBox(5,paraLabel,paraInput);
    vBox.setAlignment(Pos.CENTER);
    return vBox;
  }

  // Editing an existing object
  public VBox editParameterField(String label, String oldParameter) {
    Label paraLabel = new Label(label + ":");
    TextField paraInput = new TextField(oldParameter);
    styleTextField(paraInput);
    VBox vBox = new VBox(5,paraLabel,paraInput);
    vBox.setAlignment(Pos.CENTER);
    return vBox;
  }

  // Returning a VBox's TextField
  public String getInput(VBox vBox) {
    for (var node : vBox.getChildren()) {
      if (node instanceof TextField) {
        return ((TextField) node).getText();
      }
    }
    return ""; // Return an empty string if no TextField is found
  }
}
