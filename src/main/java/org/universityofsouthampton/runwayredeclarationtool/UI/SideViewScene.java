package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
import java.util.Objects;
import java.util.Random;

public class SideViewScene extends BaseScene {

    private final ParallelRunways runwayManager;
    private final Runway currentRunway;
    private final ArrayList<Obstacle> obstacles;
    private Obstacle obstacle;
    private final Pane animationOverlay;
    private final Pane cloudLayer = new Pane();
    private final double iconSize = 24;
    private final double speed = 1.0;
    private final Random random = new Random();
    private double TORA;
    private double TODA;
    private double ASDA ;
    private double LDA ;
    private double RESA;

    private double slopeDistance;
    private double RESADistance;
    private double displacedThresholdOffset;
    private double runwayStartX;
    private double runwayWidth;

    public SideViewScene(MainApplication app, ParallelRunways runwayManager) {
        this.app = app;
        this.currentRunway = runwayManager.getFstRunway();
        this.runwayManager = runwayManager;
        this.obstacles = currentRunway.getObstacles();
        this.RESA = (double) 240 /6;
        if (!obstacles.isEmpty()) {
            this.obstacle = obstacles.getFirst();
        }
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1200.0, 1200.0);

        if (MainApplication.isDarkMode()) {
            borderPane.setStyle("-fx-background-color: #121212;");
        } else {
            borderPane.setBackground(Background.fill(Color.rgb(201, 233, 246)));
        }

        cloudLayer.setPrefSize(800, 130);
        createPattern();
        animatePattern();

        Text title = new Text("Side View");
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


        VBox rightButtons = new VBox();
        rightButtons.setAlignment(Pos.TOP_CENTER);
        rightButtons.setSpacing(10); // Set spacing between buttons
        rightButtons.setPadding(new Insets(10)); // Set padding around the VBox

        Button button1 = new Button();
        styleButton(button1, MaterialDesign.MDI_NUMERIC_1_BOX, "L Over");  // Land Over
        button1.setOnAction(e -> animatePlane("Land Over"));
        Button button2 = new Button();
        styleButton(button2, MaterialDesign.MDI_NUMERIC_2_BOX, "L Toward"); // Land Toward
        button2.setOnAction(e -> animatePlane("Land Toward"));
        Button button3 = new Button();
        styleButton(button3, MaterialDesign.MDI_NUMERIC_3_BOX, "T Toward"); // Takeoff Toward
        button3.setOnAction(e -> animatePlane("Takeoff Toward"));
        Button button4 = new Button();
        styleButton(button4, MaterialDesign.MDI_NUMERIC_4_BOX, "T Away"); // Takeoff Away
        button4.setOnAction(e -> animatePlane("Takeoff Away"));


        rightButtons.getChildren().addAll(button1, button2, button3,button4);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        rightButtons.getChildren().add(spacer);

        VBox topLayout = new VBox();
        topLayout.setAlignment(Pos.TOP_CENTER);
        topLayout.getChildren().addAll(cloudLayer, title, buttons, distanceInfoBox);
        BorderPane.setMargin(topLayout, new Insets(10));

        borderPane.setTop(topLayout);

        borderPane.setRight(rightButtons);

        Canvas runwayCanvas = new Canvas(800, 200);
        drawRunway(runwayCanvas);

        Pane cityPane = new Pane();
        cityPane.setPrefSize(800, 50);
        cityPane.setOpacity(0.3);
        Image cityImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/city.png")), 800, 50, true, true);
        BackgroundImage cityBackground = new BackgroundImage(cityImage,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                new BackgroundPosition(Side.LEFT, 0, false, Side.TOP, 0, false),
                BackgroundSize.DEFAULT);
        cityPane.setBackground(new Background(cityBackground));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(runwayCanvas, cityPane);
        StackPane.setAlignment(cityPane, Pos.TOP_CENTER);

        borderPane.setBottom(stackPane);

