package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;


public class TetrisApp extends Application {

    public static final int TILE_SIZE = 40;
    public static final int GRID_WIDTH = 10;
    public static final int GRID_HEIGHT = 20;
    private static final Random randomStream = new Random();
    private short linesCleared = 0;
    private Label scoreLbl;
    private double time;
    private GraphicsContext g;

    private int[][] grid = new int[GRID_WIDTH][GRID_HEIGHT];


    private List<Tetromino> original = new ArrayList<>();
    private List<Tetromino> tetrominos = new ArrayList<>();

    private Tetromino selected;

    private Parent createContent() {
        StackPane root = new StackPane();
        root.setPrefSize(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
     
        Canvas canvas = new Canvas(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
        g = canvas.getGraphicsContext2D();

        scoreLbl = new Label("Lines cleared: " + linesCleared);
        StackPane.setAlignment(scoreLbl, Pos.TOP_CENTER);
        scoreLbl.setTranslateY(30);
        scoreLbl.setScaleX(3);
        scoreLbl.setScaleY(3);
     
        
        root.getChildren().addAll(canvas, scoreLbl);
        //creates I piece
        original.add(new Tetromino(Color.CYAN,
                new TetrisPiece(0, Direction.DOWN),
                new TetrisPiece(1, Direction.LEFT),
                new TetrisPiece(1, Direction.RIGHT),
                new TetrisPiece(2, Direction.RIGHT)
                
        ));
        //creates T piece
        original.add(new Tetromino(Color.FUCHSIA,
                new TetrisPiece(0, Direction.DOWN),
                new TetrisPiece(1, Direction.LEFT),
                new TetrisPiece(1, Direction.RIGHT),
                new TetrisPiece(1, Direction.DOWN)
        ));
        //creates J piece
        original.add(new Tetromino(Color.BLUE,
                new TetrisPiece(0, Direction.DOWN),
                new TetrisPiece(1, Direction.LEFT),
                new TetrisPiece(2, Direction.LEFT),
                new TetrisPiece(1, Direction.DOWN)));
        
      //creates L piece
        original.add(new Tetromino(Color.ORANGE,
                new TetrisPiece(0, Direction.DOWN),
                new TetrisPiece(1, Direction.RIGHT),
                new TetrisPiece(2, Direction.RIGHT),
                new TetrisPiece(1, Direction.DOWN)));
      //creates square piece
        original.add(new Tetromino(Color.YELLOW,
                new TetrisPiece(0, Direction.DOWN),
                new TetrisPiece(1, Direction.RIGHT),
                new TetrisPiece(1, Direction.RIGHT, Direction.DOWN),
                new TetrisPiece(1, Direction.DOWN)));
        
        //creates S piece
        original.add(new Tetromino(Color.LIME,
                new TetrisPiece(0, Direction.DOWN),
                new TetrisPiece(1, Direction.RIGHT),
                new TetrisPiece(1, Direction.DOWN, Direction.LEFT),
                new TetrisPiece(1, Direction.DOWN)));
        
        //creates Z piece
        original.add(new Tetromino(Color.RED,
                new TetrisPiece(0, Direction.DOWN),
                new TetrisPiece(1, Direction.LEFT),
                new TetrisPiece(1, Direction.DOWN, Direction.RIGHT),
                new TetrisPiece(1, Direction.DOWN)));


        spawn();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.017;

                if (time >= 0.5) {
                    update();
                    render();
                    time = 0;
                }
            }
        };
        timer.start();

        return root;
    }
/**
 * makes the pieces go down
 */
    private void update() {
        makeMove(p -> p.move(Direction.DOWN), p -> p.move(Direction.UP), true);
    }
/**
 * spawns the pieces
 */
    private void render() {
        g.clearRect(0, 0, GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);

        tetrominos.forEach(p -> p.draw(g));
    }

    private void placePiece(TetrisPiece piece) {
        grid[piece.x][piece.y]++;
    }
/**
 * when line is complete, removes the completed line
 * @param piece
 */
    private void removePiece(TetrisPiece piece) {
        grid[piece.x][piece.y]--;
    }

    private boolean isOffscreen(TetrisPiece piece) {
        return piece.x < 0 || piece.x >= GRID_WIDTH
                || piece.y < 0 || piece.y >= GRID_HEIGHT;
    }
/**
 * lets the program know the piece is still continuing to spawn
 * @param onSuccess
 * @param onFail
 * @param endMove
 */
    private void makeMove(Consumer<Tetromino> onSuccess, Consumer<Tetromino> onFail, boolean endMove) {
        selected.pieces.forEach(this::removePiece);
        
        onSuccess.accept(selected);

        boolean offscreen = selected.pieces.stream().anyMatch(this::isOffscreen);

        if (!offscreen) {
            selected.pieces.forEach(this::placePiece);
        } else {
            onFail.accept(selected);

            selected.pieces.forEach(this::placePiece);

            if (endMove) {
                sweep();
            }

            return;
        }

        if (!isValidState()) {
            selected.pieces.forEach(this::removePiece);

            onFail.accept(selected);

            selected.pieces.forEach(this::placePiece);

            if (endMove) {
                sweep();
            }
        }
    }

    private boolean isValidState() {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[x][y] > 1) {
                    return false;
                }
            }
        }

        return true;
    }
/**
 * spawns the piece
 */
    private void sweep() {
        List<Integer> rows = sweepRows();
        rows.forEach(row -> {
            for (int x = 0; x < GRID_WIDTH; x++) {
                for (Tetromino tetromino : tetrominos) {
                    tetromino.detach(x, row);
                }

                grid[x][row]--;
            }
        });
//clear line
        rows.forEach(row -> {
            tetrominos.stream().forEach(tetromino -> {
                tetromino.pieces.stream()
                        .filter(piece -> piece.y < row)
                        .forEach(piece -> {
                            removePiece(piece);
                            piece.y++;
                            placePiece(piece);
                        });
                
            });
            linesCleared++;
        });
        scoreLbl.setText("Lines cleared: " + linesCleared);
        spawn();
    }
/**
 * continues with the spawn
 * @return
 */
    private List<Integer> sweepRows() {
        List<Integer> rows = new ArrayList<>();

        outer:
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[x][y] != 1) {
                    continue outer;
                }
            }

            rows.add(y);
        }

        return rows;
    }

    private void spawn() {
        Tetromino tetromino = original.get(randomStream.nextInt(original.size())).copy(); //this random distribution sucks
        tetromino.move(GRID_WIDTH / 2, 0);

        selected = tetromino;

        tetrominos.add(tetromino);
        tetromino.pieces.forEach(this::placePiece);

        if (!isValidState()) {
            System.out.println("Game Over");
            System.exit(0);
        }
    }
/**
 * controlling the directions of all the pieces
 */
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(e -> {
        	switch (e.getCode()) {
        	case UP:
        		makeMove(p -> p.rotate(), p -> p.rotateBack(), false);
        		break;
        	case LEFT:
        		makeMove(p -> p.move(Direction.LEFT), p -> p.move(Direction.RIGHT), false);
        		break;
        	case RIGHT:
        		makeMove(p -> p.move(Direction.RIGHT), p -> p.move(Direction.LEFT), false);
        		break;
        	case DOWN:
        		makeMove(p -> p.move(Direction.DOWN), p -> p.move(Direction.UP), true);
        		break;
//        	case SPACE:
//        		makeMove(p -> p.drop(), p -> p.drop(), true);
//        		break;
        	}
        	
        	 render();
           
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
