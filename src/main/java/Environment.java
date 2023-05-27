import java.util.LinkedList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Environment extends GridPane {
    public static int GRID_SIZE = 50;
    public static int SQUARE_SIZE = 10;
    
    public int DAYS = 100;
    public int STEPS = 100;

    public int BLOBS = 25;
    public int FOOD = 25;

    private LinkedList<Blob> blobs;

    public Environment() {
        blobs = new LinkedList<>();
        createEnvironment();
    }

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

        generateBlob();
        generateFood();
    }

    private void generateBlob() {
        int sideLength = GRID_SIZE - 2; // Length of each side (excluding corners)
        int blobsPerSide = BLOBS / 4; // Number of blobs per side
        int remainingBlobs = BLOBS % 4; // Blobs remaining after distributing equally
    
        int topBlobs = blobsPerSide;
        int rightBlobs = blobsPerSide;
        int bottomBlobs = blobsPerSide;
        int leftBlobs = blobsPerSide;
    
        // Distribute remaining blobs evenly
        if (remainingBlobs > 0) {
            topBlobs += remainingBlobs;
            remainingBlobs = Math.max(remainingBlobs - sideLength, 0);
            rightBlobs += remainingBlobs;
            remainingBlobs = Math.max(remainingBlobs - sideLength, 0);
            bottomBlobs += remainingBlobs;
            remainingBlobs = Math.max(remainingBlobs - sideLength, 0);
            leftBlobs += remainingBlobs;
        }
    
        // Generate blobs on top side
        for (int i = 0; i < topBlobs; i++) {
            double position = (double) (i + 1) / (topBlobs + 1);
            int x = (int) Math.round(position * sideLength) + 1;
            int y = 0;
            Blob b = new Blob(x, y, Color.RED);
            addBlob(b);
        }
    
        // Generate blobs on right side
        for (int i = 0; i < rightBlobs; i++) {
            double position = (double) (i + 1) / (rightBlobs + 1);
            int x = GRID_SIZE - 1;
            int y = (int) Math.round(position * sideLength) + 1;
            Blob b = new Blob(x, y, Color.RED);
            addBlob(b);
        }
    
        // Generate blobs on bottom side
        for (int i = 0; i < bottomBlobs; i++) {
            double position = (double) (i + 1) / (bottomBlobs + 1);
            int x = GRID_SIZE - 1 - (int) Math.round(position * sideLength);
            int y = GRID_SIZE - 1;
            Blob b = new Blob(x, y, Color.RED);
            addBlob(b);
        }
    
        // Generate blobs on left side
        for (int i = 0; i < leftBlobs; i++) {
            double position = (double) (i + 1) / (leftBlobs + 1);
            int x = 0;
            int y = GRID_SIZE - 1 - (int) Math.round(position * sideLength);
            Blob b = new Blob(x, y, Color.RED);
            addBlob(b);
        }
    }
    
    private void generateFood() {
        for (int i = 0; i < FOOD; i++) {
            Food f = new Food();
            ((Rectangle)getNode(f.getX(), f.getY())).setFill(f.getColor());
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
        for (int i = 0; i < STEPS; i++) {
            for (Blob blob : blobs) {
                blob.randomStep(this);
            }
            //System.out.println("Step " + STEPS + " taken.");
        }

        System.out.println("Day finished");
        blobs.removeIf(b -> !b.getHasEaten());
        BLOBS = blobs.size() * 2;
        createEnvironment();
    }

    public void nextDay() {
        if (DAYS > 0) {
            nextStep();
            DAYS--;
        }
    }
}
