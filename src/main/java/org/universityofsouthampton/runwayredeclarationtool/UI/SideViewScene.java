package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;

import java.util.ArrayList;
import java.util.Arrays;

public class SideViewScene extends BaseScene {

    public SideViewScene(MainApplication app) {
        this.app = app;
        BorderPane borderPane = new BorderPane(); // Main layout container

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


    private void drawRunway(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Adjust the coordinates for drawing within the canvas
        gc.setFill(Color.GRAY);
        gc.fillRect(100, 50, 600, 10); // Example: Drawing within canvas bounds

        // Add more drawing commands here for other elements

        // Example: Drawing simple thresholds
        gc.setFill(Color.WHITE);
        gc.fillRect(100, 50, 20, 10);
        gc.fillRect(680, 50, 20, 10);
    }
    @Override
    ArrayList<Button> addButtons() {
        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        backButton.setOnAction(e -> app.displayViewsScene());

        return new ArrayList<>(Arrays.asList(backButton));
    }
}
