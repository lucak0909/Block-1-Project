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

public class AnimatedRamUsage extends Application {

    private static final int CIRCLE_RADIUS = 100;  // Radius for the circle (arc)
    private Arc ramArc;  // Arc to display RAM usage
    private Label ramLabel;  // Label to display RAM percentage

    @Override
    public void start(Stage primaryStage) {
        // Create an Arc (part of a circle) to represent RAM usage
        ramArc = new Arc();
        ramArc.setRadiusX(CIRCLE_RADIUS);
        ramArc.setRadiusY(CIRCLE_RADIUS);
        ramArc.setStartAngle(90);  // Start from the top
        ramArc.setLength(0);  // Initial arc length (representing 0% usage)
        ramArc.setType(ArcType.ROUND);
        ramArc.setFill(Color.BLUE);  // Set the arc color to blue
        ramArc.setStroke(Color.BLACK);  // Add an outline

        // Create a Label to show the percentage of RAM usage
        ramLabel = new Label("RAM Usage: 0%");
        ramLabel.setStyle("-fx-font-size: 20px;");

        // Arrange the arc and label in a StackPane (so the label is on top of the arc)
        StackPane root = new StackPane();
        root.getChildren().addAll(ramArc, ramLabel);

        // Create a Scene and add it to the Stage
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setTitle("Animated RAM Usage Pie Chart");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start a Timeline to update the RAM usage regularly
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateRamUsage())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);  // Run indefinitely
        timeline.play();  // Start the animation
    }

    // Method to update the RAM usage and adjust the arc and label accordingly
    private void updateRamUsage() {
        // Get RAM usage using OperatingSystemMXBean
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double totalRam = osBean.getTotalPhysicalMemorySize();
        double usedRam = totalRam - osBean.getFreePhysicalMemorySize();
        double ramUsage = (usedRam / totalRam) * 100;

        // Ensure RAM usage is between 0 and 100
        ramUsage = Math.max(0, Math.min(100, ramUsage));

        // Update the arc's length to represent the RAM usage
        ramArc.setLength(-ramUsage * 3.6);  // Multiply by 3.6 to convert percentage to angle

        // Update the label to show the percentage
        ramLabel.setText(String.format("RAM Usage: %.1f%%", ramUsage));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
