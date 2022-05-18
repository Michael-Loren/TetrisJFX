package main;

import java.util.Arrays;
import java.util.List;

public class TetrisPiece{

    public int distance;
    public List<Direction> directions;
    public Tetromino parent;
    public int x, y;

    /**
     * Constructs a Tetris square relative to an origin
     * @param distance - distance to construct square from origin
     * @param direction - direction to construct square
     */
    public TetrisPiece(int distance, Direction... direction) {
        this.distance = distance;
        this.directions = Arrays.asList(direction);
    }

    /**
     * Designates the parent Tetromino
     * @param parent - parent to set to
     */
    public void setParent(Tetromino parent) {
        this.parent = parent;

        int dx = 0, dy = 0;

        for (Direction d : directions) {
            dx += distance * d.x;
            dy += distance * d.y;
        }

        x = parent.x + dx;
        y = parent.y + dy;
    }

    /**
     * Set the orientation to translate the piece by.
     * Using this will maintain the offset specified by this instance's  
     * @param direction
     */
    public void setDirection(Direction... direction) {
        this.directions = Arrays.asList(direction);

        int dx = 0, dy = 0;

        for (Direction d : directions) {
            dx += distance * d.x;
            dy += distance * d.y;
        }

        x = parent.x + dx;
        y = parent.y + dy;
    }

    
    /**
     * Returns a copy of the current instance
     */
    public TetrisPiece copy() {
        return new TetrisPiece(distance, directions.toArray(new Direction[0]));
    }
}