import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Blob {
    private Color BLOB_COLOR; 

    private int x;
    private int y;

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public Color getColor() { return BLOB_COLOR; }

    public Blob() {
        this.x = 0;
        this.y = 0;
        this.BLOB_COLOR = Color.RED;
    }

    public Blob(int x, int y, Color c) {
        this.x = x;
        this.y = y;
        this.BLOB_COLOR = c;
    }

    public void move(Environment env, int newX, int newY) {
        ((Rectangle)env.getNode(x, y)).setFill(Color.WHITE);
        x = newX;
        y = newY;
        ((Rectangle)env.getNode(x, y)).setFill(BLOB_COLOR);
    }

    public void randomStep(Environment env) {
        Random random = new Random();
    
        int newX = x;
        int newY = y;
        boolean valid = false;
    
        System.out.println("randomStep");
        while (!valid) {
            int randomX = random.nextInt(2) * 2 - 1; // Randomly choose -1 or 1 for x direction
            int randomY = random.nextInt(2) * 2 - 1; // Randomly choose -1 or 1 for y direction
    
            newX = x + randomX;
            newY = y + randomY;
    
            
            if (newX >= 0 && newX < Environment.GRID_SIZE && newY >= 0 && newY < Environment.GRID_SIZE) {
                move(env, newX, newY);
                valid = true;
            }
        }
    }
}
