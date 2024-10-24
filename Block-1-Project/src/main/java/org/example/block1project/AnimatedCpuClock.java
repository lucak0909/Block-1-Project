package org.example.block1project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class AnimatedCpuClock extends Application {

    private static final int CLOCK_RADIUS = 100;  // Radius for the clock (arc)
    private Arc cpuArc;  // Arc to display CPU usage
    private Label cpuLabel;  // Label to display CPU percentage

    @Override
    public void start(Stage primaryStage) {
        // Create an Arc (part of a circle) to represent CPU usage
        cpuArc = new Arc();
        cpuArc.setRadiusX(CLOCK_RADIUS);
        cpuArc.setRadiusY(CLOCK_RADIUS);
        cpuArc.setStartAngle(90);  // Start from the top
        cpuArc.setLength(0);  // Initial arc length (representing 0% usage)
        cpuArc.setType(ArcType.ROUND);
        cpuArc.getStyleClass().add("arc");

        // Create a Label to show the percentage of CPU usage
        cpuLabel = new Label("CPU Usage: 0%");
        cpuLabel.getStyleClass().add("arc");

        // Arrange the arc and label in a StackPane (so the label is on top of the arc)
        StackPane root = new StackPane();
        root.getChildren().addAll(cpuArc, cpuLabel);

        // Create a Scene and add it to the Stage
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setTitle("Animated CPU Usage Clock");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start a Timeline to update the CPU usage regularly
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateCpuUsage())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);  // Run indefinitely
        timeline.play();  // Start the animation
    }

    // Method to update the CPU usage and adjust the arc and label accordingly
    private void updateCpuUsage() {
        // Get CPU usage using OperatingSystemMXBean
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpuUsage = osBean.getSystemCpuLoad() * 100;

        // Ensure CPU usage is between 0 and 100
        cpuUsage = Math.max(0, Math.min(100, cpuUsage));

        // Update the arc's length to represent the CPU usage
        cpuArc.setLength(-cpuUsage * 3.6);  // Multiply by 3.6 to convert percentage to angle

        // Update the label to show the percentage
        cpuLabel.setText(String.format("CPU Usage: %.1f%%", cpuUsage));

        // Change the arc color based on CPU usage (green to red)
        if (cpuUsage < 50) {
            cpuArc.setFill(Color.GREEN);
        } else if (cpuUsage < 80) {
            cpuArc.setFill(Color.ORANGE);
        } else {
            cpuArc.setFill(Color.RED);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
