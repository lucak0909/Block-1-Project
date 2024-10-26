package org.example.block1project;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Properties;

public class HomePageInfo {

    private VBox homePageLayout;

    public HomePageInfo() {
        homePageLayout = new VBox(10);  // 10px spacing between elements

        // Gather system information and update the home page
        Platform.runLater(this::updateHomePageInfo);
    }

    // Method to gather and update system information on the Home Page
    private void updateHomePageInfo() {
        // Get system properties and operating system details
        Properties properties = System.getProperties();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        // Operating System Information
        String osName = properties.getProperty("os.name");
        String osVersion = properties.getProperty("os.version");
        String osArch = properties.getProperty("os.arch");

        // CPU Information
        String cpuInfo = String.format("CPU: %s, %s cores", osBean.getName(), Runtime.getRuntime().availableProcessors());

        // Memory Information
        long totalMemory = Runtime.getRuntime().maxMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        // Create labels for displaying the system information
        Label osLabel = new Label("Operating System: " + osName + " " + osVersion + " (" + osArch + ")");
        Label cpuLabel = new Label(cpuInfo);
        Label ramLabel = new Label(String.format("Memory: %.2f GiB used / %.2f GiB total",
                bytesToGiB(usedMemory), bytesToGiB(totalMemory)));

        // Add labels to the VBox layout
        homePageLayout.getChildren().addAll(osLabel, cpuLabel, ramLabel);

        // Optional: Add more system information (storage, graphics, etc.)
        // Placeholder for storage and graphics information
        Label storageLabel = new Label("Storage: Information not available");
        Label graphicsLabel = new Label("Graphics: Information not available");

        homePageLayout.getChildren().addAll(storageLabel, graphicsLabel);
    }

    // Helper method to convert bytes to GiB for better readability
    private double bytesToGiB(long bytes) {
        return bytes / (1024.0 * 1024 * 1024);
    }

    // Method to return the VBox layout with the system info
    public VBox getHomePageLayout() {
        return homePageLayout;
    }
}
