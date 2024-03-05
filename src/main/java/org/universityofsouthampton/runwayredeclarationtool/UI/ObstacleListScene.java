package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;

public class ObstacleListScene extends VBox {
    /**
     * Scroll pane to display the obstacles of a selected runway
     */
    private ScrollPane currentObstacleScroll = new ScrollPane();

    /**
     * Scroll pane to display the obstacles that can be added to the runway
     */
    private ScrollPane otherObstaclesScroll = new ScrollPane();

    /**
     * Observable arraylist of obstacles to be displayed already in the runway
     */
    private ObservableList<Obstacle> currentObstaclesObserve = FXCollections.observableArrayList();

    /**
     * Observable arraylist of obstacles to be displayed that are not in runway
     */
    private ObservableList<Obstacle> otherObstaclesObserve = FXCollections.observableArrayList();

    /**
     * ArrayList of obstacles for selected runway
     */
    private ArrayList<Obstacle> currentObstacles;

    /**
     * ArrayList of obstacles from XML
     */
    private ArrayList<Obstacle> otherObstacles;


    /**
     *
     */
    private Obstacle selectedObstacle;

    private Runway runway;

    private Airport airport;

    public ObstacleListScene(MainApplication app, Airport airport, Runway runway) {
        setAlignment(Pos.TOP_CENTER);
        this.airport = airport;
        this.runway = runway;

        currentObstacles = runway.getObstacles();
        otherObstacles = app.getObstacles();

        var title = new Text("Obstacle Update");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        VBox.setMargin(title, new Insets(10, 0, 20, 0));


        Text title2 = new Text("List of Obstacles in " + this.airport.getAirportCode() + "--" + this.runway.getName())  ;
        title.setFont(Font.font("Arial", 20));

        Text title3 = new Text("Other obstacles ")  ;
        title.setFont(Font.font("Arial", 20));

        setPadding(new Insets(20));
        setSpacing(10);





        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        backButton.setOnAction(e -> {
            app.displayRunwayConfigScene(airport,runway);
            app.updateXMLs();
        });

        Button createButton = new Button();
        styleButton(createButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Create");
        createButton.setOnAction(e -> {
            promptCreateObstacleForm(app);
        });





        this.currentObstacleScroll.setFitToWidth(true);
        this.currentObstacleScroll.setPrefSize(700, 500);
        this.currentObstacles = this.runway.getObstacles();
        this.otherObstaclesScroll.setFitToWidth(true);
        this.otherObstaclesScroll.setPrefSize(700, 500);
        this.otherObstacles = app.getObstacles();
        updateObstaclesList();


        Button addButton = new Button();
        styleButton(addButton, MaterialDesign.MDI_PLUS_BOX, "Add");
        addButton.setOnAction(e -> {
            // Handle airport selection (we may need to implement database/xml func early)
            // Selection error handling
            if (this.selectedObstacle == null) {
                System.out.println("Nothing Selected!");
            } else if (this.currentObstacles.contains(this.selectedObstacle )) {
                System.out.println("Already in current obstacles");
            } else {
                this.currentObstacles.add(this.selectedObstacle);
                this.otherObstacles.remove(this.selectedObstacle);
                updateObstaclesList();
            }});

        Button removeButton = new Button();
        styleButton(removeButton, MaterialDesign.MDI_PLUS_BOX, "Remove");
        removeButton.setOnAction(e -> {
            // Handle airport selection (we may need to implement database/xml func early)
            // Selection error handling
            if (this.selectedObstacle == null) {
                System.out.println("Nothing Selected!");}
            else if (this.otherObstacles.contains(this.selectedObstacle )) {
                System.out.println("Already in other obstacles");
            }
            else {
                this.otherObstacles.add(this.selectedObstacle);
                this.currentObstacles.remove(this.selectedObstacle);
                updateObstaclesList();
            }});

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(backButton,addButton,removeButton,createButton);

        this.getChildren().addAll(title,title2,this.currentObstacleScroll,title3,this.otherObstaclesScroll,buttonBox);
    }

    private void viewObstacleInfo(Obstacle obstacle) {

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

//    private void updateRunwayData(){
//
//    }

    private void promptCreateObstacleForm(MainApplication app) {
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
        // Add airport

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


            // Validate
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
                    // Data valid, add runway
                    // For now, just close the form
                    Stage stage = (Stage) form.getScene().getWindow();

                    // add the Runway into the list
                    otherObstacles.add(new Obstacle(name,height,distFromThre,distFromCent));
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

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Create Obstacle");
        dialogStage.setScene(new Scene(form));

        double centerX = Screen.getPrimary().getVisualBounds().getWidth() / 2;
        double centerY = Screen.getPrimary().getVisualBounds().getHeight() / 2;
        dialogStage.setX(centerX - 150);
        dialogStage.setY(centerY - 100);
        dialogStage.showAndWait();
    }
    private void updateObstaclesList() {


        var currentObstaclesBox = new VBox();
        var otherObstaclesBox = new VBox();
        currentObstaclesBox.setSpacing(5);
        otherObstaclesBox.setSpacing(5);


        for (Obstacle obstacle : this.currentObstacles) {
            var name = (" -- " + obstacle.getName() + " -- ");
            var obstacleButton = new Button(name);

            // Button to select the airport
            obstacleButton.setOnMouseClicked(event -> {
                setSelectedObstacle(obstacle);
                System.out.println("Currently selected: " + getSelectedObstacle().getName());
            });

            currentObstaclesBox.getChildren().add(obstacleButton);

            currentObstaclesObserve.add(obstacle);
        }

        currentObstacleScroll.setContent(currentObstaclesBox);

        for (Obstacle obstacle : this.otherObstacles) {
            var name = (" -- " + obstacle.getName() + " -- ");
            var obstacleButton = new Button(name);

            // Button to select the airport
            obstacleButton.setOnMouseClicked(event -> {
                setSelectedObstacle(obstacle);
                System.out.println("Currently selected: " + getSelectedObstacle().getName());
            });

            otherObstaclesBox.getChildren().add(obstacleButton);

            otherObstaclesObserve.add(obstacle);
        }

        currentObstacleScroll.setContent(currentObstaclesBox);
        otherObstaclesScroll.setContent(otherObstaclesBox);
    }


    public void setSelectedObstacle(Obstacle selectedObstacle) {
        this.selectedObstacle = selectedObstacle;
    }

    public Obstacle getSelectedObstacle() {
        return this.selectedObstacle;
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



}