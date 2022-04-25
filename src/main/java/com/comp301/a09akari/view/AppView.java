package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AppView implements FXComponent {

  private final Controller controller;

  public AppView(Controller controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    // Create VBox to hold UI components
    VBox layout = new VBox();
    layout.setSpacing(10);
    layout.getStyleClass().add("layout");

    // Create title text
    HBox titleContainer = new HBox();
    titleContainer.setAlignment(Pos.BASELINE_CENTER);
    Label titleLabel = new Label("Akari Light Up");
    titleLabel.getStyleClass().add("title");
    titleContainer.getChildren().add(titleLabel);
    layout.getChildren().add(titleContainer);

    // Puzzle view
    FXComponent puzzleView = new PuzzleView(controller);
    layout.getChildren().add(puzzleView.render());

    // Button view
    FXComponent buttonView = new ButtonView(controller);
    layout.getChildren().add(buttonView.render());

    // Message view
    FXComponent messageView = new MessageView(controller);
    layout.getChildren().add(messageView.render());

    return layout;
  }
}
