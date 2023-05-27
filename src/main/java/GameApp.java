import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import javafx.stage.Stage;

public class GameApp extends Application {
    private final Duration delay = Duration.millis(100);

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Environment env = new Environment();
        Button startSimulationButton = new Button("Start");
        
        // Set the size and alignment of the button
        startSimulationButton.setAlignment(Pos.CENTER);
        startSimulationButton.setOnAction(event -> startSimulation(env));

        root.setCenter(env);
        root.setTop(startSimulationButton);

        Scene scene = new Scene(root, 600, 600);

        primaryStage.setTitle("Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
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
