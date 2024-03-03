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
    private ScrollPane scrollPane = new ScrollPane();

    /**
     * Observable arraylist of obstacles to be displayed
     */
    private ObservableList<Obstacle> obstaclesObserve = FXCollections.observableArrayList();

    /**
     * ArrayList of obstacles for selected runway
     */
    private ArrayList<Obstacle> importedObstacles;


    /**
     *
     */
    private Obstacle selectedObstacle;

    private Runway runway;

    private Airport airport;

    public ObstacleListScene(MainApplication app, Airport airport, Runway runway) {
        setAlignment(Pos.TOP_CENTER);
        this.runway = runway;
        this.importedObstacles = runway.getObstacles();

        var title = new Text("Obstacle Update");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #333;");
        VBox.setMargin(title, new Insets(10, 0, 20, 0));


        Text title2 = new Text("List of Obstacles in " + this.runway.getName());
        title.setFont(Font.font("Arial", 20));

        setPadding(new Insets(20));
        setSpacing(10);


//        Button updateObstacle = new Button();
//        styleButton(updateObstacle, MaterialDesign.MDI_PLUS_BOX, "Update");
//        updateObstacle.setOnAction(e -> promptUpdateObstacleForm(app));
//
//        Button updateButton = new Button();
//        styleButton(updateButton, MaterialDesign.MDI_PLUS_BOX, "Update");
//        updateButton.setOnAction(e -> {
//            // Handle airport selection (we may need to implement database/xml func early)
//            // Selection error handling
//            if (this.selectedObstacle == null) {
//                System.out.println("Nothing Selected!");
//            } else {
//                promptAddObstacleForm(app);
//            }
//
//        });

//        Button selectButton = new Button();
//        styleButton(selectButton, MaterialDesign.MDI_PLUS_BOX, "Update");
//        selectButton.setOnAction(e -> {
//            // Handle airport selection (we may need to implement database/xml func early)
//            // Selection error handling
//            if (this.selectedObstacle == null) {
//                System.out.println("Nothing Selected!");
//            } else {
//                viewObstacleInfo(this.selectedObstacle);
//            }
//
//        });



//        Button addObstacle = new Button();
//        styleButton(addObstacle, MaterialDesign.MDI_PLUS_BOX, "Add");
//        addObstacle.setOnAction(e -> addObstacleForm(app));
//
////                Button updateObstacle = new Button();
////                styleButton(updateObstacle, MaterialDesign.MDI_PLUS_BOX, "Add");
////                updateObstacle.setOnAction(e -> promptUpdateObstacleForm(app));





        Button backButton = new Button();
        styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
        backButton.setOnAction(e -> app.displayRunwayConfigScene(airport,runway));





        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(700, 500);
        importedObstacles = app.getObstacles();
        updateObstaclesList();


        Button addButton = new Button();
        styleButton(addButton, MaterialDesign.MDI_PLUS_BOX, "Add");
        addButton.setOnAction(e -> {
            // Handle airport selection (we may need to implement database/xml func early)
            // Selection error handling
            if (this.selectedObstacle == null) {
                System.out.println("Nothing Selected!");
            } else {
                this.importedObstacles.add(this.selectedObstacle);
            }});

        Button removeButton = new Button();
        styleButton(removeButton, MaterialDesign.MDI_PLUS_BOX, "Remove");
        removeButton.setOnAction(e -> {
            // Handle airport selection (we may need to implement database/xml func early)
            // Selection error handling
            if (this.selectedObstacle == null) {
                System.out.println("Nothing Selected!");
            } else {
                this.importedObstacles.remove(this.selectedObstacle);
            }});

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(backButton,addButton,removeButton);

        this.getChildren().addAll(title,scrollPane,buttonBox);
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

    private void updateObstaclesList() {
        obstaclesObserve.clear();

        var obstaclesBox = new VBox();
        obstaclesBox.setSpacing(5);
        importedObstacles = new ArrayList<>();
        importedObstacles.add(new Obstacle("Tree",10,20,30));

        for (Obstacle obstacle : importedObstacles) {
            var name = (" -- " + obstacle.getName() + " -- ");
            var obstacleButton = new Button(name);

            // Button to select the airport
            obstacleButton.setOnMouseClicked(event -> {
                setSelectedObstacle(obstacle);
                System.out.println("Currently selected: " + getSelectedObstacle().getName());
            });

            obstaclesBox.getChildren().add(obstacleButton);

            obstaclesObserve.add(obstacle);
        }

        scrollPane.setContent(obstaclesBox);
    }


    public void setSelectedObstacle(Obstacle selectedObstacle) {
        this.selectedObstacle = selectedObstacle;
    }

    public Obstacle getSelectedObstacle() {
        return this.selectedObstacle;
    }


//    private void promptAddObstacleForm(MainApplication app) {
//
//        VBox form = new VBox(10);
//        form.setAlignment(Pos.CENTER);
//        form.setPadding(new Insets(20));
//
//        Label nameLabel = new Label("Obstacle Name:");
//        TextField nameInput = new TextField();
//        styleTextField(nameInput);
//
//        Label TORALabel = new Label("TORA:");
//        TextField TORAInput = new TextField();
//        styleTextField(TORAInput);
//
//        Label TODALabel = new Label("TODA:");
//        TextField TODAInput = new TextField();
//        styleTextField(TODAInput);
//
//        Label ASDALabel = new Label("ASDA:");
//        TextField ASDAInput = new TextField();
//        styleTextField(ASDAInput);
//
//        Button submitButton = new Button();
//        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Add");
//
//        Button cancelButton = new Button();
//        styleButton(cancelButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
//        cancelButton.setOnAction(e -> {
//            Stage stage = (Stage) form.getScene().getWindow();
//            stage.close();
//        });
//
//        submitButton.setOnAction(e -> {
//            String name = nameInput.getText();
//            String TORAname = TORAInput.getText();
//            String TODAname = TODAInput.getText();
//            String ASDAname = ASDAInput.getText();
//
//            // Validate
//            if (name.isEmpty() || TODAname.isEmpty() || TORAname.isEmpty() || ASDAname.isEmpty() ) {
//                showErrorDialog("All fields are required. Please fill in all fields.");
//            } else {
//                try {
//                    int TORA = Integer.parseInt(TORAInput.getText());
//                    int TODA = Integer.parseInt(TODAInput.getText());
//                    int ASDA = Integer.parseInt(ASDAInput.getText());
//
//                    if (TODA <= 0 || TORA <= 0 || ASDA <= 0 ) {
//                        throw new IllegalArgumentException("Invalid measurements for runway.");
//                    }
//                    // Data valid, add runway
//                    // For now, just close the form
//                    Stage stage = (Stage) form.getScene().getWindow();
//
//                    // add the Runway into the list
//                    this.obstacles.add(new Obstacle(name,TODA,TORA,ASDA));
//                    updateObstaclesList();
//
//                    stage.close();
//
//                } catch (NumberFormatException ex) {
//                    showErrorDialog("Invalid input for number of runways. Please enter a valid integer.");
//                } catch (IllegalArgumentException ex) {
//                    showErrorDialog(ex.getMessage());
//                }
//            }
//        });
//
//        form.getChildren().addAll(nameLabel, nameInput, TORALabel, TORAInput, TODALabel, TODAInput,
//                ASDALabel, ASDAInput, submitButton, cancelButton);
//
//        Stage dialogStage = new Stage();
//        dialogStage.initModality(Modality.APPLICATION_MODAL);
//        dialogStage.setTitle("Add Obstacle");
//        dialogStage.setScene(new Scene(form));
//
//        double centerX = Screen.getPrimary().getVisualBounds().getWidth() / 2;
//        double centerY = Screen.getPrimary().getVisualBounds().getHeight() / 2;
//        dialogStage.setX(centerX - 150);
//        dialogStage.setY(centerY - 100);
//        dialogStage.showAndWait();
//    }

//    private void promptUpdateObstacleForm(MainApplication app) {
//
//        VBox form = new VBox(10);
//        form.setAlignment(Pos.CENTER);
//        form.setPadding(new Insets(20));
//
//        Label nameLabel = new Label("Obstacle Name:");
//        TextField nameInput = new TextField();
//        styleTextField(nameInput);
//
//        Label TORALabel = new Label("TORA:");
//        TextField TORAInput = new TextField();
//        styleTextField(TORAInput);
//
//        Label TODALabel = new Label("TODA:");
//        TextField TODAInput = new TextField();
//        styleTextField(TODAInput);
//
//        Label ASDALabel = new Label("ASDA:");
//        TextField ASDAInput = new TextField();
//        styleTextField(ASDAInput);
//
//        Button submitButton = new Button();
//        styleButton(submitButton, MaterialDesign.MDI_PLUS_BOX, "Update");
//
//        Button cancelButton = new Button();
//        styleButton(cancelButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
//        cancelButton.setOnAction(e -> {
//            Stage stage = (Stage) form.getScene().getWindow();
//            stage.close();
//        });
//
//        submitButton.setOnAction(e -> {
//            String name = nameInput.getText();
//            String TORAname = TORAInput.getText();
//            String TODAname = TODAInput.getText();
//            String ASDAname = ASDAInput.getText();
//
//            // Validate
//            if (name.isEmpty() || TODAname.isEmpty() || TORAname.isEmpty() || ASDAname.isEmpty() ) {
//                showErrorDialog("All fields are required. Please fill in all fields.");
//            } else {
//                try {
//                    int TORA = Integer.parseInt(TORAInput.getText());
//                    int TODA = Integer.parseInt(TODAInput.getText());
//                    int ASDA = Integer.parseInt(ASDAInput.getText());
//
//                    if (TODA <= 0 || TORA <= 0 || ASDA <= 0 ) {
//                        throw new IllegalArgumentException("Invalid measurements for runway.");
//                    }
//                    // Data valid, add runway
//                    // For now, just close the form
//                    Stage stage = (Stage) form.getScene().getWindow();
//
//                    // add the Runway into the list
//                    this.obstacles.add(new Obstacle(name,TODA,TORA,ASDA));
//                    updateObstaclesList();
//
//                    stage.close();
//
//                } catch (NumberFormatException ex) {
//                    showErrorDialog("Invalid input for number of runways. Please enter a valid integer.");
//                } catch (IllegalArgumentException ex) {
//                    showErrorDialog(ex.getMessage());
//                }
//            }
//        });
//
//        form.getChildren().addAll(nameLabel, nameInput, TORALabel, TORAInput, TODALabel, TODAInput,
//                ASDALabel, ASDAInput, submitButton, cancelButton);
//
//        Stage dialogStage = new Stage();
//        dialogStage.initModality(Modality.APPLICATION_MODAL);
//        dialogStage.setTitle("Add Obstacle");
//        dialogStage.setScene(new Scene(form));
//
//        double centerX = Screen.getPrimary().getVisualBounds().getWidth() / 2;
//        double centerY = Screen.getPrimary().getVisualBounds().getHeight() / 2;
//        dialogStage.setX(centerX - 150);
//        dialogStage.setY(centerY - 100);
//        dialogStage.showAndWait();
//    }


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

    private void updateList() {
        obstaclesObserve.clear();

        var runwaysBox = new VBox();
        runwaysBox.setSpacing(5);

        for (Obstacle obstacle : importedObstacles) {
            var name = (" -- " + runway.getName() + " -- ");
            var runwayButton = new Button(name);

            // Button to select the airport
            runwayButton.setOnMouseClicked(event -> {
                setSelectedObstacle(obstacle);
                System.out.println("Currently selected: " + getSelectedObstacle().getName());
            });

            runwaysBox.getChildren().add(runwayButton);

            obstaclesObserve.add(obstacle);
        }

        scrollPane.setContent(runwaysBox);
    }

}