        animationOverlay = new Pane();
        animationOverlay.setPrefSize(800, 200);
        stackPane.getChildren().add(animationOverlay);

        this.getChildren().add(borderPane);
    }

    private ImageView createPlaneImage() {
        Image planeImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/plane.png")));
        ImageView planeImageView = new ImageView(planeImage);

        planeImageView.setFitWidth(60);
        planeImageView.setPreserveRatio(true);

        return planeImageView;
    }

    private ImageView currentPlane;
    private TranslateTransition currentTransition;

    private void animatePlane(String operation) {
        if (currentTransition != null && currentPlane != null) {
            currentTransition.stop();
            animationOverlay.getChildren().remove(currentPlane);
        }

        ImageView plane = createPlaneImage();
        if (MainApplication.isDarkMode()) {
            plane.setEffect(new javafx.scene.effect.ColorAdjust(0, 0, 1, 0));
        }
        currentPlane = plane;
        animationOverlay.getChildren().add(plane);


        double startingXPosition;
        double endingXPosition;
        double startingYPosition;
        double altitude = 20;

        switch (operation) {
            case "Land Over":
                startingYPosition = 0;
                startingXPosition = -plane.getFitWidth();
                endingXPosition = RESADistance + 200;
                plane.setScaleX(-1);
                break;
            case "Land Toward":
                startingYPosition = 0;
                startingXPosition = animationOverlay.getPrefWidth();
                if (slopeDistance <= 0){
                    endingXPosition = runwayStartX + displacedThresholdOffset + 10;
                }else {
                    if (obstacle.getDistanceFromThreshold() < 1000 ) {
                        endingXPosition = slopeDistance + 50;
                    } else if (obstacle.getDistanceFromThreshold() >= 1000){
                        endingXPosition = slopeDistance - 50;
                    } else {
                        endingXPosition = slopeDistance ;
                    }
                }

                plane.setScaleX(1);
                break;
            case "Takeoff Toward":
                startingYPosition = 20;
                altitude = 0;
                if (slopeDistance <= 0){
                    startingXPosition = runwayStartX + displacedThresholdOffset + 10;
                }else {
                    startingXPosition = slopeDistance - 100;
                }
                endingXPosition = animationOverlay.getPrefWidth() + 100;
                plane.setScaleX(-1);
                break;
            case "Takeoff Away":
                startingYPosition = 20;
                if (slopeDistance <= 0){
                    startingXPosition = runwayStartX + displacedThresholdOffset + 100;
                }else {
                    startingXPosition = slopeDistance - 50;
                }
                altitude = 0;
                endingXPosition = -plane.getFitWidth() - 100;
                plane.setScaleX(1);
                break;
            default:
                return;
        }

        plane.setTranslateX(startingXPosition);
        plane.setTranslateY(altitude);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(5), plane);
        transition.setFromX(startingXPosition);
        transition.setToX(endingXPosition);
        transition.setFromY(startingYPosition);
        transition.setToY(altitude);

        currentTransition = transition;

        transition.play();
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


    private void setObstacles() {
        if (obstacles != null && !obstacles.isEmpty()) {
            Obstacle currentObstacle = obstacles.getFirst();
        }
    }

    public void drawRunway(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Color skyColor;
        if (MainApplication.isDarkMode()) {
            skyColor = Color.rgb(18, 18, 18);
        } else {
            skyColor = Color.rgb(201,233,246);
        }
        gc.setFill(skyColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 50, canvas.getWidth(), canvas.getHeight() - 50);

        runwayStartX = 100;
        double runwayStartY = 50;
        runwayWidth = (double) currentRunway.getTORA() / 6;
        double runwayHeight = 5;

        double thresholdWidth = 5;
        displacedThresholdOffset = (double) currentRunway.getDisplacedThreshold() / 6;
        double stopwayWidth = (double) currentRunway.getStopway() / 6;
        double clearwayWidth = (double) currentRunway.getClearway() / 6;

        this.TORA = (double) currentRunway.getTORA() / 6;
        this.TODA = (double) currentRunway.getTODA() / 6;
        this.ASDA = (double) currentRunway.getASDA() / 6;
        this.LDA = (double) currentRunway.getLDA() / 6;

        gc.setFill(Color.DARKGRAY);
        gc.fillRect(runwayStartX, runwayStartY, runwayWidth, runwayHeight);

        // b. Draw threshold indicators
        gc.setFill(Color.WHITE);
        gc.fillRect(runwayStartX, runwayStartY, thresholdWidth, runwayHeight);
        gc.fillRect(runwayStartX + runwayWidth - thresholdWidth, runwayStartY, thresholdWidth, runwayHeight);


        // e. Draw Stopway/Clearway
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(runwayStartX - stopwayWidth, runwayStartY, stopwayWidth, runwayHeight); // Stopway left
        gc.fillRect(runwayStartX + stopwayWidth, runwayStartY, stopwayWidth, runwayHeight); // Stopway right

        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(runwayStartX - stopwayWidth - clearwayWidth, runwayStartY, clearwayWidth, runwayHeight); // Clearway left
        gc.fillRect(runwayStartX + runwayWidth + stopwayWidth, runwayStartY, clearwayWidth, runwayHeight); // Clearway right

        Font labelFont = Font.font("Arial", 14);
        gc.setFont(labelFont);
        // if dark mode
        if (MainApplication.isDarkMode()) {
            gc.setFill(Color.WHITE);
        } else {
            gc.setFill(Color.BLACK);
        }

        if (!obstacles.isEmpty()) {
            double obstacleX = runwayStartX + ((double) obstacle.getDistanceFromThreshold() / 6);
            double obstacleWidth = 10;
            double obstacleHeight = (double) obstacle.getHeight() / 6;
            double obstacleY = runwayStartY - obstacleHeight;

            gc.fillRect(obstacleX, obstacleY, obstacleWidth, obstacleHeight);

            String obstacleText = obstacle.getName() + " (" + obstacle.getHeight() + "m)";
            gc.fillText(obstacleText, obstacleX, runwayStartY - runwayHeight - 5);

            this.TORA = (double) currentRunway.getNewTORA() / 6;
            this.TODA = (double) currentRunway.getNewTODA() / 6;
            this.ASDA = (double) currentRunway.getNewASDA() / 6;
            this.LDA = (double) currentRunway.getNewLDA() / 6;

            if (obstacle.getDistanceFromThreshold() < 1000 ) {
                // Original diagonal line points
                double startX = obstacleX + obstacleWidth; // Top-right corner of the obstacle
                double startY = obstacleY; // Top-right corner of the obstacle
                double endX = obstacleX + (obstacleHeight * 50 ); // Bottom-left corner of the obstacle
                double endY = obstacleY + obstacleHeight; // Bottom-left corner of the obstacle

                double slope = (endY - startY) / (endX - startX);

                double extensionLength = 30;

                double newStartX = startX - (extensionLength / Math.sqrt(1 + slope * slope));
                double newStartY = startY - (slope * (startX - newStartX));

                gc.setStroke(Color.RED);
                gc.strokeLine(newStartX, newStartY, endX, endY);

                this.slopeDistance = endX;

                gc.setFill(Color.ORANGE);
                gc.fillRect(obstacleX + obstacleWidth , runwayStartY, this.RESA, runwayHeight);

                this.RESADistance = obstacleX + obstacleWidth + this.RESA;

                gc.setStroke(Color.LIGHTPINK);
                gc.strokeLine(RESADistance, runwayStartY + 15, RESADistance + this.TORA, runwayStartY + 15); // TORA line
                gc.setStroke(Color.YELLOW);
                gc.strokeLine(RESADistance, runwayStartY + 30, RESADistance + this.TODA, runwayStartY + 30); // TODA line
                gc.setStroke(Color.LIGHTGREEN);
                gc.strokeLine(RESADistance, runwayStartY + 45, RESADistance + this.ASDA + stopwayWidth, runwayStartY + 45); // ASDA line
                gc.setStroke(Color.BLUE);
                gc.strokeLine(slopeDistance, runwayStartY + 60, slopeDistance + this.LDA, runwayStartY + 60); // LDA line



            } else if (obstacle.getDistanceFromThreshold() >= 1000) {
                // Original opposite diagonal line points
                double oppositeStartX = obstacleX + obstacleWidth - (obstacleHeight * 50 );
                double oppositeStartY = obstacleY + obstacleHeight;
                double oppositeEndX = obstacleX;
                double oppositeEndY = obstacleY;

                // Calculate slope of the line
                double slope = (oppositeEndY - oppositeStartY) / (oppositeEndX - oppositeStartX);

                // Length to extend the line
                double extensionLength = 30;

                // Calculate new ending point
                double newEndX = oppositeEndX + (extensionLength / Math.sqrt(1 + slope * slope)); // Note the '+' here
                double newEndY = oppositeEndY + (slope * (newEndX - oppositeEndX)); // Adjust Y coordinate accordingly

                gc.setStroke(Color.RED);
                gc.strokeLine(oppositeStartX, oppositeStartY, newEndX, newEndY);

                this.slopeDistance = oppositeStartX;

                gc.setFill(Color.ORANGE);
                gc.fillRect(obstacleX - this.RESA, runwayStartY, this.RESA, runwayHeight);

                gc.setStroke(Color.LIGHTPINK);
                gc.strokeLine(runwayStartX, runwayStartY + 15, slopeDistance -10, runwayStartY + 15); // TORA line
                gc.setStroke(Color.YELLOW);
                gc.strokeLine(runwayStartX, runwayStartY + 30, slopeDistance-10, runwayStartY + 30); // TODA line
                gc.setStroke(Color.LIGHTGREEN);
                gc.strokeLine(runwayStartX, runwayStartY + 45, slopeDistance-10, runwayStartY + 45); // ASDA line
                gc.setStroke(Color.BLUE);
                gc.strokeLine(runwayStartX +displacedThresholdOffset, runwayStartY + 60, runwayStartX + displacedThresholdOffset + this.LDA - ((double) (currentRunway.getRESA()+60) /6), runwayStartY + 60); // LDA line


            }

        }else {
            gc.setStroke(Color.LIGHTPINK);
            gc.strokeLine(runwayStartX, runwayStartY + 15, runwayStartX + this.TORA, runwayStartY + 15); // TORA line
            gc.setStroke(Color.YELLOW);
            gc.strokeLine(runwayStartX, runwayStartY + 30, runwayStartX + this.TODA, runwayStartY + 30); // TODA line
            gc.setStroke(Color.LIGHTGREEN);
            gc.strokeLine(runwayStartX, runwayStartY + 45, runwayStartX + this.ASDA + stopwayWidth , runwayStartY + 45); // ASDA line
            gc.setStroke(Color.BLUE);
            gc.strokeLine(runwayStartX + displacedThresholdOffset, runwayStartY + 60, runwayStartX + displacedThresholdOffset + this.LDA, runwayStartY + 60); // LDA line

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

        String leftRunwayName = runwayManager.getFstRunway().getName() + runwayManager.getFstRunway().getDirection();
        gc.fillText(leftRunwayName, runwayStartX - 25, runwayStartY - 25);
        // f. Indicate take-off/landing direction
        gc.fillText("Take-off/Landing →", runwayStartX - 25, runwayStartY - 40); // Adjust text position as needed

        String rightRunwayName = runwayManager.getSndRunway().getName() + runwayManager.getSndRunway().getDirection();
        int stringWidth = metrics.stringWidth(rightRunwayName);
        gc.fillText(rightRunwayName, runwayStartX + runwayWidth - stringWidth + 25, runwayStartY - 25);
        gc.fillText("← Take-off/Landing", runwayStartX + runwayWidth - stringWidth - 75, runwayStartY - 40); // Adjust text position as needed

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