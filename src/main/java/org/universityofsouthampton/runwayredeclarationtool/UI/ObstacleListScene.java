package org.universityofsouthampton.runwayredeclarationtool.UI;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

public class ObstacleListScene extends BaseScene {

    private final ScrollPane currentObstacleScroll = new ScrollPane(); // Scroll pane to display the obstacles of a selected runway
    private final ScrollPane otherObstaclesScroll = new ScrollPane(); // Scroll pane to display the obstacles that can be added to the runway
    private ArrayList<Obstacle> otherObstacles; // ArrayList of obstacles from XML
    private Obstacle selectedObstacle; // Obstacle that's currently in the selection box
    private final Airport currentAirport; // Airport that was selected to access its properties
    private final Runway currentRunway; // Runway selected to access it's properties

    public ObstacleListScene(MainApplication app, Airport airport, Runway runway) {
        this.app = app;
        this.currentAirport = airport;
        this.currentRunway = runway;
        this.otherObstacles = app.getObstacles();
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(20));
        setSpacing(10);

        // Initialise selectedObstacle if there's a pre-determined obstacle in the runway
        if (!runway.getObstacles().isEmpty()) {
            selectedObstacle = runway.getObstacles().get(0);
        }

        var title = new Text("Obstacle Update");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        VBox.setMargin(title, new Insets(10, 0, 20, 0));

        Text title2 = new Text("List of Obstacles in " + airport.getAirportCode() + "-" + runway.getName() + runway.getDirection());
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
                    Obstacle existingObstacle = this.currentRunway.getObstacles().get(0);
                    this.otherObstacles.add(existingObstacle);

