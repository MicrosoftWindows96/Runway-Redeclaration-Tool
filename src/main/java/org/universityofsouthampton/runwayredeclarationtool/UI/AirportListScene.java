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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;
import org.universityofsouthampton.runwayredeclarationtool.utility.importXML;

import java.io.File;
import java.util.ArrayList;

public class AirportListScene extends VBox {

    private final ScrollPane scrollPane = new ScrollPane();
    private final MainApplication app;
    private final ObservableList<Airport> airportsObserve = FXCollections.observableArrayList();
    private final ArrayList<Airport> importedAirports;
    private Airport selectedAirport;
    private Button currentlySelectedButton;
    private final VBox infoBox = new VBox(10);

    public AirportListScene(MainApplication app) {
        this.app = app;

        setPadding(new Insets(20));
        setSpacing(10);
        setAlignment(Pos.CENTER);

        Text title = new Text("List of Airports");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        BorderPane mainPane = new BorderPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; ");
        importedAirports = app.getAirports();
        updateList();

        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5;");
        updateAirportInfo(null);

        mainPane.setLeft(scrollPane);
        mainPane.setCenter(infoBox);

        HBox buttonBox = createButtonBox();

        getChildren().addAll(title, mainPane, buttonBox);
    }

    private HBox createButtonBox() {
        Button selectButton = new Button("Select");
        styleButton(selectButton, MaterialDesign.MDI_PLUS_BOX, "Select");
        selectButton.setOnAction(e -> {
            if (this.selectedAirport != null) {
                app.displayRunwayListScene(selectedAirport);
            }
        });

        Button backButton = new Button("Return");
        styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        backButton.setOnAction(e -> app.displayAirportScene());

        Button deleteButton = new Button("Delete");
        styleButton(deleteButton, MaterialDesign.MDI_DELETE, "Delete");
        deleteButton.setOnAction(e -> {
            importedAirports.remove(selectedAirport);
            updateList();
            app.updateAirportsXML();
            updateAirportInfo(null);
        });

        Button importXMLButton = new Button("Import");
        styleButton(importXMLButton, MaterialDesign.MDI_DOWNLOAD, "Import");
        importXMLButton.setOnAction(e -> importAirportsFromXML());

        Button exportXMLButton = new Button("Export");
        styleButton(exportXMLButton, MaterialDesign.MDI_UPLOAD, "Export");

        Button modifyButton = new Button("Modify");
        styleButton(modifyButton, MaterialDesign.MDI_WRENCH, "Modify");
        modifyButton.setOnAction(e -> {
            if (selectedAirport != null) {
                showModifyAirportDialog(selectedAirport);
            }
        });

        HBox buttonBox = new HBox(10, backButton, selectButton, modifyButton, deleteButton, importXMLButton, exportXMLButton);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }

