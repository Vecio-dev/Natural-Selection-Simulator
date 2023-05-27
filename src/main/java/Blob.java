import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Blob {
    private Color BLOB_COLOR; 

    private int x;
    private int y;

    private boolean hasEaten = false;

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public Color getColor() { return BLOB_COLOR; }
    public void setColor(Color c) { BLOB_COLOR = c; }
    public boolean getHasEaten() { return hasEaten; }
    public void setHasEaten(boolean v) { hasEaten = v; }

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

    public Blob(Blob b) {
        this.x = b.getX();
        this.y = b.getY();
        this.hasEaten = b.getHasEaten();
        this.BLOB_COLOR = b.getColor();
    }

    private Color getPositionColor(Environment env, int x, int y) {
        return ((Color)((Rectangle)env.getNode(x, y)).getFill());
    }

    public void move(Environment env, int newX, int newY) {
        ((Rectangle)env.getNode(x, y)).setFill(Color.WHITE);
        x = newX;
        y = newY;
        if (getPositionColor(env, x, y) == Food.FOOD_COLOR) {
            hasEaten = true;
        }
        ((Rectangle)env.getNode(x, y)).setFill(BLOB_COLOR);
    }

    private boolean validPosition(Environment env, int newX, int newY) {
        if (newX >= 0 && newX < Environment.GRID_SIZE && newY >= 0 && newY < Environment.GRID_SIZE) return true;
        else return false;
    }

    public void randomStep(Environment env) {
        Random random = new Random();
    
        int randomDirection = random.nextInt(3) - 1;

        int newX = x;
        int newY = y;
        boolean valid = false;
    
        while (!valid) {
            int randomX = random.nextInt(2) * 2 - 1;
            int randomY = random.nextInt(2) * 2 - 1;
            switch (randomDirection) {
                case -1: // x
                    newX = x + randomX;
                    if (validPosition(env, newX, newY)) {
                        move(env, newX, newY);
                        valid = true;
                    }
                    break;
                case 0: // x and y
                    newX = x + randomX;
                    newY = y + randomY;
                    
                    if (validPosition(env, newX, newY)) {
                        move(env, newX, newY);
                        valid = true;
                    }
                    break;
                case 1:
                    newY = y + randomY;
                        
                    if (validPosition(env, newX, newY)) {
                        move(env, newX, newY);
                        valid = true;
                    }
                    break;
            }
        }
    }
}
