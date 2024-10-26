package org.example.block1project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

public class AnimatedCpuClock {

    private XYChart.Series<Number, Number> clockSeries;  // Series to hold CPU clock data
    private int timeInMilliseconds = 0;  // Track elapsed time in milliseconds
    private static final int MAX_TIME_RANGE = 10000;  // Show the last 10 "graph seconds"
    private static final int UPDATE_INTERVAL = 100;   // Update interval (100ms)
    private static final double TIME_SCALE = 0.7;     // Faster time scale (70% of real-time speed)

    private LineChart<Number, Number> clockChart;

    public AnimatedCpuClock() {
        // Create the X and Y axes
        NumberAxis xAxis = new NumberAxis(0, MAX_TIME_RANGE / 1000, 1);  // Last 10 seconds (graph time)
        NumberAxis yAxis = new NumberAxis(0, 100, 10);  // Y-axis range between 0 and 100 (percentage)

        // Create a LineChart to display CPU clock speed over time
        clockChart = new LineChart<>(xAxis, yAxis);
        clockChart.setTitle("CPU Clock Usage Over Time");

        // Remove grid lines for a cleaner look
        clockChart.setHorizontalGridLinesVisible(false);
        clockChart.setVerticalGridLinesVisible(false);

        // Create a Series to hold the data
        clockSeries = new XYChart.Series<>();
        clockSeries.setName("CPU Clock Speed");

        // Add the series to the LineChart
        clockChart.getData().add(clockSeries);
        clockChart.setCreateSymbols(false);  // Disable symbols on data points (just the line)

        // Start a Timeline to update the clock speed regularly (every 0.1 seconds for smooth transitions)
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(UPDATE_INTERVAL), event -> updateCpuClockSpeed())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);  // Run indefinitely
        timeline.play();  // Start the animation
    }

    // Method to update the CPU clock speed and add data to the graph
    private void updateCpuClockSpeed() {
        // Simulating clock usage - replace this with real data if available
        double clockSpeed = Math.random() * 100;  // Random CPU clock speed for demonstration (0 to 100%)

        // Add the clock speed to the series (with time on the X-axis, adjusted by TIME_SCALE)
        clockSeries.getData().add(new XYChart.Data<>((timeInMilliseconds * TIME_SCALE / 1000.0), clockSpeed));

        // Increment the time counter
        timeInMilliseconds += UPDATE_INTERVAL;

        // Remove old data points to keep the X-axis range constant (last 10 seconds in graph time)
        if (timeInMilliseconds * TIME_SCALE > MAX_TIME_RANGE) {
            clockSeries.getData().remove(0);  // Remove the oldest data point
        }

        // Update the X-axis range to keep showing the last 10 seconds (in graph time)
        ((NumberAxis) clockSeries.getChart().getXAxis()).setLowerBound((timeInMilliseconds * TIME_SCALE - MAX_TIME_RANGE) / 1000.0);
        ((NumberAxis) clockSeries.getChart().getXAxis()).setUpperBound((timeInMilliseconds * TIME_SCALE) / 1000.0);
    }

    // Method to return the LineChart for embedding in the GUI
    public LineChart<Number, Number> getClockChart() {
        return clockChart;
    }
}
