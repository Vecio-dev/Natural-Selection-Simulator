import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class GameApp extends Application {
    private final Duration delay = Duration.millis(1000);

    @Override
    public void start(Stage primaryStage) {
        Environment env = new Environment(20, 25);
        Blob blob = new Blob();
        env.addBlob(blob);
        Blob blob2 = new Blob(Environment.GRID_SIZE - 1, Environment.GRID_SIZE - 1, Color.BLUE);
        env.addBlob(blob2);

        Scene scene = new Scene(env, 600, 600);
        
        primaryStage.setTitle("Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

        startSimulation(env);
    }
    
    private void startSimulation(Environment env) {
        Timeline timeline = new Timeline(new KeyFrame(delay, event -> env.nextStep()));
        timeline.setCycleCount(env.STEPS); // Number of steps to execute
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
