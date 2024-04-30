package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.ParallelRunways;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewSelectionScene extends BaseScene {
    private final Runway currentRunway;
    private final ParallelRunways runwayManager;
    public ViewSelectionScene(MainApplication app, ParallelRunways runwayManager) {
        this.app = app;
        this.currentRunway = runwayManager.getFstRunway();
        this.runwayManager = runwayManager;
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1200.0,1200.0);
        if (MainApplication.isDarkMode()) {
            borderPane.setStyle("-fx-background-color: #121212;");
        } else {
            borderPane.setBackground(Background.fill(Color.rgb(201,233,246)));
        }


        Text title = new Text("Visualisations");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        title.setStroke(Color.WHITE);
        VBox.setMargin(title, new Insets(10, 0, 10, 0));

        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(addButtons());

        VBox topLayout = new VBox();
        topLayout.setAlignment(Pos.TOP_CENTER);
        topLayout.getChildren().addAll(title, buttons);
        BorderPane.setMargin(topLayout, new Insets(10));

        borderPane.setTop(topLayout);


        this.getChildren().add(borderPane);
    }

    private Stage secondaryStage;

    public void setSecondaryStage(Stage stage) {
        this.secondaryStage = stage;
    }


    @Override
    ArrayList<Button> addButtons() {

        Button sideViewButton = new Button();
        styleButton(sideViewButton, MaterialDesign.MDI_LOGIN, "Side");
        sideViewButton.setOnAction(e -> {
            app.display2DsideViewScene(runwayManager);
            app.logOperation("viewed 2D side view of Runway " + runwayManager.getFstRunway().getNameDirection()
                + runwayManager.getSndRunway().getNameDirection());
        });

        Button topDownViewButton = new Button();
        styleButton(topDownViewButton, MaterialDesign.MDI_LOGIN, "Aerial");
        topDownViewButton.setOnAction(e -> {
            app.display2DtopDownViewScene(runwayManager, false);
            app.logOperation("viewed 2D top-down view of Runway " + runwayManager.getFstRunway().getNameDirection()
                + runwayManager.getSndRunway().getNameDirection());
        });

        Button bothViewButton = new Button();
        styleButton(bothViewButton, MaterialDesign.MDI_LOGIN, "Both");
        bothViewButton.setOnAction(e -> {
            app.display2DbothViewScene(runwayManager);
            app.logOperation("viewed BOTH 2D side AND top-down view of Runway " + runwayManager.getFstRunway().getNameDirection()
                + runwayManager.getSndRunway().getNameDirection());
        });

        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_CLOSE, "Close");
        backButton.setOnAction(e -> {
            if (secondaryStage != null) {
                secondaryStage.close();
            }
        });

        return new ArrayList<>(Arrays.asList(sideViewButton, topDownViewButton, bothViewButton, backButton));
    }
}