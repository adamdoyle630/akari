package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.Controller;
import com.comp301.a09akari.model.CellType;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class CellView implements FXComponent {

  private final Controller controller;
  private final Pane container = new StackPane();
  private final Rectangle rect = new Rectangle(); // rectangle to represent cell
  private final CellType type;
  private final int row;
  private final int col;

  public CellView(Controller controller, CellType cellType, int row, int col) {
    this.controller = controller;
    this.type = cellType;
    this.row = row;
    this.col = col;
  }

  @Override
  public Parent render() {
    rect.setWidth(35);
    rect.setHeight(35);
    container.getChildren().add(rect);

    // cell type wall
    if (type == CellType.WALL) {
      rect.getStyleClass().add("cell-filled");

      // cell type clue
    } else if (type == CellType.CLUE) {
      rect.getStyleClass().add("cell-clue");
      setClue(controller.getActivePuzzle().getClue(row, col));
      if (controller.isClueSatisfied(row, col)) {
        rect.getStyleClass().add("clue-satisfied");
      }

      // cell type corridor
    } else {
      rect.getStyleClass().add("cell-plain");
      if (controller.isLamp(row, col)) {
        addLamp();
      }
      if (controller.isLit(row, col)) {
        rect.getStyleClass().add("cell-lit");
      }
    }

    // add click handler for corridor cells
    container.setOnMouseClicked(
        mouseEvent -> {
          if (type == CellType.CORRIDOR) {
            controller.clickCell(row, col);
          }
        });

    return container;
  }

  private void addLamp() {
    // add lamp to cell
    ImageView bulb = new ImageView("light-bulb.png");
    bulb.setFitHeight(20);
    bulb.setFitWidth(20);
    container.getChildren().add(bulb);
    if (controller.isLampIllegal(row, col)) {
      rect.getStyleClass().add("cell-lit-illegal");
    }
  }

  private void setClue(int clue) {
    // adds clue number to cell
    Label label = new Label(Integer.toString(clue));
    label.getStyleClass().add("clue-text");
    container.getChildren().add(label);
  }
}
