package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class HowToUseScene extends BaseScene {

    public HowToUseScene(MainApplication app) {
        this.app = app;
        setPadding(new Insets(20));
        setSpacing(10);
        setAlignment(Pos.CENTER);

        // Set title
        Text title = new Text("How To Use");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStroke(Color.WHITE);

        VBox buttons = new VBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(addButtons());

        // Load and display PDF in a scrollable viewer
        ScrollPane pdfScrollPane = createPDFViewerScrollPane("src/main/resources/HelpGuide/HowToUse.pdf");

        this.getChildren().addAll(title,buttons);

        BorderPane rootPane = new BorderPane();
        rootPane.setTop(title);
        rootPane.setCenter(pdfScrollPane);
        rootPane.setBottom(buttons);

        this.getChildren().add(rootPane);
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

    private ScrollPane createPDFViewerScrollPane(String filePath) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(false);

        try {
            PDDocument document = PDDocument.load(new File(filePath));
            int numPages = document.getNumberOfPages();

            VBox pagesBox = new VBox(); // Container to hold all pages vertically

            for (int i = 0; i < numPages; i++) {
                PDFRenderer renderer = new PDFRenderer(document);

                // Render the current page of the PDF
                BufferedImage image = renderer.renderImageWithDPI(i, 87); // 72 DPI

                // Convert BufferedImage to JavaFX Image
                javafx.scene.image.Image fxImage = SwingFXUtils.toFXImage(image, null);

                // Display the image in an ImageView
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(fxImage);

                pagesBox.getChildren().add(imageView); // Add the image to the VBox
            }

            scrollPane.setContent(pagesBox); // Set the VBox as the content of the ScrollPane
            document.close(); // Close the PDF document when done
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scrollPane;
    }
}
