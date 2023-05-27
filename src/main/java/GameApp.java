import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.stage.Stage;

public class GameApp extends Application {
    private Timeline timeline;
    private final Duration delay = Duration.millis(25);

    Label blobsNum;
    Label daysNum;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Environment env = new Environment();

        Button startSimulationButton = new Button("Start");
        startSimulationButton.setAlignment(Pos.CENTER);
        startSimulationButton.setOnAction(event -> {
            startSimulationButton.setDisable(true);
            startSimulation(env);
        });

        Button resumeSimulationButton = new Button("Resume");
        resumeSimulationButton.setAlignment(Pos.CENTER);
        resumeSimulationButton.setOnAction(event -> resumeSimulation());

        Button pauseSimulationButton = new Button("Pause");
        pauseSimulationButton.setAlignment(Pos.CENTER);
        pauseSimulationButton.setOnAction(event -> pauseSimulation());

        root.setCenter(env);
        //root.setTop(startSimulationButton);

        blobsNum = new Label();
        daysNum = new Label();

        VBox statsBox = new VBox(startSimulationButton, resumeSimulationButton, pauseSimulationButton, blobsNum, daysNum);
        statsBox.setAlignment(Pos.CENTER);
        root.setTop(statsBox);

        Scene scene = new Scene(root, 800, 800);

        primaryStage.setTitle("Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void startSimulation(Environment env) {
        timeline = new Timeline();
        updateStats(env);
    
        KeyFrame keyFrame = new KeyFrame(delay, event -> {
            env.nextStep();
            env.STEPS--;
    
            if (env.STEPS <= 0) {
                env.nextDay();
                env.DAYS--;
                updateStats(env);
                timeline.pause();
            }
    
            if (env.DAYS <= 0) {
                timeline.stop();
            }
        });
    
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void resumeSimulation() {
        timeline.play();
    }

    private void pauseSimulation() {
        timeline.pause();
    }

    private void updateStats(Environment env) {
        blobsNum.setText("Blobs: " + env.BLOBS);
        daysNum.setText("Days: " + env.DAYS);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
