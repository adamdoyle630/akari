package com.comp301.a09akari.controller;

import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.Puzzle;
import java.util.Random;

public class ControllerImpl implements Controller {

  private final Model model;

  public ControllerImpl(Model model) {
    this.model = model;
  }

  @Override
  public void clickNextPuzzle() {
    // selects the next puzzle in the library
    model.setActivePuzzleIndex((model.getActivePuzzleIndex() + 1) % model.getPuzzleLibrarySize());
  }

  @Override
  public void clickPrevPuzzle() {
    // selects the previous puzzle in the library
    int index = model.getActivePuzzleIndex();
    if (index == 0) {
      model.setActivePuzzleIndex(model.getPuzzleLibrarySize() - 1);
    } else {
      model.setActivePuzzleIndex(model.getActivePuzzleIndex() - 1);
    }
  }

  @Override
  public void clickRandPuzzle() {
    // selects a random puzzle without selecting the same one twice
    int index = model.getActivePuzzleIndex();
    Random rand = new Random();
    while (index == model.getActivePuzzleIndex()) {
      index = rand.nextInt(model.getPuzzleLibrarySize());
    }
    model.setActivePuzzleIndex(index);
  }

  @Override
  public void clickResetPuzzle() {
    model.resetPuzzle();
  }

  @Override
  public void clickCell(int r, int c) {
    // adds or removes a lamp when cell is clicked
    if (model.isLamp(r, c)) {
      model.removeLamp(r, c);
    } else {
      model.addLamp(r, c);
    }
  }

  @Override
  public boolean isLit(int r, int c) {
    return model.isLit(r, c);
  }

  @Override
  public boolean isLamp(int r, int c) {
    return model.isLamp(r, c);
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    return model.isLampIllegal(r, c);
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    return model.isClueSatisfied(r, c);
  }

  @Override
  public boolean isSolved() {
    return model.isSolved();
  }

  @Override
  public Puzzle getActivePuzzle() {
    return model.getActivePuzzle();
  }

  @Override
  public int getActivePuzzleIndex() {
    return model.getActivePuzzleIndex();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return model.getPuzzleLibrarySize();
  }
}
