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
import oshi.hardware.UsbDevice;
import oshi.hardware.Baseboard;

public class Home {

    private VBox homePageLayout;
    private SystemInfo systemInfo;

    public Home() {
        homePageLayout = new VBox(20);  // 20px spacing between elements
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

        // Motherboard Info
        Baseboard baseboard = hal.getComputerSystem().getBaseboard();
        String manufacturer = baseboard.getManufacturer();
        String model = baseboard.getModel();
        String serialNumber = baseboard.getSerialNumber();
        String version = baseboard.getVersion();
        // OS Information
        String osInfo = String.format("\nOS: %s %s (%s), Uptime: %s",
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

        // USB Information
        String usbInfo = getUSBInfo(hal);
        Label usbInfoLabel = new Label("USB Devices: " + usbInfo);

        // Motherboard Info
        Label motherboardInfoLabel = new Label(String.format("Motherboard: %s%n Model: %s%n Serial: %s%n Version: %s",
                manufacturer, model, serialNumber, version));

        // Add labels to the VBox layout
        homePageLayout.getChildren().addAll(cpuLabel, ramLabel, storageLabel, graphicsLabel, usbInfoLabel, motherboardInfoLabel);
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

    // Method to get USB devices information
    private String getUSBInfo(HardwareAbstractionLayer hal) {
        StringBuilder usbInfoBuilder = new StringBuilder();
        List<UsbDevice> usbDevices = hal.getUsbDevices(true);

        for (int i = 0; i < usbDevices.size(); i++) {
            UsbDevice usbDevice = usbDevices.get(i);
            usbInfoBuilder.append(String.format("%s (Vendor ID: %s)", usbDevice.getName(), usbDevice.getVendorId()));
            // Add a line break for every 3 devices for better readability
            if ((i + 1) % 3 == 0 && (i + 1) < usbDevices.size()) {
                usbInfoBuilder.append("\n");
            } else {
                usbInfoBuilder.append(", ");
            }
        }

        if (usbInfoBuilder.length() == 0) {
            return "No USB devices found";
        }
        // Remove the trailing comma and space
        usbInfoBuilder.setLength(usbInfoBuilder.length() - 2);
        return usbInfoBuilder.toString();
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
