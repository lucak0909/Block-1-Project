package org.example.block1project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;  // Import VBox instead of StackPane
import javafx.stage.Stage;


public class  projectGui extends Application {

    // Instances of the animated components
    private AnimatedCpuLineGraph cpuLineGraph;
    private AnimatedRamUsage ramUsage;
    private AnimatedCpuClock cpuClock;
    private HomePageInfo homePageInfo;

    private CpuFrequencyChart cpuFrequencyChart;
    @Override
    public void start(Stage primaryStage) {
        // Initialize animated components and home page info
        cpuLineGraph = new AnimatedCpuLineGraph();
        ramUsage = new AnimatedRamUsage();
        cpuClock = new AnimatedCpuClock();
        homePageInfo = new HomePageInfo();  // Home page system information

        cpuFrequencyChart = new CpuFrequencyChart();
        // Create a VBox for the CPU Tab layout
        VBox cpuPage = new VBox();  // Change to VBox
        cpuPage.getChildren().addAll(cpuLineGraph.getLineChart(), cpuClock.getClockChart(), cpuFrequencyChart.getFrequencyChart());
        //cpuPage.getStylesheets().add(getClass().getResource("org/example/block1project/styles.css").toExternalForm());

        // Create a TabPane as the main layout
        TabPane tabPane = new TabPane();

        // Create the Home Tab with general system information
        Tab homeTab = new Tab("Home");
        homeTab.setContent(homePageInfo.getHomePageLayout());  // Use the VBox with system info
        homeTab.setClosable(false);  // Prevent closing the Home tab

        // Create the CPU Tab with both CPU load and CPU clock graphs
        Tab cpuTab = new Tab("CPU");
        cpuTab.setContent(cpuPage);  // Set the VBox as the content
        cpuTab.setClosable(false);  // Prevent closing the CPU tab

        // Create a Memory Tab with real-time RAM usage
        Tab memoryTab = new Tab("Memory");
        memoryTab.setContent(ramUsage.getRamUsagePane());
        memoryTab.setClosable(false);  // Prevent closing the Memory tab

        // Add tabs to the TabPane
        tabPane.getTabs().addAll(homeTab, cpuTab, memoryTab);

        // Create the main layout and set the TabPane as the center
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        // Create the scene and apply the CSS stylesheet
        Scene scene = new Scene(root, 1200, 600);
        scene.getStylesheets().add(getClass().getResource("/org/example/block1project/styles.css").toExternalForm());  // Apply the CSS file

        // Set up the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hardware Monitor GUI");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
