import java.util.LinkedList;
import java.util.Random;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Environment extends GridPane {
    public int GRID_SIZE = 50;
    public int SQUARE_SIZE = 12;
    
    public int DAYS = 0;
    public int MAX_STEPS = 100;
    public int STEPS = 100;

    public int BLOBS;
    public int FOOD;
    public float DEFAULT_ENERGY = 1000;
    public int DEFAULT_SPEED = 1;
    public float DEFAULT_SIZE = 1;

    public int foodStep = 0;
    public int minFood;

    public boolean foodStepEnabled;
    public boolean speedEnabled;
    public boolean sizeEnabled;

    private LinkedList<Blob> blobs;
    private LinkedList<Blob> blobsToRemove;

    public Environment(int blobsNumber, int foodNumber, boolean speedEnabled, int defaultEnergy, int gridSize, int squareSize, int stepNumber, int defaultSpeed, float defaultSize, boolean sizeEnabled) {
        BLOBS = blobsNumber;
        FOOD = foodNumber;
        this.speedEnabled = speedEnabled;
        this.sizeEnabled = sizeEnabled;
        DEFAULT_ENERGY = defaultEnergy;
        DEFAULT_SPEED = defaultSpeed;
        DEFAULT_SIZE = defaultSize;
        GRID_SIZE = gridSize;
        SQUARE_SIZE = squareSize;
        MAX_STEPS = stepNumber;
        blobs = new LinkedList<>();
        blobsToRemove = new LinkedList<>();
        createEnvironment();
    }

    enum Position {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }

    public double getAverageSpeed() {
        double avg = 0;
        for (Blob b : blobs) {
            avg += b.getSpeed();
        }

        avg /= BLOBS;
        if (Double.isNaN(avg)) return 0;
        return avg;
    }

    public double getAverageSize() {
        double avg = 0;
        for (Blob b : blobs) {
            avg += b.getSize();
        }

        avg /= BLOBS;
        if (Double.isNaN(avg)) return 0;
        return avg;
    }

    public void updateEnvironment(boolean foodStepEnabled, int foodStep, int minFood) {
        this.foodStepEnabled = foodStepEnabled;
        this.foodStep = foodStep;
        this.minFood = minFood;
    }

    public Blob getBlob(int x, int y) {
        for (Blob b : blobs) {
            if (b.getX() == x && b.getY() == y) return b;
        }
        return null;
    }

    public void removeBlob(Blob b) {
        blobsToRemove.add(b);
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
        Random random = new Random();
        
        // Clear the grid
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Rectangle square = (Rectangle)getNode(i, j);
                square.setFill(Color.WHITE);
            }
        }

        // Remove Eaten Blobs
        for (Blob b : blobsToRemove) {
            blobs.remove(b);
        }
    
        // Create children blobs
        LinkedList<Blob> children = new LinkedList<>();
        for (Blob b : blobs) { 
            boolean canReproduce = b.getHasEaten() >= 2;
            b.setHasEaten(0);
            b.setEnergy(DEFAULT_ENERGY);

            int randomSpeed = random.nextInt(2) * 2 - 1;
            float randomSize = (random.nextBoolean() ? 0.1f : -0.1f);

            if (canReproduce) {
                Blob child = new Blob(b);
                if (speedEnabled) b.setSpeed(b.getSpeed() - randomSpeed);
                if (sizeEnabled) b.setSize(b.getSize() - randomSize);
                children.add(child);
            }
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
            Blob b = new Blob(0, 0, DEFAULT_ENERGY, DEFAULT_SPEED, DEFAULT_SIZE);
            blobs.add(b);
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
            Food f = new Food(this);
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
            if (!speedEnabled) blob.randomStep(this);
            else {
                for (int i = 0; i < blob.getSpeed(); i++) {
                    blob.randomStep(this);
                }
            }
        }

        STEPS--;
    }
    
    public void nextDay() {
        blobs.removeIf(b -> b.getHasEaten() < 1);
        STEPS = MAX_STEPS;
        DAYS++;

        if (foodStepEnabled) {
            FOOD -= foodStep;
            if (FOOD < minFood) FOOD = minFood;
        }

        regenerateEnvironment();
    }
}
