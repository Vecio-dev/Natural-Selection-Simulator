import java.util.LinkedList;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Environment extends GridPane {
    public static int GRID_SIZE = 20;
    public static int SQUARE_SIZE = 25;
    
    public int STEPS = 10;

    private LinkedList<Blob> blobs;

    public Environment(int gridSize, int squareSize) {
        GRID_SIZE = gridSize;
        SQUARE_SIZE = squareSize;
        blobs = new LinkedList<>();
        createEnvironment();
    }

    public void setGridSize(int size) { GRID_SIZE = size; }
    public void setSquareSize(int size) { SQUARE_SIZE = size; }

    private void createEnvironment() {
        this.getChildren().clear(); // Clear existing content

        this.setAlignment(Pos.CENTER);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                square.setFill(Color.WHITE);
                square.setStroke(Color.BLACK);
                this.add(square, i, j);
            }
        }
    }

    public Node getNode(int x, int y) {
        for (Node node : this.getChildren()) {
            if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) return node;
        }
        return null;
    }

    public void addBlob(Blob b) {
        ((Rectangle)getNode(b.getX(), b.getY())).setFill(b.getColor());
        blobs.add(b);
    }

    public void nextStep() {
        if (STEPS > 0) {
            for (Blob blob : blobs) {
                blob.randomStep(this);
            }
            STEPS--;
            System.out.println("Step " + STEPS + " taken.");
        }
    }
}
