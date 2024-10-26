package org.example.block1project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class AnimatedRamUsage {

    private Arc usedRamArc;  // Arc representing used RAM
    private Arc freeRamArc;  // Arc representing free RAM
    private OperatingSystemMXBean osBean;
    private Text usedRamText;  // Text to display used RAM
    private Text freeRamText;  // Text to display free RAM
    private Label ramUsageLabel;  // Label for RAM usage

    public AnimatedRamUsage() {
        osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        // Create the Arcs for used and free RAM with larger radii
        usedRamArc = new Arc(0, 0, 150, 150, 90, 0);  // Adjusted radius
        usedRamArc.setType(ArcType.ROUND);
        usedRamArc.setFill(Color.RED);  

        freeRamArc = new Arc(0, 0, 150, 150, 90, 360);  // Adjusted radius
        freeRamArc.setType(ArcType.ROUND);
        freeRamArc.setFill(Color.GREEN);  

        // Create labels for RAM usage
        usedRamText = new Text("Used: 0 GB");
        freeRamText = new Text("Free: 0 GB");

        // Label for RAM usage percentage
        ramUsageLabel = new Label("RAM Usage %");
        ramUsageLabel.setTextFill(Color.WHITE);  

        // Start a Timeline to update the Arcs and labels regularly
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> updateRamUsage())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();  
    }

    private void updateRamUsage() {
        long totalMemory = osBean.getTotalPhysicalMemorySize();
        long freeMemory = osBean.getFreePhysicalMemorySize();
        long usedMemory = totalMemory - freeMemory;

        double usedPercentage = ((double) usedMemory / totalMemory) * 360;

        Platform.runLater(() -> {
            usedRamArc.setLength(-usedPercentage);
            freeRamArc.setLength(360 - usedPercentage);
            usedRamText.setText(String.format("Used: %.2f GB", usedMemory / (1024.0 * 1024 * 1024)));
            freeRamText.setText(String.format("Free: %.2f GB", freeMemory / (1024.0 * 1024 * 1024)));
        });
    }

    public Pane getRamUsagePane() {
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(ramUsageLabel, usedRamText, freeRamText);
    
        Pane arcPane = new Pane();
        arcPane.getChildren().addAll(freeRamArc, usedRamArc);
    
        // Center the arcs in the pane
        double centerX = 590;  // Center for 600x600 pane
        double centerY = 150;  // Centered vertically
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
