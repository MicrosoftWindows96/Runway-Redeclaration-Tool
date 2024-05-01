package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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

        Accordion accordion = new Accordion();

        // Adding FAQ Items
        accordion.getPanes().addAll(
                createFAQItem("What is the runway declaration tool?",
                        "The runway declaration tool is meant to provide users an easier way to decide whether a runway is safe to be used or not. \nIt provides a way to get parameters as soon as possible in case an obstruction or obstacle appears on the runway.\nWith automated calculations done for you, it should save time when faced with critical decisions."
                ),
                createFAQItem("How are the runways named in the application?",
                        "The runway declaration tool follows a certain runway naming convention when it comes to parallel runways:\n\n" +
                                "• Runways are named with numbers 00-36 to represent the degrees the runway is facing (e.g., runway 09/27 for a runway covering\n90 degrees and 270 degrees or east and west).\n" +
                                "• There can be up to 3 parallel runways, where they’re named direction if necessary L (left), R (right), C (center).\n" +
                                "• A single runway would have no direction indicator, two runways would have L and R, and three would have L, C, R.\n\n" +
                                "However, airport protocols state that leading teams should train their departments on how to use the runway declaration tool as \nsoon as they're issued at their airports."
                ),
                createFAQItem("How can I add an account to the system?",
                        "The system comes with an admin account already initialized, so you may need to ask permission from the assigned admin users at \nyour airport if you are applicable to have an account to use our application."
                ),
                createFAQItem("What OS does this run on?",
                        "This system is compatible with any device that is able to run Java 21. You may need to download the latest JDK that is able to run\nthe latest Java features. Here is the link to get the official Java 21 JDK: [Java 21 JDK]\n(https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)"
                ),
                createFAQItem("Can the tool allow me to keep track of activity?",
                        "The runway declaration tool has a built-in logging and notification system. This means that every time a user performs an \noperation, it is immediately logged. However, for security reasons, only trusted admins are able to export the logs that are \nrecorded during the system's runtime."
                ),
                createFAQItem("My XML file doesn’t import into the system. Why?",
                        "The system requires a specific XML file format to be used, which can be found in the airports.xsd file provided by the system in the \nresources folder. Your administrators should provide you with the XSD file to comply with the format the tool is able to read. \nThis is to avoid any conflicts or reading errors when importing or exporting XML files to pass around the airport departments."
                ),
                createFAQItem("Who to contact for any other issues?",
                        "You can contact our leading development team at SteveTomBusiness@gmail.com for any software-related enquiries."
                )
        );


        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().add(accordion);

        this.getChildren().addAll(title,vbox,buttons);

    }

    private TitledPane createFAQItem(String question, String answer) {
        TitledPane pane = new TitledPane();
        pane.setText(question);
        pane.setContent(new VBox());
        ((VBox) pane.getContent()).getChildren().add(new javafx.scene.control.Label(answer));
        return pane;
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
