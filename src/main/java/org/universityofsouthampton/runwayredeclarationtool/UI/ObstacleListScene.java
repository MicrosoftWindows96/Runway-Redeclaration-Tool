package org.universityofsouthampton.runwayredeclarationtool.UI;

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
    private final ScrollPane currentObstacleScroll = new ScrollPane();

    /**
     * Scroll pane to display the obstacles that can be added to the runway
     */
    private final ScrollPane otherObstaclesScroll = new ScrollPane();

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

    public ObstacleListScene(MainApplication app, Airport airport, Runway runway) {
        setAlignment(Pos.TOP_CENTER);

        currentObstacles = runway.getObstacles();
        otherObstacles = app.getObstacles();

        var title = new Text("Obstacle Update");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        VBox.setMargin(title, new Insets(10, 0, 20, 0));

        Text title2 = new Text("List of Obstacles in " + airport.getAirportCode() + "-" + runway.getName());
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
        styleButton(createButton, MaterialDesign.MDI_CREATION, "Create");
        createButton.setOnAction(e -> promptCreateObstacleForm());

        this.currentObstacleScroll.setFitToWidth(true);
        this.currentObstacleScroll.setPrefSize(700, 500);
        this.currentObstacles = runway.getObstacles();
        this.otherObstaclesScroll.setFitToWidth(true);
        this.otherObstaclesScroll.setPrefSize(700, 500);
        this.otherObstacles = app.getObstacles();
        updateObstaclesList();

        Button addButton = new Button();
        styleButton(addButton, MaterialDesign.MDI_PLUS_BOX, "Add");
        addButton.setOnAction(e -> {
            if (this.selectedObstacle == null) {
                System.out.println("Nothing Selected!");
            } else {
                if (!this.currentObstacles.isEmpty()) {
                    Obstacle existingObstacle = this.currentObstacles.getFirst();
                    this.otherObstacles.add(existingObstacle);
                    this.currentObstacles.clear();
                }
                this.currentObstacles.add(this.selectedObstacle);
                this.otherObstacles.remove(this.selectedObstacle);
                updateObstaclesList();
            }
        });

        Button modifyButton = new Button();
        styleButton(modifyButton, MaterialDesign.MDI_WRENCH, "Modify");
        modifyButton.setOnAction(e -> promptModifyObstacleForm());

        Button viewDetailsButton = new Button();
        styleButton(viewDetailsButton, MaterialDesign.MDI_EYE, "View");
        viewDetailsButton.setOnAction(e -> {
            if (selectedObstacle == null) {
                showErrorDialog("No obstacle selected to view details.");
            } else {
                showObstacleDetailsDialog(selectedObstacle);
            }
        });

        Button removeButton = new Button();
        styleButton(removeButton, MaterialDesign.MDI_MINUS_BOX, "Remove");
        removeButton.setOnAction(e -> {
            if (this.selectedObstacle == null || this.currentObstacles.isEmpty() || !this.currentObstacles.contains(this.selectedObstacle)) {
                System.out.println("Nothing Selected or not in current obstacles!");
            } else {
                this.otherObstacles.add(this.selectedObstacle);
                this.currentObstacles.remove(this.selectedObstacle);
                updateObstaclesList();
            }
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(backButton, addButton, removeButton, createButton, modifyButton, viewDetailsButton);

        this.getChildren().addAll(title,title2,this.currentObstacleScroll,title3,this.otherObstaclesScroll,buttonBox);
    }

    private void promptModifyObstacleForm() {
        if (selectedObstacle == null) {
            showErrorDialog("No obstacle selected for modification.");
            return;
        }

        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));

        Label nameLabel = new Label("Name:");
        TextField nameInput = new TextField(selectedObstacle.getName());
        styleTextField(nameInput);

        Label heightLabel = new Label("Height:");
        TextField heightInput = new TextField(String.valueOf(selectedObstacle.getHeight()));
        styleTextField(heightInput);

        Label distFromThreLabel = new Label("Distance From Threshold:");
        TextField distFromThreInput = new TextField(String.valueOf(selectedObstacle.getDistanceFromThreshold()));
        styleTextField(distFromThreInput);

        Label distFromCentLabel = new Label("Distance From Centreline:");
        TextField distFromCentInput = new TextField(String.valueOf(selectedObstacle.getDistanceFromCentreline()));
        styleTextField(distFromCentInput);

        Button submitButton = new Button("Modify");
        styleButton(submitButton, MaterialDesign.MDI_CHECK, "Modify");

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

                    selectedObstacle.setName(name);
                    selectedObstacle.setHeight(height);
                    selectedObstacle.setDistanceFromThreshold(distFromThre);
                    selectedObstacle.setDistanceFromCentreline(distFromCent);
                    updateObstaclesList();

                    Stage stage = (Stage) form.getScene().getWindow();
                    stage.close();

                } catch (NumberFormatException ex) {
                    showErrorDialog("Invalid input for numbers. Please enter valid integers.");
                } catch (IllegalArgumentException ex) {
                    showErrorDialog(ex.getMessage());
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        styleButton(cancelButton, MaterialDesign.MDI_CLOSE, "Cancel");
        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) form.getScene().getWindow();
            stage.close();
        });

        form.getChildren().addAll(nameLabel, nameInput, heightLabel, heightInput, distFromThreLabel, distFromThreInput, distFromCentLabel, distFromCentInput, submitButton, cancelButton);

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Modify Obstacle");
        dialogGenerator(form, dialogStage);
    }

    private void showObstacleDetailsDialog(Obstacle obstacle) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Obstacle Details");

        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20));
        dialogVbox.setAlignment(Pos.CENTER);

        Text name = new Text("Name: " + obstacle.getName());
        Text height = new Text("Height: " + obstacle.getHeight());
        Text distFromThresh = new Text("Distance From Threshold: " + obstacle.getDistanceFromThreshold());
        Text distFromCenter = new Text("Distance From Centreline: " + obstacle.getDistanceFromCentreline());

        Button okButton = new Button("OK");
        styleButton(okButton, MaterialDesign.MDI_CHECK, "OK");
        okButton.setOnAction(e -> dialogStage.close());

        dialogVbox.getChildren().addAll(name, height, distFromThresh, distFromCenter, okButton);

        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }



    private void styleButton(Button button, MaterialDesign icon, String text) {
        button.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        button.setPrefWidth(120);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #333; -fx-text-fill: white;"));
        styleIcon(button, icon, text);
    }

    static void styleIcon(Button button, MaterialDesign icon, String text) {
        FontIcon buttonIcon = new FontIcon(icon);
        buttonIcon.setIconColor(Color.WHITE);
        button.setGraphic(buttonIcon);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER);

        HBox hbox = new HBox(buttonIcon, label);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10);

        button.setGraphic(hbox);
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
        dialogGenerator(form, dialogStage);
    }

    static void dialogGenerator(VBox form, Stage dialogStage) {
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

        this.currentObstacles.forEach(obstacle -> {
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