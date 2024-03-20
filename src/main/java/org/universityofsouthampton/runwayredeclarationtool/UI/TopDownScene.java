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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
    private boolean isRotate;

    public TopDownScene(MainApplication app, ParallelRunways runwayManager, boolean isRotate) {
        this.app = app;
        this.currentRunway = runwayManager.getFstRunway();
        this.runwayManager = runwayManager;
        this.isRotate = isRotate;
        this.obstacles = currentRunway.getObstacles();
        this.RESA = (double) 240 /6;
        if (!obstacles.isEmpty()) {
            this.obstacle = obstacles.getFirst();
        }

        Text title = new Text("Aerial View");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        title.setStroke(Color.WHITE);
        VBox.setMargin(title, new Insets(10, 0, 10, 0));

        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        topBox.setSpacing(20);
        topBox.getChildren().addAll(addButtons().get(0), title, addButtons().get(1));

        HBox distanceInfoBox = new HBox();

        if (obstacles.isEmpty()) {
            distanceInfoBox.setAlignment(Pos.TOP_CENTER); // Align the box in the center, below the button
            distanceInfoBox.setPadding(new Insets(5)); // Padding around the box
            distanceInfoBox.setSpacing(10); // Spacing between labels
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
            distanceInfoBox.setSpacing(10); // Spacing between labels
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
        topLayout.setPadding(new Insets(20));
        topLayout.setAlignment(Pos.TOP_CENTER);
        topLayout.getChildren().addAll(topBox, distanceInfoBox);

        Canvas grassCanvas = new Canvas(800, 800);
        Canvas runwayCanvas = new Canvas(800,800);
        drawRunway(grassCanvas,runwayCanvas);

        StackPane layers = new StackPane(grassCanvas,runwayCanvas);
        layers.setAlignment(Pos.TOP_CENTER);

        StackPane root = new StackPane();
        root.getChildren().addAll(layers,topLayout);

        this.getChildren().add(root);
    }

    public void autoRotate(Canvas runwayCanvas, GraphicsContext gc, double rotation){
        double centerX = runwayCanvas.getWidth() / 2;
        double centerY = runwayCanvas.getHeight() / 2;

        gc.save(); // Save the current state of the graphics context
        gc.translate(centerX, centerY); // Translate to the center
        gc.rotate(rotation-270); // Rotate around the center
        gc.translate(-centerX, -centerY); // Translate back to the original position

    }

    public void drawRunway(Canvas grassCanvas, Canvas runwayCanvas) {
        GraphicsContext gc1 = grassCanvas.getGraphicsContext2D();
        gc1.setFill(Color.GREEN);
        gc1.fillRect(0, 0, grassCanvas.getWidth(), grassCanvas.getHeight());

        GraphicsContext gc2 = runwayCanvas.getGraphicsContext2D();

        if (isRotate) {
            autoRotate(runwayCanvas, gc2,runwayManager.getIntDegree(currentRunway.getName())*10);
        }

        double centerLineY = runwayCanvas.getHeight() / 2;
        double runwayLength = (double) currentRunway.getTORA() / 6;
        double runwayStartX = (runwayCanvas.getWidth() - runwayLength) / 2;
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

        gc2.setFill(Color.GRAY);
        gc2.fillRect(runwayStartX, centerLineY - 20, runwayLength, 40);

        gc2.setStroke(Color.WHITE);
        gc2.setLineWidth(2);
        double dashLength = 15;
        double spaceLength = 10;
        double runwayEndX = runwayStartX + runwayLength;

        for (double x = runwayStartX; x < runwayEndX; x += dashLength + spaceLength) {
            // Check if the dash extends beyond the runway length
            double dashEndX = x + dashLength;
            if (dashEndX > runwayEndX) {
                // Adjust the length of the dash so it does not extend beyond the runway
                dashEndX = runwayEndX;
            }
            gc2.strokeLine(x, centerLineY, dashEndX, centerLineY);
        }

        gc2.setFill(Color.WHITE);
        gc2.fillRect(runwayStartX, centerLineY - 20, thresholdWidth, 40);
        gc2.fillRect(runwayStartX + runwayLength - thresholdWidth, centerLineY - 20, thresholdWidth, 40);

        // e. Draw Stopway/Clearway
        gc2.setFill(Color.DARKBLUE);
        gc2.fillRect(runwayStartX - stopwayWidth, runwayStartY, stopwayWidth, runwayHeight); // Stopway left
        gc2.fillRect(runwayStartX + stopwayWidth, runwayStartY, stopwayWidth, runwayHeight); // Stopway right

        gc2.setFill(Color.LIGHTBLUE);
        gc2.fillRect(runwayStartX - stopwayWidth - clearwayWidth, runwayStartY, clearwayWidth, runwayHeight); // Clearway left
        gc2.fillRect(runwayStartX + runwayLength + stopwayWidth, runwayStartY, clearwayWidth, runwayHeight); // Clearway right


        Font labelFont = Font.font("Arial", 14);
        gc2.setFont(labelFont);
        if (MainApplication.isDarkMode()) {
            gc2.setFill(Color.WHITE);
        } else {
            gc2.setFill(Color.BLACK);
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
            gc2.fillRect(obstacleX, obstacleY, obstacleWidth, obstacleHeight);

            String obstacleText = obstacle.getName() + " (" + obstacle.getHeight() + "m)";
            gc2.fillText(obstacleText, obstacleX, runwayStartY - runwayHeight - 5);

            // gc.setStroke(Color.BLACK);
            // gc.strokeLine(runwayStartX, runwayStartY + 20, runwayStartX + TORA, runwayStartY + 20); // slope line


            this.TORA = (double) currentRunway.getNewTORA() / 6;
            this.TODA = (double) currentRunway.getNewTODA() / 6;
            this.ASDA = (double) currentRunway.getNewASDA() / 6;
            this.LDA = (double) currentRunway.getNewLDA() / 6;

            if (obstacle.getDistanceFromThreshold() < (1000 /6)) {
                this.slopeDistance = obstacleX + ((double) obstacle.getHeight() /6 * 50);

                // RESA
                gc2.setFill(Color.ORANGE);
                gc2.fillRect(obstacleX + obstacleWidth , runwayStartY, this.RESA, runwayHeight);

                // Blast (currently 0)
                // gc.setFill(Color.BLUE);
                // gc.fillRect(obstacleX + obstacleWidth + ((double) currentRunway.getBlastProtectionValue() /6), runwayStartY, 2, runwayHeight);

                this.RESADistance = obstacleX + obstacleWidth + this.RESA;

                gc2.setStroke(Color.LIGHTPINK);
                gc2.strokeLine(RESADistance, centerLineY - 20 + 40, RESADistance + this.TORA, centerLineY - 20 + 40); // TORA line
                gc2.setStroke(Color.YELLOW);
                gc2.strokeLine(RESADistance, centerLineY - 20 + 55, RESADistance + this.TODA, centerLineY - 20 + 55); // TODA line
                gc2.setStroke(Color.LIGHTGREEN);
                gc2.strokeLine(RESADistance, centerLineY - 20 + 70, RESADistance + this.ASDA, centerLineY - 20 + 70); // ASDA line
                gc2.setStroke(Color.BLUE);
                gc2.strokeLine(slopeDistance, centerLineY - 20 + 85, slopeDistance + this.LDA, centerLineY - 20 + 85); // LDA line


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
                gc2.setFill(Color.ORANGE);
                gc2.fillRect(obstacleX - this.RESA, runwayStartY, this.RESA, runwayHeight);

                // Blast (currently is 0)
//                gc.setFill(Color.BLUE);
//                gc.fillRect(obstacleX - ((double) currentRunway.getBlastProtectionValue() /6), runwayStartY, 2, runwayHeight);

                gc2.setStroke(Color.LIGHTPINK);
                gc2.strokeLine(runwayStartX, centerLineY - 20 + 40, slopeDistance -10, centerLineY - 20 + 40); // TORA line
                gc2.setStroke(Color.YELLOW);
                gc2.strokeLine(runwayStartX, centerLineY - 20 + 55, slopeDistance -10, centerLineY - 20 + 55); // TODA line
                gc2.setStroke(Color.LIGHTGREEN);
                gc2.strokeLine(runwayStartX, centerLineY - 20 + 70, slopeDistance -10, centerLineY - 20 + 70); // ASDA line
                gc2.setStroke(Color.BLUE);
                gc2.strokeLine(runwayStartX + displacedThresholdOffset, centerLineY - 20 + 85, runwayStartX + displacedThresholdOffset + this.LDA - ((double) (currentRunway.getRESA()+60) /6), centerLineY - 20 + 85); // LDA line

            }

        }else {
            gc2.setStroke(Color.LIGHTPINK);
            gc2.strokeLine(runwayStartX, centerLineY - 20 + 40, runwayStartX + this.TORA, centerLineY - 20 + 40); // TORA line
            gc2.setStroke(Color.YELLOW);
            gc2.strokeLine(runwayStartX, centerLineY - 20 + 55, runwayStartX + this.TODA, centerLineY - 20 + 55); // TODA line
            gc2.setStroke(Color.LIGHTGREEN);
            gc2.strokeLine(runwayStartX, centerLineY - 20 + 70, runwayStartX + this.ASDA, centerLineY - 20 + 70); // ASDA line
            gc2.setStroke(Color.BLUE);
            gc2.strokeLine(runwayStartX + displacedThresholdOffset, centerLineY - 20 + 85, runwayStartX +displacedThresholdOffset + this.LDA, centerLineY - 20 + 85); // LDA line

        }

        if (displacedThresholdOffset > 0) {
            gc2.setFill(Color.RED);
            gc2.fillRect(runwayStartX + displacedThresholdOffset, runwayStartY, thresholdWidth, runwayHeight);
        }

        //gc.setFill(Color.BLACK);
        if (MainApplication.isDarkMode()) {
            gc2.setFill(Color.WHITE);
        } else {
            gc2.setFill(Color.BLACK);
        }

        java.awt.FontMetrics metrics = java.awt.Toolkit.getDefaultToolkit().getFontMetrics(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));

        String leftRunwayName = runwayManager.getFstRunway().getName() + runwayManager.getFstRunway().getDirection();
        gc2.fillText(leftRunwayName, runwayStartX, centerLineY - 25);
        gc2.fillText("Take-off/Landing →", runwayStartX, centerLineY - 40); // Adjust text position as needed


        String rightRunwayName = runwayManager.getSndRunway().getName() + runwayManager.getSndRunway().getDirection();
        int stringWidth = metrics.stringWidth(rightRunwayName);
        gc2.fillText(rightRunwayName, runwayStartX + runwayLength - stringWidth, centerLineY - 25);
        gc2.fillText("← Take-off/Landing", runwayStartX + runwayLength - stringWidth -100, centerLineY - 40); // Adjust text position as needed

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

        Button rotateButton = new Button();
        styleButton(rotateButton, MaterialDesign.MDI_RECYCLE, "Rotate");
        rotateButton.setOnAction(e -> {
            app.display2DtopDownViewScene(runwayManager,!isRotate);
        });

        return new ArrayList<>(List.of(backButton,rotateButton));
    }
}
