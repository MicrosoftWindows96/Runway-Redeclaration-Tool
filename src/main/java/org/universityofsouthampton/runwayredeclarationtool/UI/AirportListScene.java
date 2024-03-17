package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;
//import org.universityofsouthampton.runwayredeclarationtool.utility.exportXML;
import org.universityofsouthampton.runwayredeclarationtool.utility.importXML;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class AirportListScene extends BaseScene {

    private final ArrayList<Airport> importedAirports; // Arraylist of airports to display on screen
    private Airport selectedAirport; // Variable to reference to call onto Runway Scene
    private Button currentlySelectedButton;
    private final VBox infoBox = new VBox(10); // When selected, it'll display the selected airport's info
    private final ScrollPane airportScroll = new ScrollPane(); // Scroll Pane to store the list of airports

    public AirportListScene(MainApplication app) {
        this.app = app;
        setPadding(new Insets(20));
        setSpacing(10);
        setAlignment(Pos.CENTER);

        // Set title
        Text title = new Text("List of Airports");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Initialise the screen contents and airportScroll
        BorderPane mainPane = new BorderPane();
        airportScroll.setFitToWidth(true);
        airportScroll.setPrefHeight(500);
        airportScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; ");
        importedAirports = app.getAirports();
        updateList();

        // Set infoBox's positionings
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5;");
        updateAirportInfo(null);

        // Set the main buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButtons());

        // Add the contents
        mainPane.setLeft(airportScroll);
        mainPane.setCenter(infoBox);
        getChildren().addAll(title, mainPane, buttonBox);
    }

    @Override
    ArrayList<Button> addButtons() {
        Button addAirport = new Button(); // Button to add a new airport
        styleButton(addAirport, MaterialDesign.MDI_PLUS_BOX, "Add");
        addAirport.setOnAction(e -> {
            promptAddAirport();
            updateList();
//            app.updateXMLs();
        });

        Button backButton = new Button("Log Out"); // Button to return to the login screen
        styleButton(backButton, MaterialDesign.MDI_LOCK, "Log Out");
        backButton.setOnAction(e -> app.displayMenu());

        Button deleteButton = new Button("Delete"); // Button to delete a selected airport
        styleButton(deleteButton, MaterialDesign.MDI_MINUS_BOX, "Delete");
        deleteButton.setOnAction(e -> {
            importedAirports.remove(selectedAirport);
            updateList();
//            app.updateXMLs();
            updateAirportInfo(null);
        });

        Button importXMLButton = new Button("Import"); // Button to Import xml file to add onto airport list
        styleButton(importXMLButton, MaterialDesign.MDI_DOWNLOAD, "Import");
        importXMLButton.setOnAction(e -> importAirportsFromXML());

        Button exportXMLButton = new Button("Export"); // Button to Export into a xml file the current airport list contents
        styleButton(exportXMLButton, MaterialDesign.MDI_UPLOAD, "Export");
//        exportXMLButton.setOnAction(e -> exportAirportsToXML());

        Button modifyButton = new Button("Modify"); // Button to edit a selected airport
        styleButton(modifyButton, MaterialDesign.MDI_WRENCH, "Modify");
        modifyButton.setOnAction(e -> {
            if (selectedAirport != null) {
                promptModifyAirport(selectedAirport);
            } else {
                showErrorDialog("Please select an airport to modify.");
            }
        });

        return new ArrayList<>(Arrays.asList(addAirport, deleteButton, modifyButton, importXMLButton,exportXMLButton, backButton));
    }

    private void updateList() { // Method updates the display of airports on the left of the screen
        VBox airportsBox = new VBox(5);
        airportsBox.setAlignment(Pos.CENTER_LEFT);
        airportsBox.setPadding(new Insets(10));
        airportScroll.setContent(airportsBox);

        importedAirports.forEach(airport -> {
            Button airportButton = new Button();
            styleButton(airportButton, MaterialDesign.MDI_AIRPLANE_LANDING, airport.getAirportName() + " (" + airport.getAirportCode() + ")");
            airportButton.setMinWidth(200);
            airportButton.setOnAction(e -> {
                setSelectedAirport(airport);
                updateAirportInfo(airport);
            });
            airportsBox.getChildren().add(airportButton);
        });
    }

    private void updateAirportInfo(Airport airport) { // This method displays the selected airport's information
        infoBox.getChildren().clear();
        if (airport == null) {
            infoBox.getChildren().add(new Text("Select an airport to view details."));
        } else {
            Text airportName = new Text("Name: " + airport.getAirportName());
            airportName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            Text airportCode = new Text("Code: " + airport.getAirportCode());
            airportCode.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
            Text numRunways = new Text("Number of Runways: " + airport.getRunways().size());
            numRunways.setFont(Font.font("Arial", FontWeight.NORMAL, 16));

            VBox runwaysInfoBox = new VBox(5);
            runwaysInfoBox.setPadding(new Insets(5, 0, 5, 10));
            for (var pRunwaySet : airport.getParallelRunwaySets()) { // FOR EACH parallel runway set
                Text runwayInfo = new Text("Degrees: " + pRunwaySet.getDegree1() + "/" + pRunwaySet.getDegree2() + " - Parallel Runways: " + pRunwaySet.getLogicalRunways().size());
                runwayInfo.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

                Button configureButton = new Button();
                configureButton.setOnAction(e -> app.displayRunwayConfigScene(airport,pRunwaySet));
                styleButton(configureButton, MaterialDesign.MDI_LOGIN, "Access");

                Button deleteRunwayButton = new Button("Delete");
                deleteRunwayButton.setOnAction(e -> {
                    airport.getParallelRunwaySets().remove(pRunwaySet);
//                    app.updateXMLs();
                    updateAirportInfo(airport);
                });
                styleButton(deleteRunwayButton, MaterialDesign.MDI_MINUS_BOX, "Delete");

                HBox runwayButtonsBox = new HBox(10, configureButton, deleteRunwayButton);
                runwaysInfoBox.getChildren().addAll(runwayInfo, runwayButtonsBox);
            }

            Button addRunwayButton = new Button("Add Runway");
            addRunwayButton.setOnAction(e -> promptAddRunway());
            styleButton(addRunwayButton, MaterialDesign.MDI_PLUS_BOX, "Add");

            ScrollPane runwaysScrollPane = new ScrollPane(runwaysInfoBox);
            runwaysScrollPane.setFitToWidth(true);
            runwaysScrollPane.setMinHeight(150);

            VBox detailsBox = new VBox(5, airportName, airportCode, numRunways, runwaysScrollPane, addRunwayButton);
            detailsBox.setPadding(new Insets(10));
            detailsBox.setStyle("-fx-background-color: #eaeaea; -fx-border-color: #c0c0c0; -fx-border-width: 1; -fx-border-radius: 5;");

            VBox.setVgrow(runwaysScrollPane, Priority.ALWAYS);

            infoBox.getChildren().add(detailsBox);
        }
    }

    private void importAirportsFromXML() { // Function to import airport xml file from local files
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Airport XML File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            importXML importer = new importXML(file);
            ArrayList<Airport> airports = importer.makeAirportsXML();
            app.mergeAirport(airports);
            updateList();
        }
    }

