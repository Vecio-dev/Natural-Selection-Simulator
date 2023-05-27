import java.util.LinkedList;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Environment extends GridPane {
    public static int GRID_SIZE = 50;
    public static int SQUARE_SIZE = 12;
    
    public int DAYS = 100;
    public int MAX_STEPS = 100;
    public int STEPS = 100;

    public int BLOBS = 25;
    public int FOOD = 50;

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

    enum Position {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }

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

    private void regenerateEnvironment() {
        // Clear the grid
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Rectangle square = (Rectangle)getNode(i, j);
                square.setFill(Color.WHITE);
            }
        }
    
        // Create children blobs
        LinkedList<Blob> children = new LinkedList<>();
        for (Blob b : blobs) { 
            b.setHasEaten(false);
            Blob child = new Blob(b);
            children.add(child);
        }
    
        // Add children to the blobs list
        blobs.addAll(children);
        BLOBS = blobs.size();
    
        // Calculate blobs per side
        int sideLength = GRID_SIZE - 2;
        int blobsPerSide = BLOBS / 4;
        int remainingBlobs = BLOBS % 4;
    
        int topBlobs = blobsPerSide;
        int rightBlobs = blobsPerSide;
        int bottomBlobs = blobsPerSide;
        int leftBlobs = blobsPerSide;
    
        // Distribute remaining blobs evenly
        if (remainingBlobs > 0) {
            topBlobs += 1;
            remainingBlobs--;
            if (remainingBlobs > 0) {
                rightBlobs += 1;
                remainingBlobs--;
            }
            if (remainingBlobs > 0) {
                bottomBlobs += 1;
                remainingBlobs--;
            }
            if (remainingBlobs > 0) {
                leftBlobs += 1;
            }
        }
    
        // Generate blobs on all sides
        int blobIndex = 0;
        blobIndex = generateBlobsOnSide(blobs, blobIndex, topBlobs, Position.TOP, sideLength);
        blobIndex = generateBlobsOnSide(blobs, blobIndex, rightBlobs, Position.RIGHT, sideLength);
        blobIndex = generateBlobsOnSide(blobs, blobIndex, bottomBlobs, Position.BOTTOM, sideLength);
        blobIndex = generateBlobsOnSide(blobs, blobIndex, leftBlobs, Position.LEFT, sideLength);
    
        // Generate food
        generateFood();
    }
    
    
    private void generateBlob() {
        int sideLength = GRID_SIZE - 2;
        int blobsPerSide = BLOBS / 4;
        int remainingBlobs = BLOBS % 4;
        
        int topBlobs = blobsPerSide;
        int rightBlobs = blobsPerSide;
        int bottomBlobs = blobsPerSide;
        int leftBlobs = blobsPerSide;
        
        // Distribute remaining blobs evenly
        if (remainingBlobs > 0) {
            topBlobs += 1;
            remainingBlobs--;
            if (remainingBlobs > 0) {
                rightBlobs += 1;
                remainingBlobs--;
            }
            if (remainingBlobs > 0) {
                bottomBlobs += 1;
                remainingBlobs--;
            }
            if (remainingBlobs > 0) {
                leftBlobs += 1;
            }
        }
        
        // Generate initial blobs and add them to the list
        for (int i = 0; i < BLOBS; i++) {
            blobs.add(new Blob(0, 0, Color.RED));
        }
        
        // Position blobs on all sides
        int blobIndex = 0;
        blobIndex = generateBlobsOnSide(blobs, blobIndex, topBlobs, Position.TOP, sideLength);
        blobIndex = generateBlobsOnSide(blobs, blobIndex, rightBlobs, Position.RIGHT, sideLength);
        blobIndex = generateBlobsOnSide(blobs, blobIndex, bottomBlobs, Position.BOTTOM, sideLength);
        blobIndex = generateBlobsOnSide(blobs, blobIndex, leftBlobs, Position.LEFT, sideLength);
    }    

    private int generateBlobsOnSide(LinkedList<Blob> blobs, int startIndex, int numberOfBlobs, Position position, int sideLength) {
        for (int i = 0; i < numberOfBlobs; i++) {
            double pos = (double) (i + 1) / (numberOfBlobs + 1);
            Blob b = blobs.get(startIndex + i);
    
            switch (position) {
                case TOP:
                    b.setX((int) Math.round(pos * sideLength) + 1);
                    b.setY(0);
                    break;
                case RIGHT:
                    b.setX(GRID_SIZE - 1);
                    b.setY((int) Math.round(pos * sideLength) + 1);
                    break;
                case BOTTOM:
                    b.setX(GRID_SIZE - 1 - (int) Math.round(pos * sideLength));
                    b.setY(GRID_SIZE - 1);
                    break;
                case LEFT:
                    b.setX(0);
                    b.setY(GRID_SIZE - 1 - (int) Math.round(pos * sideLength));
                    break;
            }
            ((Rectangle)getNode(b.getX(), b.getY())).setFill(b.getColor());
        }
        return startIndex + numberOfBlobs;
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
        for (Blob blob : blobs) {
            blob.randomStep(this);
        }
    }
    
    public void nextDay() {
        blobs.removeIf(b -> !b.getHasEaten());
        STEPS = MAX_STEPS;

        regenerateEnvironment();
    }
}
