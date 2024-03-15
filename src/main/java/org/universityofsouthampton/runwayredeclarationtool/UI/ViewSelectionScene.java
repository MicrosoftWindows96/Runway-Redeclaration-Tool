package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewSelectionScene extends BaseScene {
    private final Runway currentRunway; // currently viewed runway
    public ViewSelectionScene(MainApplication app, Runway runway) {
        this.app = app;
        this.currentRunway = runway;

        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(200);

        // Set the title of the screen
        var title = new Text("View Selection");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        VBox.setMargin(title, new Insets(10, 0, 10, 0));

        // Make the screen buttons
        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(addButtons());

        // Add nodes
        this.getChildren().addAll(title, buttons);
    }

    private Stage secondaryStage; // Field to hold the reference to the secondary stage

    public void setSecondaryStage(Stage stage) {
        this.secondaryStage = stage;
    }


    @Override
    ArrayList<Button> addButtons() {

        Button sideViewButton = new Button(); // Button to open the Login prompt
        styleButton(sideViewButton, MaterialDesign.MDI_LOGIN, "2D Side View");
        sideViewButton.setOnAction(e -> app.display2DsideViewScene(currentRunway));

        Button topDownViewButton = new Button(); // Button to open the Login prompt
        styleButton(topDownViewButton, MaterialDesign.MDI_LOGIN, "2D Top-down View");
        topDownViewButton.setOnAction(e -> app.display2DtopDownViewScene(currentRunway));

        Button bothViewButton = new Button(); // Button to open the Login prompt
        styleButton(bothViewButton, MaterialDesign.MDI_LOGIN, "2D Both View");
        bothViewButton.setOnAction(e -> app.display2DbothViewScene(currentRunway));

        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Close");
        backButton.setOnAction(e -> {
            if (secondaryStage != null) {
                secondaryStage.close();
            }
        });

        return new ArrayList<>(Arrays.asList(sideViewButton, topDownViewButton, bothViewButton, backButton));
    }}
