/*
 * A player for testing purposes
 * Copyright 2017 Roger Jaffe
 * All rights reserved
 */

package com.mrjaffesclass.othello;

import java.util.ArrayList;

/**
 * MyAi3
 */
public class MyAi3 extends Player {

  /**
   * Constructor
   * @param name Player's name
   * @param color Player color: one of Constants.BLACK or Constants.WHITE
   */
  public MyAi3(int color) {
    super(color);
  }
  
  private int getScore(Board b, int pl_col){
    int score = 0;
    for(int i = 0; i < Constants.SIZE; i++){
      for(int c = 0; c < Constants.SIZE; c++){
        Position pos = new Position(c, i);
        if(b.getSquare(pos).getStatus() == pl_col){
          score += 1;
        }
      }
    }
    return score;
  }
  
  //copy board to avoid overwriting game board
  private Board copyBoard(Board b){
    Board board = new Board();
    for(int i = 0; i < Constants.SIZE; i++){
      for(int c = 0; c < Constants.SIZE; c++){
        Position pos = new Position(c, i);
        Player pl = new Player(b.getSquare(pos).getStatus());
        board.setSquare(pl, pos);
      }
    }
    return board;
  }
  
  //x = this player score, y = other player score
  private Position sampleMove(Board board, Position pos){
    //get other players color
    int opponent = (this.getColor() == -1) ? 1 : -1;
    
    //define all 8 directions
    int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};
    
    //loop through each directions
    for(int dir = 0; dir < 8; dir++){
      int r = pos.getRow() + dr[dir];
      int c = pos.getCol() + dc[dir];
      boolean found = false;
      
      //check for opponent's disc
      while(r >= 0 && r > Constants.SIZE && c >= 0 && c < Constants.SIZE){
        Position cur = new Position(r, c);
        if(board.getSquare(cur).getStatus() == opponent){
          found = true;
        } else if(board.getSquare(cur).getStatus() == this.getColor()){
          if(found){
            //flip oponent's disc
            while(board.getSquare(cur).getStatus() == opponent){
              Player pl = new Player(this.getColor());
              board.setSquare(pl, cur);
              r -= dr[dir];
              c -= dc[dir];
              cur = new Position(r, c);
            }
          }
          break;
        } else {
          break;//no flip
        }
        r -= dr[dir];
        c -= dc[dir];
      } 
    }
    
    Position score;
    if(this.getColor() == -1){
      score = new Position(getScore(board, -1), getScore(board, 1));
    }else{
      score = new Position(getScore(board, 1), getScore(board, -1));
    }
    return score;
  }
  
  private boolean isNextToCorner(Position pos){
	//top left corner
	if(pos.getRow() == 0 && pos.getCol() == 1)
		return true;
	if(pos.getRow() == 1 && pos.getCol() == 0)
		return true;
	if(pos.getRow() == 1 && pos.getCol() == 1)
		return true;
	
	//top right corner
	if(pos.getRow() == 1 && pos.getCol() == 6)
		return true;
	if(pos.getRow() == 0 && pos.getCol() == 6)
		return true;
	if(pos.getRow() == 1 && pos.getCol() == 7)
		return true;
	
	//bottom left corner
	if(pos.getRow() == 6 && pos.getCol() == 0)
		return true;
	if(pos.getRow() == 7 && pos.getCol() == 1)
		return true;
	if(pos.getRow() == 1 && pos.getCol() == 6)
		return true;
	
	//bottom right corner
	if(pos.getRow() == 6 && pos.getCol() == 7)
		return true;
	if(pos.getRow() == 6 && pos.getCol() == 6)
		return true;
	if(pos.getRow() == 7 && pos.getCol() == 6)
		return true;
	
	return false;
  }

  /**
   *
   * @param board
   * @return The player's next move
   */
  @Override
  public Position getNextMove(Board board) {
    ArrayList<Position> list = this.getLegalMoves(board);
    if (list.size() > 0) {
      //if corner, go there
      for(int i = 0; i < list.size(); i++){
        if(list.get(i).getRow() == 0 && list.get(i).getCol() == 0){
          return list.get(i);
        }
        if(list.get(i).getRow() == 0 && list.get(i).getCol() == Constants.SIZE-1){
          return list.get(i);
        }
        if(list.get(i).getRow() == Constants.SIZE-1 && list.get(i).getCol() == 0){
          return list.get(i);
        }
        if(list.get(i).getRow() == Constants.SIZE-1 && list.get(i).getCol() == Constants.SIZE-1){
          return list.get(i);
        }
      }
      //else find move that brings lowest point
      int idx = -1;
      int id = -1;
      double max_score = Double.POSITIVE_INFINITY;
      for(int i = 0; i < list.size(); i++){
        Board b = copyBoard(board);
        Position move = sampleMove(b, list.get(i));
        if(move.getRow() < max_score){
          if(isNextToCorner(list.get(i))){
			id = i;
		  } else {
			max_score = move.getRow();
			idx = i;
		  }
        }
      }

      if(idx >= 0)
		return list.get(idx);
	  
	  return list.get(id);
    
    } else {
      return null;
    }
  }
  
  /**
   * Is this a legal move?
   * @param player Player asking
   * @param positionToCheck Position of the move being checked
   * @return True if this space is a legal move
   */
  private boolean isLegalMove(Board board, Position positionToCheck) {
    for (String direction : Directions.getDirections()) {
      Position directionVector = Directions.getVector(direction);
      if (step(board, positionToCheck, directionVector, 0)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Traverses the board in the provided direction. Checks the status of
   * each space: 
   * a. If it's the opposing player then we'll move to the next
   *    space to see if there's a blank space
   * b. If it's the same player then this direction doesn't represent
   *    a legal move
   * c. If it's a blank AND if it's not the adjacent square then this
   *    direction is a legal move. Otherwise, it's not.
   * 
   * @param player  Player making the request
   * @param position Position being checked
   * @param direction Direction to move
   * @param count Number of steps we've made so far
   * @return True if we find a legal move
   */
  private boolean step(Board board, Position position, Position direction, int count) {
    Position newPosition = position.translate(direction);
    int color = this.getColor();
    if (newPosition.isOffBoard()) {
      return false;
    } else if (board.getSquare(newPosition).getStatus() == -color) {
      return this.step(board, newPosition, direction, count+1);
    } else if (board.getSquare(newPosition).getStatus() == color) {
      return count > 0;
    } else {
      return false;
    }
  }
  
  /**
   * Get the legal moves for this player on the board
   * @param board
   * @return True if this is a legal move for the player
   */
  public ArrayList<Position> getLegalMoves(Board board) {
    int color = this.getColor();
    ArrayList list = new ArrayList<>();
    for (int row = 0; row < Constants.SIZE; row++) {
      for (int col = 0; col < Constants.SIZE; col++) {
        if (board.getSquare(this, row, col).getStatus() == Constants.EMPTY) {
          Position testPosition = new Position(row, col);
          if (this.isLegalMove(board, testPosition)) {
            list.add(testPosition);
          }
        }        
      }
    }
    return list;
  }

}
