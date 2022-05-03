package main;

public class TetrisPiece {
public int distance;
public Direction direction;
public Tetromino parent;
public int x,y;

public TetrisPiece(int distance, Direction direction) {
	this.distance=distance;
	this.direction=direction;
}

public void setParent(Tetromino parent) {
	this.parent=parent;
	x=parent.x+distance*direction.x;
	y=parent.y+distance*direction.y;
}

public void setDirection(Direction direction) {
	this.direction=direction;
	x=parent.x+distance*direction.x;
	y=parent.y+distance*direction.y;
}

public TetrisPiece copy() {
	return new TetrisPiece(distance, direction);
}

}
