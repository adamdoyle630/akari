package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ButtonView implements FXComponent {

  private final Controller controller;

  public ButtonView(Controller controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    // Create buttons
    HBox buttonContainer = new HBox();
    buttonContainer.setSpacing(10);

    Button resetButton = new Button("Reset");
    resetButton.setOnMouseClicked(event -> controller.clickResetPuzzle());

    Button prevButton = new Button("\u25C0 Previous");
    prevButton.setOnMouseClicked(event -> controller.clickPrevPuzzle());

    Button nextButton = new Button("Next \u25B6");
    nextButton.setOnMouseClicked(event -> controller.clickNextPuzzle());

    Button randButton = new Button("Random Puzzle");
    randButton.setOnMouseClicked(event -> controller.clickRandPuzzle());

    buttonContainer.setAlignment(Pos.BASELINE_CENTER);
    buttonContainer.getChildren().addAll(resetButton, prevButton, nextButton, randButton);

    return buttonContainer;
  }
}
