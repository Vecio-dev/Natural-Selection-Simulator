import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Blob {
    private Color BLOB_COLOR;

    private int x;
    private int y;

    private int hasEaten = 0;

    private double energyCost;

    private float energy;
    private int speed;
    private float size;

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public Color getColor() { return BLOB_COLOR; }
    public void setColor(Color c) { BLOB_COLOR = c; }
    public int getHasEaten() { return hasEaten; }
    public void setHasEaten(int v) { hasEaten = v; }
    public float getEnergy() { return energy; }
    public void setEnergy(float energy) { this.energy = energy; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) {
        if (speed <= 0) speed = 1; 
        this.speed = speed;
        this.energyCost = calculateEnergyCost();
        this.BLOB_COLOR = generateColor();
    }
    public float getSize() { return size; }
    public void setSize(float size) { 
        if (size <= 0) size = 0.1f;
        this.size = size;
        this.energyCost = calculateEnergyCost();
        this.BLOB_COLOR = generateColor();
    }
    public double getEnergyCost() { return energyCost; }
    public void setEnergyCost(float energyCost) { this.energyCost = energyCost; }

    public Blob() {
        this.x = 0;
        this.y = 0;
        this.energy = 0f;
        this.speed = 0;
        this.size = 1;
        this.energyCost = calculateEnergyCost();
        this.BLOB_COLOR = generateColor();
    }

    public Blob(int x, int y, float energy, int speed, float size) {
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.speed = speed;
        this.size = size;
        this.energyCost = calculateEnergyCost();
        this.BLOB_COLOR = generateColor();
    }

    public Blob(Blob b) {
        this.x = b.getX();
        this.y = b.getY();
        this.hasEaten = b.getHasEaten();
        this.BLOB_COLOR = b.getColor();
        this.energy = b.getEnergy();
        this.speed = b.getSpeed();
        this.size = b.getSize();
        this.energyCost = b.getEnergyCost();
    }

    private double calculateEnergyCost() {
        return Math.pow(size, 3) * Math.pow(speed, 2);
    }

    private Color generateColor() {
        double hue = ((speed + size) * 18) % 360;
    
        return Color.hsb(hue, 0.9, 0.9);
    }

    private Color getPositionColor(Environment env, int x, int y) {
        return ((Color)((Rectangle)env.getNode(x, y)).getFill());
    }

    public void move(Environment env, int newX, int newY) {
        ((Rectangle)env.getNode(x, y)).setFill(Color.WHITE);
        Color positionColor = getPositionColor(env, newX, newY);
        if (positionColor != Food.FOOD_COLOR && positionColor != Color.WHITE) {
            Blob enemy = env.getBlob(newX, newY);
            
            if ((size * 100) / enemy.getSize() >= 120) {
                env.removeBlob(enemy);
                hasEaten++;
            }
        } else if (positionColor == Food.FOOD_COLOR) {
            hasEaten++;
        }

        x = newX;
        y = newY;
        ((Rectangle)env.getNode(x, y)).setFill(BLOB_COLOR);
        
        energy -= energyCost;
    }

    private boolean validPosition(Environment env, int newX, int newY) {
        if (newX >= 0 && newX < Environment.GRID_SIZE && newY >= 0 && newY < Environment.GRID_SIZE) return true;
        else return false;
    }

    public void randomStep(Environment env) {
        if (energy <= energyCost) return;
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
