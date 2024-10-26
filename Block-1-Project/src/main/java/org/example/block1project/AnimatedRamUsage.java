package org.example.block1project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class AnimatedRamUsage {

    private Arc usedRamArc;  // Arc representing used RAM
    private Arc freeRamArc;  // Arc representing free RAM
    private OperatingSystemMXBean osBean;

    public AnimatedRamUsage() {
        // Initialize the OperatingSystemMXBean to get system information
        osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        // Create the Arc for used RAM
        usedRamArc = new Arc(200, 200, 100, 100, 90, 0);  // Center at (200, 200), radius 100
        usedRamArc.setType(ArcType.ROUND);
        usedRamArc.setFill(Color.RED);  // Red for used RAM

        // Create the Arc for free RAM
        freeRamArc = new Arc(200, 200, 100, 100, 90, 360);  // Full circle for free RAM
        freeRamArc.setType(ArcType.ROUND);
        freeRamArc.setFill(Color.GREEN);  // Green for free RAM

        // Start a Timeline to update the Arcs regularly
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> updateRamUsage())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();  // Start the updates
    }

    // Method to update the Arcs representing RAM usage
    private void updateRamUsage() {
        long totalMemory = osBean.getTotalPhysicalMemorySize();
        long freeMemory = osBean.getFreePhysicalMemorySize();
        long usedMemory = totalMemory - freeMemory;

        // Calculate percentage of used RAM (converted to angle 0 to 360)
        double usedPercentage = ((double) usedMemory / totalMemory) * 360;

        // Update the Arcs on the JavaFX Application Thread
        Platform.runLater(() -> {
            usedRamArc.setLength(-usedPercentage);  // Arc's angle set based on percentage used
            freeRamArc.setLength(360 - usedPercentage);  // Free RAM is the remaining part
        });
    }

    // Method to return the Pane containing the RAM usage Arcs
    public Pane getRamUsagePane() {
        Pane pane = new Pane();
        pane.getChildren().addAll(freeRamArc, usedRamArc);
        return pane;
    }
}
