package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;
import java.util.List;

public class SideViewScene extends BaseScene {

    private final Runway currentRunway; // currently viewed runway
    private final ArrayList<Obstacle> obstacles;

    public SideViewScene(MainApplication app, Runway runway) {
        this.app = app;
        this.currentRunway = runway;
        setObstacles();
        this.obstacles = runway.getObstacles();
        BorderPane borderPane = new BorderPane(); // Main layout container
        borderPane.setPrefSize(1200.0,1200.0);

        // Set the title of the screen
        Text title = new Text("Side View");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        VBox.setMargin(title, new Insets(10, 0, 10, 0));

        // Make the screen buttons
        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(addButtons());

        // Top layout with title and buttons
        VBox topLayout = new VBox();
        topLayout.setAlignment(Pos.TOP_CENTER);
        topLayout.getChildren().addAll(title, buttons);
        BorderPane.setMargin(topLayout, new Insets(10));


        // Add topLayout to the top of the BorderPane
        borderPane.setTop(topLayout);

        // Add canvas
        Canvas runwayCanvas = new Canvas(800, 200); // Set the size as needed
        drawRunway(runwayCanvas);



        // Add the canvas to the bottom of the BorderPane

        borderPane.setBottom(runwayCanvas);

        // Set the main BorderPane as the root of the scene
        this.getChildren().add(borderPane);

    }

    private void setObstacles(){
        if (obstacles != null){
            Obstacle currentObstacle = obstacles.getFirst();
        }
    }

    private void drawRunway(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Adjust the coordinates for drawing within the canvas
//        gc.setFill(Color.GRAY);
//        gc.fillRect(100, 50, 600, 10); // Example: Drawing within canvas bounds
//
//        // Add more drawing commands here for other elements
//
//        // Example: Drawing simple thresholds
//        gc.setFill(Color.WHITE);
//        gc.fillRect(100, 50, 20, 10);
//        gc.fillRect(680, 50, 20, 10);

            // Set runway parameters and positions
            double runwayStartX = 100;
            double runwayStartY = 50;
            double runwayWidth = (double) currentRunway.getTORA() /6;
            double runwayHeight = 10;

            // b. Threshold indicators
            double thresholdWidth = 5;

        // d. Displaced thresholds (example values)
            double displacedThresholdOffset = (double) currentRunway.getDisplacedThreshold() /6; // Displacement from the start of the runway

            // e. Stopway/Clearway (example values)
            double stopwayWidth = (double) currentRunway.getStopway() /6;
            double clearwayWidth = (double) currentRunway.getClearway() /6;

            // f. Take-off/Landing direction (example values)
            String takeoffDirection = "L"; // Left
            String landingDirection = "R"; // Right

            // g. Re-declared distances (example values)
            double TORA = (double) currentRunway.getTORA() / 6; // Takeoff Run Available
            double TODA = (TORA + currentRunway.getClearway()); // Takeoff Distance Available
            double ASDA = (TORA + currentRunway.getStopway()); // Accelerate-Stop Distance Available
            double LDA = (double) currentRunway.getLDA() /6; // Landing Distance Available

            // i. The obstacle (example values)
            double obstacleX = 350; // X position of the obstacle
            double obstacleY = runwayStartY - 15; // Y position above the runway
            double obstacleWidth = 10;
            double obstacleHeight = 15;

            // j. Offset caused by RESA and slope angles (example values)
            double resaOffset = 20;
            double slopeAngle = 3; // Degrees

            // Draw the runway strip
            gc.setFill(Color.GRAY);
            gc.fillRect(runwayStartX, runwayStartY, runwayWidth, runwayHeight);

            // b. Draw threshold indicators
            gc.setFill(Color.WHITE);
            gc.fillRect(runwayStartX, runwayStartY, thresholdWidth, runwayHeight);
            gc.fillRect(runwayStartX + runwayWidth - thresholdWidth, runwayStartY, thresholdWidth, runwayHeight);

            // c. Draw threshold designators
            gc.setFill(Color.BLACK);
            gc.fillText(currentRunway.getLogicalRunway1(), runwayStartX + 5, runwayStartY + 20); // Adjust text position as needed
            gc.fillText(currentRunway.getLogicalRunway2(), runwayStartX + runwayWidth - 5, runwayStartY + 20); // Adjust text position as needed

            // d. Displaced threshold representation
            gc.setFill(Color.BLACK);
            gc.fillRect(runwayStartX + displacedThresholdOffset, runwayStartY, thresholdWidth, runwayHeight);

            // e. Draw Stopway/Clearway
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(runwayStartX - stopwayWidth, runwayStartY, stopwayWidth, runwayHeight); // Stopway left
            gc.fillRect(runwayStartX + runwayWidth, runwayStartY, clearwayWidth, runwayHeight); // Clearway right

            // f. Indicate take-off/landing direction
            gc.setFill(Color.DARKBLUE);
//            gc.fillText("Take-off →", runwayStartX + TORA - 50, runwayStartY + 30); // Adjust text position as needed
//            gc.fillText("← Landing", runwayStartX + LDA - 50, runwayStartY - 10); // Adjust text position as needed

            // g. Re-declared distances indicators
            gc.setStroke(Color.RED);
            gc.strokeLine(runwayStartX, runwayStartY + 20, runwayStartX + TORA, runwayStartY + 20); // TORA line
            gc.setStroke(Color.YELLOW);
            gc.strokeLine(runwayStartX, runwayStartY + 30, runwayStartX + TODA, runwayStartY + 30); // TODA line
            gc.setStroke(Color.GREEN);
            gc.strokeLine(runwayStartX, runwayStartY + 40, runwayStartX + ASDA, runwayStartY + 40); // ASDA line
            gc.setStroke(Color.BLUE);
            gc.strokeLine(runwayStartX, runwayStartY - 10, runwayStartX + LDA, runwayStartY - 10); // LDA line

            // h. Distances broken down (including RESA/Blast Allowance)
            // This would typically be text and lines similar to the re-declared distances

//            // i. Draw the obstacle
//            gc.setFill(Color.BROWN);
//            gc.fillRect(obstacleX, obstacleY, obstacleWidth, obstacleHeight);

            // j. Offset caused by RESA and slope angles
            // This could be lines or shapes indicating the offset and the angle
            // For example, drawing a line at the slope angle
//            gc.setStroke(Color.BLUE);
//            gc.strokeLine(obstacleX, obstacleY, obstacleX - resaOffset, obstacleY - (resaOffset * Math.tan(Math.toRadians(slopeAngle))));


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
                ViewSelectionScene viewSelectionScene = new ViewSelectionScene(app, currentRunway);
                Scene viewScene = new Scene(viewSelectionScene, secondaryStage.getScene().getWidth(), secondaryStage.getScene().getHeight());
                secondaryStage.setScene(viewScene);
                viewSelectionScene.setSecondaryStage(secondaryStage);
            }
        });


        return new ArrayList<>(List.of(backButton));
    }
}
