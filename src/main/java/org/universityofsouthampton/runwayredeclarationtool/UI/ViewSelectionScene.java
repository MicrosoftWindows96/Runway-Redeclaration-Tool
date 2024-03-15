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
    private final Runway currentRunway;
    public ViewSelectionScene(MainApplication app, Runway runway) {
        this.app = app;
        this.currentRunway = runway;

        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(200);

        var title = new Text("View Selection");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        VBox.setMargin(title, new Insets(10, 0, 10, 0));

        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(addButtons());

        this.getChildren().addAll(title, buttons);
    }

    private Stage secondaryStage;

    public void setSecondaryStage(Stage stage) {
        this.secondaryStage = stage;
    }


    @Override
    ArrayList<Button> addButtons() {

        Button sideViewButton = new Button();
        styleButton(sideViewButton, MaterialDesign.MDI_LOGIN, "Side");
        sideViewButton.setOnAction(e -> app.display2DsideViewScene(currentRunway));

        Button topDownViewButton = new Button();
        styleButton(topDownViewButton, MaterialDesign.MDI_LOGIN, "Aerial");
        topDownViewButton.setOnAction(e -> app.display2DtopDownViewScene(currentRunway));

        Button bothViewButton = new Button();
        styleButton(bothViewButton, MaterialDesign.MDI_LOGIN, "Both");
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