    private void updateList() {
        VBox airportsBox = new VBox(5);
        airportsBox.setAlignment(Pos.CENTER_LEFT);
        airportsBox.setPadding(new Insets(10));
        scrollPane.setContent(airportsBox);

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

    private void setSelectedAirport(Airport airport) {
        this.selectedAirport = airport;
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        }
    }

    
    private void updateAirportInfo(Airport airport) {
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
            for (var runway : airport.getRunways()) {
                Text runwayInfo = new Text("Runway: " + runway.getName() + " - Length: " + runway.getTORA() + "m, Obstacles: " + runway.getObstacles().size());
                runwayInfo.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

                Button configureButton = new Button("Configure");
                configureButton.setOnAction(e -> app.displayRunwayConfigScene(airport, runway));
                styleButton(configureButton, MaterialDesign.MDI_SETTINGS, "Configure");

                Button deleteRunwayButton = new Button("Delete");
                deleteRunwayButton.setOnAction(e -> {
                    airport.getRunways().remove(runway);
                    app.updateAirportsXML();
                    updateAirportInfo(airport);
                });
                styleButton(deleteRunwayButton, MaterialDesign.MDI_DELETE, "Delete");

                HBox runwayButtonsBox = new HBox(10, configureButton, deleteRunwayButton);
                runwaysInfoBox.getChildren().addAll(runwayInfo, runwayButtonsBox);
            }

            Button addRunwayButton = new Button("Add Runway");
            addRunwayButton.setOnAction(e -> promptAddRunwayForm(app));
            styleButton(addRunwayButton, MaterialDesign.MDI_PLUS_BOX, "Add");

            ScrollPane runwaysScrollPane = new ScrollPane(runwaysInfoBox);
            runwaysScrollPane.setFitToWidth(true);
            runwaysScrollPane.setMaxHeight(150);

            VBox detailsBox = new VBox(5, airportName, airportCode, numRunways, runwaysScrollPane, addRunwayButton);
            detailsBox.setPadding(new Insets(10));
            detailsBox.setStyle("-fx-background-color: #eaeaea; -fx-border-color: #c0c0c0; -fx-border-width: 1; -fx-border-radius: 5;");
            infoBox.getChildren().add(detailsBox);
        }
    }

    private void promptAddRunwayForm(MainApplication app) {
        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));

        Label degreeLabel = new Label("Degree:");
        TextField degreeInput = new TextField();
        styleTextField(degreeInput);
        degreeInput.setPromptText("Degree");

        Label directionLabel = new Label("Direction (L, R, C):");
        TextField directionInput = new TextField();
        styleTextField(directionInput);
        directionInput.setPromptText("Direction (L, R, C)");

        Label TORALabel = new Label("TORA:");
        TextField TORAInput = new TextField();
        styleTextField(TORAInput);

        Label TODALabel = new Label("TODA:");
        TextField TODAInput = new TextField();
        styleTextField(TODAInput);

        Label ASDALabel = new Label("ASDA:");
        TextField ASDAInput = new TextField();
        styleTextField(ASDAInput);

        Label LDALabel = new Label("LDA:");
        TextField LDAInput = new TextField();
        styleTextField(LDAInput);

        Label DisThreshLabel = new Label("Displaced Threshold:");
        TextField DisThreshInput = new TextField();
        styleTextField(DisThreshInput);

        Button submitButton = new Button();
        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Add");

        Button cancelButton = new Button();
        styleButton(cancelButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) form.getScene().getWindow();
            stage.close();
        });

        submitButton.setOnAction(e -> {
            String degree = degreeInput.getText().trim();
            String direction = directionInput.getText().trim();
            String name = degree + direction;


            if (!name.matches("\\d{2}[LCR]")) {
                showErrorDialog("Invalid runway name. The name must consist of two digits followed by L, C, or R.");
            } else {
                try {
                    int TORA = Integer.parseInt(TORAInput.getText());
                    int TODA = Integer.parseInt(TODAInput.getText());
                    int ASDA = Integer.parseInt(ASDAInput.getText());
                    int LDA = Integer.parseInt(LDAInput.getText());
                    int DisThresh = Integer.parseInt(DisThreshInput.getText());

                    if (TODA <= 0 || TORA <= 0 || ASDA <= 0 || LDA <= 0 || DisThresh < 0) {
                        throw new IllegalArgumentException("Invalid measurements for runway.");
                    }

                    Stage stage = (Stage) form.getScene().getWindow();
                    ArrayList<Runway> runways = selectedAirport.getRunways();
                    runways.add(new Runway(degree, direction, TODA, TORA, ASDA, LDA, DisThresh));
                    app.updateAirportsXML();
                    updateList();
                    stage.close();

                } catch (NumberFormatException ex) {
                    showErrorDialog("Invalid input for number of runways. Please enter a valid integer.");
                } catch (IllegalArgumentException ex) {
                    showErrorDialog(ex.getMessage());
                }
            }
        });

        groupLabels(form, degreeLabel, degreeInput, directionLabel, directionInput, TORALabel, TORAInput, TODALabel, TODAInput, ASDALabel, ASDAInput, LDALabel, LDAInput, DisThreshLabel, DisThreshInput, submitButton, cancelButton);
    }

    static void groupLabels(VBox form, Label degreeLabel, TextField degreeInput, Label directionLabel, TextField directionInput, Label TORALabel, TextField TORAInput, Label TODALabel, TextField TODAInput, Label ASDALabel, TextField ASDAInput, Label LDALabel, TextField LDAInput, Label disThreshLabel, TextField disThreshInput, Button submitButton, Button cancelButton) {
        form.getChildren().addAll(degreeLabel, degreeInput, directionLabel, directionInput, TORALabel, TORAInput, TODALabel, TODAInput,
                ASDALabel, ASDAInput, LDALabel, LDAInput, disThreshLabel, disThreshInput,
                submitButton, cancelButton);

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Add Runway");
        AirportScene.extractedDialogStageMethod(form, dialogStage);
    }

    private void styleTextField(TextField textField) {
        textField.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        textField.setFont(Font.font("Arial", 16));
    }

    private void showErrorDialog(String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox dialogVbox = new VBox(20);

        Text errorMessage = new Text(message);
        Button okButton = new Button();
        styleButton(okButton, MaterialDesign.MDI_CHECK, "OK");

        okButton.setOnAction(e -> dialog.close());
        dialogVbox.setPadding(new Insets(20));

        dialogVbox.getChildren().addAll(errorMessage, okButton);
        Scene dialogScene = new Scene(dialogVbox);
        dialog.setScene(dialogScene);
        dialog.sizeToScene();
        dialog.centerOnScreen();
        dialogVbox.setAlignment(Pos.CENTER);

        dialog.showAndWait();
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
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            importXML importer = new importXML(file);
            ArrayList<Airport> airports = importer.makeAirportsXML();
            app.mergeAirport(airports);
            updateList();
        }
    }

    private void showModifyAirportDialog(Airport airport) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Modify Airport");

        TextField nameInput = new TextField(airport.getAirportName());
        TextField codeInput = new TextField(airport.getAirportCode());

        Button submitButton = new Button("Save Changes");
        submitButton.setOnAction(e -> {
            airport.setAirportName(nameInput.getText());
            airport.setAirportCode(codeInput.getText());
            updateList();
            app.updateAirportsXML();
            updateAirportInfo(airport);
            dialogStage.close();
        });

        VBox dialogVBox = new VBox(10, new Label("Airport Name:"), nameInput, new Label("Airport Code:"), codeInput, submitButton);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setPadding(new Insets(20));

        Scene dialogScene = new Scene(dialogVBox);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }
}
