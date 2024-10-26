package org.example.block1project;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;

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
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

        // CPU Information
        String cpuBrand = getCpuBrand();
        int cpuCores = Runtime.getRuntime().availableProcessors();
        int cpuThreads = threadBean.getThreadCount(); // Approximation of threads
        String cpuClockSpeed = getCpuClockSpeed();

        // Create labels for displaying the system information
        Label cpuLabel = new Label(String.format("\nCPU: %s, %d cores, %d threads, Clock Speed: %s",
                cpuBrand, cpuCores, cpuThreads, cpuClockSpeed));

        // Memory Information
        long totalMemory = Runtime.getRuntime().maxMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        Label ramLabel = new Label(String.format("Memory: %.2f GiB used / %.2f GiB total",
                bytesToGiB(usedMemory), bytesToGiB(totalMemory)));

        // Add labels to the VBox layout
        homePageLayout.getChildren().addAll(cpuLabel, ramLabel);

        // Optional: Add more system information (storage, graphics, etc.)
        // Gather storage information
        long totalStorage = getTotalDiskSpace();
        long freeStorage = getFreeDiskSpace();
        long usedStorage = totalStorage - freeStorage;

        Label storageLabel = new Label(String.format("Storage: %.2f GiB used / %.2f GiB total",
                bytesToGiB(usedStorage), bytesToGiB(totalStorage)));

        // Gather graphics information
        String graphicsInfo = getGraphicsInfo();
        Label graphicsLabel = new Label("Graphics: " + graphicsInfo);

        // Add storage and graphics information to the layout
        homePageLayout.getChildren().addAll(storageLabel, graphicsLabel);
    }

    // Method to get CPU brand (this can be platform-dependent)
    private String getCpuBrand() {
        // This is a placeholder; actual implementation would vary by OS
        return "Intel/AMD"; // Ideally, you would use a library or command to get this information
    }

    // Method to get CPU clock speed (this can be platform-dependent)
    private String getCpuClockSpeed() {
        // Placeholder for actual clock speed retrieval
        return "3.5 GHz"; // This should be dynamically retrieved in a real scenario
    }

    // Method to get total disk space
    private long getTotalDiskSpace() {
        File[] roots = File.listRoots();
        long totalSpace = 0;
        for (File root : roots) {
            totalSpace += root.getTotalSpace();
        }
        return totalSpace;
    }

    // Method to get free disk space
    private long getFreeDiskSpace() {
        File[] roots = File.listRoots();
        long freeSpace = 0;
        for (File root : roots) {
            freeSpace += root.getFreeSpace();
        }
        return freeSpace;
    }

    // Placeholder method for graphics information (can be enhanced further)
    private String getGraphicsInfo() {
        // This could be expanded with a library for detailed graphics info
        return "Graphics information not fully available";
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
