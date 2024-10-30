package org.example.block1project;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import oshi.SystemInfo;
import oshi.hardware.NetworkIF;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NetworkUsage {

    private ScrollPane scrollableNetworkInfo;
    private VBox networkInfoBox;
    private Map<NetworkIF, Label> networkSpeedLabels;
    private SystemInfo systemInfo;
    private Timer timer;

    public NetworkUsage() {
        systemInfo = new SystemInfo();
        List<NetworkIF> networkIFs = systemInfo.getHardware().getNetworkIFs();

        networkSpeedLabels = new HashMap<>();
        VBox infoLabels = new VBox(10);

        // Initialize each interface's information and speed label
        for (NetworkIF net : networkIFs) {
            // Display name, MAC address, and IP addresses with dark theme styling
            Label nameLabel = new Label("Name: " + net.getDisplayName());
            Label macLabel = new Label("MAC Address: " + net.getMacaddr());
            Label ipv4Label = new Label("IPv4: " + String.join(", ", net.getIPv4addr()));
            Label ipv6Label = new Label("IPv6: " + String.join(", ", net.getIPv6addr()));

            // Set text color for each label
            nameLabel.setStyle("-fx-text-fill: black; ");
            macLabel.setStyle("-fx-text-fill: black;");
            ipv4Label.setStyle("-fx-text-fill: black;");
            ipv6Label.setStyle("-fx-text-fill: black;");

            // Real-time speed label (initially set to 0 KB/s)
            Label speedLabel = new Label("Current Speed: 0 KB/s");
            speedLabel.setStyle("-fx-text-fill: black;");
            networkSpeedLabels.put(net, speedLabel);

            // Layout for each interface's information
            VBox interfaceInfo = new VBox(5, nameLabel, macLabel, ipv4Label, ipv6Label, speedLabel);
            interfaceInfo.setPadding(new Insets(10));
            infoLabels.getChildren().add(interfaceInfo);
        }

        // Main VBox layout with all interface labels
        networkInfoBox = new VBox(10, new Label("Network Interface Information"), infoLabels);
        networkInfoBox.setPadding(new Insets(20));
        networkInfoBox.setAlignment(Pos.CENTER);
        networkInfoBox.setStyle("-fx-background-color: #DDDDDD; -fx-font-weight: bold;");  // Dark background for the VBox

        // Wrap the network information in a ScrollPane to enable scrolling
        scrollableNetworkInfo = new ScrollPane(networkInfoBox);
        scrollableNetworkInfo.setFitToWidth(true);
        scrollableNetworkInfo.setPadding(new Insets(10));
        scrollableNetworkInfo.setStyle("-fx-background: #2b2b2b; -fx-border-color: transparent; -fx-background-radius: 10;");  // Dark background for ScrollPane

        // Start updating network data at intervals
        startNetworkUsageMonitoring();
    }

    private void startNetworkUsageMonitoring() {
        timer = new Timer(true);  // Create a background timer

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update each network interface's speed label
                for (NetworkIF net : networkSpeedLabels.keySet()) {
                    net.updateAttributes();  // Refresh network data

                    long bytesReceived = net.getBytesRecv();
                    long bytesSent = net.getBytesSent();

                    // Calculate the combined speed in KB/s
                    double combinedSpeed = (bytesReceived + bytesSent) / 1024.0;

                    // Update the speed label on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        Label speedLabel = networkSpeedLabels.get(net);
                        speedLabel.setText(String.format("Current Speed: %.2f KB/s", combinedSpeed));
                    });
                }
            }
        }, 0, 1000);  // Update every second (1000 ms)
    }

    public ScrollPane getNetworkUsageInfo() {
        return scrollableNetworkInfo;
    }

    // Ensure the timer is canceled when no longer needed
    public void stopMonitoring() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
