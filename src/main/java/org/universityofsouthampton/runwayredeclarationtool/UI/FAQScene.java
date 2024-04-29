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

public class FAQScene extends BaseScene {

    public FAQScene(MainApplication app) {
        this.app = app;
        setPadding(new Insets(20));
        setSpacing(10);
        setAlignment(Pos.CENTER);

        // Set title
        Text title = new Text("FAQs");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStroke(Color.WHITE);

        VBox buttons = new VBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(addButtons());

        this.getChildren().addAll(title,buttons);

    }

    @Override
    ArrayList<Button> addButtons() {

        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_CLOSE, "Return");
        backButton.setOnAction(e -> {
            app.displayHelpGuideScene();
        });


        return new ArrayList<>(Arrays.asList(backButton));
    }
}
