package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.ParallelRunways;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;
import java.util.List;

public class BothViewScene extends BaseScene {

    private final ParallelRunways runwayManager;
    private final Runway currentRunway;
    private final ArrayList<Obstacle> obstacles;

    public BothViewScene(MainApplication app, ParallelRunways runwayManager) {
        this.app = app;
        this.currentRunway = runwayManager.getFstRunway();
        this.runwayManager = runwayManager;
        this.obstacles = currentRunway.getObstacles();
        BorderPane borderPane = new BorderPane();
        if (MainApplication.isDarkMode()) {
            borderPane.setStyle("-fx-background-color: #121212;");
        } else {
            borderPane.setBackground(Background.fill(Color.rgb(201,233,246)));
        }

        Text title = new Text("Both Views");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        title.setStroke(Color.WHITE);
        VBox.setMargin(title, new Insets(10, 0, 10, 0));

        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(addButtons());

        VBox distanceInfoBox = new VBox();

        if (obstacles.isEmpty()) {
            distanceInfoBox.setAlignment(Pos.TOP_CENTER); // Align the box in the center, below the button
            distanceInfoBox.setPadding(new Insets(5)); // Padding around the box
            distanceInfoBox.setSpacing(1); // Spacing between labels
            Label toraLabel = new Label("TORA: " + currentRunway.getTORA() + "m");
            toraLabel.setTextFill(Color.LIGHTPINK);
            Label todaLabel = new Label("TODA: " + currentRunway.getTODA() + "m");
            todaLabel.setTextFill(Color.YELLOW);
            Label asdaLabel = new Label("ASDA: " + currentRunway.getASDA() + "m");
            asdaLabel.setTextFill(Color.LIGHTGREEN);
            Label ldaLabel = new Label("LDA: " + currentRunway.getLDA() + "m");
            ldaLabel.setTextFill(Color.BLUE);
            distanceInfoBox.getChildren().addAll(toraLabel, todaLabel, asdaLabel, ldaLabel);
//            if (MainApplication.isDarkMode()) {
//                //set text color to white
//                toraLabel.setTextFill(Color.WHITE);
//                todaLabel.setTextFill(Color.WHITE);
//                asdaLabel.setTextFill(Color.WHITE);
//                ldaLabel.setTextFill(Color.WHITE);
//        }
        } else {
            distanceInfoBox.setAlignment(Pos.TOP_CENTER); // Align the box in the center, below the button
            distanceInfoBox.setPadding(new Insets(5)); // Padding around the box
            distanceInfoBox.setSpacing(1); // Spacing between labels
            Label toraLabel = new Label("TORA: " + currentRunway.getNewTORA() + "m");
            toraLabel.setTextFill(Color.LIGHTPINK);
            Label todaLabel = new Label("TODA: " + currentRunway.getNewTODA() + "m");
            todaLabel.setTextFill(Color.YELLOW);
            Label asdaLabel = new Label("ASDA: " + currentRunway.getNewASDA() + "m");
            asdaLabel.setTextFill(Color.LIGHTGREEN);
            Label ldaLabel = new Label("LDA: " + currentRunway.getNewLDA() + "m");
            ldaLabel.setTextFill(Color.BLUE);
            distanceInfoBox.getChildren().addAll(toraLabel, todaLabel, asdaLabel, ldaLabel);
//            if (MainApplication.isDarkMode()) {
//                //set text color to white
//                toraLabel.setTextFill(Color.WHITE);
//                todaLabel.setTextFill(Color.WHITE);
//                asdaLabel.setTextFill(Color.WHITE);
//                ldaLabel.setTextFill(Color.WHITE);
//            }
        }

        VBox topLayout = new VBox();
        topLayout.setAlignment(Pos.TOP_CENTER);
        topLayout.getChildren().addAll(title, buttons,distanceInfoBox);
        BorderPane.setMargin(topLayout, new Insets(10));

        borderPane.setTop(topLayout);

        SplitPane viewSplitPane = new SplitPane();
        viewSplitPane.setOrientation(Orientation.VERTICAL);


        Canvas topViewCanvas = new Canvas(800, 300);
        new TopDownScene(app, runwayManager).drawRunway(topViewCanvas);
        Canvas sideViewCanvas = new Canvas(800, 300);
        new SideViewScene(app, runwayManager).drawRunway(sideViewCanvas);

        viewSplitPane.getItems().addAll(topViewCanvas, sideViewCanvas);

        borderPane.setCenter(viewSplitPane);

        this.getChildren().add(borderPane);
    }

    private Stage secondaryStage;

    public void setSecondaryStage(Stage stage) {
        this.secondaryStage = stage;
    }

    @Override
    ArrayList<Button> addButtons() {
        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        backButton.setOnAction(e -> {
            if (secondaryStage != null) {
                ViewSelectionScene viewSelectionScene = new ViewSelectionScene(app, runwayManager);
                Scene viewScene = new Scene(viewSelectionScene, 300, 600);
                secondaryStage.setScene(viewScene);
                secondaryStage.show();
                viewSelectionScene.setSecondaryStage(secondaryStage);
            }
        });


        return new ArrayList<>(List.of(backButton));
    }
}
