package main;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TetrisApp extends Application {
	private final ArrayList<GridPane> rows = new ArrayList<>(), hints = new ArrayList<>();
	private final Image gridBox = new Image("https://images.emojiterra.com/google/android-nougat/512px/25aa.png", 25, 25, false, true);
	
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Hello World!");

		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Hello World!");
			}
		});
		
		
		
		GridPane tetrisBoard = new GridPane();
		tetrisBoard.setGridLinesVisible(true);
		for (int i = 0; i < 20; i++) {
			for (int k = 0; k < 10; k++)
			tetrisBoard.add(new ImageView(gridBox), k, i, 1, 1);
		}
		
		
		StackPane root = new StackPane();
	
		root.getChildren().addAll(tetrisBoard);

		primaryStage.setScene(new Scene(root, 300, 750));
		primaryStage.show();
	}
}

