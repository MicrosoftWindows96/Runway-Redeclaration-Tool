package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.ParallelRunways;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TopDownScene extends BaseScene {

    private final ParallelRunways runwayManager;
    private final Runway currentRunway;
    private double TORA;
    private double TODA;
    private double ASDA ;
    private double LDA ;
    private final ArrayList<Obstacle> obstacles;
    private Obstacle obstacle;
    private final double RESA;
    private double slopeDistance;
    private double RESADistance;
    private final Pane cloudLayer = new Pane();
    private final double iconSize = 24;
    private final double speed = 1.0;
    private final Random random = new Random();
    private boolean isRotated = false;

    public TopDownScene(MainApplication app, ParallelRunways runwayManager) {
        this.app = app;
        this.currentRunway = runwayManager.getFstRunway();
        this.runwayManager = runwayManager;
        this.obstacles = currentRunway.getObstacles();
        this.RESA = (double) 240 /6;
        if (!obstacles.isEmpty()) {
            this.obstacle = obstacles.getFirst();
        }
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(800.0,600.0);
        if (MainApplication.isDarkMode()) {
            borderPane.setStyle("-fx-background-color: #121212;");
        } else {
            borderPane.setBackground(Background.fill(Color.rgb(201, 233, 246)));
        }

        cloudLayer.setPrefSize(800, 50);
        createPattern();
        animatePattern();


        Text title = new Text("Aerial View");
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
        topLayout.getChildren().addAll(cloudLayer, title, buttons, distanceInfoBox);
        BorderPane.setMargin(topLayout, new Insets(10));

        borderPane.setTop(topLayout);

        Canvas runwayCanvas = new Canvas(800, 600);
        drawRunway(runwayCanvas);
        //drawCompass(runwayCanvas, calculateBearingFromRunwayName(currentRunway.getName()));

        borderPane.setCenter(runwayCanvas);

        drawCompass(compassCanvas, calculateBearingFromRunwayName(currentRunway.getName()));
        compassCanvas.relocate(20, 20); // Set the position of the compass canvas within the pane
        compassPane.relocate(600, 30); // Set the position of the compass pane within the root layout
        borderPane.getChildren().add(compassPane); // Add the compass pane to the borderPane

        this.getChildren().add(borderPane);
    }


    private void createPattern() {
        cloudLayer.getChildren().clear();

        double areaWidth = cloudLayer.getPrefWidth();
        double areaHeight = cloudLayer.getPrefHeight();
        int maxCloudSize = 48;
        int minCloudSize = 24;

        int numberOfClouds = 50;

        for (int i = 0; i < numberOfClouds; i++) {
            double x = random.nextDouble() * areaWidth;
            double y = random.nextDouble() * areaHeight;

            int size = random.nextInt(maxCloudSize - minCloudSize + 1) + minCloudSize;

            var cloud = createCloudIcon(x, y, size);
            cloudLayer.getChildren().add(cloud);
        }
    }

    private FontIcon createCloudIcon(double x, double y, int size) {
        FontIcon cloudIcon = FontIcon.of(MaterialDesign.MDI_CLOUD, size);
        cloudIcon.setFill(javafx.scene.paint.Color.WHITE);
        cloudIcon.setLayoutX(x - size / 2.0);
        cloudIcon.setLayoutY(y - size / 2.0);
        return cloudIcon;
    }

    private void animatePattern() {
        Timeline cloudTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            for (var node : cloudLayer.getChildren()) {
                if (node instanceof FontIcon icon && ((FontIcon) node).getIconCode() == MaterialDesign.MDI_CLOUD) {
                    icon.setLayoutX(icon.getLayoutX() + speed);
                    if (icon.getLayoutX() > cloudLayer.getPrefWidth()) {
                        icon.setLayoutX(-iconSize);
                        icon.setLayoutY(random.nextDouble() * cloudLayer.getPrefHeight());
                    }
                }
            }
        }));
        cloudTimeline.setCycleCount(Timeline.INDEFINITE);
        cloudTimeline.play();
    }

    public void drawRunway(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double centerLineY = canvas.getHeight() / 2 - 50;
        double runwayLength = (double) currentRunway.getTORA() / 6;
        double runwayStartX = 100;
        double runwayStartY = centerLineY - 20;
        double runwayHeight = 40;

        double thresholdWidth = 5;
        double displacedThresholdOffset = (double) currentRunway.getDisplacedThreshold() / 6;
        double stopwayWidth = (double) currentRunway.getStopway() / 6;
        double clearwayWidth = (double) currentRunway.getClearway() / 6;

        this.TORA = (double) currentRunway.getTORA() / 6;
        this.TODA = (double) currentRunway.getTODA() / 6;
        this.ASDA = (double) currentRunway.getASDA() / 6;
        this.LDA = (double) currentRunway.getLDA() / 6;

        gc.setFill(Color.GRAY);
        gc.fillRect(runwayStartX, centerLineY - 20, runwayLength, 40);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        double dashLength = 15;
        double spaceLength = 10;
        double runwayEndX = runwayStartX + runwayLength;

        for (double x = runwayStartX + 5; x < runwayEndX; x += dashLength + spaceLength) {
            double dashEndX = x + dashLength - 5;
            if (dashEndX > runwayEndX) {
                dashEndX = runwayEndX - 5;
            }
            gc.strokeLine(x, centerLineY, dashEndX, centerLineY);
        }

        gc.setFill(Color.WHITE);
        gc.fillRect(runwayStartX, centerLineY - 20, thresholdWidth, 40);
        gc.fillRect(runwayStartX + runwayLength - thresholdWidth, centerLineY - 20, thresholdWidth, 40);

        // e. Draw Stopway/Clearway
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(runwayStartX - stopwayWidth, runwayStartY, stopwayWidth, runwayHeight); // Stopway left
        gc.fillRect(runwayStartX + stopwayWidth, runwayStartY, stopwayWidth, runwayHeight); // Stopway right

        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(runwayStartX - stopwayWidth - clearwayWidth, runwayStartY, clearwayWidth, runwayHeight); // Clearway left
        gc.fillRect(runwayStartX + runwayLength + stopwayWidth, runwayStartY, clearwayWidth, runwayHeight); // Clearway right

        Font labelFont = Font.font("Arial", 14);
        gc.setFont(labelFont);
        if (MainApplication.isDarkMode()) {
            gc.setFill(Color.WHITE);
        } else {
            gc.setFill(Color.BLACK);
        }

        if (!obstacles.isEmpty()) {
            Obstacle obstacle = obstacles.getFirst();
            double fromCentreline = (double) obstacle.getDistanceFromCentreline() / 6;
            double obstacleX = runwayStartX + ((double) obstacle.getDistanceFromThreshold() / 6);
            double obstacleHeight = obstacle.getHeight();
            double obstacleY = centerLineY + fromCentreline;
            double obstacleWidth = 10;

            gc.fillRect(obstacleX, obstacleY, obstacleWidth, obstacleHeight);

            String obstacleText = obstacle.getName() + " (" + obstacle.getHeight() + "m)";
            gc.fillText(obstacleText, obstacleX, runwayStartY - runwayHeight + 100);

            this.TORA = (double) currentRunway.getNewTORA() / 6;
            this.TODA = (double) currentRunway.getNewTODA() / 6;
            this.ASDA = (double) currentRunway.getNewASDA() / 6;
            this.LDA = (double) currentRunway.getNewLDA() / 6;

            if (obstacle.getDistanceFromThreshold() < (1000 / 6)) {
                this.slopeDistance = obstacleX + ((double) obstacle.getHeight() / 6 * 50);

                gc.setFill(Color.ORANGE);
                gc.fillRect(obstacleX + obstacleWidth, runwayStartY, this.RESA, runwayHeight);

                this.RESADistance = obstacleX + obstacleWidth + this.RESA;
            } else if (obstacle.getDistanceFromThreshold() >= (1000 / 6)) {
                double oppositeStartX = obstacleX + obstacleWidth - ((double) obstacle.getHeight() / 6 * 50);
                double oppositeStartY = obstacleY + obstacleHeight;
                double oppositeEndX = obstacleX;
                double oppositeEndY = obstacleY;

                double slope = (oppositeEndY - oppositeStartY) / (oppositeEndX - oppositeStartX);

                double extensionLength = 30;

                double newEndX = oppositeEndX + (extensionLength / Math.sqrt(1 + slope * slope));

                this.slopeDistance = oppositeStartX;

                gc.setFill(Color.ORANGE);
                gc.fillRect(obstacleX - this.RESA, runwayStartY, this.RESA, runwayHeight);
            }
        }

        double lineOffsetY = 40;
        double lineSpacing = 15;

        if (currentRunway.getDirection().equals("R")) {
            gc.setStroke(Color.LIGHTPINK);
            gc.strokeLine(runwayEndX - this.TORA, centerLineY - 20 + lineOffsetY, runwayEndX, centerLineY - 20 + lineOffsetY); // TORA line
            gc.setStroke(Color.YELLOW);
            gc.strokeLine(runwayEndX - this.TODA, centerLineY - 20 + lineOffsetY + lineSpacing, runwayEndX, centerLineY - 20 + lineOffsetY + lineSpacing); // TODA line
            gc.setStroke(Color.LIGHTGREEN);
            gc.strokeLine(runwayEndX - this.ASDA, centerLineY - 20 + lineOffsetY + lineSpacing * 2, runwayEndX, centerLineY - 20 + lineOffsetY + lineSpacing * 2); // ASDA line
            gc.setStroke(Color.BLUE);
            gc.strokeLine(runwayEndX - this.LDA - displacedThresholdOffset, centerLineY - 20 + lineOffsetY + lineSpacing * 3, runwayEndX - displacedThresholdOffset, centerLineY - 20 + lineOffsetY + lineSpacing * 3); // LDA line
        } else {
            gc.setStroke(Color.LIGHTPINK);
            gc.strokeLine(runwayStartX, centerLineY - 20 + lineOffsetY, runwayStartX + this.TORA, centerLineY - 20 + lineOffsetY); // TORA line
            gc.setStroke(Color.YELLOW);
            gc.strokeLine(runwayStartX, centerLineY - 20 + lineOffsetY + lineSpacing, runwayStartX + this.TODA, centerLineY - 20 + lineOffsetY + lineSpacing); // TODA line
            gc.setStroke(Color.LIGHTGREEN);
            gc.strokeLine(runwayStartX, centerLineY - 20 + lineOffsetY + lineSpacing * 2, runwayStartX + this.ASDA, centerLineY - 20 + lineOffsetY + lineSpacing * 2); // ASDA line
            gc.setStroke(Color.BLUE);
            gc.strokeLine(runwayStartX + displacedThresholdOffset, centerLineY - 20 + lineOffsetY + lineSpacing * 3, runwayStartX + displacedThresholdOffset + this.LDA, centerLineY - 20 + lineOffsetY + lineSpacing * 3); // LDA line
        }

        if (displacedThresholdOffset > 0) {
            gc.setFill(Color.RED);
            gc.fillRect(runwayStartX + displacedThresholdOffset, runwayStartY, thresholdWidth, runwayHeight);
        }

        if (MainApplication.isDarkMode()) {
            gc.setFill(Color.WHITE);
        } else {
            gc.setFill(Color.BLACK);
        }

        java.awt.FontMetrics metrics = java.awt.Toolkit.getDefaultToolkit().getFontMetrics(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));

        String leftRunwayName = runwayManager.getDegree1() + runwayManager.getFstRunway().getDirection();
        gc.fillText(leftRunwayName, runwayStartX, centerLineY - 25);
        gc.fillText("Take-off/Landing →", runwayStartX, centerLineY - 40);

        String rightRunwayName = runwayManager.getDegree2() + runwayManager.getSndRunway().getDirection();
        int stringWidth = metrics.stringWidth(rightRunwayName);
        gc.fillText(rightRunwayName, runwayStartX + runwayLength - stringWidth, centerLineY - 25);
        gc.fillText("← Take-off/Landing", runwayStartX + runwayLength - stringWidth - 100, centerLineY - 40);
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

    private void rotateRunway(int bearing) {
        // Get the canvas and its graphics context
        Canvas runwayCanvas = (Canvas) ((BorderPane) getChildren().get(0)).getCenter();
        GraphicsContext gc = runwayCanvas.getGraphicsContext2D();

        // Save the current transform state
        gc.save();

        // Translate to the center of the canvas
        double centerX = runwayCanvas.getWidth() / 2;
        double centerY = runwayCanvas.getHeight() / 2;
        gc.translate(centerX, centerY);

        // Rotate the canvas by the desired angle if not rotated, or reset if rotated
        double rotationAngle = isRotated ? 0 : bearing - 90; // Adjust the angle based on your requirements
        gc.rotate(Math.toRadians(rotationAngle));

        // Translate back to the original position
        gc.translate(-centerX, -centerY);

        // Draw the runway with the new rotation
        drawRunway(runwayCanvas);

        // Restore the previous transform state
        gc.restore();

        // Toggle the rotation state
        isRotated = !isRotated;
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

        Button rotateButton = new Button();
        styleButton(rotateButton, MaterialDesign.MDI_ROTATE_3D, "Rotate");
        rotateButton.setOnAction(e -> rotateRunway(calculateBearingFromRunwayName(currentRunway.getName())));

        return new ArrayList<>(List.of(backButton, rotateButton));
    }
}
