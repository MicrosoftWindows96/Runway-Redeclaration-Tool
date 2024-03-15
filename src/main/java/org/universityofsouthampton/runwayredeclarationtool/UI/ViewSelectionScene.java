package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import java.util.Arrays;

public class ViewSelectionScene extends BaseScene {
    private final Runway currentRunway;
    public ViewSelectionScene(MainApplication app, Runway runway) {
        this.app = app;
        this.currentRunway = runway;
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1200.0,1200.0);
        borderPane.setBackground(Background.fill(Color.rgb(201,233,246)));


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


        this.getChildren().add(borderPane);
    }

    private Stage secondaryStage;

    public void setSecondaryStage(Stage stage) {
        this.secondaryStage = stage;
    }


    @Override
    ArrayList<Button> addButtons() {

        Button sideViewButton = new Button();
        styleButton(sideViewButton, MaterialDesign.MDI_LOGIN, "Side");
        sideViewButton.setOnAction(e -> app.display2DsideViewScene(currentRunway));

        Button topDownViewButton = new Button();
        styleButton(topDownViewButton, MaterialDesign.MDI_LOGIN, "Aerial");
        topDownViewButton.setOnAction(e -> app.display2DtopDownViewScene(currentRunway));

        Button bothViewButton = new Button();
        styleButton(bothViewButton, MaterialDesign.MDI_LOGIN, "Both");
        bothViewButton.setOnAction(e -> app.display2DbothViewScene(currentRunway));

        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_CLOSE, "Close");
        backButton.setOnAction(e -> {
            if (secondaryStage != null) {
                secondaryStage.close();
            }
        });

        return new ArrayList<>(Arrays.asList(sideViewButton, topDownViewButton, bothViewButton, backButton));
    }
}