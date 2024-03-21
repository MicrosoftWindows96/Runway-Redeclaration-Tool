package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class TopDownScene extends BaseScene {

    private final ParallelRunways runwayManager;
    private final Runway currentRunway;
    private double TORA;
    private double TODA;
    private double ASDA ;
    private double LDA ;
    private final ArrayList<Obstacle> obstacles;
    private Obstacle obstacle;
    private double RESA;
    private double slopeDistance;
    private double RESADistance;

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
        borderPane.setPrefSize(1200.0,1200.0);
        if (MainApplication.isDarkMode()) {
            borderPane.setStyle("-fx-background-color: #121212;");
        } else {
            borderPane.setBackground(Background.fill(Color.rgb(201, 233, 246)));
        }


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
            distanceInfoBox.setAlignment(Pos.TOP_CENTER); // Align the box in the center, below the button
            distanceInfoBox.setPadding(new Insets(5)); // Padding around the box
            distanceInfoBox.setSpacing(1); // Spacing between labels
            Label toraLabel = new Label("TORA: " + currentRunway.getTORA() + "m");
            Label todaLabel = new Label("TODA: " + currentRunway.getTODA() + "m");
            Label asdaLabel = new Label("ASDA: " + currentRunway.getASDA() + "m");
            Label ldaLabel = new Label("LDA: " + currentRunway.getLDA() + "m");
            distanceInfoBox.getChildren().addAll(toraLabel, todaLabel, asdaLabel, ldaLabel);
            if (MainApplication.isDarkMode()) {
                //set text color to white
                toraLabel.setTextFill(Color.WHITE);
                todaLabel.setTextFill(Color.WHITE);
                asdaLabel.setTextFill(Color.WHITE);
                ldaLabel.setTextFill(Color.WHITE);
            }
        } else {
            distanceInfoBox.setAlignment(Pos.TOP_CENTER); // Align the box in the center, below the button
            distanceInfoBox.setPadding(new Insets(5)); // Padding around the box
            distanceInfoBox.setSpacing(1); // Spacing between labels
            Label toraLabel = new Label("TORA: " + currentRunway.getNewTORA() + "m");
            Label todaLabel = new Label("TODA: " + currentRunway.getNewTODA() + "m");
            Label asdaLabel = new Label("ASDA: " + currentRunway.getNewASDA() + "m");
            Label ldaLabel = new Label("LDA: " + currentRunway.getNewLDA() + "m");
            distanceInfoBox.getChildren().addAll(toraLabel, todaLabel, asdaLabel, ldaLabel);
            if (MainApplication.isDarkMode()) {
                //set text color to white
                toraLabel.setTextFill(Color.WHITE);
                todaLabel.setTextFill(Color.WHITE);
                asdaLabel.setTextFill(Color.WHITE);
                ldaLabel.setTextFill(Color.WHITE);
            }
        }


        VBox topLayout = new VBox();
        topLayout.setAlignment(Pos.TOP_CENTER);
        topLayout.getChildren().addAll(title, buttons, distanceInfoBox);
        BorderPane.setMargin(topLayout, new Insets(10));

        borderPane.setTop(topLayout);

        Canvas runwayCanvas = new Canvas(800, 600);
        drawRunway(runwayCanvas);

        borderPane.setCenter(runwayCanvas);
        this.getChildren().add(borderPane);
    }

    public void drawRunway(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double centerLineY = canvas.getHeight() / 2;
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

        for (double x = runwayStartX+5; x < runwayEndX; x += dashLength + spaceLength) {
            // Check if the dash extends beyond the runway length
            double dashEndX = x + dashLength - 5;
            if (dashEndX > runwayEndX) {
                // Adjust the length of the dash so it does not extend beyond the runway
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

        // b. Draw threshold indicators
        if (!obstacles.isEmpty()) {
            Obstacle obstacle = obstacles.getFirst();
            double fromCentreline = (double) obstacle.getDistanceFromCentreline() /6;
            double obstacleX = runwayStartX + ((double) obstacle.getDistanceFromThreshold() / 6); // X position of the obstacle
            double obstacleHeight = (double) obstacle.getHeight();
            double obstacleY = centerLineY + fromCentreline; // Adjust Y position to place on top of the runway
            double obstacleWidth = 10;

            //gc.setFill(Color.BLACK);
            gc.fillRect(obstacleX, obstacleY, obstacleWidth, obstacleHeight);

            String obstacleText = obstacle.getName() + " (" + obstacle.getHeight() + "m)";
            gc.fillText(obstacleText, obstacleX, runwayStartY - runwayHeight - 5);

            // gc.setStroke(Color.BLACK);
            // gc.strokeLine(runwayStartX, runwayStartY + 20, runwayStartX + TORA, runwayStartY + 20); // slope line


            this.TORA = (double) currentRunway.getNewTORA() / 6;
            this.TODA = (double) currentRunway.getNewTODA() / 6;
            this.ASDA = (double) currentRunway.getNewASDA() / 6;
            this.LDA = (double) currentRunway.getNewLDA() / 6;

            if (obstacle.getDistanceFromThreshold() < (1000 /6)) {
                this.slopeDistance = obstacleX + ((double) obstacle.getHeight() /6 * 50);

                // RESA
                gc.setFill(Color.ORANGE);
                gc.fillRect(obstacleX + obstacleWidth , runwayStartY, this.RESA, runwayHeight);

                // Blast (currently 0)
                // gc.setFill(Color.BLUE);
                // gc.fillRect(obstacleX + obstacleWidth + ((double) currentRunway.getBlastProtectionValue() /6), runwayStartY, 2, runwayHeight);

                this.RESADistance = obstacleX + obstacleWidth + this.RESA;

                gc.setStroke(Color.LIGHTPINK);
                gc.strokeLine(RESADistance, centerLineY - 20 + 40, RESADistance + this.TORA, centerLineY - 20 + 40); // TORA line
                gc.setStroke(Color.YELLOW);
                gc.strokeLine(RESADistance, centerLineY - 20 + 55, RESADistance + this.TODA, centerLineY - 20 + 55); // TODA line
                gc.setStroke(Color.LIGHTGREEN);
                gc.strokeLine(RESADistance, centerLineY - 20 + 70, RESADistance + this.ASDA, centerLineY - 20 + 70); // ASDA line
                gc.setStroke(Color.BLUE);
                gc.strokeLine(slopeDistance, centerLineY - 20 + 85, slopeDistance + this.LDA, centerLineY - 20 + 85); // LDA line


            } else if (obstacle.getDistanceFromThreshold() >= (1000 /6)) {
                // Original opposite diagonal line points
                double oppositeStartX = obstacleX + obstacleWidth - ((double) obstacle.getHeight() /6 * 50 );
                double oppositeStartY = obstacleY + obstacleHeight;
                double oppositeEndX = obstacleX;
                double oppositeEndY = obstacleY;

// Calculate slope of the line
                double slope = (oppositeEndY - oppositeStartY) / (oppositeEndX - oppositeStartX);

// Length to extend the line
                double extensionLength = 30;

// Calculate new ending point
                double newEndX = oppositeEndX + (extensionLength / Math.sqrt(1 + slope * slope)); // Note the '+' here

                this.slopeDistance = oppositeStartX;

                // RESA
                gc.setFill(Color.ORANGE);
                gc.fillRect(obstacleX - this.RESA, runwayStartY, this.RESA, runwayHeight);

                // Blast (currently is 0)
//                gc.setFill(Color.BLUE);
//                gc.fillRect(obstacleX - ((double) currentRunway.getBlastProtectionValue() /6), runwayStartY, 2, runwayHeight);

                gc.setStroke(Color.LIGHTPINK);
                gc.strokeLine(runwayStartX, centerLineY - 20 + 40, slopeDistance -10, centerLineY - 20 + 40); // TORA line
                gc.setStroke(Color.YELLOW);
                gc.strokeLine(runwayStartX, centerLineY - 20 + 55, slopeDistance -10, centerLineY - 20 + 55); // TODA line
                gc.setStroke(Color.LIGHTGREEN);
                gc.strokeLine(runwayStartX, centerLineY - 20 + 70, slopeDistance -10, centerLineY - 20 + 70); // ASDA line
                gc.setStroke(Color.BLUE);
                gc.strokeLine(runwayStartX + displacedThresholdOffset, centerLineY - 20 + 85, runwayStartX + displacedThresholdOffset + this.LDA - ((double) (currentRunway.getRESA()+60) /6), centerLineY - 20 + 85); // LDA line

            }

        }else {
            gc.setStroke(Color.LIGHTPINK);
            gc.strokeLine(runwayStartX, centerLineY - 20 + 40, runwayStartX + this.TORA, centerLineY - 20 + 40); // TORA line
            gc.setStroke(Color.YELLOW);
            gc.strokeLine(runwayStartX, centerLineY - 20 + 55, runwayStartX + this.TODA, centerLineY - 20 + 55); // TODA line
            gc.setStroke(Color.LIGHTGREEN);
            gc.strokeLine(runwayStartX, centerLineY - 20 + 70, runwayStartX + this.ASDA, centerLineY - 20 + 70); // ASDA line
            gc.setStroke(Color.BLUE);
            gc.strokeLine(runwayStartX + displacedThresholdOffset, centerLineY - 20 + 85, runwayStartX +displacedThresholdOffset + this.LDA, centerLineY - 20 + 85); // LDA line

        }

        if (displacedThresholdOffset > 0) {
            gc.setFill(Color.RED);
            gc.fillRect(runwayStartX + displacedThresholdOffset, runwayStartY, thresholdWidth, runwayHeight);
        }

        //gc.setFill(Color.BLACK);
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
        gc.fillText("← Take-off/Landing", runwayStartX + runwayLength - stringWidth -100, centerLineY - 40);

        drawCompass(canvas, calculateBearingFromRunwayName(currentRunway.getName()));
    }

    private int calculateBearingFromRunwayName(String runwayName) {
        try {
            String numericPart = runwayName.replaceAll("\\D+", "");
            return Integer.parseInt(numericPart) * 10;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void drawCompass(Canvas canvas, int bearing) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double radius = 40;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval((double) 700 - radius, (double) 50 - radius, radius * 2, radius * 2);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillOval((double) 700 - radius, (double) 50 - radius, radius * 2, radius * 2);

        Font oldFont = gc.getFont();
        gc.setFont(new Font("Arial", 12));
        gc.setFill(Color.BLACK);
        gc.fillText("N", (double) 700 - 5, (double) 50 - radius + 15);
        gc.fillText("S", (double) 700 - 5, (double) 50 + radius - 5);
        gc.fillText("E", (double) 700 + radius - 15, (double) 50 + 5);
        gc.fillText("W", (double) 700 - radius + 5, (double) 50 + 5);

        gc.setFont(oldFont);

        double endX = (double) 700 + radius * 0.8 * Math.sin(Math.toRadians(bearing));
        double endY = (double) 50 - radius * 0.8 * Math.cos(Math.toRadians(bearing));
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeLine(700, 50, endX, endY);

        gc.setFill(Color.RED);
        gc.fillOval((double) 700 - 3, (double) 50 - 3, 6, 6);

        gc.setFill(Color.BLACK);
        gc.fillText(bearing + "°", (double) 700 - 10, (double) 50 + radius + 25);
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
