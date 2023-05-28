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

    private LineChart<Number, Number> blobsChart;
    private XYChart.Series<Number, Number> blobSeries;

    private LineChart<Number, Number> avgSpeedChart;
    private XYChart.Series<Number, Number> avgSpeedSeries;

    private LineChart<Number, Number> avgSizeChart;
    private XYChart.Series<Number, Number> avgSizeSeries;

    Label blobsNum;
    Label daysNum;

    @Override
    public void start(Stage primaryStage) {
        ScrollPane scrollPane = new ScrollPane();
        VBox mainLayout = new VBox();
        BorderPane root = new BorderPane();
        HBox centerBox = new HBox();
        env = new Environment(0, 0, false, 0, 50, 12, 100, 1, 1, false);

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
        simulationButtonsBox.setAlignment(Pos.CENTER);
        simulationButtonsBox.setSpacing(10);
        VBox statsBox = new VBox(simulationButtonsBox, blobsNum, daysNum);
        statsBox.setAlignment(Pos.CENTER);
        
        // SETTINGS
        Button generateSimulationButton = new Button("Generate");
        generateSimulationButton.setAlignment(Pos.CENTER);
        
        Label blobsNumberLabel = new Label("Blobs: ");
        TextField blobsNumberTextField = new TextField("0");
        HBox blobsBox = new HBox(blobsNumberLabel, blobsNumberTextField);
        
        Label foodNumberLabel = new Label("Food: ");
        TextField foodNumberTextField = new TextField("0");
        HBox foodBox = new HBox(foodNumberLabel, foodNumberTextField);

        Label gridSizeLabel = new Label("Grid Size: ");
        TextField gridSizeTextField = new TextField("50");
        HBox gridSizeBox = new HBox(gridSizeLabel, gridSizeTextField);

        Label squareSizeLabel = new Label("Square Size: ");
        TextField squareSizeTextField = new TextField("12");
        HBox squareSizeBox = new HBox(squareSizeLabel, squareSizeTextField);

        Label stepsNumberLabel = new Label("Steps: ");
        TextField stepNumberTextField = new TextField("100");
        HBox stepNumberBox = new HBox(stepsNumberLabel, stepNumberTextField);

        VBox environmentSettings = new VBox(gridSizeBox, squareSizeBox, blobsBox, foodBox, stepNumberBox);
        environmentSettings.setSpacing(5);

        TitledPane environmentDropDown = new TitledPane();
        environmentDropDown.setText("Environment");
        environmentDropDown.setContent(environmentSettings);
        
        Label speedLabel = new Label("Speed: ");
        CheckBox speedCheckbox = new CheckBox();
        HBox speedBox = new HBox(speedLabel, speedCheckbox);

        Label sizeLabel = new Label("Size: ");
        CheckBox sizeCheckbox = new CheckBox();
        HBox sizeBox = new HBox(sizeLabel, sizeCheckbox);

        Label defaultEnergyLabel = new Label("Default energy: ");
        TextField defaultEnergyTextField = new TextField("1000");
        HBox energyBox = new HBox(defaultEnergyLabel, defaultEnergyTextField);

        Label defaultSpeedLabel = new Label("Default Speed: ");
        TextField defaultSpeedTextField = new TextField("1");
        HBox defaultSpeedBox = new HBox(defaultSpeedLabel, defaultSpeedTextField);

        Label defaultSizeLabel = new Label("Default Size: ");
        TextField defaultSizeTextField = new TextField("1");
        HBox defaultSizeBox = new HBox(defaultSizeLabel, defaultSizeTextField);

        VBox blobSettings = new VBox(speedBox, sizeBox, energyBox, defaultSpeedBox, defaultSizeBox);
        blobSettings.setSpacing(5);
        TitledPane blobsDropDown = new TitledPane();
        blobsDropDown.setText("Blobs");
        blobsDropDown.setContent(blobSettings);

        Label foodStepLabel = new Label("Food Step: ");
        CheckBox foodStepCheckbox = new CheckBox();
        HBox foodStepBox = new HBox(foodStepLabel, foodStepCheckbox);

        Label foodStepNumLabel = new Label("Food Step N: ");
        TextField foodStepNumTextField = new TextField("0");
        HBox foodStepNumBox = new HBox(foodStepNumLabel, foodStepNumTextField);

        Label minFoodStepNumLabel = new Label("Min Food: ");
        TextField minFoodStepNumTextField = new TextField("0");
        HBox minFoodStepNumBox = new HBox(minFoodStepNumLabel, minFoodStepNumTextField);

        Button foodStepButton = new Button("Update");
        foodStepButton.setAlignment(Pos.CENTER);
        foodStepButton.setOnAction(event -> updateEnvironment(foodStepCheckbox, foodStepNumTextField, minFoodStepNumTextField));

        VBox liveSettings = new VBox(foodStepBox, foodStepNumBox, minFoodStepNumBox, foodStepButton);
        liveSettings.setSpacing(5);

        TitledPane liveDropDown = new TitledPane();
        liveDropDown.setText("Live");
        liveDropDown.setContent(liveSettings);
        
        VBox settingsBox = new VBox(generateSimulationButton, environmentDropDown, blobsDropDown, liveDropDown);
        settingsBox.setSpacing(10);

        centerBox.setSpacing(20);
        centerBox.getChildren().addAll(env, settingsBox);

        generateSimulationButton.setOnAction(event -> generateEnvironment(root, centerBox, settingsBox, blobsNumberTextField, foodNumberTextField, speedCheckbox, defaultEnergyTextField, gridSizeTextField, squareSizeTextField, stepNumberTextField, defaultSpeedTextField, defaultSizeTextField, sizeCheckbox));

        // CHARTS
        VBox graphsBox = new VBox();
        generateCharts(graphsBox);

        // WINDOW SETTINGS
        root.setTop(statsBox);
        root.setLeft(centerBox);
        //root.setRight(settingsBox);
        //root.setBottom(graphsBox);

        mainLayout.getChildren().addAll(root, graphsBox);

        scrollPane.setContent(mainLayout);
        Scene scene = new Scene(scrollPane, 1200, 800);

        primaryStage.setTitle("Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateEnvironment(CheckBox foodStepCheckbox, TextField foodStepNumTextField, TextField minFoodStepNumTextField) {
        boolean foodStepEnabled = foodStepCheckbox.isSelected();
        int foodStepNum = Integer.parseInt(foodStepNumTextField.getText());
        int minFood = Integer.parseInt(minFoodStepNumTextField.getText());
        
        env.updateEnvironment(foodStepEnabled, foodStepNum, minFood);
    }

    private void generateCharts(VBox graphsBox) {
        graphsBox.getChildren().clear();
    
        // Blobs Chart
        final NumberAxis blobsXAxis = new NumberAxis();
        final NumberAxis blobsYAxis = new NumberAxis();
        blobsXAxis.setLabel("Days");
        blobsXAxis.setTickUnit(1);
        blobsYAxis.setLabel("Blobs");
    
        blobsChart = new LineChart<>(blobsXAxis, blobsYAxis);
        blobsChart.setCreateSymbols(false);
        blobsChart.setLegendVisible(false);
        blobSeries = new XYChart.Series<>();
        blobSeries.getData().add(new XYChart.Data<>(env.DAYS, env.BLOBS));
        blobsChart.getData().add(blobSeries);
    
        // Average Speed Chart
        final NumberAxis speedXAxis = new NumberAxis();
        final NumberAxis speedYAxis = new NumberAxis();
        speedXAxis.setLabel("Days");
        speedXAxis.setTickUnit(1);
        speedYAxis.setLabel("Avg. Speed");
    
        avgSpeedChart = new LineChart<>(speedXAxis, speedYAxis);
        avgSpeedChart.setCreateSymbols(false);
        avgSpeedChart.setLegendVisible(false);
        avgSpeedSeries = new XYChart.Series<>();
        avgSpeedSeries.getData().add(new XYChart.Data<>(env.DAYS, env.getAverageSpeed()));
        avgSpeedChart.getData().add(avgSpeedSeries);
    
        HBox firstLine = new HBox();
        firstLine.getChildren().addAll(blobsChart, avgSpeedChart);

        // Average Size Chart
        final NumberAxis sizeXAxis = new NumberAxis();
        final NumberAxis sizeYAxis = new NumberAxis();
        sizeXAxis.setLabel("Days");
        sizeXAxis.setTickUnit(1);
        sizeYAxis.setLabel("Avg. Size");
    
        avgSizeChart = new LineChart<>(sizeXAxis, sizeYAxis);
        avgSizeChart.setCreateSymbols(false);
        avgSizeChart.setLegendVisible(false);
        avgSizeSeries = new XYChart.Series<>();
        avgSizeSeries.getData().add(new XYChart.Data<>(env.DAYS, env.getAverageSize()));
        avgSizeChart.getData().add(avgSizeSeries);

        HBox secondLine = new HBox();
        secondLine.getChildren().addAll(avgSizeChart);

        graphsBox.getChildren().addAll(firstLine, secondLine);
    }    

    private void generateEnvironment(BorderPane root, HBox centerbox, VBox settingsBox, TextField blobsNumberTextField, TextField foodNumberTextField, CheckBox speedCheckbox, TextField defaultEnergyTextField, TextField gridSizeTextField, TextField squareSizeTextField, TextField stepNumberTextField, TextField defaultSpeedTextField, TextField defaultSizeTextField, CheckBox sizeCheckBox) {
        int blobsNumber = Integer.parseInt(blobsNumberTextField.getText());
        int foodNumber = Integer.parseInt(foodNumberTextField.getText());
        boolean speedEnabled = speedCheckbox.isSelected();
        int defaultEnergy = Integer.parseInt(defaultEnergyTextField.getText());
        int gridSize = Integer.parseInt(gridSizeTextField.getText());
        int squareSize = Integer.parseInt(squareSizeTextField.getText());
        int stepNumber = Integer.parseInt(stepNumberTextField.getText());
        int defaultSpeed = Integer.parseInt(defaultSpeedTextField.getText());
        float defaultSize = Float.parseFloat(defaultSizeTextField.getText());
        boolean sizeEnabled = sizeCheckBox.isSelected();

        env = new Environment(blobsNumber, foodNumber, speedEnabled, defaultEnergy, gridSize, squareSize, stepNumber, defaultSpeed, defaultSize, sizeEnabled);

        centerbox.getChildren().clear();
        centerbox.getChildren().addAll(env, settingsBox);
        root.setLeft(centerbox);
        updateStats();

        blobSeries.getData().clear();
        blobSeries.getData().add(new XYChart.Data<>(env.DAYS, env.BLOBS));

        avgSpeedSeries.getData().clear();
        avgSpeedSeries.getData().add(new XYChart.Data<>(env.DAYS, env.getAverageSpeed()));

        avgSizeSeries.getData().clear();
        avgSizeSeries.getData().add(new XYChart.Data<>(env.DAYS, env.getAverageSize()));
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
                blobSeries.getData().add(new XYChart.Data<>(env.DAYS, env.BLOBS));
                avgSpeedSeries.getData().add(new XYChart.Data<>(env.DAYS, env.getAverageSpeed()));
                updateStats();
                avgSizeSeries.getData().add(new XYChart.Data<>(env.DAYS, env.getAverageSize()));
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

                blobSeries.getData().add(new XYChart.Data<>(env.DAYS, env.BLOBS));
                avgSpeedSeries.getData().add(new XYChart.Data<>(env.DAYS, env.getAverageSpeed()));
                avgSizeSeries.getData().add(new XYChart.Data<>(env.DAYS, env.getAverageSize()));
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
