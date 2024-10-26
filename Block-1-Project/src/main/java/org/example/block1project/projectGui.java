package org.example.block1project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class projectGui extends Application {

    // Instances of the animated components
    private AnimatedCpuLineGraph cpuLineGraph;
    private AnimatedRamUsage ramUsage;
    private AnimatedCpuClock cpuClock;
    private HomePageInfo homePageInfo;

    @Override
    public void start(Stage primaryStage) {
        // Initialize animated components and home page info
        cpuLineGraph = new AnimatedCpuLineGraph();
        ramUsage = new AnimatedRamUsage();
        cpuClock = new AnimatedCpuClock();
        homePageInfo = new HomePageInfo();  // Home page system information

        // CPU Line Graph and Clock in one scene (for the CPU Tab)
        StackPane cpuChart = new StackPane(cpuLineGraph.getLineChart());
        StackPane cpuClockChart = new StackPane(cpuClock.getClockChart());

        // Create a TabPane as the main layout
        TabPane tabPane = new TabPane();

        // Create the Home Tab with general system information
        Tab homeTab = new Tab("Home");
        homeTab.setContent(homePageInfo.getHomePageLayout());  // Use the VBox with system info

        // Create the CPU Tab with both CPU load and CPU clock graphs
        Tab cpuTab = new Tab("CPU");
        StackPane cpuPage = new StackPane(cpuChart, cpuClockChart);
        cpuTab.setContent(cpuPage);

        // Create a Memory Tab with real-time RAM usage
        Tab memoryTab = new Tab("Memory");
        StackPane memoryPage = new StackPane(ramUsage.getRamUsagePane());  // Use the Arc-based RAM visualization
        memoryTab.setContent(memoryPage);

        // Add tabs to the TabPane
        tabPane.getTabs().addAll(homeTab, cpuTab, memoryTab);

        // Create the main layout and set the TabPane as the center
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        // Create the scene and apply the CSS stylesheet
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());  // Apply the CSS file

        // Set up the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hardware Monitor GUI");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
