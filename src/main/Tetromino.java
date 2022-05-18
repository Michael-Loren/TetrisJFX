package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static main.TetrisApp.TILE_SIZE;

public class Tetromino {
	public int x, y;
	public Color color;
	public List<TetrisPiece> pieces;

	
	/**
	 * Creates a Tetromino using multiple TetrisPiece instances
	 * @param color 
	 * @param pieces
	 */
	public Tetromino(Color color, TetrisPiece... pieces) {
		this.color = color;
		this.pieces = new ArrayList<>(Arrays.asList(pieces));
		
		//sets all of the pieces given to this current Tetromino
		for (TetrisPiece piece : this.pieces) 
			piece.setParent(this);
	}

	/**
	 * Physically move the Tetromino across the board
	 */
	public void move(int dx, int dy) {
		x += dx;
		y += dy;

		pieces.forEach(p -> {
			p.x += dx;
			p.y += dy;
		});
	}
	/**
	 * Physically move the Tetromino across the board
	 */
	public void move(Direction direction) {
		move(direction.x, direction.y);
	}

	/**
	 * Drops piece to bottom of grid
	 */
	public void drop() {
		pieces.forEach(p -> {
			p.y += 2; 
			//supposed to drop to bottom, not move by 2
			//whenever a piece goes inside another, the game ends
			//fix boundary check
		});
		
	}

	
	/**
	 * Draw Tetromino
	 * @param g
	 */
	public void draw(GraphicsContext g) {
		g.setFill(color);

		pieces.forEach(p -> g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
	}

	public void rotateBack() {
		pieces.forEach(p -> p.setDirection(
				p.directions.stream().map(Direction::prev).collect(Collectors.toList()).toArray(new Direction[0])));
	}

	public void rotate() {
		pieces.forEach(p -> p.setDirection(
				p.directions.stream().map(Direction::next).collect(Collectors.toList()).toArray(new Direction[0])));
	}

	public void detach(int x, int y) {
		pieces.removeIf(p -> p.x == x && p.y == y);
	}

	public Tetromino copy() {
		return new Tetromino(color,
				pieces.stream().map(TetrisPiece::copy).collect(Collectors.toList()).toArray(new TetrisPiece[0]));
	}
}
