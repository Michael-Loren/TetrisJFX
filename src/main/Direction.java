package main;

public enum Direction {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

	int x, y;
	
    private Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 
     * @return relative counter-clockwise direction value
     */
    public Direction prev() {
        int nextIndex = ordinal() - 1;

        if (nextIndex == -1) {
            nextIndex = Direction.values().length - 1;
        }

        return Direction.values()[nextIndex];
    }

    /**
     * @return relative clockwise direction value
     */
    public Direction next() {
        int nextIndex = ordinal() + 1;

        if (nextIndex == Direction.values().length) {
            nextIndex = 0;
        }

        return Direction.values()[nextIndex];
    }
}
