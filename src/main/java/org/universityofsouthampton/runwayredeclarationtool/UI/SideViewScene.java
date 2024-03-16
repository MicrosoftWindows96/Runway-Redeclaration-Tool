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
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SideViewScene extends BaseScene {

    private final Runway currentRunway;
    private final ArrayList<Obstacle> obstacles;
    private final Pane animationOverlay;
    private final Pane cloudLayer = new Pane();
    private final double iconSize = 24;
    private final double speed = 1.0;
    private final Random random = new Random();
    private double TORA;
    private double TODA;
    private double ASDA ;
    private double LDA ;

    public SideViewScene(MainApplication app, Runway runway) {
        this.app = app;
        this.currentRunway = runway;
        this.obstacles = runway.getObstacles();
//        currentObstacle = obstacles.getFirst();
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1200.0, 1200.0);
        borderPane.setBackground(Background.fill(Color.rgb(201,233,246)));
        cloudLayer.setPrefSize(800, 200);
        createPattern();
        animatePattern();

        Text title = new Text("Side View");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        VBox.setMargin(title, new Insets(10, 0, 10, 0));

        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(addButtons());

        VBox topLayout = new VBox();
        topLayout.setAlignment(Pos.TOP_CENTER);
        topLayout.getChildren().addAll(cloudLayer, title, buttons);
        BorderPane.setMargin(topLayout, new Insets(10));

        borderPane.setTop(topLayout);

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

        double runwayEndX = 100;

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
        Color skyColor = Color.rgb(201,233,246);
        gc.setFill(skyColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 50, canvas.getWidth(), canvas.getHeight() - 50);

        double runwayStartX = 100;
        double runwayStartY = 50;
        double runwayWidth = (double) currentRunway.getTORA() / 6;
        double runwayHeight = 5;

        double thresholdWidth = 5;
        double displacedThresholdOffset = (double) currentRunway.getDisplacedThreshold() / 6;
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

        if (displacedThresholdOffset > 0) {
            gc.setFill(Color.RED);
            gc.fillRect(runwayStartX + displacedThresholdOffset, runwayStartY, thresholdWidth, runwayHeight);
        }

        // e. Draw Stopway/Clearway
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(runwayStartX - stopwayWidth, runwayStartY, stopwayWidth, runwayHeight); // Stopway left
        gc.fillRect(runwayStartX + stopwayWidth, runwayStartY, stopwayWidth, runwayHeight); // Stopway right

        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(runwayStartX - stopwayWidth - clearwayWidth, runwayStartY, clearwayWidth, runwayHeight); // Clearway left
        gc.fillRect(runwayStartX + runwayWidth + stopwayWidth, runwayStartY, clearwayWidth, runwayHeight); // Clearway right

        Font labelFont = Font.font("Arial", 14);
        gc.setFont(labelFont);
        gc.setFill(Color.BLACK);

        if (!obstacles.isEmpty()) {
            Obstacle obstacle = obstacles.getFirst();
            double obstacleX = runwayStartX + ((double) obstacle.getDistanceFromThreshold() / 6); // X position of the obstacle
            double obstacleHeight = (double) obstacle.getHeight() / 6;
            double obstacleY = runwayStartY - obstacleHeight; // Adjust Y position to place on top of the runway
            double obstacleWidth = 10;

            gc.setFill(Color.BLACK);
            gc.fillRect(obstacleX, obstacleY, obstacleWidth, obstacleHeight);

            String obstacleText = obstacle.getName() + " (" + obstacle.getHeight() + "m)";
            gc.fillText(obstacleText, obstacleX, runwayStartY - runwayHeight - 5);

            gc.setStroke(Color.BLACK);
//            gc.strokeLine(runwayStartX, runwayStartY + 20, runwayStartX + TORA, runwayStartY + 20); // slope line


            this.TORA = (double) currentRunway.getNewTORA() / 6;
            this.TODA = (double) currentRunway.getNewTODA() / 6;
            this.ASDA = (double) currentRunway.getNewASDA() / 6;
            this.LDA = (double) currentRunway.getNewLDA() / 6;

        }

        //indicate distances
        gc.setStroke(Color.LIGHTPINK);
        gc.strokeLine(runwayStartX, runwayStartY + 15, runwayStartX + this.TORA, runwayStartY + 15); // TORA line
        gc.setStroke(Color.YELLOW);
        gc.strokeLine(runwayStartX, runwayStartY + 30, runwayStartX + this.TODA, runwayStartY + 30); // TODA line
        gc.setStroke(Color.LIGHTGREEN);
        gc.strokeLine(runwayStartX, runwayStartY + 45, runwayStartX + this.ASDA, runwayStartY + 45); // ASDA line
        gc.setStroke(Color.BLUE);
        gc.strokeLine(runwayStartX, runwayStartY + 60, runwayStartX + this.LDA, runwayStartY + 60); // LDA line


        String toraText = "TORA: " + this.TORA * 6 + "m";
        gc.fillText(toraText, runwayStartX + this.TORA, runwayStartY + 20);

        String todaText = "TODA: " + this.TODA * 6+ "m";
        gc.fillText(todaText, runwayStartX + this.TODA, runwayStartY + 35);

        String asdaText = "ASDA: " + this.ASDA * 6+ "m";
        gc.fillText(asdaText, runwayStartX + this.ASDA, runwayStartY + 50);

        String ldaText = "LDA:" + this.LDA * 6+ "m";
        gc.fillText(ldaText, runwayStartX + this.LDA, runwayStartY + 65);

        java.awt.FontMetrics metrics = java.awt.Toolkit.getDefaultToolkit().getFontMetrics(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));

        String leftRunwayName = currentRunway.getLogicalRunway1();
        gc.fillText(leftRunwayName, runwayStartX - 25, runwayStartY - 25);

        String rightRunwayName = currentRunway.getLogicalRunway2();
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