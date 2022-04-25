package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MessageView implements FXComponent {
  private final Controller controller;

  public MessageView(Controller controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    // Create container to hold labels
    VBox container = new VBox();
    container.setSpacing(10.0);
    container.setAlignment(Pos.BASELINE_CENTER);

    // Set puzzle label
    Label puzzleMessage = new Label();
    puzzleMessage.setText(
        "Puzzle "
            + (controller.getActivePuzzleIndex() + 1)
            + " of "
            + controller.getPuzzleLibrarySize());

    // Set solved label
    Label solvedMessage = new Label();
    solvedMessage.getStyleClass().add("solved-message");
    if (controller.isSolved()) {
      solvedMessage.setText("Congratulations! You've solved the puzzle");
    } else {
      solvedMessage.setText("");
    }

    container.getChildren().addAll(puzzleMessage, solvedMessage);
    return container;
  }
}
