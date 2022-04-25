package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ModelImpl implements Model {

  private final PuzzleLibrary library;
  private final List<ModelObserver> activeObservers =
      new ArrayList<>(); // list of lamps on the board
  private final List<Lamp> lamps = new ArrayList<>();
  private int activePuzzleIndex;
  private Puzzle activePuzzle;
  private CellState[][] cellStates; // store locations of lamps and lit cells
  private int[][] cellBrightness; // keeps track of how many lamps are lighting a cell
  private boolean solved;

  public ModelImpl(PuzzleLibrary library) {
    this.library = library;
    this.activePuzzle = getActivePuzzle();
    resetCellStates();
  }

  @Override
  public void addLamp(int r, int c) {
    if (r >= activePuzzle.getHeight() || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException();
    }
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }

    // add lamp to cell and light surrounding cells
    cellStates[r][c] = CellState.LAMP;
    lamps.add(new Lamp(r, c));
    cellBrightness[r][c]++;
    lightColumn(r, c);
    lightRow(r, c);
    checkForIllegalLamps();
    checkSolved();
    notifyObservers();
  }

  @Override
  public void removeLamp(int r, int c) {
    if (r >= activePuzzle.getHeight() || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException();
    }
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }

    // remove lamp and reset brightness
    if (isLampIllegal(r, c)) {
      cellStates[r][c] = CellState.LIT;
    } else {
      cellStates[r][c] = CellState.PLAIN;
    }
    cellBrightness[r][c]--;

    // traverse adjacent row and column and reset each cell
    int i = r - 1;
    while (i >= 0 && activePuzzle.getCellType(i, c) == CellType.CORRIDOR) {
      if (cellBrightness[i][c] == 1) {
        // cell is only lit by this lamp
        cellStates[i][c] = CellState.PLAIN;
      }
      cellBrightness[i][c]--;
      i--;
    }

    i = r + 1;
    while (i < activePuzzle.getHeight() && activePuzzle.getCellType(i, c) == CellType.CORRIDOR) {
      if (cellBrightness[i][c] == 1) {
        cellStates[i][c] = CellState.PLAIN;
      }
      cellBrightness[i][c]--;
      i++;
    }

    i = c + 1;
    while (i < activePuzzle.getWidth() && activePuzzle.getCellType(r, i) == CellType.CORRIDOR) {
      if (cellBrightness[r][i] == 1) {
        cellStates[r][i] = CellState.PLAIN;
      }
      cellBrightness[r][i]--;
      i++;
    }

    i = c - 1;
    while (i >= 0 && activePuzzle.getCellType(r, i) == CellType.CORRIDOR) {
      if (cellBrightness[r][i] == 1) {
        cellStates[r][i] = CellState.PLAIN;
      }
      cellBrightness[r][i]--;
      i--;
    }

    checkForIllegalLamps();
    lamps.removeIf(lamp -> lamp.getCol() == c && lamp.getRow() == r);
    checkSolved();
    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) {
    // return true if the current cell is lit by a lamp
    if (r >= activePuzzle.getHeight() || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException();
    }
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }

    return cellStates[r][c] == CellState.LIT || cellStates[r][c] == CellState.LAMP;
  }

  @Override
  public boolean isLamp(int r, int c) {
    // return true if the current cell contains a lamp
    if (r >= activePuzzle.getHeight() || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException();
    }
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }

    return cellStates[r][c] == CellState.LAMP;
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    if (r >= activePuzzle.getHeight() || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException();
    }
    if (!isLamp(r, c)) {
      throw new IllegalArgumentException();
    }

    // find the matching lamp and check if it is illegal
    for (Lamp lamp : lamps) {
      if (lamp.getRow() == r && lamp.getCol() == c) {
        return lamp.isIllegal();
      }
    }

    // lamp not found in the list of lamps
    throw new NoSuchElementException();
  }

  @Override
  public Puzzle getActivePuzzle() {
    return this.library.getPuzzle(activePuzzleIndex);
  }

  @Override
  public int getActivePuzzleIndex() {
    return this.activePuzzleIndex;
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index > library.size()) {
      throw new IndexOutOfBoundsException();
    }
    activePuzzleIndex = index;
    activePuzzle = getActivePuzzle();
    resetCellStates();
    notifyObservers();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return this.library.size();
  }

  @Override
  public void resetPuzzle() {
    resetCellStates();
    notifyObservers();
  }

  @Override
  public boolean isSolved() {
    return this.solved;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    if (r >= activePuzzle.getHeight() || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException();
    }
    if (activePuzzle.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException();
    }

    // count the number of lamps placed adjacent to the cell
    int lampCount = 0;
    if (r + 1 < activePuzzle.getHeight()) {
      if (activePuzzle.getCellType(r + 1, c) == CellType.CORRIDOR) {
        if (isLamp(r + 1, c)) {
          lampCount++;
        }
      }
    }
    if (c + 1 < activePuzzle.getWidth()) {
      if (activePuzzle.getCellType(r, c + 1) == CellType.CORRIDOR) {
        if (isLamp(r, c + 1)) {
          lampCount++;
        }
      }
    }
    if (c - 1 >= 0) {
      if (activePuzzle.getCellType(r, c - 1) == CellType.CORRIDOR) {
        if (isLamp(r, c - 1)) {
          lampCount++;
        }
      }
    }
    if (r - 1 >= 0) {
      if (activePuzzle.getCellType(r - 1, c) == CellType.CORRIDOR) {
        if (isLamp(r - 1, c)) {
          lampCount++;
        }
      }
    }
    return lampCount == activePuzzle.getClue(r, c);
  }

  @Override
  public void addObserver(ModelObserver observer) {
    activeObservers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    activeObservers.remove(observer);
  }

  private void notifyObservers() {
    for (ModelObserver o : activeObservers) {
      o.update(this);
    }
  }

  private void resetCellStates() {
    this.lamps.clear();
    this.cellStates = new CellState[activePuzzle.getHeight()][activePuzzle.getWidth()];
    this.cellBrightness = new int[activePuzzle.getHeight()][activePuzzle.getWidth()];
    for (int row = 0; row < activePuzzle.getHeight(); row++) {
      for (int col = 0; col < activePuzzle.getWidth(); col++) {
        cellStates[row][col] = CellState.PLAIN;
        cellBrightness[row][col] = 0;
      }
    }
    solved = false;
  }

  private void lightColumn(int row, int col) {
    // traverse through and light cells in same column as cell
    // stops when a wall or edge of grid is reached

    int i = row - 1;
    while (i >= 0 && activePuzzle.getCellType(i, col) == CellType.CORRIDOR) {
      if (cellStates[i][col] == CellState.PLAIN) {
        cellStates[i][col] = CellState.LIT;
      }
      cellBrightness[i][col]++;
      i--;
    }

    i = row + 1;
    while (i < activePuzzle.getHeight() && activePuzzle.getCellType(i, col) == CellType.CORRIDOR) {
      if (cellStates[i][col] == CellState.PLAIN) {
        cellStates[i][col] = CellState.LIT;
      }
      cellBrightness[i][col]++;
      i++;
    }
  }

  private void lightRow(int row, int col) {
    // traverse through and light cells in same row as cell
    // stops when a wall or edge of grid is reached

    int i = col - 1;
    while (i >= 0 && activePuzzle.getCellType(row, i) == CellType.CORRIDOR) {
      // light cell if it is not already lit
      if (cellStates[row][i] == CellState.PLAIN) {
        cellStates[row][i] = CellState.LIT;
      }
      cellBrightness[row][i]++;
      i--;
    }

    i = col + 1;
    while (i < activePuzzle.getWidth() && activePuzzle.getCellType(row, i) == CellType.CORRIDOR) {
      if (cellStates[row][i] == CellState.PLAIN) {
        cellStates[row][i] = CellState.LIT;
      }
      cellBrightness[row][i]++;
      i++;
    }
  }

  private void checkForIllegalLamps() {
    // checks each lamp to see if it is legally placed
    // traverse adjacent row and column and see if there is another lamp placed
    for (Lamp lamp : lamps) {
      lamp.setIllegal(false);
      int r = lamp.getRow();
      int c = lamp.getCol();
      int i = r - 1;
      while (i >= 0 && activePuzzle.getCellType(i, c) == CellType.CORRIDOR) {
        if (cellStates[i][c] == CellState.LAMP) {
          lamp.setIllegal(true);
        }
        i--;
      }

      i = r + 1;
      while (i < activePuzzle.getHeight() && activePuzzle.getCellType(i, c) == CellType.CORRIDOR) {
        if (cellStates[i][c] == CellState.LAMP) {
          lamp.setIllegal(true);
        }
        i++;
      }

      i = c - 1;
      while (i >= 0 && activePuzzle.getCellType(r, i) == CellType.CORRIDOR) {
        if (cellStates[r][i] == CellState.LAMP) {
          lamp.setIllegal(true);
        }
        i--;
      }

      i = c + 1;
      while (i < activePuzzle.getWidth() && activePuzzle.getCellType(r, i) == CellType.CORRIDOR) {
        if (cellStates[r][i] == CellState.LAMP) {
          lamp.setIllegal(true);
        }
        i++;
      }
    }
  }

  private void checkSolved() {
    // check that clues are satisfied
    // check that all corridors are lit
    // check that there are no illegal lamps
    for (int row = 0; row < activePuzzle.getHeight(); row++) {
      for (int col = 0; col < activePuzzle.getWidth(); col++) {
        if (activePuzzle.getCellType(row, col) == CellType.CLUE) {
          if (!isClueSatisfied(row, col)) {
            solved = false;
            return;
          }
        }
        if (activePuzzle.getCellType(row, col) == CellType.CORRIDOR) {
          if (!isLit(row, col) && !isLamp(row, col)) {
            solved = false;
            return;
          }
          if (isLamp(row, col)) {
            if (isLampIllegal(row, col)) {
              solved = false;
              return;
            }
          }
        }
      }
    }
    solved = true;
  }
}
