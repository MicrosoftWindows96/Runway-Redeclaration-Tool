package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.universityofsouthampton.runwayredeclarationtool.MainApplication;

public class AirportListScene extends VBox {

    public AirportListScene(MainApplication app) {
        setPadding(new Insets(20));
        setSpacing(10);

        Text title = new Text("List of Airports");
        title.setFont(Font.font("Arial", 20));

        ListView<String> listView = new ListView<>();
        ObservableList<String> airports = FXCollections.observableArrayList();

        // Populate the list of airports
        airports.addAll("Airport 1", "Airport 2", "Airport 3"); // Example airports
        listView.setItems(airports);

        Button backButton = new Button("Back");
        styleButton(backButton);
        backButton.setOnAction(e -> app.displayAirportScene());

        getChildren().addAll(title, listView, backButton);
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        button.setFont(Font.font("Arial", 16));
        button.setPrefWidth(120);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #333; -fx-text-fill: white;"));
    }

    public Scene createScene(MainApplication app) {
        return new Scene(this, 300, 400);
    }
}
