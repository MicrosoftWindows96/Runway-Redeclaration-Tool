package org.universityofsouthampton.runwayredeclarationtool.UI;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window {

  private final Stage stage;
  protected Scene menu;
  protected Scene airports;
  private final int width;
  private final int height;
  private Scene scene;

  public Window (Stage stage, int width, int height) {
    this.stage = stage;
    this.width = width;
    this.height = height;

    init();

    // Set initial scene
    stage.setScene(menu);
    stage.setTitle("RunwayRedeclarationTool");
    stage.show();

  }

  /**
   * Initialise all scenes
   */
  private void init() {
    menu = new Scene(new MenuScene(this),width,height);
    airports = new Scene(new AirportScene(this),width,height);

  }

  public void changeScene(Scene scene) {
    stage.setScene(scene);
  }

  /**
   * Get the current scene being displayed
   *
   * @return scene
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * Get the width of the Game Window
   *
   * @return width
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Get the height of the Game Window
   *
   * @return height
   */
  public int getHeight() {
    return this.height;
  }

}
