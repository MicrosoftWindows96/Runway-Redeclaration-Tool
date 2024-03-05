package org.universityofsouthampton.runwayredeclarationtool.UI;

import java.io.File;
import java.util.ArrayList;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.utility.importXML;

public class AirportListScene extends VBox {

    /**
     * Scroll pane to display the airports
     */
    private final ScrollPane scrollPane = new ScrollPane();
    private MainApplication app;

    /**
     * Observable arraylist of airports to be displayed
     */
    private ObservableList<Airport> airportsObserve = FXCollections.observableArrayList();

    /**
     * Airports from the XML file
     */
    private ArrayList<Airport> importedAirports;

    /**
     * Variable to store currently selected airport
     */
    private Airport selectedAirport;

    public AirportListScene(MainApplication app) {
        this.app = app;

        setPadding(new Insets(20));
        setSpacing(10);

        Text title = new Text("List of Airports");
        title.setFont(Font.font("Arial", 20));


        // Populate list of airports from XML file
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(700,500);
        importedAirports = app.getAirports();
        updateList();

        Button selectButton = new Button();
        styleButton(selectButton, MaterialDesign.MDI_PLUS_BOX, "Select");
        selectButton.setOnAction(e -> {
            // Handle airport selection (we may need to implement database/xml func early)
            // Selection error handling
            if (this.selectedAirport == null) {
                System.out.println("Nothing Selected!");
            } else {
                app.displayRunwayListScene(selectedAirport);
            }

        });

        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        backButton.setOnAction(e -> app.displayAirportScene());

        Button importXMLButton = new Button("Import XML");
        styleButton(importXMLButton, MaterialDesign.MDI_DOWNLOAD, "Import");
        importXMLButton.setOnAction(e -> importAirportsFromXML());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(selectButton, importXMLButton, backButton);

        getChildren().addAll(title, scrollPane, buttonBox);
    }

    private void updateList() {
        airportsObserve.clear();
        var airportsBox = new VBox();
        airportsBox.setSpacing(5);

        for (Airport airport : importedAirports) {
            var name = (airport.getAirportName() + " -- " + airport.getAirportCode());
            var airportButton = new Button(name);

            // Button to select the airport
            airportButton.setOnMouseClicked(event -> {
                setSelectedAirport(airport);
                System.out.println("Currently selected: " + getSelectedAirport().getAirportName());
            });

            airportsBox.getChildren().add(airportButton);

            airportsObserve.add(airport);
        }

        scrollPane.setContent(airportsBox);
    }

    /*
    Member Variable of selected airport changes to be referenced for next screen!
     */
    private void setSelectedAirport (Airport airport) {
        selectedAirport = airport;
    }

    private Airport getSelectedAirport () {
        return selectedAirport;
    }

    private void styleButton(Button button, MaterialDesign icon, String text) {
        button.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        button.setPrefWidth(120);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #333; -fx-text-fill: white;"));

        FontIcon buttonIcon = new FontIcon(icon);
        buttonIcon.setIconColor(Color.WHITE);
        button.setGraphic(buttonIcon);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // Only display the icon

        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER);

        HBox hbox = new HBox(buttonIcon, label);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10);

        button.setGraphic(hbox);
    }

    private void importAirportsFromXML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Airport XML File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(null); // Use your stage here if possible

        if (file != null) {
            importXML importer = new importXML(file);
            ArrayList<Airport> airports = importer.makeAirportsXML();
            app.setAirports(airports); // Ensure you have this method in your MainApplication
            updateList();
        }

    }


    public Scene createScene(MainApplication app) {
        return new Scene(this, 300, 400);
    }
}