                    // update runway object
                    currentRunway.removeObstacle(existingObstacle);
                }
                currentRunway.addObstacle(selectedObstacle);
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
                this.currentRunway.removeObstacle(this.selectedObstacle);
                if (!currentRunway.getObstacles().isEmpty()) {
                    currentRunway.removeObstacle(selectedObstacle);
                }
                updateObstaclesList();
            }
        });

        Button createButton = new Button();
        styleButton(createButton, MaterialDesign.MDI_PLUS_BOX, "Create");
        createButton.setOnAction(e -> promptCreateObstacleForm());
        
        Button editButton = new Button();
        styleButton(editButton, MaterialDesign.MDI_WRENCH,"Configure");
        editButton.setOnAction(e -> {
            if (this.selectedObstacle != null) {
                promptEDITObstacleForm();
            } else {
                System.out.println("Nothing Selected!");
            }
        });

        Button deleteButton = new Button();
        styleButton(deleteButton, MaterialDesign.MDI_DELETE, "Delete");
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
                app.displayRunwayConfigScene(currentAirport,currentRunway);
                app.updateXMLs();
            } else {
                promptAddBPV();
            }
        });

        return new ArrayList<>(
            Arrays.asList(addButton, removeButton, createButton, editButton, deleteButton, backButton));
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

    private void promptCreateObstacleForm() {
        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));

        Label nameLabel = new Label("Name:");
        TextField nameInput = new TextField();
        styleTextField(nameInput);

        Label heightLabel = new Label("Height:");
        TextField heightInput = new TextField();
        styleTextField(heightInput);

        Label distFromThreLabel = new Label("Distance From Threshold:");
        TextField distFromThreInput = new TextField();
        styleTextField(distFromThreInput);

        Label distFromCentLabel= new Label("Distance From Centreline:");
        TextField distFromCentInput = new TextField();
        styleTextField(distFromCentInput);

        Button submitButton = new Button();
        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Create");

        Button cancelButton = new Button();
        styleButton(cancelButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) form.getScene().getWindow();
            stage.close();
        });

        submitButton.setOnAction(e -> {
            String name = nameInput.getText();
            String heightName = heightInput.getText();
            String distFromThreName = distFromThreInput.getText();
            String distFromCentname = distFromCentInput.getText();

            if (name.isEmpty() || heightName.isEmpty() || distFromThreName.isEmpty() || distFromCentname.isEmpty()) {
                showErrorDialog("All fields are required. Please fill in all fields.");
            } else {
                try {
                    int height = Integer.parseInt(heightInput.getText());
                    int distFromThre = Integer.parseInt(distFromThreInput.getText());
                    int distFromCent = Integer.parseInt(distFromCentInput.getText());

                    if (height <= 0 || distFromThre <= 0 || distFromCent <= 0) {
                        throw new IllegalArgumentException("Invalid measurements for obstacle");
                    }

                    Stage stage = (Stage) form.getScene().getWindow();

                    otherObstacles.add(new Obstacle(name,height,distFromThre,distFromCent));
                    app.showNotification("Obstacle Warning", "Obstacle " + name + " created for " + currentAirport.getAirportCode() + " runway " + currentRunway.getName() + currentRunway.getDirection());
                    updateObstaclesList();

                    stage.close();

                } catch (NumberFormatException ex) {
                    showErrorDialog("Invalid input for number of obstacles. Please enter a valid integer.");
                } catch (IllegalArgumentException ex) {
                    showErrorDialog(ex.getMessage());
                }
            }
        });

        form.getChildren().addAll(nameLabel, nameInput, heightLabel, heightInput, distFromThreLabel, distFromThreInput, distFromCentLabel, distFromCentInput, submitButton, cancelButton);

        dialogGenerator(form, "Create Obstacle");
    }

    private void promptEDITObstacleForm() {
        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));

        Label nameLabel = new Label("Name:");
        TextField nameInput = new TextField(selectedObstacle.getName());
        styleTextField(nameInput);

        Label heightLabel = new Label("Height:");
        TextField heightInput = new TextField(Integer.toString(selectedObstacle.getHeight()));
        styleTextField(heightInput);

        Label distFromThreLabel = new Label("Distance From Threshold:");
        TextField distFromThreInput = new TextField(Integer.toString(selectedObstacle.getDistanceFromThreshold()));
        styleTextField(distFromThreInput);

        Label distFromCentLabel= new Label("Distance From Centreline:");
        TextField distFromCentInput = new TextField(Integer.toString(selectedObstacle.getDistanceFromCentreline()));
        styleTextField(distFromCentInput);

        Button submitButton = new Button();
        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Reconfigure");

        Button cancelButton = new Button();
        styleButton(cancelButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) form.getScene().getWindow();
            stage.close();
        });

        submitButton.setOnAction(e -> {
            String name = nameInput.getText();
            String heightName = heightInput.getText();
            String distFromThreName = distFromThreInput.getText();
            String distFromCentname = distFromCentInput.getText();

            if (name.isEmpty() || heightName.isEmpty() || distFromThreName.isEmpty() || distFromCentname.isEmpty()) {
                showErrorDialog("All fields are required. Please fill in all fields.");
            } else {
                try {
                    int height = Integer.parseInt(heightInput.getText());
                    int distFromThre = Integer.parseInt(distFromThreInput.getText());
                    int distFromCent = Integer.parseInt(distFromCentInput.getText());

                    if (height <= 0 || distFromThre <= 0 || distFromCent < 0) {
                        throw new IllegalArgumentException("Invalid measurements for obstacle");
                    }

                    Stage stage = (Stage) form.getScene().getWindow();

                    var newObstacle = new Obstacle(name,height,distFromThre,distFromCent);
                    // replace the obstacle in runway object
                    if (otherObstacles.contains(selectedObstacle)) {
                        otherObstacles.remove(selectedObstacle);
                        otherObstacles.add(newObstacle);
                        app.showNotification("Obstacle Warning", "Obstacle " + name + " reconfigured for " + currentAirport.getAirportCode() + " runway " + currentRunway.getName() + currentRunway.getDirection());
                    } else if (currentRunway.getObstacles().contains(selectedObstacle)) {
                        currentRunway.removeObstacle(selectedObstacle);
                        currentRunway.addObstacle(newObstacle);
                        app.showNotification("Obstacle Warning", "Obstacle " + name + " reconfigured for " + currentAirport.getAirportCode() + " runway " + currentRunway.getName() + currentRunway.getDirection());
                    }

                    updateObstaclesList();

                    stage.close();

                } catch (NumberFormatException ex) {
                    showErrorDialog("Invalid input for number of obstacles. Please enter a valid integer.");
                } catch (IllegalArgumentException ex) {
                    showErrorDialog(ex.getMessage());
                }
            }
        });

        form.getChildren().addAll(nameLabel, nameInput, heightLabel, heightInput, distFromThreLabel, distFromThreInput, distFromCentLabel, distFromCentInput, submitButton, cancelButton);

        dialogGenerator(form, "Edit Obstacle");
    }

    private void promptAddBPV() {
        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));

        Label BPVLabel = new Label("Blast Protection Value:");
        TextField BPVInput = new TextField();
        styleTextField(BPVInput);

        Button submitButton = new Button();
        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Create");

        Button cancelButton = new Button();
        styleButton(cancelButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) form.getScene().getWindow();
            stage.close();
        });

        submitButton.setOnAction(e -> {
            String BPV = BPVInput.getText();

            if (BPV.isEmpty()) {
                showErrorDialog("Blast Protection Value Required. Please fill in all fields.");
            } else {
                try {
                    int BPVInt = Integer.parseInt(BPV);

                    if (BPVInt <= 0) {
                        throw new IllegalArgumentException("Invalid measurements for Blast Protection Value");
                    }

                    Stage stage = (Stage) form.getScene().getWindow();

                    // update the runway object to run calculations to display in the config runway scene
                    currentRunway.setBlastProtectionValue(BPVInt);
                    currentRunway.runCalculations();
                    app.displayRunwayConfigScene(currentAirport,currentRunway);
                    app.updateXMLs();

                    stage.close();

                } catch (NumberFormatException ex) {
                    showErrorDialog("Invalid input for number of BPV. Please enter a valid integer.");
                } catch (IllegalArgumentException ex) {
                    showErrorDialog(ex.getMessage());
                }
            }
        });

        form.getChildren().addAll(BPVLabel, BPVInput, submitButton, cancelButton);

        dialogGenerator(form, "Set Blast Protection Value");
    }

    public void setSelectedObstacle(Obstacle selectedObstacle) {
        this.selectedObstacle = selectedObstacle;
    }

    public Obstacle getSelectedObstacle() {
        return this.selectedObstacle;
    }
}