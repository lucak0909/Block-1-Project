package org.example.block1project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class  projectGui extends Application {

    // Instances of the animated components
    private CpuUsageGraph cpuLineGraph;
    private RamUsageGauge ramUsage;
    private CpuClockGraph cpuClockGraph;
    private Home home;
    private Battery battery;

    @Override
    public void start(Stage primaryStage) {
        // Initialize animated components and home page info
        cpuLineGraph = new CpuUsageGraph();
        ramUsage = new RamUsageGauge();
        cpuClockGraph = new CpuClockGraph();
        home = new Home();  // Home page system information
        battery = new Battery();  // Initialize battery status

        // Create a VBox to hold battery information
        VBox batteryVBox = new VBox(10); // 10 pixels of spacing between elements
        batteryVBox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Retrieve and display battery information in batteryVBox
        Battery.getBatteryInfo(batteryVBox);  // Pass the VBox as an argument

        cpuFrequencyChart = new CpuFrequencyChart();
        // Create a VBox for the CPU Tab layout
        VBox cpuPage = new VBox();  // Change to VBox
        cpuPage.getChildren().addAll(cpuLineGraph.getLineChart(), cpuClockGraph.getClockChart());

        // Create a TabPane as the main layout
        TabPane tabPane = new TabPane();

        // Create the Home Tab with general system information
        Tab homeTab = new Tab("Home");
        homeTab.setContent(home.getHomePageLayout());  // Use the VBox with system info
        homeTab.setClosable(false);  // Prevent closing the Home tab

        // Create the CPU Tab with both CPU load and CPU clock graphs
        Tab cpuTab = new Tab("CPU");
        cpuTab.setContent(cpuPage);  // Set the VBox as the content
        cpuTab.setClosable(false);  // Prevent closing the CPU tab

        // Create a Memory Tab with real-time RAM usage
        Tab memoryTab = new Tab("Memory");
        memoryTab.setContent(ramUsage.getRamUsagePane());
        memoryTab.setClosable(false);  // Prevent closing the Memory tab

        // Create a Battery Tab and set batteryVBox as its content
        Tab batteryTab = new Tab("Battery");
        batteryTab.setContent(batteryVBox);  // Display battery information in the tab
        batteryTab.setClosable(false);

        // Add all tabs to the TabPane
        tabPane.getTabs().addAll(homeTab, cpuTab, memoryTab, batteryTab);

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
