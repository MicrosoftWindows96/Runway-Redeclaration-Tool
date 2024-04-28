package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kotlin.coroutines.AbstractCoroutineContextElement;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;
import org.universityofsouthampton.runwayredeclarationtool.users.Account;
import org.universityofsouthampton.runwayredeclarationtool.utility.exportXML;
import org.universityofsouthampton.runwayredeclarationtool.utility.importXML;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class HelpGuideScene extends BaseScene {

    public HelpGuideScene(MainApplication app) {
        this.app = app;
        setPadding(new Insets(20));
        setSpacing(10);
        setAlignment(Pos.CENTER);

        // Set title
        Text title = new Text("Help Guide");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStroke(Color.WHITE);

        VBox buttons = new VBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(addButtons());

        this.getChildren().addAll(title,buttons);

    }

    @Override
    ArrayList<Button> addButtons() {
        Button howToUseButton = new Button();
        styleButton(howToUseButton, MaterialDesign.MDI_LOGIN, "How To Use");
//        sideViewButton.setOnAction(e -> app.display2DsideViewScene(runwayManager));

        Button FAQButton = new Button();
        styleButton(FAQButton, MaterialDesign.MDI_LOGIN, "FAQs");
//        topDownViewButton.setOnAction(e -> app.display2DtopDownViewScene(runwayManager, false));

        Button issueButton = new Button();
        styleButton(issueButton, MaterialDesign.MDI_LOGIN, "Known issues with app");
//        bothViewButton.setOnAction(e -> {
////            app.display2DbothViewScene(runwayManager);
////        });
        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_CLOSE, "Return");
                backButton.setOnAction(e -> {
            app.displayAirportListScene();
        });


        return new ArrayList<>(Arrays.asList(howToUseButton, FAQButton, issueButton, backButton));
    }
}
