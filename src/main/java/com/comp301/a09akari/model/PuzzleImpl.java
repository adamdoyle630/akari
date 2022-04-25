package com.comp301.a09akari.model;

public class PuzzleImpl implements Puzzle {

  private final int[][] board;

  public PuzzleImpl(int[][] board) {
    if (board == null) {
      throw new IllegalArgumentException();
    }
    this.board = board;
  }

  @Override
  public int getWidth() {
    return this.board[0].length;
  }

  @Override
  public int getHeight() {
    return this.board.length;
  }

  @Override
  public CellType getCellType(int r, int c) {
    if (r >= this.getHeight() || c >= this.getWidth()) {
      throw new IndexOutOfBoundsException();
    }
    int cellValue = board[r][c];
    if (cellValue < 5) {
      return CellType.CLUE;
    }
    if (cellValue == 5) {
      return CellType.WALL;
    } else {
      return CellType.CORRIDOR;
    }
  }

  @Override
  public int getClue(int r, int c) {
    if (r >= this.getHeight() || c >= this.getWidth()) {
      throw new IndexOutOfBoundsException();
    }
    if (getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException();
    }
    return board[r][c];
  }
}
