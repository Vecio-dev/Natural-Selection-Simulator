import java.util.logging.Logger;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class HelloFX extends Application {
    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);

        for (int row = 0; row < 480 / 25; row++) {
            for (int col = 0; col < 640 / 25; col++) {
                Rectangle cell = new Rectangle(25, 25, Color.WHITE);
                grid.add(cell, col, row);
            }
        }

        Rectangle player = new Rectangle(25, 25, Color.BLUE);
        int[] coordinates = {1, 0};
        grid.add(player, coordinates[0], coordinates[1]);
        grid.requestFocus();
        
        Scene scene = new Scene(grid, 640, 480);
        scene.setOnKeyPressed(e -> {
            System.out.println("GOKU");
            //grid.add(new Rectangle(25, 25, Color.RED), 2, 0);
            if (e.getCode() == KeyCode.D) {
                coordinates[0] += 1;
                GridPane.setColumnIndex(player, coordinates[0]);
            }else if (e.getCode() == KeyCode.A) {
                coordinates[0] -= 1;
                GridPane.setColumnIndex(player, coordinates[0]);
            }else if (e.getCode() == KeyCode.W) {
                coordinates[1] += 1;
                GridPane.setRowIndex(player, coordinates[1]);
            }else if (e.getCode() == KeyCode.S) {
                coordinates[0] -= 1;
                GridPane.setRowIndex(player, coordinates[1]);
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}