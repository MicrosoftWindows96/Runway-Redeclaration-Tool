package org.universityofsouthampton.runwayredeclarationtool.UI;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

public class RoleCheckBoxes extends BaseScene {

  public ArrayList<CheckBox> checkBoxes;
  public CheckBox selectedBox;

  public RoleCheckBoxes () {
    setSpacing(5);

    checkBoxes = new ArrayList<>();

    CheckBox adminCheck = new CheckBox("admin");
    adminCheck.setOnAction(e -> updateCheckBoxes(adminCheck));

    CheckBox editorCheck = new CheckBox("editor");
    editorCheck.setOnAction(e -> updateCheckBoxes(editorCheck));

    CheckBox viewerCheck = new CheckBox("viewer");
    viewerCheck.setOnAction(e -> updateCheckBoxes(viewerCheck));

    checkBoxes.addAll(Arrays.asList(adminCheck,editorCheck,viewerCheck));
    getChildren().addAll(adminCheck,editorCheck,viewerCheck);

  }

  private void updateCheckBoxes(CheckBox selectedBox) { // Makes sure only one box is selected
    for (CheckBox checkBox : checkBoxes) {
      if (!checkBox.equals(selectedBox)) {
        checkBox.setSelected(false);
      }
    }
    if (selectedBox.isSelected()) {
      this.selectedBox = selectedBox;
    } else {
      this.selectedBox = null;
    }
    System.out.println("SELECTED BOX: " + selectedBox.getText());
  }

  public Boolean isSelectedNull () { // Checks if a checkBox is SELECTED
    return selectedBox == null;
  }

  public String getSelectedRole() {
    return selectedBox.getText();
  }

  @Override
  ArrayList<Button> addButtons() {
    return null;
  }

}
