package com.mrjaffesclass.othello;

import java.awt.Point;
/**
 * Set up HashMap with the compass rose directions and 
 * pair with a vector that will implement the change 
 * in position
 * 
 * @author Mr. Jaffe
 * @version 1.0
 */
public class Directions {

  /**
   * Gets the requested direction vector
   * @param compassPoint Direction whose vector we want to retrieve
   * @return Direction vector
   */
  public static Position getVector(String compassPoint) {
    switch (compassPoint) {
      case "N":  return new Position(-1,0);
      case "NE": return new Position(-1,1);
      case "E":  return new Position(0,1);
      case "SE": return new Position(1,1);
      case "S":  return new Position(1,0);
      case "SW": return new Position(1,-1);
      case "W":  return new Position(0,-1);
      case "NW": return new Position(-1,-1);
    }
    return null;
  }
  
  /**
   * Gets an array of strings representing cardinal
   * direction points
   * @return Array of direction strings
   */
  public static String[] getDirections() {
    String[] points = {"N","NE","E","SE","S","SW","W","NW"};
    return points;
  }
  
}
