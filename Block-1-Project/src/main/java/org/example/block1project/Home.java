package org.example.block1project;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GraphicsCard;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

public class Home {

    private VBox homePageLayout;
    private SystemInfo systemInfo;

    public Home() {
        homePageLayout = new VBox(10);  // 10px spacing between elements
        systemInfo = new SystemInfo();  // Initialize OSHI SystemInfo

        // Gather system information and update the home page
        Platform.runLater(this::updateHomePageInfo);
    }

    // Method to gather and update system information on the Home Page
    private void updateHomePageInfo() {
        // OSHI Hardware Abstraction Layer
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor cpu = hal.getProcessor();
        GlobalMemory memory = hal.getMemory();
        OperatingSystem os = systemInfo.getOperatingSystem();

        // OS Information
        String osInfo = String.format("OS: %s %s (%s), Uptime: %s",
                os.getFamily(),
                os.getVersionInfo().getVersion(),
                os.getManufacturer(),
                formatUptime(os.getSystemUptime()));
        Label osLabel = new Label(osInfo);

        // CPU Information
        String cpuBrand = cpu.getProcessorIdentifier().getName();
        int cpuCores = cpu.getLogicalProcessorCount();
        double cpuLoad = cpu.getSystemCpuLoad(1000) * 100; // CPU load in percentage

        Label cpuLabel = new Label(String.format("CPU: %s, %d cores, %.2f%% load",
                cpuBrand, cpuCores, cpuLoad));

        // Memory Information
        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();
        long usedMemory = totalMemory - availableMemory;

        Label ramLabel = new Label(String.format("Memory: %.2f GiB used / %.2f GiB total",
                bytesToGiB(usedMemory), bytesToGiB(totalMemory)));

        // Storage Information
        long totalStorage = getTotalDiskSpace();
        long freeStorage = getFreeDiskSpace();
        long usedStorage = totalStorage - freeStorage;

        Label storageLabel = new Label(String.format("Storage: %.2f GiB used / %.2f GiB total",
                bytesToGiB(usedStorage), bytesToGiB(totalStorage)));

        // Graphics Information
        String graphicsInfo = getGraphicsInfo(hal);
        Label graphicsLabel = new Label("Graphics: " + graphicsInfo);

        // Add all labels to the VBox layout
        homePageLayout.getChildren().addAll(osLabel, cpuLabel, ramLabel, storageLabel, graphicsLabel);
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

    // Method to get graphics information
    private String getGraphicsInfo(HardwareAbstractionLayer hal) {
        StringBuilder graphicsInfo = new StringBuilder();
        for (GraphicsCard gpu : hal.getGraphicsCards()) {
            graphicsInfo.append(String.format("%s, VRam: %.2f GiB\n", gpu.getName(), bytesToGiB(gpu.getVRam())));
        }
        return graphicsInfo.toString();
    }

    // Helper method to format system uptime in hours, minutes, seconds
    private String formatUptime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
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
