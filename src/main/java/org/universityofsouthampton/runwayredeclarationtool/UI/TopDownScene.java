package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;
import java.util.Arrays;

public class TopDownScene extends BaseScene {

    private Runway currentRunway; // currently viewed runway

    public TopDownScene(MainApplication app, Runway runway) {
        this.app = app;
        this.currentRunway = runway;
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(200);

        // Set the title of the screen
        var title = new Text("Top down view");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        VBox.setMargin(title, new Insets(10, 0, 10, 0));

        // Make the screen buttons
        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(addButtons());

        // Add nodes
        this.getChildren().addAll(title,buttons);
    }

    @Override
    ArrayList<Button> addButtons() {
        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        backButton.setOnAction(e -> app.displayViewsScene(currentRunway));

        return new ArrayList<>(Arrays.asList(backButton));
    }
}