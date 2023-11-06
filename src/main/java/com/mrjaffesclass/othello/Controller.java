package com.mrjaffesclass.othello;

import java.util.concurrent.TimeUnit;


/**
 * Game controller
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Controller
{
  private final Board board;
  private final Player player1, player2;
  
  /**
   * Constructor for objects of class Controller
   * @param player1 Player 1
   * @param player2 Player 2
   */
  public Controller(Player player1, Player player2)
  {
    this.board = new Board();
    this.player1 = player1;
    this.player2 = player2;
  }
  
  /**
   * Displays the match up with the player names
   */
  public void displayMatchup() {
    System.out.println("OTHELLO Version "+Constants.VERSION+"\n\n******* MATCHUP *******");
    System.out.println(this.player1.toString() + " vs. " + this.player2.toString() + "\n");
  }
  
  /**
   * Run the game with the two players.  Player 1 BLACK always goes first
   * @return Return code for the game
   */
  public int run() {
    try {
      // Print heading
      System.out.println("START OF GAME");
      System.out.println(board.toString());

      // Black - Player 1 goes first
      boolean blackPlayersTurn = true;
      boolean onePlayerCantMove = false;
      boolean neitherPlayerCanMove = false;
      
      // While there are still spaces left
      while (!neitherPlayerCanMove && board.countSquares(Constants.EMPTY) > 0) {

        // Get the player whose turn it is
        Player playerToMove = (blackPlayersTurn) ? this.player1 : this.player2;

        // Get the player's next move from the student's Player class and 
        // display the move
        Position nextMove = playerToMove.getNextMove(this.board);
        if (nextMove != null) {
          System.out.println(playerToMove.toString()+ " MOVE to "+nextMove.toString());
        }

        // If a nextMove was NOT returned but there are legal moves available then quit with a DQ
        if (nextMove == null && !board.noMovesAvailable(playerToMove)) {
          String errString = "!!!! YOU MUST MOVE IF A MOVE IS AVAILABLE !!!!\n";
          errString += "GAME OVER! PLAYER " + playerToMove.getName() + " IS DISQUALIFIED. IT SUCKS TO BE YOU";
          RuntimeException re = new RuntimeException(errString);
          throw re;

        // If a nextMove was returned but it's not an empty space and a legal move then quit with a DQ
        } else if (nextMove != null && !board.isLegalMove(playerToMove, nextMove)) {
          String errString = "!!!! YOU CANNOT MOVE IN SPACE ["+nextMove.getRow()+","+nextMove.getCol()+"] !!!!\n";
          errString += "GAME OVER! PLAYER " + playerToMove.getName() + " IS DISQUALIFIED. IT SUCKS TO BE YOU";
          RuntimeException re = new RuntimeException(errString);
          throw re;
          
        } else if (nextMove != null) {
          // Check that the move is legal
          if (this.board.isLegalMove(playerToMove, nextMove)) {

            // Make the move and print the move and the new board
            board.makeMove(playerToMove, nextMove);
            System.out.println(board.toString());
            onePlayerCantMove = false;
          }
          
        } else {
          // No move was found. Skip the player's turn
          if (onePlayerCantMove) {
            neitherPlayerCanMove = true;
          } else {
            onePlayerCantMove = true;
          }
          System.out.println(playerToMove.toString()+" HAS NO MOVE THIS TURN\n");
        }
        // Delay so we can see the progression in the console
        TimeUnit.MILLISECONDS.sleep(Constants.DELAY);
        // Switch turn to other player
        blackPlayersTurn = !blackPlayersTurn;
      }

      // Print Game Over message
      System.out.println("GAME OVER!");

      // Get the score
      int black = board.countSquares(Constants.BLACK);
      int white = board.countSquares(Constants.WHITE);
      System.out.println(this.player1.getName()+"-BLACK: "+black+" SQUARES");
      System.out.println(this.player2.getName()+"-WHITE: "+white+" SQUARES\n");

      // And who wins
      if (black > white) {
        System.out.println(this.player1.getName()+"-BLACK WINS!");
        return 1;
      } else if (white > black) {
        System.out.println(this.player2.getName()+"-WHITE WINS!");
        return 2;
      } else {
        System.out.println("TIE GAME");
        return 3;
      }
    } catch (InterruptedException e) {
      System.out.println("OH NO!!! There was a time exception!\n");
      System.out.println(e.getMessage());
      return 0;
      
    } catch(RuntimeException e) {
      System.out.println(e.getMessage());
      return 0;
    }
    
  }
    
}
