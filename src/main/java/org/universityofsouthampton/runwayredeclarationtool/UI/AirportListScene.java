package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.utility.importXML;

import java.io.File;
import java.util.ArrayList;

public class AirportListScene extends VBox {

    /**
     * Scroll pane to display the airports
     */
    private final ScrollPane scrollPane = new ScrollPane();
    private final MainApplication app;

    /**
     * Observable arraylist of airports to be displayed
     */
    private final ObservableList<Airport> airportsObserve = FXCollections.observableArrayList();

    /**
     * Airports from the XML file
     */
    private final ArrayList<Airport> importedAirports;

    /**
     * Variable to store currently selected airport
     */
    private Airport selectedAirport;

    private Button currentlySelectedButton;

    public AirportListScene(MainApplication app) {
        this.app = app;

        setPadding(new Insets(20));
        setSpacing(10);

        Text title = new Text("List of Airports");
        title.setFont(Font.font("Arial", 20));

        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(700,500);
        importedAirports = app.getAirports();
        updateList();

        Button selectButton = new Button();
        styleButton(selectButton, MaterialDesign.MDI_PLUS_BOX, "Select");
        selectButton.setOnAction(e -> {
            if (this.selectedAirport == null) {
                System.out.println("Nothing Selected!");
            } else {
                app.displayRunwayListScene(selectedAirport);
            }

        });

        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        backButton.setOnAction(e -> app.displayAirportScene());

        Button deleteButton = new Button();
        styleButton(deleteButton, MaterialDesign.MDI_DELETE, "Delete");
        deleteButton.setOnAction(e -> {
            importedAirports.remove(selectedAirport);
            updateList();
            app.updateAirportsXML();
        });

        Button importXMLButton = new Button();
        styleButton(importXMLButton, MaterialDesign.MDI_DOWNLOAD, "Import");
        importXMLButton.setOnAction(e -> importAirportsFromXML());

        Button exportXMLButton = new Button();
        styleButton(exportXMLButton, MaterialDesign.MDI_UPLOAD, "Export");
        //exportXMLButton.setOnAction(e -> exportAirportsToXML());

        Button modifyButton = new Button();
        styleButton(modifyButton, MaterialDesign.MDI_WRENCH, "Modify");
        modifyButton.setOnAction(e -> {
            showModifyAirportDialog(selectedAirport);
        });


        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(backButton, selectButton, modifyButton, deleteButton, importXMLButton, exportXMLButton);

        getChildren().addAll(title, scrollPane, buttonBox);
    }

    private void showModifyAirportDialog(Airport airport) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Modify Airport");

        TextField nameInput = new TextField(selectedAirport.getAirportName());
        VBox dialogVBox = getAirportDataForm(selectedAirport, nameInput, dialogStage);

        Scene dialogScene = new Scene(dialogVBox);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    @NotNull
    private VBox getAirportDataForm(Airport airport, TextField nameInput, Stage dialogStage) {
        TextField codeInput = new TextField(airport.getAirportCode());

        Button submitButton = new Button("Save Changes");
        submitButton.setOnAction(e -> {
            airport.setAirportName(nameInput.getText());
            airport.setAirportCode(codeInput.getText());
            updateList();
            app.updateXMLs();
            dialogStage.close();
        });

        VBox dialogVBox = new VBox(10, new Label("Airport Name:"), nameInput, new Label("Airport Code:"), codeInput, submitButton);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setPadding(new Insets(20));
        return dialogVBox;
    }


    private void updateList() {
        airportsObserve.clear();
        var airportsBox = new VBox();
        airportsBox.setSpacing(5);

        for (Airport airport : importedAirports) {
            var name = (airport.getAirportName() + " -- " + airport.getAirportCode());
            var airportButton = new Button(name);
            extractedStylingMethod(airportButton, MaterialDesign.MDI_AIRPLANE_LANDING, airport.getAirportCode());

            airportButton.setOnMouseExited(e -> {
                if (airportButton != currentlySelectedButton){
                    airportButton.setStyle(("-fx-background-color: #333; -fx-text-fill: white;"));
                }
            });

            airportButton.setOnMouseClicked(e -> {
                if (currentlySelectedButton != null) {
                    currentlySelectedButton.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
                }
                airportButton.setStyle(("-fx-background-color: #555; -fx-text-fill: white;"));
                setSelectedAirport(airport);
                currentlySelectedButton = airportButton;
                System.out.println("Currently selected: " + getSelectedAirport().getAirportName());
            });


            airportsBox.getChildren().add(airportButton);

            airportsObserve.add(airport);
        }

        scrollPane.setContent(airportsBox);
    }

    private void setSelectedAirport (Airport airport) {
        selectedAirport = airport;
    }

    private Airport getSelectedAirport () {
        return selectedAirport;
    }

    private void styleButton(Button button, MaterialDesign icon, String text) {
        extractedStylingMethod(button, icon, text);
    }

    static void extractedStylingMethod(Button button, MaterialDesign icon, String text) {
        button.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        button.setPrefWidth(120);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #333; -fx-text-fill: white;"));
        button.setOnMouseClicked(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;"));

        ObstacleListScene.styleIcon(button, icon, text);
    }

    private void importAirportsFromXML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Airport XML File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            importXML importer = new importXML(file);
            ArrayList<Airport> airports = importer.makeAirportsXML();
            app.setAirports(airports);
            updateList();
        }
    }

}