//    private void exportAirportsToXML() { // Function to export airport xml file of current contents to local files
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Airport XML File");
//        File file = fileChooser.showSaveDialog(null);
//        if (file != null) {
//            exportXML exporter = new exportXML(importedAirports, new ArrayList<>(), file);
//            exporter.writeXML();
//        }
//    }

    public static String capitalize(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    // PROMPT METHODS
    private void promptAddAirport() {
        PromptWindow promptWindow = new PromptWindow(app);
        var nameBox = promptWindow.addParameterField("Airport Name");
        var codeBox = promptWindow.addParameterField("Airport Code");

        // Implement submitButton
        Button submitButton = new Button("Add Airport");
        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Add");
        submitButton.setOnAction(e -> {
            String name = capitalize(promptWindow.getInput(nameBox));
            String code = promptWindow.getInput(codeBox).toUpperCase();


            if (name.isEmpty() || code.isEmpty()) {
                showErrorDialog("Please enter a valid airport name and code.");
                return;
            }
            Airport newAirport = new Airport(name, code);
            app.addAirport(newAirport);
//            app.updateXMLs();
            ((Stage) promptWindow.getScene().getWindow()).close();
        });
        promptWindow.getChildren().addAll(nameBox,codeBox,submitButton);
        promptWindow.getChildren().addAll(promptWindow.addButtons());
        dialogGenerator(promptWindow, "Add NEW Airport");
    }

    private void promptAddRunway() {
        PromptWindow promptWindow = new PromptWindow(app);
        Text runways = new Text("Runway 1  ---  Runway 2");
        runways.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        var degreeBox1 = promptWindow.addParameterField("Degree 1");
        var degreeBox2 = promptWindow.addParameterField("Degree 2");
        var deBox = new HBox(10,degreeBox1,degreeBox2);

        var stopwayBox1 = promptWindow.addParameterField("Stopway");
        var stopwayBox2 = promptWindow.addParameterField("Stopway");
        var sBox = new HBox(10,stopwayBox1,stopwayBox2);

        var clearwayBox1 = promptWindow.addParameterField("Clearway");
        var clearwayBox2 = promptWindow.addParameterField("Clearway");
        var cBox = new HBox(10,clearwayBox1,clearwayBox2);

        var TORAbox1 = promptWindow.addParameterField("TORA");
        var TORAbox2 = promptWindow.addParameterField("TORA");
        var tBox = new HBox(10,TORAbox1,TORAbox2);

        var dispThreshBox1 = promptWindow.addParameterField("Displaced Threshold:");
        var dispThreshBox2 = promptWindow.addParameterField("Displaced Threshold:");
        var diBox = new HBox(10,dispThreshBox1,dispThreshBox2);

        // Implement addButton
        Button submitButton = new Button("Add");
        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Add");
        submitButton.setOnAction(e -> {
            String degree1 = promptWindow.getInput(degreeBox1);
            String degree2 = promptWindow.getInput(degreeBox2);
            // Try making a runway object and check for any illegal parameters
            if (!degree1.matches("\\d{2}") || !degree2.matches("\\d{2}")) {
                showErrorDialog("Invalid runway name. The name must be a valid degree.");
            } else {
                try {
                    int stopway1 = Integer.parseInt(promptWindow.getInput(stopwayBox1));
                    int clearway1 = Integer.parseInt(promptWindow.getInput(clearwayBox1));
                    int TORA1 = Integer.parseInt(promptWindow.getInput(TORAbox1));
                    int dispThresh1 = Integer.parseInt(promptWindow.getInput(dispThreshBox1));

                    int stopway2 = Integer.parseInt(promptWindow.getInput(stopwayBox2));
                    int clearway2 = Integer.parseInt(promptWindow.getInput(clearwayBox2));
                    int TORA2 = Integer.parseInt(promptWindow.getInput(TORAbox2));
                    int dispThresh2 = Integer.parseInt(promptWindow.getInput(dispThreshBox2));

                    if (stopway1 < 0 || clearway1 < 0 || TORA1 < 0 || dispThresh1 < 0 ||
                        stopway2 < 0 || clearway2 < 0 || TORA2 < 0 || dispThresh2 < 0) {
                        throw new IllegalArgumentException("Invalid measurements for runways.");
                    }
                    // If Runway valid, add object to application and close window
                    Runway testRunway1 = new Runway(degree1,stopway1,clearway1,TORA1,dispThresh1);
                    Runway testRunway2 = new Runway(degree2,stopway2,clearway2,TORA2,dispThresh2);

                    selectedAirport.addNewRunway(testRunway1,testRunway2);
//                    app.updateXMLs();
                    updateAirportInfo(selectedAirport);
                    Stage stage = (Stage) promptWindow.getScene().getWindow();
                    stage.close();

                } catch (NumberFormatException ex) {
                    showErrorDialog("Invalid input for runway measurements. Please enter valid integers.");
                } catch (IllegalArgumentException ex) {
                    showErrorDialog(ex.getMessage());
                }
            }
        });
        promptWindow.getChildren().addAll(runways,deBox,sBox,cBox,
            tBox,diBox,submitButton);
        promptWindow.getChildren().addAll(promptWindow.addButtons());
        dialogGenerator(promptWindow, "Add NEW Runways");
    }

    private void promptModifyAirport(Airport airport) {
        PromptWindow promptWindow = new PromptWindow(app);
        var nameBox = promptWindow.editParameterField("Airport Name", airport.getAirportName());
        var codeBox = promptWindow.editParameterField("Airport Code", airport.getAirportCode());

        Button submitButton = new Button("Save Changes");
        styleButton(submitButton, MaterialDesign.MDI_CHECK, "Save");
        submitButton.setOnAction(e -> {
            airport.setAirportName(promptWindow.getInput(nameBox));
            airport.setAirportCode(promptWindow.getInput(codeBox));
            updateList();
//            app.updateXMLs();
            updateAirportInfo(airport);
            Stage stage = (Stage) promptWindow.getScene().getWindow();
            stage.close();
        });

        promptWindow.getChildren().addAll(nameBox,codeBox,submitButton);
        dialogGenerator(promptWindow,"Modify Airport");
    }

    private void setSelectedAirport(Airport airport) {
        this.selectedAirport = airport;
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        }
    }
}
