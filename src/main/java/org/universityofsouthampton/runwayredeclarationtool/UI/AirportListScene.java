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
        title.setStroke(Color.WHITE);


        VBox helpBox = new VBox(10);
        helpBox.setAlignment(Pos.TOP_RIGHT);
        Button helpButton = new Button();
        styleDarkButton(helpButton, MaterialDesign.MDI_HELP, "");
        helpButton.setOnAction(e -> app.displayHelpGuideScene());
        helpButton.setLayoutX(20);
        helpButton.setLayoutY(20);
        helpButton.setPrefWidth(5);
        helpBox.getChildren().add(helpButton);



        // Initialise the screen contents and airportScroll
        BorderPane mainPane = new BorderPane();
        airportScroll.setFitToWidth(true);
        airportScroll.setPrefHeight(500);
        airportScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; ");
        importedAirports = app.getAirports();
        updateList();
        VBox leftBox = new VBox(5,airportScroll);

        // For ADMINS initialise the "Users" button to access user management
        if (app.getAccountManager().getLoggedInAccount().getRole().equals("admin")) {
            Button usersButton = new Button("Users"); // Button to Import xml file to add onto airport list
            styleButton(usersButton, MaterialDesign.MDI_EMOTICON, "Users");
            usersButton.setOnAction(e -> {app.displayUsersScene();});
            leftBox.getChildren().add(usersButton);
        }

        // Set infoBox's positioning
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5;");
        updateAirportInfo(null);

        // Set the main buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButtons());

        // Add the contents
        mainPane.setLeft(leftBox);
        mainPane.setCenter(infoBox);
        getChildren().addAll(title, mainPane, buttonBox,helpBox);
    }

    @Override
    ArrayList<Button> addButtons() {
        ArrayList<Button> buttons = new ArrayList<>();

        if (app.getAccountManager().getLoggedInAccount().getRole().equals("admin")) { // ADMIN EXCLUSIVE BUTTONS
            Button addAirport = new Button(); // Button to add a new airport
            styleButton(addAirport, MaterialDesign.MDI_PLUS_BOX, "Add");
            addAirport.setOnAction(e -> {
                promptAddAirport();
                updateList();
                app.updateXMLs();
            });

            Button deleteButton = new Button("Delete"); // Button to delete a selected airport
            styleButton(deleteButton, MaterialDesign.MDI_MINUS_BOX, "Delete");
            deleteButton.setOnAction(e -> {
                importedAirports.remove(selectedAirport);
                updateList();
                app.showNotification("Delete Airport", "Airport " + selectedAirport.getAirportName() + " has been deleted.");
                app.updateXMLs();
                updateAirportInfo(null);
            });

            buttons.add(addAirport);
            buttons.add(deleteButton);
        }

        if (app.getAccountManager().getLoggedInAccount().getRole().equals("admin") ||
            app.getAccountManager().getLoggedInAccount().getRole().equals("editor")) { // BUTTONS ACCESSED BY ADMIN AND EDITOR

        Button modifyButton = new Button("Modify"); // Button to edit a selected airport
        styleButton(modifyButton, MaterialDesign.MDI_WRENCH, "Modify");
        modifyButton.setOnAction(e -> {
            if (selectedAirport != null) {
                promptModifyAirport(selectedAirport);
            } else {
                showErrorDialog("Please select an airport to modify.");
            }
        });

        buttons.add(modifyButton);

        }

        // REST ARE ACCESSED BY ALL ROLES:
        Button importXMLButton = new Button("Import"); // Button to Import xml file to add onto airport list
        styleButton(importXMLButton, MaterialDesign.MDI_DOWNLOAD, "Import");
        importXMLButton.setOnAction(e -> importAirportsFromXML());

        Button exportXMLButton = new Button("Export"); // Button to Export into a xml file the current airport list contents
        styleButton(exportXMLButton, MaterialDesign.MDI_UPLOAD, "Export");
        exportXMLButton.setOnAction(e -> exportAirportsToXML());

        Button backButton = new Button("Log Out"); // Button to return to the login screen
        styleButton(backButton, MaterialDesign.MDI_LOCK, "Log Out");
        backButton.setOnAction(e -> app.displayMenu());

        buttons.add(importXMLButton);
        buttons.add(exportXMLButton);
        buttons.add(backButton);

        return buttons;
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
            Text numRunways = new Text("Number of Parallel Runway Sets: " + airport.getParallelRunwaySets().size());
            numRunways.setFont(Font.font("Arial", FontWeight.NORMAL, 16));

            VBox runwaysInfoBox = new VBox(5);
            runwaysInfoBox.setPadding(new Insets(5, 0, 5, 10));
            for (var pRunwaySet : airport.getParallelRunwaySets()) { // FOR EACH parallel runway set
                Text runwayInfo = new Text("Degrees: " + pRunwaySet.getDegree1() + "/" + pRunwaySet.getDegree2() + " - Parallel Runways: " + pRunwaySet.getLogicalRunways().size());
                runwayInfo.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

                HBox runwayButtonsBox = new HBox(10); // INITIALISE INFO BOX

                Button configureButton = new Button(); // ALL ROLES CAN VIEW THE RUNWAY DETAILS
                configureButton.setOnAction(e -> app.displayRunwayConfigScene(airport,pRunwaySet));
                styleButton(configureButton, MaterialDesign.MDI_LOGIN, "Access");
                runwayButtonsBox.getChildren().add(configureButton);

                if (app.getAccountManager().getLoggedInAccount().getRole().equals("admin")) { // ONLY ADMINS CAN ADD/DELETE RUNWAYS
                    Button deleteRunwayButton = new Button("Delete");
                    deleteRunwayButton.setOnAction(e -> {
                        airport.getParallelRunwaySets().remove(pRunwaySet);
                        app.updateXMLs();
                        updateAirportInfo(airport);
                        app.showNotification("Runway Delete", "Runway " + pRunwaySet.getDegree1() + "/" + pRunwaySet.getDegree2() + " has been deleted");
                    });
                    styleButton(deleteRunwayButton, MaterialDesign.MDI_MINUS_BOX, "Delete");
                    runwayButtonsBox.getChildren().add(deleteRunwayButton);
                }

                runwaysInfoBox.getChildren().addAll(runwayInfo, runwayButtonsBox);
            }

            ScrollPane runwaysScrollPane = new ScrollPane(runwaysInfoBox);
            runwaysScrollPane.setFitToWidth(true);
            runwaysScrollPane.setMinHeight(150);

            VBox detailsBox = new VBox(5, airportName, airportCode, numRunways, runwaysScrollPane);

            if (app.getAccountManager().getLoggedInAccount().getRole().equals("admin")) {
                Button addRunwayButton = new Button("Add Runway");
                addRunwayButton.setOnAction(e -> promptAddRunway());
                styleButton(addRunwayButton, MaterialDesign.MDI_PLUS_BOX, "Add");
                detailsBox.getChildren().add(addRunwayButton);
            }

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
            app.showNotification("Airport Import", "Airport file has been imported.");
        }
    }

    private void exportAirportsToXML() { // Function to export airport xml file of current contents to local files
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Airport XML File");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            exportXML exporter = new exportXML(this.importedAirports, new ArrayList<>(), file);
            exporter.buildAirportsXML();
            exporter.writeXML();
            app.showNotification("Airport Export", "Airport file has been exported.");
        }
    }

    public static String capitalize(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    // PROMPT METHODS
    private void promptAddAirport() {
        PromptWindow promptWindow = new PromptWindow(app);
        var nameBox = promptWindow.addParameterField("Airport Name");
        TextField textFieldName = (TextField) nameBox.getChildren().get(1);
        var codeBox = promptWindow.addParameterField("Airport Code");
        TextField textFieldCode = (TextField) codeBox.getChildren().get(1);

        setupValidation(textFieldName, "^[A-Za-z ]+$", "Please enter a valid airport name (Only letters and spaces)");
        setupValidation(textFieldCode, "^[A-Za-z]{1,3}$", "Please enter a valid airport code (1-3 letters)");

        Button submitButton = new Button("Add Airport");
        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Add");
        submitButton.setOnAction(e -> {
            String validationError = validateInputs(new TextField[]{textFieldName, textFieldCode});
            if (validationError.isEmpty()) {
                String name = capitalize(textFieldName.getText().trim());
                String code = textFieldCode.getText().toUpperCase();

                if (name.isEmpty() || code.isEmpty()) {
                    showErrorDialog("Please enter a valid airport name and code.");
                    return;
                }
                Airport newAirport = new Airport(name, code);
                app.addAirport(newAirport);
                app.updateXMLs();
                ((Stage) promptWindow.getScene().getWindow()).close();
                app.showNotification("Add Airport", "Airport " + newAirport.getAirportName() + " has been added.");
            } else {
                showErrorDialog(validationError);
            }
        });

        promptWindow.getChildren().addAll(nameBox, codeBox, submitButton);
        dialogGenerator(promptWindow, "Add NEW Airport");
    }



    private void promptAddRunway() {
        PromptWindow promptWindow = new PromptWindow(app);
        Text runways = new Text("Runway 1  ---  Runway 2");
        runways.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        var degreeBox1 = promptWindow.addParameterField("Degree 1 (0-36)");
        TextField textFieldDegree1 = (TextField) degreeBox1.getChildren().get(1);
        var degreeBox2 = promptWindow.addParameterField("Degree 2 (0-36)");
        TextField textFieldDegree2 = (TextField) degreeBox2.getChildren().get(1);
        var deBox = new HBox(10, degreeBox1, degreeBox2);

        var stopwayBox1 = promptWindow.addParameterField("Stopway (m)");
        TextField textFieldStopway1 = (TextField) stopwayBox1.getChildren().get(1);
        var stopwayBox2 = promptWindow.addParameterField("Stopway (m)");
        TextField textFieldStopway2 = (TextField) stopwayBox2.getChildren().get(1);
        var sBox = new HBox(10, stopwayBox1, stopwayBox2);

        var clearwayBox1 = promptWindow.addParameterField("Clearway (m)");
        TextField textFieldClearway1 = (TextField) clearwayBox1.getChildren().get(1);
        var clearwayBox2 = promptWindow.addParameterField("Clearway (m)");
        TextField textFieldClearway2 = (TextField) clearwayBox2.getChildren().get(1);
        var cBox = new HBox(10, clearwayBox1, clearwayBox2);

        var TORAbox1 = promptWindow.addParameterField("TORA (m)");
        TextField textFieldTORA1 = (TextField) TORAbox1.getChildren().get(1);
        var TORAbox2 = promptWindow.addParameterField("TORA (m)");
        TextField textFieldTORA2 = (TextField) TORAbox2.getChildren().get(1);
        var tBox = new HBox(10, TORAbox1, TORAbox2);

        var dispThreshBox1 = promptWindow.addParameterField("Displaced Threshold (m)");
        TextField textFieldDispThresh1 = (TextField) dispThreshBox1.getChildren().get(1);
        var dispThreshBox2 = promptWindow.addParameterField("Displaced Threshold (m)");
        TextField textFieldDispThresh2 = (TextField) dispThreshBox2.getChildren().get(1);
        var diBox = new HBox(10, dispThreshBox1, dispThreshBox2);

        setupValidation(textFieldDegree1, "\\d{2}", "Please enter a valid degree (0-36) e.g. 09 not 9");
        setupValidation(textFieldDegree2, "\\d{2}", "Please enter a valid degree (0-36) e.g. 09 not 9");
        setupValidation(textFieldStopway1, "\\d+", "Please enter a valid stopway length (+ve integer)");
        setupValidation(textFieldStopway2, "\\d+", "Please enter a valid stopway length (+ve integer)");
        setupValidation(textFieldClearway1, "\\d+", "Please enter a valid clearway length (+ve integer)");
        setupValidation(textFieldClearway2, "\\d+", "Please enter a valid clearway length (+ve integer)");
        setupValidation(textFieldTORA1, "\\d+", "Please enter a valid TORA length (+ve integer)");
        setupValidation(textFieldTORA2, "\\d+", "Please enter a valid TORA length (+ve integer)");
        setupValidation(textFieldDispThresh1, "\\d+", "Please enter a valid displaced threshold length (+ve integer)");
        setupValidation(textFieldDispThresh2, "\\d+", "Please enter a valid displaced threshold length (+ve integer)");

        Button submitButton = new Button("Add");
        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Add");
        submitButton.setOnAction(e -> {
            String validationError = validateInputs(new TextField[]{textFieldDegree1, textFieldDegree2, textFieldStopway1, textFieldStopway2, textFieldClearway1, textFieldClearway2, textFieldTORA1, textFieldTORA2, textFieldDispThresh1, textFieldDispThresh2});
            if (validationError.isEmpty()) {
                    int stopway1 = Integer.parseInt(textFieldStopway1.getText());
                    int clearway1 = Integer.parseInt(textFieldClearway1.getText());
                    int TORA1 = Integer.parseInt(textFieldTORA1.getText());
                    int dispThresh1 = Integer.parseInt(textFieldDispThresh1.getText());

                    int stopway2 = Integer.parseInt(textFieldStopway2.getText());
                    int clearway2 = Integer.parseInt(textFieldClearway2.getText());
                    int TORA2 = Integer.parseInt(textFieldTORA2.getText());
                    int dispThresh2 = Integer.parseInt(textFieldDispThresh2.getText());

                    Runway testRunway1 = new Runway(textFieldDegree1.getText(), stopway1, clearway1, TORA1, dispThresh1);
                    Runway testRunway2 = new Runway(textFieldDegree2.getText(), stopway2, clearway2, TORA2, dispThresh2);
                    selectedAirport.addNewRunway(testRunway1, testRunway2);
                    app.updateXMLs();
                    updateAirportInfo(selectedAirport);
                    Stage stage = (Stage) promptWindow.getScene().getWindow();
                    stage.close();
                    app.showNotification("Runway Add ", "Runway " + testRunway1.getName()+"/"+testRunway2.getName()+"has been added");
        } else {
            showErrorDialog(validationError); // Show the first encountered validation error
        }
    });

        promptWindow.getChildren().addAll(runways, deBox, sBox, cBox, tBox, diBox, submitButton);
        dialogGenerator(promptWindow, "Add NEW Runways");
    }

    private void setupValidation(TextField textField, String pattern, String errorMessage) {
        textField.getProperties().put("validationPattern", pattern);
        textField.getProperties().put("errorMessage", errorMessage);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(pattern)) {
                textField.setStyle("-fx-control-inner-background: red;");
            } else {
                textField.setStyle("-fx-control-inner-background: white;");
            }
        });
    }

    private String validateInputs(TextField[] textFields) {
        for (TextField textField : textFields) {
            String pattern = (String) textField.getProperties().get("validationPattern");
            String errorMessage = (String) textField.getProperties().get("errorMessage");
            if (!textField.getText().matches(pattern)) {
                return errorMessage;
            }
        }
        return "";
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
            app.updateXMLs();
            updateAirportInfo(airport);
            Stage stage = (Stage) promptWindow.getScene().getWindow();
            stage.close();
            app.showNotification("Airport Update", "Airport " + selectedAirport.getAirportName() + " has been updated.");
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
