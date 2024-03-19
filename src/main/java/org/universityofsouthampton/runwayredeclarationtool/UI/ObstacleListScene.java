package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.ParallelRunways;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;
import java.util.Arrays;

public class ObstacleListScene extends BaseScene {

    private final ScrollPane currentObstacleScroll = new ScrollPane(); // Scroll pane to display the obstacles of a selected runway
    private final ScrollPane otherObstaclesScroll = new ScrollPane(); // Scroll pane to display the obstacles that can be added to the runway
    private ArrayList<Obstacle> otherObstacles; // ArrayList of obstacles from XML
    private Obstacle selectedObstacle; // Obstacle that's currently in the selection box
    private final Airport currentAirport; // Airport that was selected to access its properties
    private final Runway currentRunway; // Runway selected to access it's properties
    private final ParallelRunways runwayManager; // This stores the list of parallel runways

    public ObstacleListScene(MainApplication app, Airport airport, ParallelRunways runwaySet) {
        this.app = app;
        this.currentAirport = airport;
        this.runwayManager = runwaySet;
        this.currentRunway = runwaySet.getCurrentRunways().getKey();
        this.otherObstacles = app.getObstacles();
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(20));
        setSpacing(10);

        // Initialise selectedObstacle if there's a pre-determined obstacle in the runway
        if (!currentRunway.getObstacles().isEmpty()) {
            selectedObstacle = currentRunway.getObstacles().getFirst();
        }

        var title = new Text("Obstacle Update");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        VBox.setMargin(title, new Insets(10, 0, 20, 0));

        Text title2 = new Text("List of Obstacles in " + airport.getAirportCode() + "-" + currentRunway.getName() + currentRunway.getDirection());
        title.setFont(Font.font("Arial", 20));

        Text title3 = new Text("Other obstacles ")  ;
        title.setFont(Font.font("Arial", 20));

