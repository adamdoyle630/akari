package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.Controller;
import com.comp301.a09akari.model.Puzzle;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class PuzzleView implements FXComponent {

  private final Controller controller;
  private GridPane board;

  public PuzzleView(Controller controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    // Set up Board
    Pane container = new StackPane();
    Rectangle rect = new Rectangle(400, 400);
    rect.getStyleClass().add("rect-plain");
    board = new GridPane();
    board.setAlignment(Pos.CENTER);
    updatePuzzle();
    container.getChildren().add(rect);
    container.getChildren().add(board);
    return container;
  }

  private void updatePuzzle() {
    board.getChildren().clear();
    Puzzle puzzle = controller.getActivePuzzle();

    for (int row = 0; row < puzzle.getHeight(); row++) {
      for (int col = 0; col < puzzle.getWidth(); col++) {
        CellView currCell = new CellView(controller, puzzle.getCellType(row, col), row, col);
        board.add(currCell.render(), col, row);
      }
    }
  }
}
