package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
            distanceInfoBox.setAlignment(Pos.TOP_CENTER);
            distanceInfoBox.setPadding(new Insets(5));
            distanceInfoBox.setSpacing(1);

            Label toraLabel = new Label("TORA: " + currentRunway.getTORA() + "m");
            toraLabel.setStyle("-fx-background-color: #FFC0CB;");

            Label todaLabel = new Label("TODA: " + currentRunway.getTODA() + "m");
            todaLabel.setStyle("-fx-background-color: #FFFF00;");

            Label asdaLabel = new Label("ASDA: " + currentRunway.getASDA() + "m");
            asdaLabel.setStyle("-fx-background-color: #90EE90;");

            Label ldaLabel = new Label("LDA: " + currentRunway.getLDA() + "m");
            ldaLabel.setStyle("-fx-background-color: #ADD8E6;");

            distanceInfoBox.getChildren().addAll(toraLabel, todaLabel, asdaLabel, ldaLabel);
        } else {
            distanceInfoBox.setAlignment(Pos.TOP_CENTER);
            distanceInfoBox.setPadding(new Insets(5));
            distanceInfoBox.setSpacing(1);

            Label toraLabel = new Label("TORA: " + currentRunway.getTORA() + "m  |  " + currentRunway.getNewTORA() + "m");
            toraLabel.setStyle("-fx-background-color: #FFC0CB;");

            Label todaLabel = new Label("TODA: " + currentRunway.getTODA() + "m  |  " + currentRunway.getNewTODA() + "m");
            todaLabel.setStyle("-fx-background-color: #FFFF00;");

            Label asdaLabel = new Label("ASDA: " + currentRunway.getASDA() + "m  |  " + currentRunway.getNewASDA() + "m");
            asdaLabel.setStyle("-fx-background-color: #90EE90;");

            Label ldaLabel = new Label("LDA: " + currentRunway.getLDA() + "m  |  " + currentRunway.getNewLDA() + "m");
            ldaLabel.setStyle("-fx-background-color: #ADD8E6;");

            distanceInfoBox.getChildren().addAll(toraLabel, todaLabel, asdaLabel, ldaLabel);
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

        drawCompass(compassCanvas, calculateBearingFromRunwayName(currentRunway.getName()));
        compassCanvas.relocate(20, 20); // Set the position of the compass canvas within the pane
        compassPane.relocate(600, 30); // Set the position of the compass pane within the root layout
        borderPane.getChildren().add(compassPane); // Add the compass pane to the borderPane

        this.getChildren().add(borderPane);
    }

    private int calculateBearingFromRunwayName(String runwayName) {
        try {
            String numericPart = runwayName.replaceAll("\\D+", "");
            return Integer.parseInt(numericPart) * 10;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Canvas compassCanvas = new Canvas(150, 150);
    private Pane compassPane = new Pane(compassCanvas);

    private void drawCompass(Canvas canvas, int bearing) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double radius = 40;
        double compassX = canvas.getWidth() - radius * 2 - 10; // Position the compass 10 units from the right edge
        double compassY = radius + 10; // Position the compass 10 units below the top edge

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(compassX, compassY, radius * 2, radius * 2);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillOval(compassX, compassY, radius * 2, radius * 2);

        Font oldFont = gc.getFont();
        gc.setFont(new Font("Arial", 12));
        gc.setFill(Color.BLACK);
        gc.fillText("N", compassX + radius - 5, compassY + 15);
        gc.fillText("S", compassX + radius - 5, compassY + radius * 2 - 5);
        gc.fillText("E", compassX + radius * 2 - 15, compassY + radius + 5);
        gc.fillText("W", compassX + 5, compassY + radius + 5);

        gc.setFont(oldFont);

        double endX = compassX + radius + radius * 0.8 * Math.sin(Math.toRadians(bearing));
        double endY = compassY + radius - radius * 0.8 * Math.cos(Math.toRadians(bearing));
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeLine(compassX + radius, compassY + radius, endX, endY);

        gc.setFill(Color.RED);
        gc.fillOval(compassX + radius - 3, compassY + radius - 3, 6, 6);

        gc.setFill(Color.BLACK);
        gc.fillText(bearing + "°", compassX + radius - 10, compassY + radius);
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
