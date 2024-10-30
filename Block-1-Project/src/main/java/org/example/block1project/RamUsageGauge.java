package org.example.block1project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.util.Duration;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class RamUsageGauge {

    private Arc usedRamArc;  // Arc representing used RAM
    private Arc freeRamArc;  // Arc representing free RAM
    private GlobalMemory memory;  // OSHI GlobalMemory object
    private Text usedRamText;  // Text to display used RAM
    private Text freeRamText;  // Text to display free RAM
    private Label ramUsageLabel;  // Label for RAM usage

    public RamUsageGauge() {
        // Initialize OSHI components
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        memory = hal.getMemory();

        // Set up the arcs to form a semi-circle (half-gauge)
        usedRamArc = new Arc(0, 0, 150, 150, 180, 0);  // Center (0,0), radius 150
        usedRamArc.setType(ArcType.OPEN);  // Use OPEN instead of ROUND for a hollow look
        usedRamArc.setStroke(Color.web("#ffa000"));
        usedRamArc.setStrokeWidth(30);  // Set the desired thickness of the arc
        usedRamArc.setFill(Color.TRANSPARENT);  // No fill to create the hollow center

        freeRamArc = new Arc(0, 0, 150, 150, 360, 0);
        freeRamArc.setType(ArcType.OPEN);
        freeRamArc.setStroke(Color.GRAY);
        freeRamArc.setStrokeWidth(30);
        freeRamArc.setFill(Color.TRANSPARENT);

        // Create labels for RAM usage
        usedRamText = new Text("Used: 0 GB");
        freeRamText = new Text("Free: 0 GB");

        // Set the text color to black
        usedRamText.setFill(Color.BLACK);
        freeRamText.setFill(Color.BLACK);

        // Label for RAM usage percentage
        ramUsageLabel = new Label("RAM Usage %");
        ramUsageLabel.setTextFill(Color.BLACK); // Set to black as well

        // Start a Timeline to update the Arcs and labels regularly
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateRamUsage())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateRamUsage() {
        long totalMemory = memory.getTotal(); // Total physical memory
        long freeMemory = memory.getAvailable(); // Free physical memory
        long usedMemory = totalMemory - freeMemory;

        double usedPercentage = ((double) usedMemory / totalMemory) * 180;

        Platform.runLater(() -> {
            usedRamArc.setLength(-usedPercentage);
            freeRamArc.setLength(180 - usedPercentage);

            // Update the RAM usage text
            usedRamText.setText(String.format("Used: %.2f GB", usedMemory / (1024.0 * 1024 * 1024)));
            freeRamText.setText(String.format("Free: %.2f GB", freeMemory / (1024.0 * 1024 * 1024)));

            // Center the text horizontally within the Pane
            usedRamText.setX(290 - usedRamText.getLayoutBounds().getWidth() / 2);  // Adjust X to center
            freeRamText.setX(290 - freeRamText.getLayoutBounds().getWidth() / 2);  // Adjust X to center

            // Set Y position to avoid overlap
            usedRamText.setY(180);  // Position for used RAM text
            freeRamText.setY(210);  // Position for free RAM text
        });
    }

    public Pane getRamUsagePane() {
        VBox vbox = new VBox(10);

        Region spacer = new Region();
        spacer.setPrefHeight(40); // Set the height of the spacer

        vbox.getChildren().addAll(spacer, ramUsageLabel, usedRamText, freeRamText);
        Pane arcPane = new Pane();
        arcPane.getChildren().addAll(freeRamArc, usedRamArc);

        //Center the arcs in the pane
        double centerX = 250;  // Center for 600x600 pane
        double centerY = 200;  // Centered vertically
        usedRamArc.setTranslateX(centerX);
        usedRamArc.setTranslateY(centerY);
        freeRamArc.setTranslateX(centerX);
        freeRamArc.setTranslateY(centerY);

        arcPane.setPrefSize(600, 300);  // Adjust size to fit
        arcPane.setStyle("-fx-alignment: center;");  // Center alignment for Pane

        vbox.getChildren().add(arcPane);
        vbox.setStyle("-fx-alignment: center;");  // Center VBox contents

        return vbox;
    }
}
