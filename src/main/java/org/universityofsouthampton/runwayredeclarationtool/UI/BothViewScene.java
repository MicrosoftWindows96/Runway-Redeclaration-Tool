package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
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

public class BothViewScene extends BaseScene {

    private final Runway currentRunway;

    public BothViewScene(MainApplication app, Runway runway) {
        this.app = app;
        this.currentRunway = runway;
        BorderPane borderPane = new BorderPane();
        borderPane.setBackground(Background.fill(Color.rgb(201,233,246)));

        Text title = new Text("Both Top Down and Side View");
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

        SplitPane viewSplitPane = new SplitPane();
        viewSplitPane.setOrientation(Orientation.VERTICAL);


        Canvas topViewCanvas = new Canvas(800, 200);
        new TopDownScene(app, currentRunway).drawRunway(topViewCanvas);
        Canvas sideViewCanvas = new Canvas(800, 200);
        new SideViewScene(app, currentRunway).drawRunway(sideViewCanvas);

        viewSplitPane.getItems().addAll(topViewCanvas, sideViewCanvas);

        borderPane.setCenter(viewSplitPane);

        this.getChildren().add(borderPane);
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
