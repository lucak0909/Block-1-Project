package org.example.block1project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class projectGui extends Application {

    // Instances of the animated components
    private AnimatedCpuLineGraph cpuLineGraph;
    private AnimatedRamUsage ramUsage;
    private AnimatedCpuClock cpuClock;

    // Placeholder for real-time components (like graphs or charts)
    private StackPane homePage, usbPage, pciPage;

    @Override
    public void start(Stage primaryStage) {
        // Initialize animated components
        cpuLineGraph = new AnimatedCpuLineGraph();
        ramUsage = new AnimatedRamUsage();
        cpuClock = new AnimatedCpuClock();

        // CPU Line Graph and Clock in one scene (for the CPU Tab)
        LineChart<Number, Number> cpuChart = cpuLineGraph.getLineChart();
        LineChart<Number, Number> cpuClockChart = cpuClock.getClockChart(); // Assuming your class returns a LineChart for CPU Clock

        // Create a TabPane as the main layout
        TabPane tabPane = new TabPane();

        // Create the Home Tab
        Tab homeTab = new Tab("Home");
        homePage = createHomePage();
        homeTab.setContent(homePage);

        // Create the CPU Tab with both CPU load and CPU clock graphs
        Tab cpuTab = new Tab("CPU");
        VBox cpuPage = new VBox(cpuChart, cpuClockChart); // Display both CPU usage and clock animations
        cpuTab.setContent(cpuPage);

        // Create a Memory Tab with real-time RAM usage
        Tab memoryTab = new Tab("Memory");
        LineChart<Number, Number> ramChart = ramUsage.getRamUsageChart(); // Assuming AnimatedRamUsage returns a LineChart
        StackPane memoryPage = new StackPane(ramChart);
        memoryTab.setContent(memoryPage);

        // Create a USB Devices Tab (Placeholder)
        Tab usbTab = new Tab("USB");
        usbPage = new StackPane(new Label("USB devices list will go here"));
        usbTab.setContent(usbPage);

        // Create a PCI Devices Tab (Placeholder)
        Tab pciTab = new Tab("PCI");
        pciPage = new StackPane(new Label("PCI devices list will go here"));
        pciTab.setContent(pciPage);

        // Add tabs to the TabPane
        tabPane.getTabs().addAll(homeTab, cpuTab, memoryTab, usbTab, pciTab);

        // Create the main layout and set the TabPane as the center
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        // Optional: Status bar at the bottom (for real-time system stats like date/time)
        Label statusBar = new Label("Status: Ready");  // Placeholder for a status bar
        root.setBottom(statusBar);

        // Create the scene and show the stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hardware Monitor GUI");
        primaryStage.show();
    }

    // Home page method to show general device info (CPU, RAM, USB, PCI summary)
    private StackPane createHomePage() {
        VBox homeLayout = new VBox(10);  // 10px spacing between elements

        // General Information Labels (placeholders for now)
        Label cpuInfo = new Label("CPU: 8 cores, Intel Core i7");  // Example data, replace with real query later
        Label ramInfo = new Label("RAM: 16GiB total, 8GiB used");  // Example data
        Label usbInfo = new Label("USB Devices: 3 devices connected");
        Label pciInfo = new Label("PCI Devices: 2 devices connected");

        // Add these labels to the home layout
        homeLayout.getChildren().addAll(cpuInfo, ramInfo, usbInfo, pciInfo);

        return new StackPane(homeLayout);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
