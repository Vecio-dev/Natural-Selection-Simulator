import java.util.Random;

import javafx.scene.paint.Color;

public class Food {
    public static final Color FOOD_COLOR = Color.GREEN;

    private int x;
    private int y;

    public Color getColor() { return FOOD_COLOR; }
    public int getX() { return x; }
    public int getY() { return y; }

    public Food() {
        Random random = new Random();

        this.x = random.nextInt(Environment.GRID_SIZE - 2) + 1;
        this.y = random.nextInt(Environment.GRID_SIZE - 2) + 1;
    }
}
