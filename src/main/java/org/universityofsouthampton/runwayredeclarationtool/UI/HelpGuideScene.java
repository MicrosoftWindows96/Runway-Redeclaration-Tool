package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;

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
        styleButton(howToUseButton, MaterialDesign.MDI_HELP, "How To Use");
        howToUseButton.setPrefWidth(150);
        howToUseButton.setOnAction(e -> app.displayHowToUseScene());

        Button FAQButton = new Button();
        styleButton(FAQButton, MaterialDesign.MDI_HELP, "FAQs");
        FAQButton.setPrefWidth(150);
        FAQButton.setOnAction(e -> app.displayFAQScene());

//        Button issueButton = new Button();
//        styleButton(issueButton, MaterialDesign.MDI_HELP, "Known Issues");
//        issueButton.setPrefWidth(150);
////        bothViewButton.setOnAction(e -> {
//////            app.display2DbothViewScene(runwayManager);
//////        });
        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_CLOSE, "Return");
                backButton.setOnAction(e -> {
            app.returnFromHelpScene();
        });


        return new ArrayList<>(Arrays.asList(howToUseButton, FAQButton, backButton));
    }
}
