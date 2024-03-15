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
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;
import java.util.List;

public class TopDownScene extends BaseScene {

    private final Runway currentRunway;

    public TopDownScene(MainApplication app, Runway runway) {
        this.app = app;
        this.currentRunway = runway;
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1200.0,1200.0);


        Text title = new Text("Top Down View");
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

        Canvas runwayCanvas = new Canvas(800, 600);
        drawRunway(runwayCanvas);

        borderPane.setCenter(runwayCanvas);

        this.getChildren().add(borderPane);
    }

    private void drawRunway(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double centerLineY = canvas.getHeight() / 2;
        double runwayLength = canvas.getWidth() - 200;
        double runwayStartX = 100;

        gc.setFill(Color.GRAY);
        gc.fillRect(runwayStartX, centerLineY - 20, runwayLength, 40);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        double dashLength = 15;
        double spaceLength = 10;
        for (double x = runwayStartX; x < runwayStartX + runwayLength; x += dashLength + spaceLength) {
            gc.strokeLine(x, centerLineY, x + dashLength, centerLineY);
        }

        Font labelFont = new Font("Arial", 14);
        gc.setFont(labelFont);
        gc.setFill(Color.BLACK);

        java.awt.FontMetrics metrics = java.awt.Toolkit.getDefaultToolkit().getFontMetrics(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));

        String leftRunwayName = currentRunway.getName() + "L";
        gc.fillText(leftRunwayName, runwayStartX, centerLineY - 25);

        String rightRunwayName = currentRunway.getName() + "R";
        int stringWidth = metrics.stringWidth(rightRunwayName);
        gc.fillText(rightRunwayName, runwayStartX + runwayLength - stringWidth, centerLineY - 25);

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
