package com.mrjaffesclass.othello;

/**
 * Represents a square on the checkerboard
 * 
 * @author Mr. Jaffe
 * @version 1.0
 */
public final class Square
{
  // -1 Black, 1 White, 0 Blank
  private int status;

  /**
   * Constructor for objects of class Square
   * @param status Initial status of the square. One of RED, BLACK, EMPTY
   */
  public Square(int status)
  {
    this.setStatus(status);
  }

  /**
   * Get current status
   * @return Current status
   */
  public int getStatus() {
    return status;
  }
  
  /**
   * Set new status
   * @param status New status
   */
  public void setStatus(int status) {
    this.status = status;
  }
  
  @Override
  public String toString() {
    switch (status) {
      case Constants.WHITE: return " W ";
      case Constants.BLACK: return " B ";
    }
    return "   ";
  }
  
}
