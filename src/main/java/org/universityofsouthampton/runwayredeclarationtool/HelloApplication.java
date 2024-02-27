package org.universityofsouthampton.runwayredeclarationtool;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    public void start(Stage stage) {
        // Create a Menu
        Menu menu = new Menu("Menu");

        // Create menuitems
        MenuItem item1 = new MenuItem("Item 1");
        MenuItem item2 = new MenuItem("Item 2");

        // Add menu items to the menu
        menu.getItems().add(item1);
        menu.getItems().add(item2);

        // Create a menubar
        MenuBar menubar = new MenuBar();

        // Add menu to the menubar
        menubar.getMenus().add(menu);

        // Create a border pane
        BorderPane bPane = new BorderPane();

        // Set menubar to the top of border pane
        bPane.setTop(menubar);

        // Create a scene
        Scene scene = new Scene(bPane, 960, 600);

        // Set the scene
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}