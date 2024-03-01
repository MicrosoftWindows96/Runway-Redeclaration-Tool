package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import javafx.scene.shape.Rectangle;
import java.util.LinkedList;
import java.util.Random;

public class AnimatedPatternBackground extends Pane {
  private final Random random = new Random();
  private final Pane planeLayer = new Pane();
  private final Pane cloudLayer = new Pane();
  private final double iconSize = 24;
  private final double speed = 1.0;
  //private final double planeSpeed = 4.0;
  private static AnimatedPatternBackground instance;

  public AnimatedPatternBackground(double initialWidth, double initialHeight) {
    this.setStyle("-fx-background-color: #c9e9f6;");
    this.getChildren().addAll(planeLayer, cloudLayer);

    cloudLayer.setPrefSize(initialWidth, initialHeight);
    planeLayer.setPrefSize(initialWidth, initialHeight);

    Rectangle clip = new Rectangle();
    clip.widthProperty().bind(this.widthProperty());
    clip.heightProperty().bind(this.heightProperty());
    this.setClip(clip);

    createPattern();
    animatePattern();
    launchPlanes();
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


  private void launchPlanes() {
    Timeline planeTimeline = new Timeline(new KeyFrame(Duration.seconds(random.nextDouble(5,  10)), e -> {
      FontIcon planeIcon = FontIcon.of(MaterialDesign.MDI_AIRPLANE, (int) iconSize);

      planeIcon.setLayoutX(random.nextDouble() * (getWidth() - iconSize));
      planeIcon.setLayoutY(-iconSize);

      planeIcon.setRotate(180);

      planeLayer.getChildren().add(planeIcon);

      LinkedList<Circle> smokeTrail = new LinkedList<>();

      Timeline smokeTimeline = new Timeline(new KeyFrame(Duration.millis(200), ev -> {
        if (planeIcon.getParent() == null) {
          return;
        }

        Circle smoke = new Circle(planeIcon.getLayoutX() + planeIcon.getIconSize() / 2.0,
                planeIcon.getLayoutY() - 30,
                5, javafx.scene.paint.Color.GRAY);
        smoke.setOpacity(0.3);

        planeLayer.getChildren().addFirst(smoke);

        smokeTrail.add(smoke);

        if (smokeTrail.size() > 5) {
          planeLayer.getChildren().remove(smokeTrail.removeFirst());
        }

        for (Circle s : smokeTrail) {
          s.setRadius(s.getRadius() + 0.2);
          s.setOpacity(s.getOpacity() * 0.5);
        }
      }));
      smokeTimeline.setCycleCount(Timeline.INDEFINITE);
      smokeTimeline.play();

      planeIcon.layoutYProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal.doubleValue() > getHeight() + 50) {
          smokeTimeline.stop();
          smokeTrail.forEach(smoke -> planeLayer.getChildren().remove(smoke));
        }
      });

      double planeSpeed = random.nextDouble() * 2 + 3; // Randomize plane speed
      Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(50), ev -> {
        planeIcon.setLayoutY(planeIcon.getLayoutY() + planeSpeed);

        if (planeIcon.getLayoutY() > getHeight() + 50) {
          planeLayer.getChildren().remove(planeIcon);
        }
      }));
      moveTimeline.setCycleCount(Timeline.INDEFINITE);
      moveTimeline.play();
    }));
    planeTimeline.setCycleCount(Timeline.INDEFINITE);
    planeTimeline.play();
  }


  public static synchronized AnimatedPatternBackground getInstance() {
    if (instance == null) {
      instance = new AnimatedPatternBackground(1920, 1080);
      instance.initialSetup();
    }
    return instance;
  }


  private void initialSetup() {
    this.setStyle("-fx-background-color: #c9e9f6;");

    if (!this.getChildren().contains(planeLayer) && !this.getChildren().contains(cloudLayer)) {
      this.getChildren().addAll(planeLayer, cloudLayer);
    }

    Rectangle clip = new Rectangle(1920, 1080);
    this.setClip(clip);

    createPattern();
    animatePattern();
    launchPlanes();
  }
}