        this.currentObstacleScroll.setFitToWidth(true);
        this.currentObstacleScroll.setPrefSize(700, 500);
        this.otherObstaclesScroll.setFitToWidth(true);
        this.otherObstaclesScroll.setPrefSize(700, 500);
        this.otherObstacles = app.getObstacles();
        updateObstaclesList();

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButtons());

        this.getChildren().addAll(title,title2,this.currentObstacleScroll,title3,this.otherObstaclesScroll,buttonBox);
    }

    @Override
    ArrayList<Button> addButtons() {
        Button addButton = new Button();
        styleButton(addButton, MaterialDesign.MDI_PLUS, "Add");
        addButton.setOnAction(e -> {
            if (this.selectedObstacle == null) {
                System.out.println("Nothing Selected!");
            } else {
                if (!this.currentRunway.getObstacles().isEmpty()) {
                    // Replace the current Obstacle with selected one
                    Obstacle existingObstacle = this.currentRunway.getObstacles().getFirst();
                    this.otherObstacles.add(existingObstacle);

                    // update runway object
                    runwayManager.removeObstacle(existingObstacle);
                }
                runwayManager.placeObstacle(selectedObstacle);
                this.otherObstacles.remove(this.selectedObstacle);
                updateObstaclesList();
            }
        });

        Button removeButton = new Button();
        styleButton(removeButton, MaterialDesign.MDI_MINUS, "Remove");
        removeButton.setOnAction(e -> {
            if (this.selectedObstacle == null || this.currentRunway.getObstacles().isEmpty() || !this.currentRunway.getObstacles().contains(this.selectedObstacle)) {
                System.out.println("Nothing Selected or not in current obstacles!");
            } else {
                this.otherObstacles.add(this.selectedObstacle);
                runwayManager.removeObstacle(selectedObstacle);
                if (!currentRunway.getObstacles().isEmpty()) {
                    runwayManager.removeObstacle(selectedObstacle);
                }
                updateObstaclesList();
            }
        });

        Button createButton = new Button();
        styleButton(createButton, MaterialDesign.MDI_PLUS_BOX, "Create");
        createButton.setOnAction(e -> promptCreateObstacle());
        
        Button editButton = new Button();
        styleButton(editButton, MaterialDesign.MDI_WRENCH,"Modify");
        editButton.setOnAction(e -> {
            if (this.selectedObstacle != null) {
                promptEditObstacle();
            } else {
                System.out.println("Nothing Selected!");
            }
        });

        Button deleteButton = new Button();
        styleButton(deleteButton, MaterialDesign.MDI_MINUS_BOX, "Delete");
        deleteButton.setOnAction(e -> {
            if (this.selectedObstacle == null) {
                System.out.println("Nothing Selected!");
            } else if  (this.currentRunway.getObstacles().isEmpty() || !this.currentRunway.getObstacles().contains(this.selectedObstacle)) {
                this.otherObstacles.remove(this.selectedObstacle);
                updateObstaclesList();
                app.updateXMLs();
            } else {
                System.out.println("Obstacle is in current obstacles!");
            }
        });

        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        backButton.setOnAction(e -> {
            // If theres no selected obstacles go back
            if (currentRunway.getObstacles().isEmpty()) {
                app.displayRunwayConfigScene(currentAirport,runwayManager);
                app.updateXMLs();
            } else {
                promptSetBPV();
            }
        });

        return new ArrayList<>(
            Arrays.asList(addButton, removeButton, createButton, deleteButton, editButton, backButton));
    }

    private void updateObstaclesList() {
        var currentObstaclesBox = new VBox();
        var otherObstaclesBox = new VBox();
        currentObstaclesBox.setSpacing(5);
        otherObstaclesBox.setSpacing(5);

        this.currentRunway.getObstacles().forEach(obstacle -> {
            var name = obstacle.getName();
            var obstacleButton = new Button(name);
            styleButton(obstacleButton, MaterialDesign.MDI_EYE, name);
            obstacleButton.setOnMouseClicked(event -> {
                setSelectedObstacle(obstacle);
                System.out.println("Currently selected: " + getSelectedObstacle().getName());
            });
            currentObstaclesBox.getChildren().add(obstacleButton);
        });
        this.currentObstacleScroll.setContent(currentObstaclesBox);

        this.otherObstacles.forEach(obstacle -> {
            var name = obstacle.getName();
            var obstacleButton = new Button(name);
            styleButton(obstacleButton, MaterialDesign.MDI_EYE, name);
            obstacleButton.setOnMouseClicked(event -> {
                setSelectedObstacle(obstacle);
                System.out.println("Currently selected: " + getSelectedObstacle().getName());
            });
            otherObstaclesBox.getChildren().add(obstacleButton);
        });
        this.otherObstaclesScroll.setContent(otherObstaclesBox);
    }

    // PROMPT METHODS
    private void promptCreateObstacle() {
        PromptWindow promptWindow = new PromptWindow(app);
        var nameBox = promptWindow.addParameterField("Name");
        TextField textFieldName = (TextField) nameBox.getChildren().get(1);
        var heightBox = promptWindow.addParameterField("Height (m)");
        TextField textFieldHeight = (TextField) heightBox.getChildren().get(1);
        var distFromThreBox = promptWindow.addParameterField("Distance From Threshold (m)");
        TextField textFieldDistFromThre = (TextField) distFromThreBox.getChildren().get(1);
        var distFromCentBox = promptWindow.addParameterField("Distance From Centreline (m)");
        TextField textFieldDistFromCent = (TextField) distFromCentBox.getChildren().get(1);

        // Set up validation
        setupValidation(textFieldName, "^[A-Za-z0-9- ]+$", "Please enter a valid name. Alphanumeric and hyphens only.");
        setupValidation(textFieldHeight, "\\d+", "Please enter a valid height. Positive integers only.");
        setupValidation(textFieldDistFromThre, "\\d+", "Please enter a valid distance. Positive integers only.");
        setupValidation(textFieldDistFromCent, "\\d+", "Please enter a valid distance. Positive integers only.");

        Button submitButton = new Button("Create");
        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Create");
        submitButton.setOnAction(e -> {
            String errorMessage = validateInputs(new TextField[]{textFieldName, textFieldHeight, textFieldDistFromThre, textFieldDistFromCent});
            if (errorMessage.isEmpty()) {
                String name = textFieldName.getText();
                int height = Integer.parseInt(textFieldHeight.getText());
                int distFromThre = Integer.parseInt(textFieldDistFromThre.getText());
                int distFromCent = Integer.parseInt(textFieldDistFromCent.getText());

                Obstacle newObstacle = new Obstacle(name, height, distFromThre, distFromCent);
                otherObstacles.add(newObstacle);
                app.showNotification("Obstacle Created", "Obstacle " + name + " created successfully.");
                updateObstaclesList();
                ((Stage) promptWindow.getScene().getWindow()).close();
            } else {
                showErrorDialog(errorMessage);
            }
        });

        promptWindow.getChildren().addAll(nameBox, heightBox, distFromThreBox, distFromCentBox, submitButton);
        dialogGenerator(promptWindow, "Create Obstacle");
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


    private void promptEditObstacle() {
        if (selectedObstacle == null) {
            System.out.println("No obstacle selected for editing.");
            return;
        }

        PromptWindow promptWindow = new PromptWindow(app);
        var nameBox = promptWindow.editParameterField("Name", selectedObstacle.getName());
        TextField textFieldName = (TextField) nameBox.getChildren().get(1);
        var heightBox = promptWindow.editParameterField("Height (m)", String.valueOf(selectedObstacle.getHeight()));
        TextField textFieldHeight = (TextField) heightBox.getChildren().get(1);
        var distFromThreBox = promptWindow.editParameterField("Distance From Threshold (m)", String.valueOf(selectedObstacle.getDistanceFromThreshold()));
        TextField textFieldDistFromThre = (TextField) distFromThreBox.getChildren().get(1);
        var distFromCentBox = promptWindow.editParameterField("Distance From Centreline (m)", String.valueOf(selectedObstacle.getDistanceFromCentreline()));
        TextField textFieldDistFromCent = (TextField) distFromCentBox.getChildren().get(1);

        // Set up validation
        setupValidation(textFieldName, "^[A-Za-z0-9- ]+$", "Please enter a valid name. Alphanumeric and hyphens only.");
        setupValidation(textFieldHeight, "\\d+", "Please enter a valid height. Positive integers only.");
        setupValidation(textFieldDistFromThre, "\\d+", "Please enter a valid distance. Positive integers only.");
        setupValidation(textFieldDistFromCent, "\\d+", "Please enter a valid distance. Positive integers only.");

        Button submitButton = new Button("Save Changes");
        styleButton(submitButton, MaterialDesign.MDI_CHECK, "Save");
        submitButton.setOnAction(e -> {
            String errorMessage = validateInputs(new TextField[]{textFieldName, textFieldHeight, textFieldDistFromThre, textFieldDistFromCent});
            if (errorMessage.isEmpty()) {
                String name = textFieldName.getText();
                int height = Integer.parseInt(textFieldHeight.getText());
                int distFromThre = Integer.parseInt(textFieldDistFromThre.getText());
                int distFromCent = Integer.parseInt(textFieldDistFromCent.getText());

                selectedObstacle.setName(name);
                selectedObstacle.setHeight(height);
                selectedObstacle.setDistanceFromThreshold(distFromThre);
                selectedObstacle.setDistanceFromCentreline(distFromCent);

                app.showNotification("Obstacle Updated", "Obstacle " + name + " updated successfully.");
                updateObstaclesList();
                ((Stage) promptWindow.getScene().getWindow()).close();
            } else {
                showErrorDialog(errorMessage);
            }
        });

        promptWindow.getChildren().addAll(nameBox, heightBox, distFromThreBox, distFromCentBox, submitButton);
        dialogGenerator(promptWindow, "Edit Obstacle");

    }

    private void promptSetBPV() {
        PromptWindow promptWindow = new PromptWindow(app);
        var BPVBox = promptWindow.addParameterField("Blast Protection Value");

        // Implement Submit button
        Button submitButton = new Button();
        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Create");
        submitButton.setOnAction(e -> {
            String BPV = promptWindow.getInput(BPVBox);
            if (BPV.isEmpty()) {
                showErrorDialog("Blast Protection Value Required. Please fill in all fields.");
            } else {
                try {
                    int BPVInt = Integer.parseInt(BPV);
                    if (BPVInt <= 0) {
                        throw new IllegalArgumentException("Invalid measurements for Blast Protection Value");
                    }
                    // update the runway object to run calculations to display in the config runway scene
                    runwayManager.setBPV(BPVInt);
                    runwayManager.runCalcOnBothRunways();
                    app.displayRunwayConfigScene(currentAirport,runwayManager);
                    app.updateXMLs();
                    Stage stage = (Stage) promptWindow.getScene().getWindow();
                    stage.close();

                } catch (NumberFormatException ex) {
                    showErrorDialog("Invalid input for number of BPV. Please enter a valid integer.");
                } catch (IllegalArgumentException ex) {
                    showErrorDialog(ex.getMessage());
                }
            }
        });
        promptWindow.getChildren().addAll(BPVBox, submitButton);
        promptWindow.getChildren().addAll(promptWindow.addButtons());
        dialogGenerator(promptWindow, "Set Blast Protection Value");
    }

    public void setSelectedObstacle(Obstacle selectedObstacle) {
        this.selectedObstacle = selectedObstacle;
    }

    public Obstacle getSelectedObstacle() {
        return this.selectedObstacle;
    }
}