package org.example.block1project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class projectGui extends Application {

    // Instances of the animated components
    private AnimatedCpuLineGraph cpuLineGraph;
    private AnimatedRamUsage ramUsage;
    private AnimatedCpuClock cpuClock;
    private HomePageInfo home;
    private NetworkUsage networkUsage;
    private AnimatedDiskLineGraph diskLineGraph;

    @Override
    public void start(Stage primaryStage) {
        // Initialize animated components and home page info
        cpuLineGraph = new AnimatedCpuLineGraph();
        ramUsage = new AnimatedRamUsage();
        cpuClock = new AnimatedCpuClock();
        home = new HomePageInfo();  // Home page system information
        networkUsage = new NetworkUsage();  // Network usage information with scrollable, text-based display
        diskLineGraph = new AnimatedDiskLineGraph();  // Disk usage line graph for read/write speeds

        // Create a TabPane as the main layout
        TabPane tabPane = new TabPane();

        // Create the Home Tab with general system information
        Tab homeTab = new Tab("Home");
        homeTab.setContent(home.getHomePageLayout());
        homeTab.setClosable(false);

        // Create the CPU Tab with both CPU load and CPU clock graphs
        Tab cpuTab = new Tab("CPU");
        cpuTab.setContent(cpuLineGraph.getLineChart());
        cpuTab.setClosable(false);

        // Create a Memory Tab with real-time RAM usage
        Tab memoryTab = new Tab("Memory");
        memoryTab.setContent(ramUsage.getRamUsagePane());
        memoryTab.setClosable(false);

        // Create a Network Tab with scrollable, text-based information display (dark theme)
        Tab networkTab = new Tab("Network");
        networkTab.setContent(networkUsage.getNetworkUsageInfo());
        networkTab.setClosable(false);

        // Create a Disk Tab for Disk read/write speeds
        Tab diskTab = new Tab("Disk");
        diskTab.setContent(diskLineGraph.getLineChart());
        diskTab.setClosable(false);

        // Add all tabs to the TabPane
        tabPane.getTabs().addAll(homeTab, cpuTab, memoryTab, networkTab, diskTab);

        // Create the main layout and set the TabPane as the center
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        // Create the scene and apply the CSS stylesheet
        Scene scene = new Scene(root, 1200, 600);
        scene.getStylesheets().add(getClass().getResource("/org/example/block1project/styles.css").toExternalForm());

        // Set up the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hardware Monitor GUI");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
