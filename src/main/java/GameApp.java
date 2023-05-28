import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.stage.Stage;

public class GameApp extends Application {
    private Timeline timeline;
    private final Duration delay = Duration.millis(50);
    private Environment env;

    private boolean simulationPlaying = false;

    private LineChart<Number,Number> lineChart;
    private XYChart.Series<Number, Number> series;

    Label blobsNum;
    Label daysNum;

    @Override
    public void start(Stage primaryStage) {
        ScrollPane scrollPane = new ScrollPane();
        BorderPane root = new BorderPane();
        env = new Environment(0, 0, false);

        // SIMULATION BUTTONS
        Button startSimulationButton = new Button("Start");
        startSimulationButton.setAlignment(Pos.CENTER);
        startSimulationButton.setOnAction(event -> startSimulation());

        Button resumeSimulationButton = new Button("Resume");
        resumeSimulationButton.setAlignment(Pos.CENTER);
        resumeSimulationButton.setOnAction(event -> resumeSimulation());

        Button pauseSimulationButton = new Button("Pause");
        pauseSimulationButton.setAlignment(Pos.CENTER);
        pauseSimulationButton.setOnAction(event -> pauseSimulation());

        Button skipButton = new Button("Skip");
        TextField skipTextField = new TextField("0");
        skipButton.setOnAction(event -> skip(skipTextField));
        HBox skipBox = new HBox(skipButton, skipTextField);

        // STATS
        blobsNum = new Label("Blobs: " + env.BLOBS);
        daysNum = new Label("Days: " + env.DAYS);

        HBox simulationButtonsBox = new HBox(startSimulationButton, resumeSimulationButton, pauseSimulationButton, skipBox);
        simulationButtonsBox.setSpacing(10);
        VBox statsBox = new VBox(simulationButtonsBox, blobsNum, daysNum);
        statsBox.setAlignment(Pos.CENTER);

        // SETTINGS
        Button generateSimulationButton = new Button("Generate");
        generateSimulationButton.setAlignment(Pos.CENTER);

        Label blobsNumberLabel = new Label("Blobs N: ");
        TextField blobsNumberTextField = new TextField("0");
        HBox blobsBox = new HBox(blobsNumberLabel, blobsNumberTextField);

        Label foodNumberLabel = new Label("Food N: ");
        TextField foodNumberTextField = new TextField("0");
        HBox foodBox = new HBox(foodNumberLabel, foodNumberTextField);

        VBox environmentSettings = new VBox(blobsBox, foodBox);
        TitledPane environmentDropDown = new TitledPane();
        environmentDropDown.setText("Environment");
        environmentDropDown.setContent(environmentSettings);

        Label traitsLabel = new Label("Traits:");
        Label speedLabel = new Label("Speed: ");
        CheckBox speedCheckbox = new CheckBox();
        HBox speedBox = new HBox(speedLabel, speedCheckbox);

        VBox blobSettings = new VBox(traitsLabel, speedBox);
        TitledPane blobsDropDown = new TitledPane();
        blobsDropDown.setText("Blobs");
        blobsDropDown.setContent(blobSettings);

        generateSimulationButton.setOnAction(event -> generateEnvironment(root, blobsNumberTextField, foodNumberTextField, speedCheckbox));
        VBox settingsBox = new VBox(generateSimulationButton, environmentDropDown, blobsDropDown);
        settingsBox.setSpacing(10);

        // CHARTS
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days");
        yAxis.setLabel("Blobs");

        lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setLegendVisible(false);
        series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<Number, Number>(env.DAYS, env.BLOBS));
        lineChart.getData().add(0, series);

        HBox graphsBox = new HBox(lineChart);

        // WINDOW SETTINGS
        root.setTop(statsBox);
        root.setLeft(env);
        root.setRight(settingsBox);
        root.setBottom(graphsBox);

        scrollPane.setContent(root);
        Scene scene = new Scene(scrollPane, 1000, 800);

        primaryStage.setTitle("Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateEnvironment(BorderPane root, TextField blobsNumberTextField, TextField foodNumberTextField, CheckBox speedCheckbox) {
        int blobsNumber = Integer.parseInt(blobsNumberTextField.getText());
        int foodNumber = Integer.parseInt(foodNumberTextField.getText());
        boolean speedEnabled = speedCheckbox.isSelected();

        env = new Environment(blobsNumber, foodNumber, speedEnabled);
        root.setLeft(env);
        updateStats();
        series.getData().clear();
        series.getData().add(new XYChart.Data<Number, Number>(env.DAYS, env.BLOBS));
    }

    private void skip(TextField skipNumTextField) {
        int skipNum = Integer.parseInt(skipNumTextField.getText());
        if (simulationPlaying) pauseSimulation();
    
        int remainingSteps = skipNum * env.MAX_STEPS;
    
        while (remainingSteps > 0) {
            int stepsToSimulate = Math.min(remainingSteps, env.STEPS);
            for (int i = 0; i < stepsToSimulate; i++) {
                env.nextStep();
            }
            remainingSteps -= stepsToSimulate;
    
            if (env.STEPS <= 0) {
                env.nextDay();
                series.getData().add(new XYChart.Data<>(env.DAYS, env.BLOBS));
                updateStats();
            }
        }
    }
    
    private void startSimulation() {
        simulationPlaying = true;
        timeline = new Timeline();
        updateStats();
    
        KeyFrame keyFrame = new KeyFrame(delay, event -> {
            env.nextStep();
    
            if (env.STEPS <= 0) {
                env.nextDay();
                updateStats();

                series.getData().add(new XYChart.Data<Number, Number>(env.DAYS, env.BLOBS));

                //timeline.pause();
            }
        });
    
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void resumeSimulation() {
        simulationPlaying = true;
        timeline.play();
    }

    private void pauseSimulation() {
        simulationPlaying = false;
        timeline.pause();
    }

    private void updateStats() {
        blobsNum.setText("Blobs: " + env.BLOBS);
        daysNum.setText("Days: " + env.DAYS);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
