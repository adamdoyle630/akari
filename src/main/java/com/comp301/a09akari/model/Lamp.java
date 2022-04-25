package com.comp301.a09akari.model;

public class Lamp {

  private final int row;
  private final int col;
  private boolean isIllegal;

  public Lamp(int row, int col) {
    this.row = row;
    this.col = col;
    this.isIllegal = false;
  }

  public boolean isIllegal() {
    return this.isIllegal;
  }

  public void setIllegal(boolean b) {
    this.isIllegal = b;
  }

  public int getRow() {
    return this.row;
  }

  public int getCol() {
    return this.col;
  }
}
