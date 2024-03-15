package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SideViewScene extends BaseScene {

    private final Runway currentRunway;
    private final ArrayList<Obstacle> obstacles;
    private final Pane animationOverlay;

    public SideViewScene(MainApplication app, Runway runway) {
        this.app = app;
        this.currentRunway = runway;
        setObstacles();
        this.obstacles = runway.getObstacles();
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1200.0, 1200.0);
        borderPane.setBackground(Background.fill(Color.rgb(201,233,246)));

        Text title = new Text("Side View");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        VBox.setMargin(title, new Insets(10, 0, 10, 0));

        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(addButtons());

        VBox topLayout = new VBox();
        topLayout.setAlignment(Pos.TOP_CENTER);
        topLayout.getChildren().addAll(title, buttons);
        BorderPane.setMargin(topLayout, new Insets(10));

        borderPane.setTop(topLayout);

        Canvas runwayCanvas = new Canvas(800, 200);
        drawRunway(runwayCanvas);

        borderPane.setBottom(runwayCanvas);

        animationOverlay = new Pane();
        animationOverlay.setPrefSize(800, 200);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(runwayCanvas, animationOverlay);

        borderPane.setBottom(stackPane);

        animatePlane();

        this.getChildren().add(borderPane);
    }

    private ImageView createPlaneImage() {
        Image planeImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/plane.png")));
        ImageView planeImageView = new ImageView(planeImage);

        planeImageView.setFitWidth(40);
        planeImageView.setPreserveRatio(true);

        return planeImageView;
    }


    private void animatePlane() {
        ImageView plane = createPlaneImage();

        double startingAltitude = 0;

        double runwayEndX = 100; // Needs changing to be in line with calculations!

        plane.setTranslateX(animationOverlay.getPrefWidth());
        plane.setTranslateY(startingAltitude);

        animationOverlay.getChildren().add(plane);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(5), plane);
        transition.setFromX(animationOverlay.getPrefWidth());
        transition.setToX(runwayEndX);
        transition.setFromY(startingAltitude);
        transition.setToY(30);

        transition.play();
    }


    private void setObstacles() {
        if (obstacles != null && !obstacles.isEmpty()) {
            Obstacle currentObstacle = obstacles.getFirst();
        }
    }

    public void drawRunway(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Color skyColor = Color.rgb(201,233,246);
        gc.setFill(skyColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 50, canvas.getWidth(), canvas.getHeight() - 50);

        double runwayStartX = 100;
        double runwayStartY = 50;
        double runwayWidth = (double) currentRunway.getTORA() / 6;
        double runwayHeight = 10;

        double thresholdWidth = 5;
        double displacedThresholdOffset = (double) currentRunway.getDisplacedThreshold() / 6;
        double stopwayWidth = (double) currentRunway.getStopway() / 6;
        double clearwayWidth = (double) currentRunway.getClearway() / 6;

        double TORA = (double) currentRunway.getTORA() / 6;
        double TODA = (TORA + currentRunway.getClearway());
        double ASDA = (TORA + currentRunway.getStopway());
        double LDA = (double) currentRunway.getLDA() / 6;

        gc.setFill(Color.DARKGRAY);
        gc.fillRect(runwayStartX, runwayStartY, runwayWidth, runwayHeight);

        gc.setFill(Color.WHITE);
        gc.fillRect(runwayStartX, runwayStartY, thresholdWidth, runwayHeight);
        gc.fillRect(runwayStartX + runwayWidth - thresholdWidth, runwayStartY, thresholdWidth, runwayHeight);

        if (displacedThresholdOffset > 0) {
            gc.setFill(Color.DARKGRAY);
            gc.fillRect(runwayStartX, runwayStartY, displacedThresholdOffset, runwayHeight);
        }

        Font labelFont = Font.font("Arial", 14);
        gc.setFont(labelFont);
        gc.setFill(Color.BLACK);

        if (!obstacles.isEmpty()) {
            Obstacle obstacle = obstacles.getFirst();
            double obstacleX = runwayStartX + ((double) obstacle.getDistanceFromThreshold() / 6);
            double obstacleY = runwayStartY - ((double) obstacle.getHeight() / 2);
            double obstacleWidth = 10;
            double obstacleHeight = obstacle.getHeight();

            gc.setFill(Color.BLACK);
            gc.fillRect(obstacleX, obstacleY - obstacleHeight, obstacleWidth, obstacleHeight);

            String obstacleText = obstacle.getName() + " (" + obstacle.getHeight() + "m)";
            gc.fillText(obstacleText, obstacleX + 15, runwayStartY - runwayHeight - 5);
        }

        String toraText = "TORA: " + currentRunway.getTORA() + "m";
        gc.fillText(toraText, runwayStartX + runwayWidth, runwayStartY + 20);

        String todaText = "TODA: " + currentRunway.getTODA() + "m";
        gc.fillText(todaText, runwayStartX + runwayWidth, runwayStartY + 35);

        String asdaText = "ASDA: " + currentRunway.getASDA() + "m";
        gc.fillText(asdaText, runwayStartX + runwayWidth, runwayStartY + 50);

        String ldaText = "LDA: " + currentRunway.getLDA() + "m";
        gc.fillText(ldaText, runwayStartX + runwayWidth, runwayStartY + 65);

        java.awt.FontMetrics metrics = java.awt.Toolkit.getDefaultToolkit().getFontMetrics(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));

        String leftRunwayName = currentRunway.getName() + "L";
        gc.fillText(leftRunwayName, runwayStartX - 25, runwayStartY - 25);

        String rightRunwayName = currentRunway.getName() + "R";
        int stringWidth = metrics.stringWidth(rightRunwayName);
        gc.fillText(rightRunwayName, runwayStartX + runwayWidth - stringWidth + 25, runwayStartY - 25);
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
                Scene viewScene = new Scene(viewSelectionScene, 300, 600);
                secondaryStage.setScene(viewScene);
                secondaryStage.show();
                viewSelectionScene.setSecondaryStage(secondaryStage);
            }
        });

        return new ArrayList<>(List.of(backButton));
    }
}