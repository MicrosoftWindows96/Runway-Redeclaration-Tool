package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

public class RunwayDetailsDialog extends Dialog<Runway> {

    public RunwayDetailsDialog() {
        setTitle("Runway Details");
        setHeaderText("Enter runway details");

        ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField degreeField = new TextField();
        degreeField.setPromptText("Degree");
        TextField directionField = new TextField();
        directionField.setPromptText("Direction (L, R, C)");
        TextField toraField = new TextField();
        toraField.setPromptText("TORA");
        TextField todaField = new TextField();
        todaField.setPromptText("TODA");
        TextField asdaField = new TextField();
        asdaField.setPromptText("ASDA");
        TextField ldaField = new TextField();
        ldaField.setPromptText("LDA");
        TextField dispThresholdField = new TextField();
        dispThresholdField.setPromptText("Displaced Threshold");

        grid.add(degreeField, 0, 0);
        grid.add(directionField, 1, 0);
        grid.add(toraField, 0, 1);
        grid.add(todaField, 1, 1);
        grid.add(asdaField, 0, 2);
        grid.add(ldaField, 1, 2);
        grid.add(dispThresholdField, 0, 3);

        getDialogPane().setContent(grid);

        setResultConverter(buttonType -> {
            if (buttonType == submitButtonType) {
                try {
                    String name = degreeField.getText().trim() + directionField.getText().trim();
                    int tora = Integer.parseInt(toraField.getText().trim());
                    int toda = Integer.parseInt(todaField.getText().trim());
                    int asda = Integer.parseInt(asdaField.getText().trim());
                    int lda = Integer.parseInt(ldaField.getText().trim());
                    int displacedThreshold = Integer.parseInt(dispThresholdField.getText().trim());

                    // Replace isValidName
                    if (!isValidName(name)) {
                        // Handle invalid name input
                        return null;
                    }

                    return new Runway(name, tora, toda, asda, lda, displacedThreshold);
                } catch (NumberFormatException e) {
                    // Handle invalid number inputs
                    return null;
                }
            }
            return null;
        });
    }

    private boolean isValidName(String name) {
        // placeholder implementation.
        return name != null && !name.isEmpty() && name.matches("\\d{2}[LCR]");
    }
}
