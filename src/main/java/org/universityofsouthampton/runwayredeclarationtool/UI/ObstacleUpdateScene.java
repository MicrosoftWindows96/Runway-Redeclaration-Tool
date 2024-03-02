package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;
import org.universityofsouthampton.runwayredeclarationtool.airport.Airport;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import java.util.ArrayList;

public class ObstacleUpdateScene extends VBox{
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
        private ArrayList<Obstacle> obstacles;


        /**
         *
         */
        private Obstacle selectedObstacle;

        private Runway runway;


    public ObstacleUpdateScene (MainApplication app, Airport airport, Runway runway) {
            setAlignment(Pos.TOP_CENTER);
            this.runway = runway;
            this.obstacles = runway.getObstacles();

            var title = new Text("Obstacle Update");
            title.setFont(Font.font("Arial", 24));
            title.setStyle("-fx-fill: #333;");
            VBox.setMargin(title, new Insets(10, 0, 20, 0));

            VBox buttons = new VBox(10);
            buttons.setAlignment(Pos.CENTER);
            this.setSpacing(200);

            Button backButton = new Button();
            styleButton(backButton, MaterialDesign.MDI_KEYBOARD_RETURN, "Return");
            backButton.setOnAction(e -> app.displayAirportListScene());

            buttons.getChildren().addAll(backButton);

            Text title2 = new Text("List of Obstacles in " + this.runway.getName());
            title.setFont(Font.font("Arial", 20));

            setPadding(new Insets(20));
            setSpacing(10);

            Button selectButton = new Button();
            styleButton(selectButton, MaterialDesign.MDI_PLUS_BOX, "Update");
            selectButton.setOnAction(e -> {
                    // Handle airport selection (we may need to implement database/xml func early)
                    // Selection error handling
                    if (selectedObstacle.getName().isEmpty()) {
                            System.out.println("Nothing Selected!");
                    } else {
//                            app.displayRConfigScene(airport, airport;
                    }

            });

//            Button addRunway = new Button();
//            styleButton(addRunway, MaterialDesign.MDI_PLUS_BOX, "Add");
//            addRunway.setOnAction(e -> promptAddObstacleForm(app));


            HBox buttonBox = new HBox(10);
            buttonBox.getChildren().addAll(selectButton, backButton);



            // Populate list of runways from Airport's arrayLists
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefSize(700,500);
            updateObstaclesList();

            this.getChildren().addAll(title, buttons, scrollPane);
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

                for (Obstacle obstacle : obstacles) {
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

        public Obstacle getSelectedObstacle () {
                return this.selectedObstacle;
        }}


//private void promptAddObstacleForm(MainApplication app) {
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
//}


